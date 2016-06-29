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
        }, search: function (response) {
            var data = [];
            if (response) {
                for (var i = 0; i < response.length; i++) {
                    data.push({
                        name: response[i].deptNm,
                        attendCnt: response[i].attendCnt,
                        absentCnt: response[i].absentCnt
                    });
                }
                this.chart.setData(data);
            }
        }
    });
});