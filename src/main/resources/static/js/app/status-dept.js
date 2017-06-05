define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/status-dept.js');
    var Summary = require('../grid/status-summary.js');
    var Toolbar = require('../toolbar/status-dept.js');
    var Template = require('text!/tpl/status-dept.html');
    var InnerTemplate = require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        render: function () {
            var _this = this;

            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);

            _this.viewGrid(window.param);

            $(window.$('#admNm, #tNm, #exDate')).change(function () {
                // refresh param
                var param = window.param;
                _this.refresh(param);
            });

            /*this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
             //this.chart1 = new Chart1({el: '#hm-ui-chart'}).render();
             this.summary = new Summary({el: '#hm-ui-summary', parent: this});
             this.summary.render();
             this.list = new List({el: '.hm-ui-grid', parent: this}).render();*/

        }, search: function (o) {

            if(o.deptNm == undefined) o.deptNm = '';

            var _param = {
                deptNm: o.deptNm,
                filter: window.param.filter
            };

            if(_param.filter == 'with') {
                _param.admissionNm = $('#admissionNm').val();
                _param.typeNm = $('#typeNm').val();
                _param.examDate = $('#examDate').val();
                _param.deptNm = $('#deptNm').val();
            }

            this.list.search(_param);
            this.summary.render(_param);

        }, viewGrid: function (o) {
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this, param: o}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/all', param: o});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this, param: o}).render();

        }, refresh: function (o) {
            this.summary.remove();
            this.list.remove();

            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);

            this.viewGrid(o);
            this.search(o);
        }
    });
});