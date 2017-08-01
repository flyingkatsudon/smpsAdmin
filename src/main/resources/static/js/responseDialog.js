/**
 * Created by Jeremy on 2017. 7. 28..
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var BootstrapDialog = require('bootstrap-dialog');

    return Backbone.View.extend({
        notify: function (param) {

            var dialog = new BootstrapDialog({
                title: '',
                message: '<h5>' + param.msg + '</h5>',
                closable: param.closable
            });

            dialog.realize();

            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalDialog().css('text-align', 'center');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().hide();

            // open 이전에 dialog 전체 삭제
            if (param.closeAll || param.closeAll == undefined) BootstrapDialog.closeAll();

            dialog.open();

        }
    });
});
