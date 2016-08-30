define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Summary = require('../grid/status-summary.js');
    var Template = require('text!/tpl/home.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.summary = new Summary({el: '#hm-ui-summary'}).render();
        }, search: function (o) {
            this.list.search(o);
        }
    });
});