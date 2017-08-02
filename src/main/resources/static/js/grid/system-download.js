define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admission.admissionNm', label: '전형'},
                {name: 'examCd', label: '시험코드', hidden:true},
                {name: 'examNm', label: '시험명'},
                {name: 'examDate', label: '시험일자'}
                /*
                {name: 'examTime', label: '시험시간'},
                {name: 'hall.hallCd', label: '고사실코드', hidden:true},
                {name: 'hall.headNm', label: '고사본부'},
                {name: 'hall.bldgNm', label: '고사건물'},
                {name: 'hall.hallNm', label: '고사실'}
                */
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/examList.json',
                    colModel: colModel,
                    multiselect : true,
                    rowNum : 10
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            return this;
        }
    });
});