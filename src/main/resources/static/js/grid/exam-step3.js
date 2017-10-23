/**
 * Created by Jeremy on 2017. 9. 5..
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var BootstrapDialog = require('bootstrap-dialog');

    var dialogStep3 = require('text!tpl/exam-step3.html');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    require('jquery-simple-datetimepicker');

    return Backbone.View.extend({
        render: function (rowData) {
            var _this = this;

            // 1. item에서 데이터 가져옴
            $.ajax({
                url: 'system/getStep3?examCd=' + rowData.examCd,
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                success: function (response) {
                    var step3 = new BootstrapDialog({
                        title: '<h3>' + rowData.admissionNm + ' / ' + rowData.examNm + '</h3>',
                        size: 'size-wide',
                        closable: false,
                        onshown: function (dialogRef) {
                            var body = dialogRef.$modalBody;
                            body.append(dialogStep3);

                            for (var i = 0; i < response.length; i++) {

                                var keypadType = response[i]['keypadType'];

                                if (keypadType == 0) {
                                    if(response[i].maxWarning == undefined) response[i].maxWarning = 9;
                                    if(response[i].minWarning == undefined) response[i].minWarning = 0;
                                    response[i].scoreMap = '';
                                } else if (keypadType == 2) {
                                    response[i].maxWarning = '';
                                    response[i].minWarning = '';
                                    response[i].scoreMap = '';
                                } else if (keypadType.includes('3.')){
                                    response[i].maxWarning = '';
                                    response[i].minWarning = '';
                                    response[i].scoreMap = '';
                                } else if (keypadType == 4) {
                                    response[i].maxWarning = '';
                                    response[i].minWarning = '';
                                } else if (keypadType == 5) {
                                    response[i].maxWarning = '';
                                    response[i].minWarning = '';
                                    response[i].scoreMap = '';
                                }

                                var html = '';
                                // 경고점수가 필요없는 서류평가 등의 경우
                                if (keypadType == 4) {
                                    var tmp =
                                        '<h5>' +
                                        '<div id="' + response[i].id + '" class="col-lg-12">' +
                                        '<div class="col-lg-4">' +
                                        '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                        '<div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '항목명' + '<input type="text" id="itemNm' + i + '" class="set-basic"  value="' + response[i].itemNm + '"></div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '순&nbsp;&nbsp;&nbsp;&nbsp;서' + '<input type="text" id="itemNo' + i + '" class="set-basic"  value="' + response[i].itemNo + '"></div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="col-lg-4">' +
                                        '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                        '<div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '최댓점' + '<input type="text" id="maxScore' + i + '" class="set-mid"  value="' + response[i].maxScore + '"></div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '최솟점' + '<input type="text" id="minScore' + i + '" class="set-mid"  value="' + response[i].minScore + '"></div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="col-lg-4">' +
                                        '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                        '<div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '키패드타입' + '<input style="width: 60%" type="text" id="keypadType' + i + '" class="set-lg"  value="' + response[i].keypadType + '"></div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '점수&nbsp;&nbsp;&nbsp;&nbsp;매핑' + '<input style="width: 60%" type="text" id="scoreMap' + i + '" class="set-lg"  value="' + response[i].scoreMap + '"></div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</h5>';

                                    html += tmp;
                                } else {
                                    var tmp =
                                        '<h5>' +
                                        '<div id="' + response[i].id + '" class="col-lg-12">' +
                                        '<div class="col-lg-3">' +
                                        '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                        '<div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '항목명' + '<input type="text" id="itemNm' + i + '" class="set-basic"  value="' + response[i].itemNm + '"></div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '순&nbsp;&nbsp;&nbsp;&nbsp;서' + '<input type="text" id="itemNo' + i + '" class="set-basic"  value="' + response[i].itemNo + '"></div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="col-lg-3">' +
                                        '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                        '<div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '최댓점' + '<input type="text" id="maxScore' + i + '" class="set-mid"  value="' + response[i].maxScore + '"></div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '최솟점' + '<input type="text" id="minScore' + i + '" class="set-mid"  value="' + response[i].minScore + '"></div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="col-lg-3">' +
                                        '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                        '<div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '최대경고점수' + '<input type="text" id="maxWarning' + i + '" class="set-mid"  value="' + response[i].maxWarning + '"></div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '최소경고점수' + '<input type="text" id="minWarning' + i + '" class="set-mid"  value="' + response[i].minWarning + '"></div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '<div class="col-lg-3">' +
                                        '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                                        '<div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '키패드타입' + '<input type="text" id="keypadType' + i + '" class="set-lg"  value="' + response[i].keypadType + '"></div>' +
                                        '<div style="margin:3% 0 0 3%;">' + '점수&nbsp;&nbsp;&nbsp;&nbsp;매핑' + '<input type="text" id="scoreMap' + i + '" class="set-lg"  value="' + response[i].scoreMap + '"></div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</div>' +
                                        '</h5>';

                                    html += tmp;
                                }

                                if (i != response.length - 1)
                                    html += '<div class="col-lg-12"><div id="line" style="margin: 3% 0 3% 0; border: 1px solid #d8d6d5"></div></div>';
                                $('#item').append(html);
                            }
                        }, // onShown
                        buttons: [
                            {
                                label: '변경 내용 저장',
                                cssClass: 'btn-primary',
                                action: function () {

                                    var param = [];

                                    for (var i = 0; i < response.length; i++) {
                                        var tmp = {
                                            id: response[i].id,
                                            examCd: rowData.examCd,
                                            itemNm: $('#itemNm' + i).val(),
                                            itemNo: $('#itemNo' + i).val(),
                                            maxScore: $('#maxScore' + i).val(),
                                            minScore: $('#minScore' + i).val(),
                                            maxWarning: $('#maxWarning' + i).val(),
                                            minWarning: $('#minWarning' + i).val(),
                                            keypadType: $('#keypadType' + i).val(),

                                            _itemNm: response[i].itemNm,
                                            _itemNo: response[i].itemNo
                                        };

                                        if ($('#keypadType' + i).val().includes('3.')){
                                            tmp.maxWarning = null;
                                            tmp.minWarning = null;
                                            tmp.scoreMap = null;
                                        }

                                        param.push(tmp);

                                    }
                                    // 유효성 검사
                                    _this.validate(param);
                                } // button action
                            },
                            {
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }] // buttons
                    }); // BootstrapDialog

                    step3.realize();
                    step3.getModalDialog().css('width', '90%');
                    step3.open();

                }, // success
                error: function (response) {
                    responseDialog.notify({msg: response.JSON});
                }
            }); // ajax
        },
        validate: function (param) {

            // 빈 값이 있는지 검사
            for (var i = 0; i < param.length; i++) {
                var tmp = {
                    id: param[i].id,
                    examCd: param[i].examCd,
                    itemNm: param[i].itemNm,
                    itemNo: param[i].itemNo,
                    maxScore: param[i].maxScore,
                    minScore: param[i].minScore,
                    maxWarning: param[i].maxWarning,
                    minWarning: param[i].minWarning,
                    keypadType: param[i].keypadType,

                    _itemNm: param[i]._itemNm,
                    _itemNo: param[i]._itemNo
                };

                for (var obj in tmp) {
                    if (obj != 'keypadType') {
                        if (tmp[obj] == '') {
                            responseDialog.notify({msg: '빈 값을 확인하세요', closeAll: false});
                            return false;
                        }
                    }
                }
            }

            // 빈 값이 없다면
            $.ajax({
                url: 'system/modifyStep3',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(param),
                success: function (response) {
                    responseDialog.notify({msg: response});
                    $('#search').trigger('click');
                    return true;
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                    $('#search').trigger('click');
                }
            });
        }
    });
});