define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var GridBase = require('../dist/jqgrid.js');
    //var _ = require('underscore');
    var BootstrapDialog = require('bootstrap-dialog');
    var List = require('./score-fixList.js');

    var GetUrl = require('./../getUrl.js');

    var JSON = '.json';
    var XLSX = '.xlsx';

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            this.param = options.param;
            this.baseName = options.baseName;

            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'examDate', label: '시험일자'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
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
                    url: new GetUrl({baseName: this.baseName, suffix: JSON, param: this.param}).getUrl(),
                    colModel: colModel,
                    onSelectRow: function (rowid, index, contents, event) {
                        var rowdata = $(this).jqGrid('getRowData', rowid);

                        BootstrapDialog.show({
                            title: '평가수정이력',
                            size: 'size-wide',
                            closable: true,
                            onshown: function (dialog) {
                                dialog.list = new List({el: dialog.$modalBody, virtNo: rowdata.virtNo, scorerNm: rowdata.scorerNm}).render();
                            },
                            buttons: [
                                {
                                    label: '닫기',
                                    action: function(dialog){
                                        dialog.close();
                                    }
                                }
                            ]

                        });
                    }
                }
            }, options);
            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('score/fix.xlsx');
            return this;
        }
    });
});