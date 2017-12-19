/**
 * Created by eugene on 2017-07-05.
 */
define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    var Step1 = require('./exam-step1.js');
    var step1 = new Step1();

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'examNm', label: '평가명'},
                {name: 'period', label: '단계'},

                {name: 'admissionCd', hidden: true},    // 전형 코드
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
                {name: 'totScore', hidden: true},        // 총점
                {name: 'isTimer', hidden: true},        // 타이머 사용 여부

                {name: 'printContent1', hidden: true},
                {name: 'printContent2', hidden: true},
                {name: 'printSign', hidden: true},      // 평가표 서명부 (평가위원)
                {name: 'printTitle1', hidden: true},
                {name: 'printTitle2', hidden: true},
                {name: 'scorerCnt', hidden: true},      // 평가인원 수

                {name: 'virtNoDigits', hidden: true},   // 가번호 표시 자릿수
                {name: 'virtNoType', hidden: true},     // 가번호 표시 내용
                {name: 'virtNoAssignType', hidden: true},     // 가번호 관리
                {name: 'fkExamCd', hidden: true},       // 상위 시험 코드

                {name: 'absentValue', hidden: true}     // 결시로 사용할 값
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/getStep1',
                    colModel: colModel,
                    onCellSelect: function (rowId, index, contents, event) {
                        var colModel = $(this).jqGrid('getGridParam', 'colModel');
                        var rowData = $(this).jqGrid('getRowData', rowId);

                        step1.render(rowData);

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