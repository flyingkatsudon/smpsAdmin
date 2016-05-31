define(function (require) {
    "use strict";

    var Toolbar = require('../dist/toolbar.js');
    var Template = require('text!tpl/print-idCard.html');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    var DlgPdf = require('../dist/dlg-pdf.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.dlgView = new DlgPdf();
        },
        render: function () {
            this.$el.html(Template);
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm()));
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate()));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));

            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
        }, events: {
            'change #headNm': 'headNmChanged',
            'change #bldgNm': 'bldgNmChanged',
            'change #hallNm': 'hallNmChanged',
            'change #examDate': 'examDateChanged',
            'change #examTime': 'examTimeChanged',

            'change #admissionNm': 'admissionNmChanged',
            'change #deptNm': 'deptNmChanged',
            'change #majorNm': 'majorNmChanged',

            'click #printHall': 'printHallClicked',
            'click #printDept': 'printDeptClicked',
            'click #printExamineeList': 'printExamineeListClicked',
            'click #printExaminee': 'printExamineeClicked'
        }, headNmChanged: function (e) {
            var param = {
                headNm: e.currentTarget.value
            };
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
        }, bldgNmChanged: function (e) {
            var param = {
                headNm: this.$('#headNm').val(),
                bldgNm: e.currentTarget.value
            };
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
        }, hallNmChanged: function (e) {
            var param = {
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: e.currentTarget.value
            };
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
        }, examDateChanged: function (e) {
            var param = {
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                examDate: e.currentTarget.value
            };
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
        }, admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        }, deptNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: e.currentTarget.value
            };
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        }, printHallClicked: function (e) {
            e.preventDefault();

            var param = {
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                examDate: this.$('#examDate').val(),
                examTime: this.$('#examTime').val()
            };
            this.openPrintWindow(param);
        }, printDeptClicked: function (e) {
            e.preventDefault();

            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val()
            };
            this.openPrintWindow(param);
        }, printExamineeListClicked: function (e) {
            e.preventDefault();

            var param = {
                fromExamineeCd: this.$('#fromExamineeCd').val(),
                toExamineeCd: this.$('#toExamineeCd').val()
            };

            if (param.fromExamineeCd || param.toExamineeCd) this.openPrintWindow(param);

        }, printExamineeClicked: function (e) {
            e.preventDefault();

            var param = {
                examineeCd: this.$('#examineeCd').val(),
                examineeNm: this.$('#examineeNm').val()
            };

            if (param.examineeCd || param.examineeNm) this.openPrintWindow(param);

        }, openPrintWindow: function (param) {
            this.dlgView.setUrl('data/examineeId.pdf?' + $.param(param)).render();
        }
    });
});