define(function (require) {
    "use strict";

    var Backbone = require('backbone');

    var Collection = Backbone.Collection.extend({
        url: 'model/getExamInfoToolbar.json',
        initialize: function () {
            this.fetch({
                async: false
            });
        }, toArray: function (obj, sort) {
            var keys = Object.keys(obj);

            var i, rtn = [];
            for (i = 0; i < keys.length; i++) {
                var key = keys[i];
                var value = obj[keys[i]];
                if (value != undefined && value != '')
                    rtn.push({key: key, value: value});
            }
            if (sort)
                rtn.sort(function (a, b) {
                    if (a[sort] < b[sort])  return -1;
                    else if (a[sort] > b[sort]) return 1;
                    else return 0;
                });

            return rtn;
        }, getObjectList: function (obj, key, value) {
            $.each(obj, function (k, v) {
                console.log("exam is... (" + k + " : " + v + ")");
                if (v === '' || v === null || v === undefined) {
                    delete obj[k];
                }
            });
            var data = obj === undefined || Object.keys(obj).length == 0 ? this.models : this.where(obj);
            var i, length = data.length, rtn = {};
            for (i = 0; i < length; i++)
                rtn[data[i].get(key)] = data[i].get(value);

            console.log(this.toArray(rtn, 'value'));
            return this.toArray(rtn, 'value');
        }, getExamCd: function (obj) {
            return this.getObjectList(obj, 'examCd', 'examCd');
        }, getAdmissionCd: function (obj) {
            return this.getObjectList(obj, 'admissionCd', 'admissionCd');
        }, getAdmissionNm: function (obj) {
            return this.getObjectList(obj, 'admissionNm', 'admissionNm');
        }, getExamNm: function (obj) {
            return this.getObjectList(obj, 'examNm', 'examNm');
        }
    });
    return new Collection();
});