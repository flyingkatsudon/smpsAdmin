define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            this.param = options.param;

            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'examDate', label: '시험일자'},
                {name: 'examTime', label: '시험시간'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'examineeCnt', label: '지원자수', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'attendCnt', label: '응시자수', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'attendPer', label: '응시율', formatter: 'number', formatoptions: {suffix: '%'}},
                {name: 'absentCnt', label: '결시자수', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'absentPer', label: '결시율', formatter: 'number', formatoptions: {suffix: '%'}},
                {name: '', label: '출결시스템'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var url = 'status/dept.json';

            var param = this.param;

            if(param.filter == 'header') {
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

            var opt = $.extend(true, {
                defaults: {
                    url: url,
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('status/dept.xlsx');
            return this;
        }
    });
});