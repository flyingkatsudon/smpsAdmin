define(function (require) {
    "use strict";

    var BootstrapDialog = require('bootstrap-dialog');
    var Backbone = require('backbone');


    return Backbone.View.extend({
        initialize: function (options) {
            this.options = options;
        },
        setUrl: function (url) {
            this.url = url;
            return this;
        },
        render: function () {
            var height = $(window).height() * 0.7;
            BootstrapDialog.show({
                title: '',
                message: '<object type="application/pdf" data="' + this.url + '" width="100%" height="' + height + '">No Support</object>',
                size: 'size-wide',
                closable: false,
                buttons: [{
                    label: '닫기',
                    action: function (dialog) {
                        dialog.close();
                    }
                }]
            });
            return this;
        }
    });
});