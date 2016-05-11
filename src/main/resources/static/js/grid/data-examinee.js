define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'virtNo', label: '가번호'},
                {name: 'score1Avg', label: '항목1평균'},
                {name: 'score2Avg', label: '항목2평균'},
                {name: 'score3Avg', label: '항목3평균'},
                {name: 'scoreSum', label: '합계'},
                {name: 'scorerCnt', label: '평가위원수'},
                {name: 'memo', label: '메모'},
                {name: 'isAttend', label: '응시여부', formatter: 'select', editoptions: {value: {true: '응시', false: '미응시'}}}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/examinee/list',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/examinee/xlsx');
            return this;
        }
    });
});