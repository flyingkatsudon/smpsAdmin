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

            var url = this.baseName.replace('-', '/') + '.json';
            var param = this.param;

            if (param.filter == 'header') {
                if (!param.empty) {
                    if (param.admissionCd != '' && param.typeNm != undefined) {
                        if (param.typeNm != '' && param.typeNm != undefined) {
                            if (param.examDate != '' && param.examDate != undefined)
                                url += '?admissionNm=' + param.admissionNm + '&typeNm=' + param.typeNm + '&examDate=' + param.examDate;
                            else
                                url += '?admissionNm=' + param.admissionNm + '&typeNm=' + param.typeNm;
                        }
                        else {
                            if (param.examDate != '' && param.examDate != undefined)
                                url += '?admissionNm=' + param.admissionNm + '&examDate=' + param.examDate;
                            else
                                url += '?admissionNm=' + param.admissionNm;
                        }
                    }
                    else {
                        if (param.typeNm != '' && param.typeNm != undefined) {
                            if (param.examDate != '' && param.examDate != undefined)
                                url += '?typeNm=' + param.typeNm + '&examDate=' + param.examDate;
                            else
                                url += '?typeNm=' + param.typeNm;
                        }
                        else {
                            if (param.examDate != '' && param.examDate != undefined)
                                url += '?examDate=' + param.examDate;
                        }
                    }
                }
            }
            console.log(url);

            _this.viewGrid(param);

        }, viewGrid: function (o) {
            var List = require('./grid/' + this.baseName + '.js');
            var Summary = require('./grid/status-summary.js');
            var Toolbar = require('./toolbar/' + this.baseName + '.js');

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this, param: o}).render();
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/all', param: o});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this, param: o}).render();

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

            this.list.search(_param);
            this.summary.render(_param);
        }
    });
});