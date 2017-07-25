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
                    url: 'system/getExamInfo',
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
                                    // 세부정보 설정값
                                    //TODO: 확인 필요!
                                var detail1Html = '<div style="margin:3% 0 0 3%;">조정&nbsp;&nbsp;&nbsp;&nbsp;점수<input type="text" id="adjust" class="set-short" value="' + rowData.adjust + '"/></div>';
                                detail1Html += '<div style="margin:3% 0 0 3%;">키패드 타입<input type="number" min="0" id="keypadType" class="set-short" value="' + rowData.keypadType + '"/></div>';
                                detail1Html += '<div style="margin:3% 0 0 3%;">바코드&nbsp;종류<input type="text" id="barcodeType" class="set-mid" value="' + rowData.barcodeType + '"/></div>';

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

                            }, // onShown
                            buttons: [{
                                id: 'modify',
                                label: '변경 내용 저장',
                                cssClass: 'btn-primary',
                                action: function (dialog) {
                                    console.log($('#examName').val());
                                    var param = {
                                        examCd: rowData.examCd
                                        , typeNm: $('#typeNm').val()
                                        , examNm: $('#examName').val()
                                        , period: $('#period').val()
                                        , hallDate: $('#hallDate').val()

                                        , adjust: $('#adjust').val()
                                        , barcodeType: $('#barcodeType').val()
                                        , examineeLen: $('#examineeLen').val()
                                        , isAbsence: $('input:radio[name="isAbsence"]:checked').val()
                                        , isClosedView: $('input:radio[name="isClosedView"]:checked').val()
                                        , isHorizontal: $('input:radio[name="isHorizontal"]:checked').val()
                                        , isMgrAuto: $('input:radio[name="isMgrAuto"]:checked').val()
                                        , isMove: $('input:radio[name="isMove"]:checked').val()
                                        , itemCnt: $('#itemCnt').val()
                                        , keypadType: $('#keypadType').val()
                                        , printTitle1: $('#printTitle1').val()
                                        , printContent1: $('#printContent1').val()
                                        , printTitle2: $('#printTitle2').val()
                                        , printContent2: $('#printContent2').val()
                                        , scorerCnt: $('#scorerCnt').val()
                                        , totScore: $('#totScore').val()
                                        , virtNoDigits: $('#virtNoDigits').val()
                                        , virtNoType: $('#virtNoType').val()
                                        , virtNoStart: $('#virtNoStart').val()
                                        , virtNoEnd: $('#virtNoEnd').val()
                                    };

                                    //TODO: 빈 항목 체크, 개선 필요!
                                    var validated = true;
                                    for (var obj in param) {
                                        if (obj == '') {
                                            $('#detailPart').append('<div style="margin:10% 0 0 0; text-align:center"><h4 style="margin-top: 1%; color:crimson">빈 항목을 확인하세요</h4></div>');
                                            validated = false;
                                            return true;
                                        }
                                    }

                                    if (validated) {
                                        $.ajax({
                                            url: 'system/modifyExamInfo',
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