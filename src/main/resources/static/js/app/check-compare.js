define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/check-compare.js');
    var Toolbar = require('../toolbar/check-compare.js');
    var Template = require('text!/tpl/check-compare.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid'}).render();
        }, search: function (o) {
            this.list.search(o);
        }
    });
});