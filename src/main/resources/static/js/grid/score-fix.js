define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'examDate', label: '시험일자'},
                {name: 'examTime', label: '시험시간'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'examNm', label: '시험명'},
                {name: 'headNm', label: '고사본부'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'virtNo', label: '가번호'},
                {name: 'scorerNm', label: '평가위원'},
                {name: 'updateCnt', label: '수정횟수'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'score/fix.json',
                    colModel: colModel,
                    onSelectRow : function(rowid, status, e){
                        var rowdata = $(this).jqGrid('getRowData', rowid);
                        BootstrapDialog.show({
                            title : rowdata.scorerNm + '::' + rowdata.examNm + "::" + rowdata.examDate + " " + rowdata.examTime,
                            message: '평가수정 이력',
                            size: 'size-wide',
                            closable: true,
                            buttons: [{
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }]
                        });
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('score/scoreFix.xlsx');
            return this;
        }
    });
});