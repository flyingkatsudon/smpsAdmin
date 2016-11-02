define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'examDate', label: '시험일자'},
                /*{name: 'examTime', label: '시험시간'},*/
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'virtNo', label: '가번호'},
                {name: 'scorerCnt', label: '평가위원수'},
                {name: 'scoredCnt', label: '채점평가위원수'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'check/scorer.json',
                    colModel: colModel/*,
                    loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 0; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.scoredCnt) {
                                if (rowData.scoredCnt != rowData.scorerCnt)
                                    $(this).setRowData(ids[i], false, {background: "#f5a7a4"});
                                else
                                    $(this).setRowData(ids[i], false, {background: "#d9edf7"});
                            }
                        }
                    }*/
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('check/scorer.xlsx');
            return this;
        }
    });
});