define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/data-report.html');
    var DlgDownload = require('../dist/dlg-download.js');

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.dlgDownload = new DlgDownload();
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));
        },
        events: {
            'click .btn': 'buttonClicked',
            'click .noIdCard': 'noIdCardClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #attendDate': 'attendDateChanged'
        },
        buttonClicked: function (e) {
            e.preventDefault();

            var admissionNm = this.$('#admissionNm').val();
            var attendDate = this.$('#attendDate').val();
            var attendTime = this.$('#attendTime').val();

            var url = e.currentTarget.form.action;
            this.dlgDownload.render({url: url + "?admissionNm=" + admissionNm + "&attendTime=" + attendTime + "&attendDate=" + attendDate });

            return false;
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        attendDateChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        noIdCardClicked: function (e) {
            e.preventDefault();

            var admissionNm = this.$('#admissionNm').val();
            var attendDate = this.$('#attendDate').val();
            var attendTime = this.$('#attendTime').val();

            var url = e.currentTarget.form.action;
            this.dlgDownload.render({url: url + "?isAttend=true" + "&admissionNm=" + admissionNm + "&attendTime=" + attendTime + "&attendDate=" + attendDate  });

            return false;
        }
    });
});