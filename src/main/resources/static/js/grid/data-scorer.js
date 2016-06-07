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
                {name: 'virtNo', label: '가번호'},
                {name: 'scorerNm', label: '평가위원'},
                {name: 'score01', label: '항목1'},
                {name: 'score02', label: '항목2'},
                {name: 'score03', label: '항목3'},
                {name: 'score04', label: '항목4'},
                {name: 'score05', label: '항목5'},
                {name: 'score06', label: '항목6'},
                {name: 'score07', label: '항목7'},
                {name: 'score08', label: '항목8'},
                {name: 'score09', label: '항목9'},
                {name: 'score10', label: '항목10'},
                {name: 'totalScore', label: '총점'},
                {name: 'memo', label: '메모'},
                {name: 'isPhoto', label: '사진', formatter: 'select', editoptions: {value: {true: 'Y', false: 'N'}}},
                {name: 'scoreDttm', label: '등록시간'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/scorer.json',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/scorer.xlsx');
            return this;
        }
    });
});