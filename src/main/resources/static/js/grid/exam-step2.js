define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var BootstrapDialog = require('bootstrap-dialog');

    var dialogStep2 = require('text!tpl/exam-step2.html');

    var Step3 = require('./exam-step3.js');
    var step3 = new Step3();

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    require('jquery-simple-datetimepicker');

    return Backbone.View.extend({
        render: function (rowData) {
            var _this = this;

            // 1. exam_hall_date에서 데이터 가져옴
            $.ajax({
                url: 'system/getStep2?examCd=' + rowData.examCd,
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                success: function (response) {
                    var step2 = new BootstrapDialog({
                        title: '<h3>' + rowData.admissionNm + ' / ' + rowData.examNm + '</h3>',
                        size: 'size-wide',
                        closable: false,
                        onshown: function (dialogRef) {
                            var body = dialogRef.$modalBody;
                            body.append(dialogStep2);

                            for (var i = 0; i < response.length; i++) {

                                if (response[i]['virtNoStart'] == undefined) response[i].virtNoStart = '';
                                if (response[i]['virtNoEnd'] == undefined) response[i].virtNoEnd = '';

                                var html =
                                    '<h5><div id="' + response[i].id + '" class="col-lg-12">' +
                                    '<div class="col-lg-6">' +
                                    '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                    '<div>' +
                                    '<div style="margin:3% 0 0 3%;">' + '시험&nbsp;&nbsp;&nbsp;&nbsp;일자' + '<input type="text" name="hallDate" id="hallDate' + i + '" class="set-basic"  value="' + response[i].hallDate + '"></div>' +
                                    '<div style="margin:3% 0 0 3%;">' + '가번호 시작' + '<input type="text" id="virtNoStart' + i + '" class="set-basic"  value="' + response[i].virtNoStart + '"></div>' +
                                    '<div style="margin:3% 0 0 3%;">' + '가번호 종료' + '<input type="text" id="virtNoEnd' + i + '" class="set-basic"  value="' + response[i].virtNoEnd + '"></div>' +
                                    '</div>' +
                                    '</div>' +
                                    '</div>' +
                                    '<div class="col-lg-6">' +
                                    '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                    '<div>' +
                                    '<div style="margin:3% 0 0 3%;">' + '고사본부' + '<input type="text" name="head" id="headNm' + i + '" class="set-basic"  value="' + response[i].headNm + '"></div>' +
                                    '<div style="margin:3% 0 0 3%;">' + '고사건물' + '<input type="text" name="bldg" id="bldgNm' + i + '" class="set-basic"  value="' + response[i].bldgNm + '"></div>' +
                                    '<div style="margin:3% 0 0 3%;">' + '&nbsp;&nbsp;&nbsp;&nbsp;고사실' + '<input type="text" name="hall" id="hallNm' + i + '" class="set-basic"  value="' + response[i].hallNm + '">' +
                                    '</div>' +
                                    '</div>' +
                                    '</div></h5>';

                                if (i != response.length - 1)
                                    html += '<div class="col-lg-12"><div id="line" style="margin: 3% 0 3% 0; border: 1px solid #d8d6d5"></div></div>';
                                $('#detail').append(html);
                            }

                            $('input[name=head]').attr('disabled', true);
                            $('input[name=head]').css('background', '#fbf7f7');
                            $('input[name=head]').css('color', 'graytext');
                            $('input[name=head]').css('cursor', 'not-allowed');


                            $('input[name=bldg]').attr('disabled', true);
                            $('input[name=bldg]').css('background', '#fbf7f7');
                            $('input[name=bldg]').css('color', 'graytext');
                            $('input[name=bldg]').css('cursor', 'not-allowed');


                            $('input[name=hall]').attr('disabled', true);
                            $('input[name=hall]').css('background', '#fbf7f7');
                            $('input[name=hall]').css('color', 'graytext');
                            $('input[name=hall]').css('cursor', 'not-allowed');

                            $('input[name=hallDate]').appendDtpicker({
                                autodateOnStart: false,
                                dateOnly: true,
                                dateFormat: 'YYYY-MM-DD'
                            });
                        }, // onShown
                        buttons: [
                            {
                                label: '변경 내용 저장',
                                cssClass: 'btn-primary',
                                action: function () {
                                    // 유효성 검사
                                    _this.validate(_this.setParam(rowData.examCd, response));
                                    responseDialog.notify({msg: "변경되었습니다"});
                                } // button action
                            }, // modify
                            {
                                label: '평가항목 설정',
                                cssClass: 'btn-success',
                                action: function () {
                                    // 유효성 검사
                                    _this.validate(_this.setParam(rowData.examCd, response));
                                    step2.close();
                                    step3.render(rowData);

                                } // step2 action
                            },
                            {
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }] // buttons
                    }); // BootstrapDialog

                    step2.realize();
                    step2.getModalDialog().css('width', '70%');
                    step2.open();

                }, // success
                error: function (response) {
                    responseDialog.notify({msg: response.JSON});
                }
            }); // ajax
        },
        setParam: function(examCd, response){
            // 전달할 값, 상단필터와 다름
            var param = [];

            for (var i = 0; i < response.length; i++) {
                var tmp = {
                    id: response[i].id,
                    examCd: examCd,
                    hallCd: response[i].hallCd,
                    hallDate: $('#hallDate' + i).val(),
                    virtNoStart: $('#virtNoStart' + i).val(),
                    virtNoEnd: $('#virtNoEnd' + i).val(),
                    headNm: $('#headNm' + i).val(),
                    bldgNm: $('#bldgNm' + i).val(),
                    hallNm: $('#hallNm' + i).val(),

                    _virtNoStart: response[i].virtNoStart,
                    _virtNoEnd: response[i].virtNoEnd,
                    _hallDate: response[i].hallDate,
                    _hallCd: response[i].hallCd
                };

                param.push(tmp);
            }
            return param;

        },
        validate: function (param) {

            // 빈 값이 있는지 검사
            for (var i = 0; i < param.length; i++) {
                var tmp = {
                    examCd: param[i].examCd,
                    hallDate: param[i].hallDate,
                    virtNoStart: param[i].virtNoStart,
                    virtNoEnd: param[i].virtNoEnd,
                    headNm: param[i].headNm,
                    bldgNm: param[i].bldgNm,
                    hallNm: param[i].hallNm,

                    _virtNoStart: param[i]._virtNoStart,
                    _virtNoEnd: param[i]._virtNoEnd,
                    _hallDate: param[i]._hallDate,
                    _hallCd: param[i]._hallCd
                };

              /*  for (var obj in tmp) {
                    if (tmp[obj] == '') {
                        responseDialog.notify({msg: '빈 값을 확인하세요', closeAll: false});
                        return false;
                    }
                }*/
            }

            // 빈 값이 없다면
            $.ajax({
                url: 'system/modifyStep2',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(param),
                success: function () {
                    $('#search').trigger('click');
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                    $('#search').trigger('click');
                }
            });
        }
    });
});