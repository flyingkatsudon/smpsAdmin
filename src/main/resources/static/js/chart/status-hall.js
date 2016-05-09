define(function (require) {
    "use strict";

    require('morris');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            this.chart = Morris.Bar({
                element: this.el.id,
                data: [{name: '', attendCnt: 0, absentCnt: 0}],
                xkey: 'name',
                ykeys: ['attendCnt', 'absentCnt'],
                labels: ['응시자수', '결시자수'],
                stacked: true
            });
            this.search();
            this.resize();
            return this;
        }, resize: function () {
            var _this = this;
            $(window).bind('resizeEnd.Morris' + this.cid, function () {
                _this.chart.redraw();
            });
        }, close: function () {
            $(window).unbind('resizeEnd.Morris' + this.cid);
        }, search: function (o) {
            var _this = this;

            $.ajax({
                url: 'status/hall/chart',
                data: o
            }).done(function (response) {
                var data = [];
                for (var i = 0; i < response.length; i++) {
                    data.push({
                        name: response[i].hallNm,
                        attendCnt: response[i].attendCnt,
                        absentCnt: response[i].absentCnt
                    })
                }
                _this.chart.setData(data);
            })
        }
    });
});