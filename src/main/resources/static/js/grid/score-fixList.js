define(function (require) {
    "use strict";

    require('jqgrid');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        initialize: function (options) {

            var colModel = [];

            $.ajax({
                url: 'score/fixList.colmodel',
                async: false,
                success: function (data) {
                    colModel = data;
                }
            });

            this.options = $.extend(true, {
                defaults: {
                    styleUI: 'Bootstrap',
                    url: 'score/fixList.json?virtNo=' + options.virtNo + '&scorerNm=' + options.scorerNm,
                    colModel: colModel
                }
            });

            this.$grid = $(document.createElement('table')).attr('id', 'fixList');
        },
        render: function () {
            this.$el.empty().append(this.$grid);
            this.$grid.jqGrid(this.options.defaults);
            this.$grid.jqGrid('setGridWidth', this.$el.width());
            this.$('.ui-jqgrid .ui-jqgrid-bdiv').css('overflow-x', 'hidden');

            if (this.options.defaults.url) this.$grid.jqGrid('setGridParam', {
                datatype: 'json'
            }).trigger('reloadGrid');

            return this;
        }
    });
});