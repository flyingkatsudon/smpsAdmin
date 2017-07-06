/**
 * Created by eugene on 2017-07-05.
 */
define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    var dialogDetail = require('text!tpl/examInfo-detail.html');

    require('jquery-simple-datetimepicker');

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'examNm', label: '평가명'},
                {name: 'period', label: '단계'},
                {name: 'hallDate', label: '일자'},

                {name: 'examCd', hidden: true},         // 평가 코드
                {name: 'adjust', hidden: true},         // 조정점수 (예시 : 전체 합계 + adjust)
                {name: 'barcodeType', hidden: true},    // 바코드 종류

                {name: 'examineeLen', hidden: true},    // 수험번호 자릿수
                {name: 'isAbsence', hidden: true},      // 결시 사용 여부
                {name: 'isClosedView', hidden: true},   // 마감데이터 사용 여부
                {name: 'isHorizontal', hidden: true},   // 채점방향 (가로 vs 세로)
                {name: 'isMgrAuto', hidden: true},      // 가번호 자동 여부
                {name: 'isMove', hidden: true},         // 지정이동 사용 여부 (점수 입력 시, 지정이동 될 수 있음)
                {name: 'itemCnt', hidden: true},        // 항목 갯수
                {name: 'keypadType', hidden: true},     // 점수 입력 패드 타입

                {name: 'printContent1', hidden: true},
                {name: 'printContent2', hidden: true},
                {name: 'printSign', hidden: true},      // 평가표 서명부 (평가위원)
                {name: 'printTitle1', hidden: true},
                {name: 'printTitle2', hidden: true},
                {name: 'scorerCnt', hidden: true},      // 평가인원 수
                {name: 'totScore', hidden: true},       // 총점

                {name: 'virtNoDigits', hidden: true},   // 가번호 표시 자릿수
                {name: 'virtNoType', hidden: true},     // 가번호 표시 내용
                {name: 'fkExamCd', hidden: true},       // 상위 시험 코드
                {name: 'admissionCd', hidden: true},    // 전형 코드

                {name: 'virtNoEnd', hidden: true},      // 마지막 가번호
                {name: 'virtNoStart', hidden: true},    // 시작 가번호
                {name: 'hallCd', hidden: true},         // 고사실 코드

                {name: 'headNm', hidden: true},         // 고사본부
                {name: 'bldgNm', hidden: true},         // 고사건물
                {name: 'hallNm', hidden: true}          // 고사실
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'status/getExamInfo',
                    colModel: colModel,
                    onCellSelect: function (rowId, index, contents, event) {
                        var colModel = $(this).jqGrid('getGridParam', 'colModel');
                        var rowData = $(this).jqGrid('getRowData', rowId);

                        BootstrapDialog.show({
                            title: '<h3>' + rowData.admissionNm + ' / ' + rowData.examNm + '</h3>',
                            size: 'size-wide',
                            closable: false,
                            onshown: function (dialogRef) {
                                var body = dialogRef.$modalBody;
                                body.append(dialogDetail);

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
                                var basicHtml  = '<div style="margin:3% 0 0 3%;">'
                                              +      '계&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;열'
                                              +      '<input type="text" id="typeNm" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.typeNm + '"/>'
                                              +  '</div>';   // 계열 (typeNm)

                                    basicHtml += '<div style="margin:3% 0 0 3%;">'
                                              +      '평&nbsp;&nbsp;가&nbsp;&nbsp;명'
                                              +      '<input type="text" id="examNm" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.examNm + '"/>'
                                              +  '</div>';   // 평가 (examNm)

                                    basicHtml += '<div style="margin:3% 0 0 3%;">'
                                              +      '단&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;계'
                                              +      '<input type="text" id="period" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.period + '"/>'
                                              +  '</div>';   // 단계 (period)

                                    basicHtml += '<div style="margin:3% 0 0 3%;">'
                                              +      '시험일자'
                                              +      '<input type="text" id="hallDate" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.hallDate + '"/>'
                                              +  '</div>';   // 시험일자 (hallDate)

                                $('#basicPart').append(basicHtml);


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
                                 *
                                 * 19. 가번호 시작번호       (virtNoStart)      *기본값 : 1
                                 * 20. 가번호 종료번호       (virtNoEnd)        *기본값 : 1
                                 *
                                 * @type {string}
                                 */
                                var detailHtml  = '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '조&nbsp;정&nbsp;점&nbsp;수'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="text" id="adjust" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.adjust + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                //TODO: 확인 필요!
                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '바코드&nbsp;종류'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="text" id="barcodeType" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.barcodeType + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '수험번호&nbsp;자릿수'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="number" min="0" id="examineeLen" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.examineeLen + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '결시&nbsp;사용&nbsp;여부'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="radio" name="isAbsence" id="isAbsenceTrue" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=true />'
                                               +          '<label for="isAbsenceTrue">&nbsp;사용</label>'
                                               +          '<input type="radio" name="isAbsence" id="isAbsenceFalse" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=false />'
                                               +          '<label for="isAbsenceFalse">&nbsp;미사용</label>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '마담데이터&nbsp;사용&nbsp;여부'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="radio" name="isClosedView" id="isClosedViewTrue" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=true />'
                                               +          '<label for="isClosedViewTrue">&nbsp;사용</label>'
                                               +          '<input type="radio" name="isClosedView" id="isClosedViewFalse" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=false />'
                                               +          '<label for="isClosedViewFalse">&nbsp;미사용</label>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '채&nbsp;점&nbsp;방&nbsp;향'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="radio" name="isHorizontal" id="isHorizontalTrue" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=true />'
                                               +          '<label for="isHorizontalTrue">&nbsp;가로</label>'
                                               +          '<input type="radio" name="isHorizontal" id="isHorizontalFalse" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=false />'
                                               +          '<label for="isHorizontalFalse">&nbsp;세로</label>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '가번호&nbsp;자동&nbsp;할당&nbsp;여부'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="radio" name="isMgrAuto" id="isMgrAutoTrue" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=true />'
                                               +          '<label for="isMgrAutoTrue">&nbsp;사용</label>'
                                               +          '<input type="radio" name="isMgrAuto" id="isMgrAutoFalse" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=false />'
                                               +          '<label for="isMgrAutoFalse">&nbsp;미사용</label>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '지정이동&nbsp;사용&nbsp;여부'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="radio" name="isMove" id="isMoveTrue" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=true />'
                                               +          '<label for="isMoveTrue">&nbsp;사용</label>'
                                               +          '<input type="radio" name="isMove" id="isMoveFalse" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value=false />'
                                               +          '<label for="isMoveFalse">&nbsp;미사용</label>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '항목&nbsp;갯수'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="number" min="0" id="itemCnt" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.itemCnt + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                //TODO: select 박스 도입 필요
                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '점수&nbsp;입력패드&nbsp;종류'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="number" min="0" id="keypadType" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.keypadType + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '평가표&nbsp;제목&nbsp;1'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="text" id="printTitle1" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.printTitle1 + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '평가표&nbsp;내용&nbsp;1'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="text" id="printContent1" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.printContent1 + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '평가표&nbsp;제목&nbsp;2'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="text" id="printTitle2" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.printTitle2 + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '평가표&nbsp;내용&nbsp;2'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="text" id="printContent2" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.printContent2 + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '평가&nbsp;인원수'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="number" min="0" id="scorerCnt" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.scorerCnt + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '총&nbsp;&nbsp;&nbsp;&nbsp;점'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="number" min="0" id="totSccore" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.totScore + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '가번호&nbsp;표시&nbsp;자릿수'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="number" min="0" id="virtNoDigits" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.virtNoDigits + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '가번호&nbsp;표시&nbsp;내용'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="text" id="virtNoType" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.virtNoType + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '가번호&nbsp;시작번호'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="number" min="1" id="virtNoStart" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.virtNoStart + '"/>'
                                               +      '</div>'
                                               +  '</div>';

                                    detailHtml += '<div style="margin:3% 0 0 3%;">'
                                               +      '<div style="width:30%;float:left;">'
                                               +          '가번호&nbsp;종료번호'
                                               +      '</div>'
                                               +      '<div>'
                                               +          '<input type="number" min="1" id="virtNoEnd" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.virtNoEnd + '"/>'
                                               +      '</div>'
                                               +  '</div>';


                                $('#detailPart').append(detailHtml);

                                /**
                                 * input[type="radio"] set checked
                                 *
                                 * 1. 결시 사용 여부          (isAbsence)
                                 * 2. 마감데이터 사용 여부    (isClosedView)
                                 * 3. 채점방향                (isHorizontal)
                                 * 4. 가번호 자동 할당 여부   (isMgrAuto)
                                 * 5. 지정이동 사용 여부      (isMove)
                                 */
                                $('input:radio[name="isAbsence"][value="' + (rowData.isAbsence == 0 ? 'false' : 'true') + '"]').prop('checked', true);
                                $('input:radio[name="isClosedView"][value="' + (rowData.isClosedView == 0 ? 'false' : 'true') + '"]').prop('checked', true);
                                $('input:radio[name="isHorizontal"][value="' + (rowData.isHorizontal == 0 ? 'false' : 'true') + '"]').prop('checked', true);
                                $('input:radio[name="isMgrAuto"][value="' + (rowData.isMgrAuto == 0 ? 'false' : 'true') + '"]').prop('checked', true);
                                $('input:radio[name="isMove"][value="' + (rowData.isMove == 0 ? 'false' : 'true') + '"]').prop('checked', true);

                            }, // onShown
                            buttons: [{
                                id: 'modify',
                                label: '변경 내용 저장',
                                cssClass: 'btn-primary',
                                action: function (dialog) {
                                    var param = {
                                          examCd        : rowData.examCd
                                        , typeNm        : $('#typeNm').val()
                                        , examNm        : $('#examNm').val()
                                        , period        : $('#period').val()
                                        , hallDate      : $('#hallDate').val()

                                        , adjust        : $('#adjust').val()
                                        , barcodeType   : $('#barcodeType').val()
                                        , examineeLen   : $('#examineeLen').val()
                                        , isAbsence     : $('input:radio[name="isAbsence"]:checked').val()
                                        , isClosedView  : $('input:radio[name="isClosedView"]:checked').val()
                                        , isHorizontal  : $('input:radio[name="isHorizontal"]:checked').val()
                                        , isMgrAuto     : $('input:radio[name="isMgrAuto"]:checked').val()
                                        , isMove        : $('input:radio[name="isMove"]:checked').val()
                                        , itemCnt       : $('#itemCnt').val()
                                        , keypadType    : $('#keypadType').val()
                                        , printTitle1   : $('#printTitle1').val()
                                        , printContent1 : $('#printContent1').val()
                                        , printTitle2   : $('#printTitle2').val()
                                        , printContent2 : $('#printContent2').val()
                                        , scorerCnt     : $('#scorerCnt').val()
                                        , totScore      : $('#totScore').val()
                                        , virtNoDigits  : $('#virtNoDigits').val()
                                        , virtNoType    : $('#virtNoType').val()
                                        , virtNoStart   : $('#virtNoStart').val()
                                        , virtNoEnd     : $('#virtNoEnd').val()
                                    };

                                    //TODO: 빈 항목 체크, 개선 필요!
                                    var validated = true;
                                    for(var obj in param) {
                                        if (obj == '') {
                                            $('#detailPart').append('<div style="margin:10% 0 0 0; text-align:center"><h4 style="margin-top: 1%; color:crimson">빈 항목을 확인하세요</h4></div>');
                                            validated = false;
                                            return true;
                                        }
                                    }


                                    if (validated) {
                                        $.ajax({
                                            url: 'status/modifyExamInfo',
                                            type: 'POST',
                                            contentType: 'application/json; charset=utf-8',
                                            data: JSON.stringify(param),
                                            success: function (res) {
                                                BootstrapDialog.show({
                                                    title: '<h3>평가(시험) 정보 관리</h3>',
                                                    message: '<label><h4>변경되었습니다.</h4></label>',
                                                    buttons: [{
                                                        label: '닫기',
                                                        action: function (dialog) {
                                                            $('#search').trigger('click');
                                                            dialog.close();
                                                        }
                                                    }]
                                                });
                                            },
                                            error: function (req, status, err) {
                                                BootstrapDialog.show({
                                                    title: '<h3>오류</h3>',
                                                    message: '<label><h4>잠시뒤에 다시 시도해주세요.</h4></label>',
                                                    buttons: [{
                                                        label: '닫기',
                                                        action: function (dialog) {
                                                            dialog.close();
                                                        }
                                                    }]
                                                });
                                            }
                                        });
                                        dialog.close();
                                    } // validated
                                } // button action
                            }, {
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }] // buttons
                        }); // BootstrapDialog
                    } // onCellSelect
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            return this;
        }
    });
});