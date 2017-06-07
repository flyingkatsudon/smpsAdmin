define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/status-dept.js');
    var Summary = require('../grid/status-summary.js');
    var Toolbar = require('../toolbar/status-dept.js');
    var Template = require('text!/tpl/status-dept.html');
    var InnerTemplate = require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        render: function () {

            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);

            var param = window.param;

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this, param: param}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/all', param: param});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this, param: param}).render();

        }, search: function (o) {
            this.list.search(o);
            this.summary.render(o);
        }
    });
});