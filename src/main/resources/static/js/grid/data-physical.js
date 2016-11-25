define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {

            var colModel = [];

            $.ajax({
                url : 'data/physical.colmodel',
                async : false,
                success : function(data){
                    colModel = data;
                }
            });

            for(var i = 0; i < colModel.length; i++){
                var col = colModel[i];
                col['fixed'] = true;
                col['width'] = 220;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/physical.json',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.$grid.closest('.ui-jqgrid-bdiv').css('overflow-x', 'auto');
            this.addExcel('data/physical.xlsx');
            return this;
        }
    });
});