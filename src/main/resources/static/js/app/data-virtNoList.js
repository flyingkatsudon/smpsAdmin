define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/data-virtNoList.js');
    var Summary = require('../grid/status-summary.js');
    var Toolbar = require('../toolbar/data-virtNoList.js');
    var Template = require('text!/tpl/data-virtNoList.html');
    var InnerTemplate = require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid'}).render();
        }, search: function (o) {
            this.list.search(o);
        }
    });
});