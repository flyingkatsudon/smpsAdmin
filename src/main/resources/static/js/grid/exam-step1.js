define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var BootstrapDialog = require('bootstrap-dialog');

    var dialogStep1 = require('text!tpl/exam-step1.html');

    var Step2 = require('./exam-step2.js');
    var step2 = new Step2();

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        render: function (rowData) {
            var _this = this;
            var step1 = new BootstrapDialog({
                title: '<h3>' + rowData.admissionNm + ' / ' + rowData.examNm + '</h3>',
                closable: false,
                onshown: function (dialogRef) {
                    var body = dialogRef.$modalBody;
                    body.append(dialogStep1);

                    /**
                     * 기본 정보 : 수정가능
                     *
                     * 1. 계열 (typeNm)
                     * 2. 평가 (examNm)
                     * 3. 단계 (period)
                     * 4. 시험일자 (hallDate)    *주의! examDate 가 아님!
                     *
                     * @type {string}
                     */
                        // 기본정보 설정값

                    var basic1Html = '<div style="margin:3% 0 0 3%;">시험구분<input type="text" id="examName" class="set-basic" value="' + rowData.examNm + '"/></div>'; // 평가 (examNm)
                    basic1Html += '<div style="margin:3% 0 0 3%;">수험번호<input type="number" min="0" id="examineeLen" class="set-short" value="' + rowData.examineeLen + '"/>&nbsp;자리</div>';

                    var basic2Html = '<div style="margin:3% 0 0 3%;">계&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;열<input type="text" id="typeNm" class="set-basic" value="' + rowData.typeNm + '"/></div>';   // 계열 (typeNm)
                    basic2Html += '<div style="margin:3% 0 0 3%;">평가항목<input type="number" min="0" id="itemCnt" class="set-short" value="' + rowData.itemCnt + '"/>&nbsp;개</div>';
                    basic2Html += '<div style="margin:3% 0 0 3%;">총&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;점<input type="number" min="0" id="totScore" class="set-short" value="' + rowData.totScore + '"/></div>';

                    var basic3Html = '<div style="margin:3% 0 0 3%;">단&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;계<input type="text" id="period" class="set-basic" value="' + rowData.period + '"/></div>';   // 단계 (period)
                    basic3Html += '<div style="margin:3% 0 0 3%;">평가위원<input type="number" min="0" id="scorerCnt" class="set-short" value="' + rowData.scorerCnt + '"/>&nbsp;명</div>';

                    $('#basicPart1').append(basic1Html);
                    $('#basicPart2').append(basic2Html);
                    $('#basicPart3').append(basic3Html);

                    // 세부정보 설정값
                    //TODO: 확인 필요!
                    /**
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
                     * 18. 가번호 관리 방식      (virtNoAssignType)
                     *
                     * 19. 가번호 시작번호       (virtNoStart)      *기본값 : 1
                     * 20. 가번호 종료번호       (virtNoEnd)        *기본값 : 1
                     *
                     * @type {string}
                     */

                    var virtNoAssignType;
                    switch (rowData.virtNoAssignType) {
                        case 'virtNo':
                            virtNoAssignType = '가번호';
                            break;
                        case 'examineeCd':
                            virtNoAssignType = '수험번호';
                            break;
                        case 'manageNo':
                            virtNoAssignType = '관리번호';
                            break;
                        default:
                            virtNoAssignType = '가번호';
                    }


                    var detail1Html = '<div style="margin:3% 0 0 3%;">조정&nbsp;&nbsp;&nbsp;&nbsp;점수<input type="text" id="adjust" class="set-lg" value="' + rowData.adjust + '"/></div>';
                    detail1Html += '<div style="margin:3% 0 0 3%;">바코드&nbsp;종류<input type="text" id="barcodeType" class="set-lg" value="' + rowData.barcodeType + '"/></div>';
                    detail1Html += '<div style="margin:3% 0 0 3%;">평가자&nbsp;명칭<input type="text" id="printSign" class="set-lg" value="' + rowData.printSign + '"/></div>';
                    detail1Html += '<div style="margin:3% 0 0 3%;">가번호 표시<input type="text" id="virtNoType" class="set-lg" value="' + rowData.virtNoType + '"/></div>';
                    detail1Html += '<div style="margin:3% 0 0 3%;">가번호 관리<input type="text" id="virtNoAssignType" class="set-lg" value="' + virtNoAssignType + '"/></div>';
                    detail1Html += '<div style="margin:3% 0 0 3%;">가번호&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="number" min="0" id="virtNoDigits" class="set-short" value="' + rowData.virtNoDigits + '"/>&nbsp;&nbsp;자리</div>';

                    $('#detailPart1').append(detail1Html);

                    //TODO: select 박스 도입 필요
                    var detail2Html = '<div style="margin:3% 0 0 3%; padding-bottom: 2%">결시&nbsp;&nbsp;&nbsp;&nbsp;버튼'
                        + '<input type="radio" name="isAbsence" id="isAbsenceTrue" class="set-radioBtn" value=true />&nbsp;사용'
                        + '<input type="radio" name="isAbsence" id="isAbsenceFalse" class="set-radioBtn" value=false />&nbsp;미사용'
                        + '</div>';

                    detail2Html += '<div style="margin:3% 0 0 3%; padding-bottom: 2%">마감 데이터'
                        + '<input type="radio" name="isClosedView" id="isClosedViewTrue" class="set-radioBtn" value=true />&nbsp;사용'
                        + '<input type="radio" name="isClosedView" id="isClosedViewFalse" class="set-radioBtn" value=false />&nbsp;미사용'
                        + '</div>';

                    detail2Html += '<div style="margin:3% 0 0 3%; padding-bottom: 2%">채점&nbsp;&nbsp;&nbsp;&nbsp;방향'
                        + '<input type="radio" name="isHorizontal" id="isHorizontalTrue" class="set-radioBtn" value=true />&nbsp;가로'
                        + '<input type="radio" name="isHorizontal" id="isHorizontalFalse" class="set-radioBtn" value=false />&nbsp;세로'
                        + '</div>';

                    detail2Html += '<div style="margin:3% 0 0 3%; padding-bottom: 2%">가번호 할당'
                        + '<input type="radio" name="isMgrAuto" id="isMgrAutoTrue" class="set-radioBtn" value=true />&nbsp;자동'
                        + '<input type="radio" name="isMgrAuto" id="isMgrAutoFalse" class="set-radioBtn" value=false />&nbsp;수동'
                        + '</div>';

                    detail2Html += '<div style="margin:3% 0 0 3%; padding-bottom: 2%">지정&nbsp;&nbsp;&nbsp;&nbsp;이동'
                        + '<input type="radio" name="isMove" id="isMoveTrue" class="set-radioBtn" value=true />&nbsp;사용'
                        + '<input type="radio" name="isMove" id="isMoveFalse" class="set-radioBtn" value=false />&nbsp;미사용'
                        + '</div>';

                    detail2Html += '<div style="margin:3% 0 0 3%; padding-bottom: 2%">타이머&nbsp;사용'
                        + '<input type="radio" name="isTimer" id="isTimerTrue" class="set-radioBtn" value=true />&nbsp;사용'
                        + '<input type="radio" name="isTimer" id="isTimerFalse" class="set-radioBtn" value=false />&nbsp;미사용'
                        + '</div>';

                    $('#detailPart2').append(detail2Html);

                    // 평가표 설정값
                    var detail4Html = '<div style="padding: 2% 0 0 2%">평가표&nbsp;제목&nbsp;1<input type="text" id="printTitle1" class="sheet" value="' + rowData.printTitle1 + '"/></div>';
                    detail4Html += '<div style="padding: 2% 0 0 2%">평가표&nbsp;제목&nbsp;2<input type="text" id="printTitle2" class="sheet"  value="' + rowData.printTitle2 + '"/></div>';
                    detail4Html += '<div style="padding: 2% 0 0 2%">평가표&nbsp;내용&nbsp;1<input type="text" id="printContent1" class="sheet"  value="' + rowData.printContent1 + '"/></div>';
                    detail4Html += '<div style="padding: 2% 0 0 2%">평가표&nbsp;내용&nbsp;2<input type="text" id="printContent2" class="sheet" value="' + rowData.printContent2 + '"/></div>';

                    $('#detailPart4').append(detail4Html);

                    /**
                     * input[type="radio"] set checked
                     *
                     * 1. 결시 사용 여부          (isAbsence)
                     * 2. 마감데이터 사용 여부    (isClosedView)
                     * 3. 채점방향                (isHorizontal)
                     * 4. 가번호 자동 할당 여부   (isMgrAuto)
                     * 5. 지정이동 사용 여부      (isMove)
                     */
                    $('input:radio[name="isAbsence"][value="' + rowData.isAbsence + '"]').prop('checked', true);
                    $('input:radio[name="isClosedView"][value="' + rowData.isClosedView + '"]').prop('checked', true);
                    $('input:radio[name="isHorizontal"][value="' + rowData.isHorizontal + '"]').prop('checked', true);
                    $('input:radio[name="isMgrAuto"][value="' + rowData.isMgrAuto + '"]').prop('checked', true);
                    $('input:radio[name="isMove"][value="' + rowData.isMove + '"]').prop('checked', true);
                    $('input:radio[name="isTimer"][value="' + rowData.isTimer + '"]').prop('checked', true);
                }, // onShown
                buttons: [{
                    label: '변경 내용 저장',
                    cssClass: 'btn-primary',
                    action: function () {

                        var str = $('#virtNoAssignType').val();

                        if(str != '가번호' && str != '관리번호' && str != '수험번호'){
                            responseDialog.notify({msg: "'가번호 관리'를 확인하세요", closeAll: false});
                            return false;
                        }
                        _this.validate(rowData.examCd);
                        responseDialog.notify({msg: "변경되었습니다"});
                    }
                }, // modify
                    {
                        label: '시험일자 설정',
                        cssClass: 'btn-success',
                        action: function () {
                            _this.validate(rowData.examCd);
                            step1.close();
                            step2.render(rowData);
                        } // step2 action
                    },
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }] // buttons
            }); // BootstrapDialog

            step1.realize();
            step1.getModalDialog().css('width', '70%');
            step1.open();
        },
        validate: function (examCd) {

            var str;
            switch ($('#virtNoAssignType').val()) {
                case '가번호':
                    str = 'virtNo';
                    break;
                case '관리번호':
                    str = 'manageNo';
                    break;
                case '수험번호':
                    str = 'examineeCd';
                    break;
                default:
                    str = null;
            }

            var tmp = {
                examCd: examCd
                , typeNm: $('#typeNm').val()
                , examNm: $('#examName').val()
                , period: $('#period').val()
                // , hallDate: $('#hallDate').val()

                , adjust: $('#adjust').val()
                , barcodeType: $('#barcodeType').val()
                , examineeLen: $('#examineeLen').val()
                , isAbsence: $('input:radio[name="isAbsence"]:checked').val()
                , isClosedView: $('input:radio[name="isClosedView"]:checked').val()
                , isHorizontal: $('input:radio[name="isHorizontal"]:checked').val()
                , isMgrAuto: $('input:radio[name="isMgrAuto"]:checked').val()
                , isMove: $('input:radio[name="isMove"]:checked').val()
                , isTimer: $('input:radio[name="isTimer"]:checked').val()
                , itemCnt: $('#itemCnt').val()
                , totScore: $('#totScore').val()
                , printTitle1: $('#printTitle1').val()
                , printContent1: $('#printContent1').val()
                , printTitle2: $('#printTitle2').val()
                , printContent2: $('#printContent2').val()
                , scorerCnt: $('#scorerCnt').val()
                //, totScore: $('#totScore').val()
                , virtNoDigits: $('#virtNoDigits').val()
                , virtNoType: $('#virtNoType').val()
                , virtNoAssignType: str
            };

            /*//TODO: 빈 항목 체크, 개선 필요!
             var validated = true;
             for (var obj in tmp) {
             console.log(tmp[obj]);
             // 평가표 제목2 혹은 평가표 내용2가 아니면 빈 항목 검사
             if(obj != 'printTitle2' || obj != 'printContent2'){
             if (tmp[obj] == '') {
             responseDialog.notify({msg: '빈 항목을 확인하세요', closeAll: false});
             validated = false;
             return false;
             }
             }
             }*/

            $.ajax({
                url: 'system/modifyStep1',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify(tmp),
                success: function () {
                    $('#search').trigger('click');
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON, closeAll: false});
                    return false;
                }
            });
        }
    });
}); // onCellSelect