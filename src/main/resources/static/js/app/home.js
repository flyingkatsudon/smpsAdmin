define(function (require) {
    "use strict";
    var Backbone = require('backbone');
/*
    var Summary = require('../grid/status-summary.js');
    var Template = require('text!/tpl/home.html');
*/

    // 가번호 배정 현황만 보이게 하기
    var Toolbar = require('../toolbar/data-virtNoList.js');
    var VirtNoList = require('../grid/data-virtNoList.js');
    var Template = require('text!/tpl/data-virtNoList.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            // this.summary = new Summary({el: '#hm-ui-summary'}).render();

            // 가번호 배정 현황만 보이게 하기
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.virtNoList = new VirtNoList({el: '.hm-ui-grid'}).render();

        }, search: function (o) {
            this.list.search(o);
        }
    });
});