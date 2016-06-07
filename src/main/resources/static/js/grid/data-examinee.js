define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'examDate', label: '시험일자'},
                {name: 'examTime', label: '시험시간'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'headNm', label: '고사본부'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'virtNo', label: '가번호'},
                {name: 'avgScore01', label: '항목1평균'},
                {name: 'avgScore02', label: '항목2평균'},
                {name: 'avgScore03', label: '항목3평균'},
                {name: 'avgScore04', label: '항목4평균'},
                {name: 'avgScore05', label: '항목5평균'},
                {name: 'avgScore06', label: '항목6평균'},
                {name: 'avgScore07', label: '항목7평균'},
                {name: 'avgScore08', label: '항목8평균'},
                {name: 'avgScore09', label: '항목9평균'},
                {name: 'avgScore10', label: '항목10평균'},
                {name: 'totalScore', label: '합계'},
                {name: 'scorerCnt', label: '평가위원수'},
                {name: 'memo', label: '메모'},
                {name: 'isAttend', label: '응시여부', formatter: 'select', editoptions: {value: {true: '응시', false: '미응시'}}}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/examinee.json',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/examinee.xlsx');
            return this;
        }
    });
});