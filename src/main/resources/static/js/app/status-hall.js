define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/status-hall.js');
    var Summary = require('../grid/status-summary.js');
    var Toolbar = require('../toolbar/status-hall.js');
    var Template = require('text!/tpl/status-hall.html');
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
            if (o.headNm == undefined) o.headNm = '';
            if (o.bldgNm == undefined) o.bldgNm = '';
            if (o.hallNm == undefined) o.hallNm = '';

            var _param = {
                headNm: o.headNm,
                bldgNm: o.bldgNm,
                hallNm: o.hallNm,
                filter: window.param.filter
            };

            // 두 필터를 함께 사용한다면
            if (_param.filter == 'with') {
                _param.admissionNm = $('#admissionNm').val();
                _param.typeNm = $('#typeNm').val();
                _param.examDate = $('#examDate').val();
                _param.headNm = $('#headNm').val();
                _param.bldgNm = $('#bldgNm').val();
                _param.hallNm = $('#hallNm').val();
            }

            this.list.search(_param);
            this.summary.render(_param);
        }
    });
});