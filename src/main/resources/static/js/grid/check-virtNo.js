define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'admissionNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'examNm', label: 'APP UI'},
                {name: 'hallNm', label: '고사실'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'virtNo', label: '가번호'},
                {name: 'scorerCnt', label: '평가위원수'},
                {name: 'finishScorerCnt', label: '채점평가위원수'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'check/virtNo/list',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('check/virtNo/xlsx');
            return this;
        }
    });
});