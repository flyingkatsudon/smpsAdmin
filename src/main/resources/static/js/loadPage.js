/**
 * Created by Jeremy on 2017. 6. 7..
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');

    return Backbone.View.extend({
        initialize: function (o) {
            var _this = this;
            this.param = o.param;
            this.baseName = o.baseName;

            var Template = require('text!/tpl/' + this.baseName + '.html');
            var InnerTemplate = require('text!/tpl/status-summary.html');

            $('#page-wrapper').html(Template);
            $('#hm-ui-summary').html(InnerTemplate);

            var param = this.param;

            _this.viewGrid(param);

        }, viewGrid: function (o) {

            var List = require('./grid/' + this.baseName + '.js');
            var Summary = require('./grid/status-summary.js');
            var Toolbar = require('./toolbar/' + this.baseName + '.js');

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this, param: o}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/all', param: o});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this, param: o, baseName: this.baseName}).render();

        }, search: function (o) {

            if (o.deptNm == undefined) o.deptNm = '';
            if (o.majorNm == undefined) o.majorNm = '';
            if (o.headNm == undefined) o.headNm = '';
            if (o.bldgNm == undefined) o.bldgNm = '';
            if (o.hallNm == undefined) o.hallNm = '';
            if (o.isVirtNo == undefined) o.isVirtNo == '';
            if (o.groupNm == undefined) o.groupNm == '';
            if (o.scorerNm == undefined) o.scorerNm == '';
            if (o.examineeCd == undefined) o.examineeCd == '';
            if (o.examineeNm == undefined) o.examineeNm == '';
            if (o.roleName == undefined) o.roleName == '';
            if (o.examNm == undefined) o.examNm == '';
            if (o.isAttend == undefined) o.isAttend = '';

            var _param = {
                deptNm: o.deptNm,
                majorNm: o.majorNm,
                headNm: o.headNm,
                bldgNm: o.bldgNm,
                hallNm: o.hallNm,
                isVirtNo: o.isVirtNo,
                groupNm: o.groupNm,
                scorerNm: o.scorerNm,
                examineeCd: o.examineeCd,
                examineeNm: o.examineeNm,
                roleName: o.roleName,
                examNm: o.examNm,
                isAttend: o.isAttend,
                filter: window.param.filter
            };

            if (_param.filter == 'with') {
                _param.admissionNm = $('#admissionNm').val();
                _param.typeNm = $('#typeNm').val();
                _param.examDate = $('#examDate').val();
                _param.deptNm = $('#deptNm').val();
                _param.majorNm = $('#majorNm').val();
                _param.headNm = $('#headNm').val();
                _param.bldgNm = $('#bldgNm').val();
                _param.hallNm = $('#hallNm').val();
                _param.isVirtNo = $('#isVirtNo').val();
                _param.groupNm = $('#groupNm').val();
                _param.scorerNm = $('#scorerNm').val();
                _param.examineeCd = $('#examineeCd').val();
                _param.examineeNm = $('#examineeNm').val();
                _param.roleName = $('#roleName').val();
                _param.examNm = $('#examNm').val();
                _param.isAttend = $('#isAttend').val();
            }

            this.list.search(_param);
            this.summary.render(_param);
        }
    });
});