/**
 * Created by eugene on 2017-07-05.
 */
/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-exam-toolbar.js');


    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm()));

            console.log(this.$('#examNm').html());
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged'
        },
        searchClicked: function (o) {
            var _this = this;

            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    examNm: _this.$('#examNm').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };

            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm(param)));

        }
    });
});