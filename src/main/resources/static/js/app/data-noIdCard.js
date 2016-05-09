define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/data-noIdCard.js');
    var Toolbar = require('../toolbar/data-noIdCard.js');
    var Template = require('text!/tpl/data-noIdCard.html');

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