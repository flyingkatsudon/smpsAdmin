define(function (require) {
    "use strict";

    require('jquery-bootstrap');
    require('jquery-file-download');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        initialize: function (options) {
            this.options = options;
        },
        render: function (options) {
            options = options ? options : this.options;
            $.fileDownload(options.url, {
                data : options.data ? options.data: null,
                preparingMessageHtml: "파일을 다운로드 중입니다. 잠시만 기다려주세요.",
                failMessageHtml: "관리자에게 문의하세요."
            });
        }
    });
});