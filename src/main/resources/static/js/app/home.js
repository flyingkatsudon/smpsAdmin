define(function (require) {
    "use strict";
    var Backbone = require('backbone');
/*
    var Summary = require('../grid/status-summary.js');
    var Template = require('text!/tpl/home.html');
*/

    // 가번호 배정 현황만 보이게 하기
    var Summary = require('../grid/status-summary.js');
    var Toolbar = require('../toolbar/data-virtNoList.js');
    var VirtNoList = require('../grid/data-virtNoList.js');
    var Template = require('text!/tpl/data-virtNoList.html');
    var InnerTemplate = require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);
            // 가번호 배정 현황만 보이게 하기
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this});
            this.summary.render();
            this.list = new VirtNoList({el: '.hm-ui-grid', parent: this}).render();

        }, search: function (o) {
            this.list.search(o);
            this.summary.render(o);
        }
    });
});