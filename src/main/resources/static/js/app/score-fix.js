define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/score-fix.js');
    var Toolbar = require('../toolbar/score-fix.js');
    var Template = require('text!/tpl/score-fix.html');

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