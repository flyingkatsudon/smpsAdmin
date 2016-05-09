define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/data-report.html');
    var DlgDownload = require('../dist/dlg-download.js');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.dlgDownload = new DlgDownload();
        }, render: function () {
            this.$el.html(Template);
        }, events: {
            'click .btn': 'buttonClicked'
        }, buttonClicked: function (e) {
            var url = e.currentTarget.form.action;
            this.dlgDownload.render({url: url});
            return false;
        }
    });
});