define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/status-dept.js');
    var Summary = require('../grid/status-summary.js');
    var Chart1 = require('../chart/status-dept.js');
    var Toolbar = require('../toolbar/status-dept.js');
    var Template = require('text!/tpl/status-dept.html');
    var InnerTemplate = require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.chart1 = new Chart1({el: '#hm-ui-chart'}).render();
            this.summary = new Summary({el: '#hm-ui-summary'}).render();
            this.list = new List({el: '.hm-ui-grid', parent:this}).render();
        }, search: function (o) {
            this.list.search(o);
        }, renderChart: function(o){
            this.chart1.search(o);
        }
    });
});