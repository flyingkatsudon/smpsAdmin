define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var GetUrl = require('./../getUrl.js');

    var JSON = '.json';
    var XLSX = '.xlsx';

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            this.param = options.param;
            this.baseName = options.baseName;

            console.log(this.baseName);

            var colModel = [
                {name: 'virtNo', label: '가번호'},
                {name: 'groupNm', label: '조'},
                {name: 'scanDttm', label: '등록시간'},
                {name: 'examineeCd', label: '수험번호'},
                /*{name: 'examineeNm', label: '수험생명'},*/
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'examDate', label: '시험일자'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                /*{name: 'headNm', label: '고사본부'},
                {name: 'bldgNm', label: '고사건물'},*/
                {name: 'hallNm', label: '고사실'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: new GetUrl({baseName: this.baseName, suffix: JSON, param: this.param}).getUrl(),
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel(new GetUrl({baseName: this.baseName, suffix: XLSX, param: this.param}).getUrl());
            return this;
        }
    });
});