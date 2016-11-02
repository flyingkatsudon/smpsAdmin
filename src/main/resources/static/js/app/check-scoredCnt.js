define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/check-scoredCnt.js');
    var Toolbar = require('../toolbar/check-scoredCnt.js');
    var Template = require('text!/tpl/check-scoredCnt.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();
        }, search: function (o) {
            this.list.search(o);
        }
    });
});