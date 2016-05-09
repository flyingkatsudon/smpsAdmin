define(function (require) {
    "use strict";

    require('morris');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            this.chart = Morris.Bar({
                element: this.el.id,
                data: [{name: '응시현황', examineeCnt: 0, attendCnt: 0, absentCnt: 0}],
                xkey: 'name',
                ykeys: ['examineeCnt', 'attendCnt', 'absentCnt'],
                labels: ['지원자수', '응시자수', '결시자수']
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
                url: 'status/all/chart',
                data: o
            }).done(function (response) {
                _this.chart.setData([{
                    name: '응시현황',
                    examineeCnt: response.examineeCnt,
                    attendCnt: response.attendCnt,
                    absentCnt: response.absentCnt
                }]);
            });
        }
    });
});