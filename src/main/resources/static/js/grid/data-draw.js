define(function (require) {
    "use strict";
    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [];

            $.ajax({
                url: 'data/draw.colmodel',
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
                    url: 'data/draw.json',
                    colModel: colModel,
                    sortname : 'rank',
                    loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 0; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.cnt > 1) $(this).setRowData(ids[i], false, {background: "#f5a7a4"});
                        }
                    }
                },
                scrollable: true
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.$grid.closest('.ui-jqgrid-bdiv').css('overflow-x', 'auto');
            this.addExcel('data/draw.xlsx');
            return this;
        }
    });
});