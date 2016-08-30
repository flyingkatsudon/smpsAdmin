define(function (require) {
    "use strict";
    var Backbone = require('backbone');


    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
        },
        render: function () {
            var _this = this;
            $.ajax({
                url: 'status/all',
                success: function (response) {
                    _this.$('#examineeCnt').html(response.examineeCnt);
                    _this.$('#attendCnt').html(response.attendCnt);
                    _this.$('#absentCnt').html(response.absentCnt);
                    _this.$('#attendPer').html(response.attendPer+"%");
                    _this.$('#absentPer').html(response.absentPer+"%");
                }
            });
        }
    });
});