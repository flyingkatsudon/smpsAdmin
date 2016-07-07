define(function (require) {
    "use strict";
    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [];

            $.ajax({
                url: 'data/scorerH.colmodel',
                async: false,
                success: function (data) {
                    colModel = data;
                }
            });

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/scorerH.json',
                    colModel: colModel
                },
                scrollable: false
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/scorerH.xlsx');
            return this;
        }
    });
});