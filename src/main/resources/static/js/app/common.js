/**
 * Created by Jeremy on 2017. 6. 8..
 */
define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    return Backbone.View.extend({
        initialize: function () {
            this.baseName = location.hash.substring(1, location.hash.length);
            this.param = window.param;
        },
        render: function () {
            var Template = require('text!/tpl/' + this.baseName + '.html');
            var InnerTemplate = require('text!/tpl/status-summary.html');
            var Summary = require('../grid/status-summary.js');
            var List = require('../grid/' + this.baseName + '.js');
            var Toolbar = require('../toolbar/' + this.baseName + '.js');

            $('#page-wrapper').html(Template);
            $('#hm-ui-summary').html(InnerTemplate);

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this, param: this.param}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/all', param: this.param});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this, param: this.param}).render();

        }, search: function (o) {
            // 상단 필터와 기존 필터가 동시에 쓰이도록 함
            if (o.deptNm == undefined) o.deptNm = '';
            if (o.majorNm == undefined) o.majorNm = '';
            if (o.headNm == undefined) o.headNm = '';
            if (o.bldgNm == undefined) o.bldgNm = '';
            if (o.hallNm == undefined) o.hallNm = '';

            // 직접 타이핑하는 필터
            if (o.groupNm == undefined) o.groupNm = '';

            var _param = {
                deptNm: o.deptNm,
                majorNm: o.majorNm,
                headNm: o.headNm,
                bldgNm: o.bldgNm,
                hallNm: o.hallNm,
                groupNm: o.groupNm,
                filter: window.param.filter
            };

            // 두 필터를 함께 사용한다면
            if (_param.filter == 'with') {
                _param.admissionNm = $('#admissionNm').val();
                _param.typeNm = $('#typeNm').val();
                _param.examDate = $('#examDate').val();
                _param.deptNm = $('#deptNm').val();
                _param.majorNm = $('#majorNm').val();
                _param.headNm = $('#headNm').val();
                _param.bldgNm = $('#bldgNm').val();
                _param.hallNm = $('#hallNm').val();
                _param.groupNm = $('#groupNm').val();
            }

            this.list.search(_param);
            this.summary.render(_param);
        }
    });
});