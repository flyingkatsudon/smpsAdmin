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
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));

            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #examDate': 'examDateChanged',
            'change #examTime': 'examTimeChanged',
            'change #deptNm': 'deptNmChanged',
            'change #majorNm': 'majorNmChanged',
            'change #headNm': 'headNmChanged',
            'change #bldgNm': 'bldgNmChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    examDate : _this.$('#examDate').val(),
                    examTime : _this.$('#examTime').val(),
                    headNm: _this.$('#headNm').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm : _this.$('#hallNm').val(),
                    scorerNm : _this.$('#scorerNm').val()
                });
            }
        },

        admissionNmChanged: function (e){
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },

        examDateChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                examDate: e.currentTarget.value
            };
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },

        examTimeChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                examDate: this.$('#examDate').val(),
                examTime: e.currentTarget.value
            };
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        headNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                examDate: this.$('#examDate').val(),
                examTime: this.$('#examTime').val(),
                headNm: e.currentTarget.value
            };
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        bldgNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                examDate: this.$('#examDate').val(),
                examTime: this.$('#examTime').val(),
                bldgNm: e.currentTarget.value
            };
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        }
    });
});