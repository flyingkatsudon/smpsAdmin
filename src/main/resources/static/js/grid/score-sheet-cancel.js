define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'logNo', label: '로그번호'},
                {name: 'writeDttm', label: '기록시간'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'admissionNm', label: '전형'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'scorerNm', label: '평가위원'},
                {name: 'cancelMemo', label: '취소메모'},
                {name: 'uuid', label: 'UUID'},
                {name: 'cancelNo', label: '취소번호'},
                {name: 'cancelDttm', label: '취소시간'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'score/sheet-cancel/list',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('score/sheet-cancel/xlsx');
            return this;
        }
    });
});