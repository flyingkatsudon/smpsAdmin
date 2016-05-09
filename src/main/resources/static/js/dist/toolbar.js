define(function (require) {
    var Backbone = require('backbone');

    return Backbone.View.extend({
        getOptions: function (options) {
            var html = '<option value="">전체</option>';

            if (options) {
                if (options.constructor === Array) {
                    for (var i = 0; i < options.length; i++) {
                        var obj = options[i];
                        html += '<option value="' + obj.key + '">' + obj.value + '</option>';
                    }
                } else {
                    var keys = Object.keys(options);
                    for (var i = 0; i < keys.length; i++) {
                        var key = keys[i], value = options[key];
                        html += '<option value="' + key + '">' + value + '</option>';
                    }
                }
            }
            return html;
        }
    });
});