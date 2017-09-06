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

                            /*
                             /!**
                             * 기본 정보 : 수정가능
                             *
                             * 1. 계열 (typeNm)
                             * 2. 평가 (examNm)
                             * 3. 단계 (period)
                             * 4. 시험일자 (hallDate)    *주의! examDate 가 아님!
                             *
                             * @type {string}
                             *!/
                             // 기본정보 설정값

                             var basic1Html = '<div style="margin:3% 0 0 3%;">시험구분<input type="text" id="examName" class="set-basic" value="' + rowData.examNm + '"/></div>'; // 평가 (examNm)
                             basic1Html += '<div style="margin:3% 0 0 3%;">계&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;열<input type="text" id="typeNm" class="set-basic" value="' + rowData.typeNm + '"/></div>';   // 계열 (typeNm)
                             basic1Html += '<div style="margin:3% 0 0 3%;">수험번호<input type="number" min="0" id="examineeLen" class="set-short" value="' + rowData.examineeLen + '"/>&nbsp;자리</div>';
                             basic1Html += '<div style="margin:3% 0 0 3%;">평가항목<input type="number" min="0" id="itemCnt" class="set-short" value="' + rowData.itemCnt + '"/>&nbsp;개</div>';

                             var basic2Html = '<div style="margin:3% 0 0 3%;">단&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;계<input type="text" id="period" class="set-basic" value="' + rowData.period + '"/></div>';   // 단계 (period)
                             basic2Html += '<div style="margin:3% 0 0 3%;">시험일자<input type="text" id="hallDate" class="set-basic" value="' + rowData.hallDate + '"/></div>';   // 시험일자 (hallDate)
                             basic2Html += '<div style="margin:3% 0 0 3%;">평가위원<input type="number" min="0" id="scorerCnt" class="set-short" value="' + rowData.scorerCnt + '"/>&nbsp;명</div>';
                             basic2Html += '<div style="margin:3% 0 0 3%;">총&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;점<input type="number" min="0" id="totSccore" class="set-short" value="' + rowData.totScore + '"/>&nbsp;점</div>';

                             $('#basicPart1').append(basic1Html);
                             $('#basicPart2').append(basic2Html);

                             /!**
                             * 세부 정보 : 수정 가능
                             *
                             *  1. 조정점수              (adjust)
                             *  2. 바코드 종류           (barcodeType)       *TODO: 확인필요
                             *  3. 수험번호 자릿수       (examineeLen)
                             *  4. 결시 사용 여부        (isAbsence)
                             *  5. 마감데이터 사용 여부  (isClosedView)
                             *  6. 채점방향              (isHorizontal)
                             *  7. 가번호 자동 할당 여부 (isMgrAuto)
                             *  8. 지정이동 사용 여부    (isMove)
                             *  9. 항목 갯수             (itemCnt)
                             * 10. 점수 입력패드 종류    (keypadType)       *TODO: selectBox 로 대체 가능하지 않을까?
                             * 11. 평가표 내용 1         (printContent1)
                             * 12. 평가표 내용 2         (printContent2)
                             * 13. 평가표 제목 1         (printTitle1)
                             * 14. 평가표 제목 2         (printTitle2)
                             * 15. 평가 인원수           (scorerCnt)        *기본값 : 0
                             * 16. 총점                  (totScore)         *기본값 : 0
                             * 17. 가번호 표시 자릿수    (virtNoDigits)
                             * 18. 가번호 표시 내용      (virtNoType)
                             *
                             * 19. 가번호 시작번호       (virtNoStart)      *기본값 : 1
                             * 20. 가번호 종료번호       (virtNoEnd)        *기본값 : 1
                             *
                             * @type {string}
                             *!/
                             // 세부정보 설정값
                             //TODO: 확인 필요!
                             var detail1Html = '<div style="margin:3% 0 0 3%;">조정&nbsp;&nbsp;&nbsp;&nbsp;점수<input type="text" id="adjust" class="set-short" value="' + rowData.adjust + '"/></div>';
                             detail1Html += '<div style="margin:3% 0 0 3%;">키패드 타입<input type="number" min="0" id="keypadType" class="set-short" value="' + rowData.keypadType + '"/></div>';
                             detail1Html += '<div style="margin:3% 0 0 3%;">바코드&nbsp;종류<input type="text" id="barcodeType" class="set-mid" value="' + rowData.barcodeType + '"/></div>';
                             detail1Html += '<div style="margin:3% 0 0 3%;">평가자&nbsp;명칭<input type="text" id="printSign" class="set-mid" value="' + rowData.printSign + '"/></div>';

                             var detail2Html = '<div style="margin:3% 0 0 3%;">가번호&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="number" min="0" id="virtNoDigits" class="set-short" value="' + rowData.virtNoDigits + '"/>&nbsp;자리</div>';
                             detail2Html += '<div style="margin:3% 0 0 3%;">가번호 표시<input type="text" id="virtNoType" class="set-mid" value="' + rowData.virtNoType + '"/></div>';
                             detail2Html += '<div style="margin:3% 0 0 3%;">가번호 시작<input type="number" min="1" id="virtNoStart" class="set-mid" value="' + rowData.virtNoStart + '"/></div>';
                             detail2Html += '<div style="margin:3% 0 0 3%;">가번호 종료<input type="number" min="1" id="virtNoEnd" class="set-mid" value="' + rowData.virtNoEnd + '"/></div>';

                             $('#detailPart1').append(detail1Html);
                             $('#detailPart2').append(detail2Html);

                             //TODO: select 박스 도입 필요
                             var detail3Html = '<div style="margin:3% 0 0 3%; padding-bottom: 6%">결시&nbsp;&nbsp;&nbsp;&nbsp;버튼'
                             + '<input type="radio" name="isAbsence" id="isAbsenceTrue" class="set-radioBtn" value=true />&nbsp;사용'
                             + '<input type="radio" name="isAbsence" id="isAbsenceFalse" class="set-radioBtn" value=false />&nbsp;미사용'
                             + '</div>';

                             detail3Html += '<div style="margin:3% 0 0 3%; padding-bottom: 6%">마감 데이터'
                             + '<input type="radio" name="isClosedView" id="isClosedViewTrue" class="set-radioBtn" value=true />&nbsp;사용'
                             + '<input type="radio" name="isClosedView" id="isClosedViewFalse" class="set-radioBtn" value=false />&nbsp;미사용'
                             + '</div>';

                             detail3Html += '<div style="margin:3% 0 0 3%; padding-bottom: 6%">채점&nbsp;&nbsp;&nbsp;&nbsp;방향'
                             + '<input type="radio" name="isHorizontal" id="isHorizontalTrue" class="set-radioBtn" value=true />&nbsp;가로'
                             + '<input type="radio" name="isHorizontal" id="isHorizontalFalse" class="set-radioBtn" value=false />&nbsp;세로'
                             + '</div>';

                             detail3Html += '<div style="margin:3% 0 0 3%; padding-bottom: 6%">가번호 할당'
                             + '<input type="radio" name="isMgrAuto" id="isMgrAutoTrue" class="set-radioBtn" value=true />&nbsp;자동'
                             + '<input type="radio" name="isMgrAuto" id="isMgrAutoFalse" class="set-radioBtn" value=false />&nbsp;수동'
                             + '</div>';

                             detail3Html += '<div style="margin:3% 0 0 3%; padding-bottom: 6%">지정&nbsp;&nbsp;&nbsp;&nbsp;이동'
                             + '<input type="radio" name="isMove" id="isMoveTrue" class="set-radioBtn" value=true />&nbsp;사용'
                             + '<input type="radio" name="isMove" id="isMoveFalse" class="set-radioBtn" value=false />&nbsp;미사용'
                             + '</div>';

                             $('#detailPart3').append(detail3Html);

                             // 평가표 설정값
                             var detail4Html = '<div style="padding: 2% 0 0 2%">평가표&nbsp;제목&nbsp;1<input type="text" id="printTitle1" class="sheet" value="' + rowData.printTitle1 + '"/></div>';
                             detail4Html += '<div style="padding: 2% 0 0 2%">평가표&nbsp;제목&nbsp;2<input type="text" id="printTitle2" class="sheet"  value="' + rowData.printTitle2 + '"/></div>';
                             detail4Html += '<div style="padding: 2% 0 0 2%">평가표&nbsp;내용&nbsp;1<input type="text" id="printContent1" class="sheet"  value="' + rowData.printContent1 + '"/></div>';
                             detail4Html += '<div style="padding: 2% 0 0 2%">평가표&nbsp;내용&nbsp;2<input type="text" id="printContent2" class="sheet" value="' + rowData.printContent2 + '"/></div>';

                             $('#detailPart4').append(detail4Html);

                             /!**
                             * input[type="radio"] set checked
                             *
                             * 1. 결시 사용 여부          (isAbsence)
                             * 2. 마감데이터 사용 여부    (isClosedView)
                             * 3. 채점방향                (isHorizontal)
                             * 4. 가번호 자동 할당 여부   (isMgrAuto)
                             * 5. 지정이동 사용 여부      (isMove)
                             *!/
                             $('input:radio[name="isAbsence"][value="' + rowData.isAbsence + '"]').prop('checked', true);
                             $('input:radio[name="isClosedView"][value="' + rowData.isClosedView + '"]').prop('checked', true);
                             $('input:radio[name="isHorizontal"][value="' + rowData.isHorizontal + '"]').prop('checked', true);
                             $('input:radio[name="isMgrAuto"][value="' + rowData.isMgrAuto + '"]').prop('checked', true);
                             $('input:radio[name="isMove"][value="' + rowData.isMove + '"]').prop('checked', true);*/
                            //step1.getModalDialog().hide();

                            for (var i = 0; i < response.length; i++) {
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

                for (var obj in tmp) {
                    if (tmp[obj] == '') {
                        responseDialog.notify({msg: '빈 값을 확인하세요', closeAll: false});
                        return false;
                    }
                }
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