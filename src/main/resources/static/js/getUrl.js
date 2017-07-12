define(function (require) {
    "use strict";

    var Backbone = require('backbone');

    return Backbone.View.extend({
        initialize: function (options) {
            this.param = options.param;
            this.url = options.baseName.replace('-', '/') + options.suffix;

        }, getUrl: function () {
            var param = this.param;
            var url = this.url;

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
            return url;
        }
    });
});