define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/status-group.js');
    var Chart1 = require('../chart/status-group.js');
    var Chart2 = require('../chart/status-all-donut.js');
    var Chart3 = require('../chart/status-all-bar.js');
    var Toolbar = require('../toolbar/status-group.js');
    var Template = require('text!/tpl/status-group.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid'}).render();
            this.chart1 = new Chart1({el: '#hm-ui-chart'}).render();
            this.chart2 = new Chart2({el: '#hm-ui-chart-all-1'}).render();
            this.chart3 = new Chart3({el: '#hm-ui-chart-all-2'}).render();
        }, search: function (o) {
            this.list.search(o);
            this.chart1.search(o);
        }
    });
});