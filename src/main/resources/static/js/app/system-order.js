define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var _ = require('underscore');
    var BootstrapDialog = require('bootstrap-dialog');
    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    var admissionList = [];

    var updateFrame = require('text!tpl/system-updateOrder.html');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;

        }, render: function () {
            //this.$el.html(Template);
            var _this = this;

            // TODO: 토론면접 조가 있는지 여부 확인한 후 그것이 있다면 바로 뷰, 없다면 입력 창 띄우기

            var updateDialog = new BootstrapDialog({
                title: '<h4>업로드할 방법을 선택하세요</h4>',
                buttons: [
                    {
                        label: '닫기',
                        action: function (updateDialog) {
                            updateDialog.close();
                        }
                    }
                ],
                onshown: function (updateDialog) {

                    var body = updateDialog.$modalBody;
                    body.append(updateFrame);

                    $('#server').click(function () {
                        _this.fromServer();
                    });

                    $('#file').click(function () {
                        _this.fromFile();
                    });
                }
            });

            updateDialog.realize();
            updateDialog.getModalDialog().css('margin-top', '18%');
            updateDialog.getModalDialog().css('width', '60%');
            updateDialog.getModalBody().css('text-align', 'center');
            updateDialog.open();

            // 2. 파일로 업로드


        }, search: function (o) {
            this.list.search(o);

        }, fromServer: function () {
            // 1. 서버에서 내려받아 순번 저장
            var _this = this;

            $.ajax({
                url: 'system/admission',
                success: function (response) {

                    var innerText = '<select id="admissionCd"><option value="">선택하세요</option>';

                    // 콤보박스 만들기
                    for (var i = 0; i < response.length; i++) {
                        innerText += '<option value="' + response[i].admissionCd + '">' + response[i].admissionNm + '</option>';
                        admissionList.push({
                            admissionCd: response[i].admissionCd,
                            admissionNm: response[i].admissionNm
                        });
                    }
                    innerText += '</select>';

                    var dialog = new BootstrapDialog({
                        title: '<h5><div style="font-weight: normal; font-size: medium">서버에서 내려받기를 진행합니다</div></h5>',
                        message: '<h5 style="margin-left: 10%">전형을 선택하세요&nbsp;&nbsp;&nbsp;&nbsp;' + _this.getAdmissionList(innerText) + '<span id="msg" style="color: crimson"></span></h5>',
                        buttons: [
                            {
                                label: '가져오기',
                                cssClass: 'btn-primary',
                                action: function (dialog) {

                                    var admissionCd = $('#admissionCd').val();

                                    if (admissionCd == '') {
                                        $('#msg').html('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;전형을 선택하지 않았습니다!');
                                        return false;
                                    } else {
                                        responseDialog.complete('<div style="cursor: wait">서버로부터 순번을 내려받고 있습니다. 잠시만 기다려주세요</div>');
                                    }

                                    var param = {
                                        url: "http://erica.humanesystem.com:9000",
                                        admissionCd: admissionCd
                                    };

                                    $.ajax({
                                        url: 'system/order?admissionCd=' + param.admissionCd + '&url=' + param.url,
                                        success: function (response) {
                                            responseDialog.complete(response);
                                        }
                                    });
                                    dialog.close();
                                }
                            },
                            {
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }
                        ]
                    });

                    dialog.realize();
                    dialog.getModalDialog().css('margin-top', '20%');
                    dialog.open();
                }
            });
        }, fromFile: function () {
            var _this = this;

            var html = '<div><form id="uploadOrder" action="upload/order" method="post" enctype="multipart/form-data">';
                html += '<input type="file" name="file" class="pull-left chosen"/><input type="submit" class="btn btn-regist pull-right" value="등록"/></form></div>';

            var dialog = new BootstrapDialog({
                title: '<h5><div style="font-weight: normal; font-size: medium">엑셀 파일로 업로드합니다. 파일을 선택하세요</div></h5>',
                message: html,
                onshown: function(){
                    _this.uploadForm('#uploadOrder');
                },
                buttons: [
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.open();

        }, getAdmissionList: function (text) {
            $.ajax({
                url: 'system/admission',
                success: function (response) {

                    // 콤보박스 만들기
                    for (var i = 0; i < response.length; i++) {
                        text += '<option value="' + response[i].admissionCd + '">' + response[i].admissionNm + '</option>';
                        admissionList.push({
                            admissionCd: response[i].admissionCd,
                            admissionNm: response[i].admissionNm
                        });
                    }
                    text += '</select>';
                }
            });

            return text;

        }, uploadForm: function(id){
            this.$(id).ajaxForm({
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            responseDialog.complete('파일을 선택하세요');
                            return false;
                        }
                    }
                    responseDialog.complete('<div style="cursor: wait">업로드 중 입니다. 창이 사라지지 않으면 관리자에게 문의하세요</div>');
                },
                error: function (response) {
                    responseDialog.error(response.responseJSON);
                },
                success: function (response) {
                    responseDialog.complete(response);
                }
            });
        }
    });
});