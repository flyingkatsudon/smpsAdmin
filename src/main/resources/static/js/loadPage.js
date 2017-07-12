/**
 * Created by Jeremy on 2017. 6. 7..
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var GetUrl = require('./getUrl.js');

    var JSON = '.json';
    var XLSX = '.xlsx';

    return Backbone.View.extend({
        initialize: function (o) {
            var _this = this;
            this.param = o.param;
            this.baseName = o.baseName;
            this.when = o.when;

            var Template = require('text!/tpl/' + this.baseName + '.html');
            var InnerTemplate = require('text!/tpl/status-summary.html');

            $('#page-wrapper').html(Template);
            $('#hm-ui-summary').html(InnerTemplate);

            var param = this.param;
            var url = new GetUrl({baseName: this.baseName, suffix: JSON, param: this.param}).getUrl();

            var afterUrlList = [
                'data-scorerH', 'data-scorer', 'data-draw', 'data-physical',
                'check-item', 'check-scoredCnt', 'check-scorer', 'check-scoredF'
            ];
            for (var i = 0; i < afterUrlList.length; i++) {
                if (param.typeNm != '' && param.typeNm != undefined) {
                    if (param.examDate != '' && param.examDate != undefined) {
                        url += '?admissionNm=' + param.admissionNm + '&typeNm=' + param.typeNm + '&examDate=' + param.examDate;
                    }
                    else
                        url += '?admissionNm=' + param.admissionNm + '&typeNm=' + param.typeNm;
                }
                else {
                    if (param.examDate != '' && param.examDate != undefined)
                        url += '?admissionNm=' + param.admissionNm + '&examDate=' + param.examDate;
                    else
                        url += '?admissionNm=' + param.admissionNm;
                }
                if (this.baseName == afterUrlList[i]) {
                    this.when = 'after';
                    break;
                }
            }

            _this.viewGrid(param, this.when);

        }, viewGrid: function (o, when) {

            var List = require('./grid/' + this.baseName + '.js');
            var Summary = require('./grid/status-summary.js');
            var Toolbar = require('./toolbar/' + this.baseName + '.js');

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this, param: o}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/all', param: o});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this, param: o, baseName: this.baseName}).render();

            // TODO: 필터링 후 로딩 페이지 완성해야
            /*if (when == 'before') this.list = new List({
             el: '.hm-ui-grid',
             parent: this,
             param: o,
             baseName: this.baseName
             }).render();
             else $('.hm-ui-grid').html("<div align='center'><h3>" + "필터를 설정하고 '검색' 버튼을 눌러서 실행하세요" + "</h3></div>");*/

        }, search: function (o) {

            if (o.deptNm == undefined) o.deptNm = '';
            if (o.majorNm == undefined) o.majorNm = '';
            if (o.headNm == undefined) o.headNm = '';
            if (o.bldgNm == undefined) o.bldgNm = '';
            if (o.hallNm == undefined) o.hallNm = '';

            var _param = {
                deptNm: o.deptNm,
                majorNm: o.majorNm,
                headNm: o.headNm,
                bldgNm: o.bldgNm,
                hallNm: o.hallNm,
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
            }
/*
            if (this.when == 'after') {
                var List = require('./grid/' + this.baseName + '.js');
                this.list = new List({
                    el: '.hm-ui-grid',
                    parent: this,
                    param: _param,
                    baseName: this.baseName
                }).render();
            }*/
            this.list.search(_param);
            this.summary.render(_param);
        }
    });
});