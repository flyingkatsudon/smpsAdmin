define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var GetUrl = require('./../getUrl.js');
    var DlgPdf = require('../dist/dlg-pdf.js');

    var JSON = '.json';
    var XLSX = '.xlsx';
    var PDF = '.pdf';

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            this.param = options.param;
            this.baseName = options.baseName;
            this.dlgView = new DlgPdf();

            var colModel = [
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'virtNo', label: '가번호'},
                {name: 'groupNm', label: '조'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: new GetUrl({baseName: this.baseName, suffix: JSON, param: this.param}).getUrl(),
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            console.log(this.param);
            this.addPdf(new GetUrl({baseName: this.baseName, suffix: PDF, param: this.param}).getUrl());
            return this;
        }
    });
});