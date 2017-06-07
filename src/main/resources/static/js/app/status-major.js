define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/status-major.js');
    var Summary = require('../grid/status-summary.js');
    var Toolbar = require('../toolbar/status-major.js');
    var Template = require('text!/tpl/status-major.html');
    var InnerTemplate = require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        render: function () {

            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);

            var param = window.param;

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this, param: param}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/all', param: param});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this, param: param}).render();

        }, search: function (o) {
            // 상단 필터와 기존 필터가 동시에 쓰이도록 함
            if (o.deptNm == undefined) o.deptNm = '';
            if (o.majorNm == undefined) o.majorNm = '';

            var _param = {
                deptNm: o.deptNm,
                majorNm: o.majorNm,
                filter: window.param.filter
            };

            // 두 필터를 함께 사용한다면
            if (_param.filter == 'with') {
                _param.admissionNm = $('#admissionNm').val();
                _param.typeNm = $('#typeNm').val();
                _param.examDate = $('#examDate').val();
                _param.deptNm = $('#deptNm').val();
                _param.majorNm = $('#majorNm').val();
            }

            this.list.search(_param);
            this.summary.render(_param);
        }
    });
});