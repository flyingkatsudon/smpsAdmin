define(function (require) {
    "use strict";
    var GridBase = require('../dist/jqgrid.js');
    var GetUrl = require('./../getUrl.js');

    var JSON = '.json';
    var XLSX = '.xlsx';

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            this.param = options.param;
            this.baseName = options.baseName;

            var colModel = [];

            $.ajax({
                url: 'data/scorerH.colmodel',
                async: false,
                success: function (data) {
                    colModel = data;
                }
            });

            for (var i = 0; i < colModel.length; i++) {
                var col = colModel[i];
                col['fixed'] = true;
                col['width'] = 100;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: new GetUrl({baseName: this.baseName, suffix: JSON, param: this.param}).getUrl(),
                    colModel: colModel,
                    loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 0; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.totalScore1 == '결시' && rowData.totalScore2 == '결시')
                                $(this).setRowData(ids[i], false, {background: "#d9edf7"});
                            else if (rowData.totalScore1 == '결시' || rowData.totalScore2 == '결시')
                                $(this).setRowData(ids[i], false, {background: "#f5a7a4"});
                        }
                    }
                },
                scrollable: false
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.$grid.closest('.ui-jqgrid-bdiv').css('overflow-x', 'auto');
            this.addExcel(new GetUrl({baseName: this.baseName, suffix: XLSX, param: this.param}).getUrl());
            return this;
        }
    });
});