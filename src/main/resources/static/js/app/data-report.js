define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/data-report.html');
    var DlgDownload = require('../dist/dlg-download.js');

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    var BootstrapDialog = require('bootstrap-dialog');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.dlgDownload = new DlgDownload();
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate()));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime()));
        },
        events: {
            'click .btn': 'buttonClicked',
            'click #txtDownload': 'txtDownloadClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #examDate': 'examDateClicked'
        },
        buttonClicked: function (e) {
            e.preventDefault();

            var admissionNm = this.$('#admissionNm').val();
            var examDate = this.$('#examDate').val();
            var examTime = this.$('#examTime').val();

            var url = e.currentTarget.form.action;
            this.dlgDownload.render({url: url + "?admissionNm=" + admissionNm + "&examTime=" + examTime + "&examDate=" + examDate });

            return false;
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
        },
        examDateClicked: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                examDate: e.currentTarget.value
            };
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
        },
        txtDownloadClicked: function(e){
            BootstrapDialog.show({
                title: '텍스트파일 다운로드',
                message: '다운로드가 완료되었습니다.',
                closable: true,
                onshow: function (dialog) {
                    $.ajax({
                        url: 'data/physical.txt',
                        success: function (data) {
                        }
                    });
                }
            });
        }
    });
});