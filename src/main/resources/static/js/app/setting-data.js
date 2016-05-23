define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/setting-data.html');
    var DlgDownload = require('../dist/dlg-download.js');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.dlgDownload = new DlgDownload();
        }, render: function () {
            this.$el.html(Template);
        }
    });
});