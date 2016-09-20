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

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/draw.json',
                    colModel: colModel
                },
                scrollable: false
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/draw.xlsx');
            return this;
        }
    });
});