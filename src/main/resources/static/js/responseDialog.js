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
        },
        progress: function (msg) {

            var dialog = new BootstrapDialog({
                title: '',
                message: '<div style="cursor: wait"><h5><span id="noti">' + msg + ' 중입니다. 잠시만 기다려주세요</span><br><br><div id="myProgress" class="myProgress"></div></h5></div>',
                closable: false
            });

            dialog.realize();

            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalDialog().css('text-align', 'center');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().hide();

            dialog.open();
        },
        move: function (msg) {
            var _this = this;

            var width = 1;

            var id = setInterval(frame, 15);

            function frame() {
                if (width >= 100) {
                    clearInterval(id);
                    _this.notify({msg: msg});
                } else {
                    width++;
                    var html = '<div id="myBar" class="myBar" style="width: ' + width + '%"></div>';
                    $('#myProgress').html(html);
                }
            }
        },
        dialogFormat: function (message, label, url) {
            var _this = this;
            var dialog = new BootstrapDialog({
                message: '<div style="text-align:center"><h5>' + message + '</h5></div>',
                closable: true,
                buttons: [
                    {
                        label: label,
                        cssClass: 'btn-delete',
                        action: function () {
                            $.ajax({
                                url: url,
                                success: function (response) {
                                    _this.notify({msg: response, closable: true});
                                }
                            });
                        }
                    }, {
                        label: '닫기',
                        cssClass: 'btn-normal',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalDialog().css('text-align', 'center');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().css('padding', '1%');
            dialog.open();
        }
    });
});
