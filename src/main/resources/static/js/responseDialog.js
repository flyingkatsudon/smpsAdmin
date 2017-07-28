/**
 * Created by Jeremy on 2017. 7. 28..
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var BootstrapDialog = require('bootstrap-dialog');

    return Backbone.View.extend({
        complete: function (msg) {
            BootstrapDialog.closeAll();

            var dialog = new BootstrapDialog({
                title: '',
                message: '<h5>' + msg + '</h5>',
                closable: true
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalDialog().css('text-align', 'center');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().hide();
            dialog.open();

        }, error: function (msg) {
            BootstrapDialog.closeAll();

            var dialog = new BootstrapDialog({
                title: '',
                message: '<h5>' + msg + '</h5>',
                closable: true
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalDialog().css('text-align', 'center');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().hide();
            dialog.open();
        }
    });
});
