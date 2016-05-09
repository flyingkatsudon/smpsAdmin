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
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));

            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
        }, events: {
            'change #headNm': 'headNmChanged',
            'change #bldgNm': 'bldgNmChanged',
            'change #hallNm': 'hallNmChanged',
            'change #attendDate': 'attendDateChanged',
            'change #attendTime': 'attendTimeChanged',

            'change #typeNm': 'typeNmChanged',
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
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        }, bldgNmChanged: function (e) {
            var param = {
                headNm: this.$('#headNm').val(),
                bldgNm: e.currentTarget.value
            };
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        }, hallNmChanged: function (e) {
            var param = {
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: e.currentTarget.value
            };
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        }, attendDateChanged: function (e) {
            var param = {
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        }, typeNmChanged: function (e) {
            var param = {
                typeNm: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        }, deptNmChanged: function (e) {
            var param = {
                typeNm: this.$('#typeNm').val(),
                deptNm: e.currentTarget.value
            };
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        }, printHallClicked: function (e) {
            e.preventDefault();

            var param = {
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val()
            };
            this.openPrintWindow(param);
        }, printDeptClicked: function (e) {
            e.preventDefault();

            var param = {
                typeNm: this.$('#typeNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val()
            };
            this.openPrintWindow(param);
        }, printExamineeListClicked: function (e) {
            e.preventDefault();

            var param = {
                firstExamineeCd: this.$('#firstExamineeCd').val(),
                lastExamineeCd: this.$('#lastExamineeCd').val()
            };

            if (param.firstExamineeCd || param.lastExamineeCd) this.openPrintWindow(param);

        }, printExamineeClicked: function (e) {
            e.preventDefault();

            var param = {
                examineeCd: this.$('#examineeCd').val(),
                examineeNm: this.$('#examineeNm').val()
            };

            if (param.examineeCd || param.examineeNm) this.openPrintWindow(param);

        }, openPrintWindow: function (param) {
            this.dlgView.setUrl('data/examineeId/pdf?' + $.param(param)).render();
        }
    });
});