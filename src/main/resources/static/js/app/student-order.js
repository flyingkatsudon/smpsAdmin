define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var _ = require('underscore');
    var BootstrapDialog = require('bootstrap-dialog');
    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    var admissionList = [];

    var InnerTemplate = require('text!tpl/student-updateOrder.html');
    var LoadPage = require('../loadPage.js');

    require('../grid/student-order.js');
    // 미완성
    require('../toolbar/student-order.js');
    require('text!/tpl/student-order.html');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;

            this.onStart();
            new LoadPage({baseName: location.hash.substring(1, location.hash.length), param: window.param}).render();

        }, render: function () {
            var _this = this;

            $('#refresh').click(function () {
                _this.onStart();
            });

        }, search: function (o) {
            this.list.search(o);

        },
        onStart: function () {
            var _this = this;

            // TODO: 토론면접 조가 있는지 여부 확인한 후 그것이 있다면 바로 뷰, 없다면 입력 창 띄우기
            $.ajax({
                url: 'student/local/orderCnt',
                success: function (response) {
                    // 순번이 저장되어 있으면
                    if (response) {
                        var dialog = new BootstrapDialog({
                            message: '<h5>순번 데이터가 저장되어 있습니다. 갱신하시겠습니까?</h5>',
                            buttons: [
                                {
                                    label: '갱신하기',
                                    cssClass: 'btn-success',
                                    action: function () {
                                        _this.uploadOrder();
                                    }
                                }, {
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }
                            ]
                        });

                        dialog.realize();

                        dialog.getModalDialog().css('margin-top', '20%');
                        dialog.getModalDialog().css('text-align', 'center');
                        dialog.getModalHeader().hide();

                        dialog.open();

                    }
                    // 순번이 저장되어 있지 않다면
                    else if (!response) {
                        _this.uploadOrder();
                        responseDialog.notify({
                            msg: '순번 데이터의 등록이 필요합니다. 클릭 후 진행하세요',
                            closeAll: false
                        });
                    }
                }
            });

        },
        fromServer: function () {
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

                                    // TODO: 순번 불러올 URL 설정
                                    var param = {
                                        url: "http://test.humanesystem.com:9000",
                                        admissionCd: admissionCd
                                    };

                                    if (admissionCd == '') {
                                        $('#msg').html('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;전형을 선택하지 않았습니다!');
                                        return false;

                                    } else {
                                        // 전형을 선택하고 진행하면 서버 내에 순번 데이터가 있는지 확인
                                        $.ajax({
                                            url: 'student/server/orderCnt?admissionCd=' + param.admissionCd + '&url=' + param.url,
                                            success: function (response) {
                                                // 순번 데이터가 없다면 알림창 띄우기
                                                if (!response) $('#msg').html('&nbsp;&nbsp;&nbsp;&nbsp;&nbsp순번 데이터가 존재하지 않습니다!');

                                                // 순번 데이터가 있다면
                                                else{
                                                    responseDialog.notify({
                                                        msg: '<div style="cursor: wait">서버로부터 순번을 내려받고 있습니다. 잠시만 기다려주세요</div>',
                                                        closable: false
                                                    });

                                                    $.ajax({
                                                        url: 'student/saveOrder?admissionCd=' + param.admissionCd + '&url=' + param.url,
                                                        success: function (response) {
                                                            $('#search').trigger('click');
                                                            responseDialog.notify({msg: response, closeAll: true});
                                                        }
                                                    });
                                                } // else
                                            } // success
                                        }); // ajax
                                    }
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
        },
        fromFile: function () {
            var _this = this;

            var html = '<form id="uploadOrder" action="upload/order" method="post" enctype="multipart/form-data">' +
                '<input type="file" name="file" style="width: 70%;" class="pull-left chosen"/>' +
                '<input type="submit" style="width: 12%; padding: 2%" class="btn btn-success" value="등록"/>' +
                '<input type="button" id="close" style="width: 12%; padding: 2%" class="btn pull-right" value="닫기"/>' +
                '</form>';

            var dialog = new BootstrapDialog({
                title: '<h5><div id="alert" style="font-weight: bold; font-size: medium; color: crimson"><span style="color: black">엑셀 파일로 업로드합니다</span></div></h5>',
                message: html,
                onshown: function () {

                    $('#close').click(function(){
                        BootstrapDialog.closeAll();
                    });

                    _this.uploadForm('#uploadOrder');
                }
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalBody().css('margin-bottom', '7%');
            dialog.open();

        },
        getAdmissionList: function (text) {
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

        },
        uploadOrder: function () {
            var _this = this;

            BootstrapDialog.closeAll();
            var dialog = new BootstrapDialog({
                title: '<h4>업로드할 방법을 선택하세요</h4>',
                buttons: [
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ],
                onshown: function (dialog) {

                    var body = dialog.$modalBody;
                    body.append(InnerTemplate);

                    // 1. 서버에서 내려받기
                    $('#server').click(function () {
                        _this.fromServer();
                    });

                    // 2. 파일로 업로드
                    $('#file').click(function () {
                        _this.fromFile();
                    });
                }
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '18%');
            dialog.getModalDialog().css('width', '60%');
            dialog.getModalBody().css('text-align', 'center');
            dialog.open();

        },
        uploadForm: function (id) {
            $(id).ajaxForm({
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            $('#alert').html('파일을 선택하세요!');
                            return false;
                        }
                    }
                    responseDialog.notify({
                        msg: '<div style="cursor: wait">업로드 중 입니다. 창이 사라지지 않으면 관리자에게 문의하세요</div>',
                        closable: false
                    });
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                },
                success: function (response) {
                    $('#search').trigger('click');
                    responseDialog.notify({msg: response});
                }
            });
        }
    });
});