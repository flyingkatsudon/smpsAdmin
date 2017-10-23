/**
 * Created by Jeremy on 2017. 6. 8..
 */
define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    return Backbone.View.extend({
        initialize: function (baseName) {
            if (baseName == 'data-virtNo') this.baseName = baseName;
            else this.baseName = location.hash.substring(1, location.hash.length);
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

            this.list = new List({
                el: '.hm-ui-grid',
                parent: this,
                param: this.param,
                baseName: this.baseName
            }).render();

        }, search: function (o) {

            // 상단 필터와 기존 필터가 동시에 쓰이도록 함
            if (o.admissionNm == undefined) o.admissionNm = '';
            if (o.typeNm == undefined) o.typeNm = '';
            if (o.examDate == undefined) o.examDate = '';
            if (o.deptNm == undefined) o.deptNm = '';
            if (o.majorNm == undefined) o.majorNm = '';
            if (o.headNm == undefined) o.headNm = '';
            if (o.bldgNm == undefined) o.bldgNm = '';
            if (o.hallNm == undefined) o.hallNm = '';
            if (o.isAttend == undefined) o.isAttend = '';

            // 직접 타이핑하는 필터
            if (o.groupNm == undefined) o.groupNm = '';
            if (o.scorerNm == undefined) o.scorerNm = '';
            if (o.isVirtNo == undefined) o.isVirtNo = '';
            if (o.virtNo == undefined) o.virtNo = '';
            if (o.examineeCd == undefined) o.examineeCd = '';
            if (o.examineeNm == undefined) o.examineeNm = '';

            if (o.roleName == undefined) o.roleName = '';

            var _param = {
                admissionNm: o.admissionNm,
                typeNm: o.typeNm,
                examDate: o.examDate,
                deptNm: o.deptNm,
                majorNm: o.majorNm,
                headNm: o.headNm,
                bldgNm: o.bldgNm,
                hallNm: o.hallNm,
                groupNm: o.groupNm,
                scorerNm: o.scorerNm,
                isVirtNo: o.isVirtNo,
                virtNo: o.virtNo,
                examineeCd: o.examineeCd,
                examineeNm: o.examineeNm,
                roleName: o.roleName,
                isAttend: o.isAttend,
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
                _param.scorerNm = $('#scorerNm').val();
                _param.isVirtNo = $('#isVirtNo').val();
                _param.virtNo = $('#virtNo').val();
                _param.examineeCd = $('#examineeCd').val();
                _param.examineeNm = $('#examineeNm').val();
                _param.roleName = $('#roleName').val();
                _param.isAttend = $('#isAttend').val();
            }

            this.summary.render(_param);
            this.list.search(_param);
        }
    });
});