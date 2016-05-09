define(function (require) {
    "use strict";

    require('morris');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            this.chart = Morris.Donut({
                element: this.el.id,
                data: [{label: '응시율', value: 0.00}, {label: '결시율', value: 100.00}],
                formatter: function (y, data) {
                    return y + '%'
                }
            });
            this.search();
            this.resize();
            return this;
        }, resize: function () {
            var _this = this;
            $(window).bind('resizeEnd.Morris' + this.cid, function () {
                _this.chart.redraw();
            });
        }, close: function () {
            $(window).unbind('resizeEnd.Morris' + this.cid);
        }, search: function (o) {
            var _this = this;
            $.ajax({
                url: 'status/all/chart',
                data: o
            }).done(function (response) {
                _this.chart.setData([
                    {label: '응시율', value: response.attendPer},
                    {label: '결시율', value: response.absentPer}
                ]);
            });
        }
    });
});