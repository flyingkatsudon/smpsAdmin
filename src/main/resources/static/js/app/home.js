define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Chart1 = require('../chart/status-all-donut.js');
    var Chart2 = require('../chart/status-all-bar.js');
    var Template = require('text!/tpl/home.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.chart1 = new Chart1({el: '#hm-ui-chart-all-1'}).render();
            this.chart2 = new Chart2({el: '#hm-ui-chart-all-2'}).render();
        }, search: function (o) {
            this.list.search(o);
        }
    });
});