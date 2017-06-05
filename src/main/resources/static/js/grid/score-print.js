define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var DlgPdf = require('../dist/dlg-pdf.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'examCd', label: '시험코드', hidden:true},
                {name: 'examDate', label: '시험일자'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallCd', label: '고사실코드', hidden:true},
                {name: 'hallNm', label: '고사실'},
                {name: 'scorerNm', label: '평가위원'},
                {name: 'sheetNo', label: '채점마감번호'},
                {name: 'regDttm', label: '채점마감시간'},
                {name: 'cancelDttm', label: '취소시간'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'score/print.json',
                    colModel: colModel/*,
                    onSelectRow : function(rowid, status, e){
                        var param = $(this).jqGrid('getRowData', rowid);
                        new DlgPdf().setUrl('score/detail.pdf?' + $.param(param)).render();
                    }*/
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('score/print.xlsx');
            return this;
        }
    });
});