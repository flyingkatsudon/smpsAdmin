define(function (require) {
    "use strict";

    require('jquery-bootstrap');
    require('jquery-file-download');

    var Backbone = require('backbone');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        initialize: function (options) {
            this.options = options;
        },
        render: function (options) {
            options = options ? options : this.options;
            $.fileDownload(options.url, {
                data: options.data ? options.data : null,
                preparingMessageHtml: responseDialog.progress('생성'),
                failMessageHtml: "관리자에게 문의하세요."
            });

            this.move();
        },
        move: function () {
            var width = 1;

            var id = setInterval(frame, 150);

            function frame() {
                if (width >= 100) {
                    clearInterval(id);
                    responseDialog.notify({msg: '완료되었습니다. 내려받기가 완료되면 확인해보세요'});
                } else {
                    width++;
                    var html = '<div id="myBar" class="myBar" style="width: ' + width + '%"></div>';
                    $('#myProgress').html(html);
                }
            }
        }
    });
});