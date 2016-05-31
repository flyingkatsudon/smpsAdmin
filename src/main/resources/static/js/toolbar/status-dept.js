/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate()));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #examDate': 'examDateChanged',
            'change #examTime': 'examTimeChanged',
            'change #deptNm': 'deptNmChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    examDate: _this.$('#examDate').val(),
                    examTime: _this.$('#examTime').val(),
                    deptNm: _this.$('#deptNm').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
        },
        examDateChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                examDate: e.currentTarget.value
            };
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
        },
        examTimeChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                examDate: this.$('#examDate').val(),
                examTime: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
        }
    });
});