// TODO: header.html에 툴바를 만들 때
/*
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/data-report.html');
    var DlgDownload = require('../dist/dlg-download.js');

    var BootstrapDialog = require('bootstrap-dialog');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.dlgDownload = new DlgDownload();
        },
        render: function () {
            this.$el.html(Template);

            var _this = this;
            $('.report').hide(); // 모든 버튼을 가린다

            $(window.document).ready(function(){
                if($(window.$('#admissionCd').val() != null))
                    _this.viewButton();
            });

            $(window.$('#admissionCd')).change(function (){
                _this.viewButton();
            });
        },
        viewButton: function(){
            var admissions = window.admissions;

            var admissionCd = window.$('#admissionCd').val();
            var univCd = admissionCd.substr(0, 3); // 전형코드의 앞 3자리는 항상 학교를 의미함

            $('.report').hide(); // 모든 버튼 및 타이틀을 가린다

            // '전체'를 선택하면 모든 항목을 보여줌
            if (admissionCd == '') {
                for (var i = 0; i < admissions.length; i++) {
                    $("[id='" + admissions[i].admissionCd.substr(0, 3) + "']").show();
                    $("[id='" + admissions[i].admissionCd + "']").show();
                }
            }
            else if ($('#' + admissionCd)[0] != undefined && admissionCd != undefined) { // 해당 전형만의 산출물이 존재하는지 검사
                $('#' + univCd).show(); // 해당 전형의 학교 전용이라는 것을 표시한다
                $('#' + admissionCd).show(); // 있다면 보여주고
            }
            else $('#' + univCd).hide(); // 없다면 'OO대학교 전용' 이란 표시도 숨김*!/
        },
        events: {
            'click .btn': 'buttonClicked',
            'click #txtDownload': 'txtDownloadClicked'
        },
        buttonClicked: function (e) {
            e.preventDefault();

            var admissionCd = window.$('#admissionCd').val();
            var examDate = window.$('#examDate').val();
            var examTime = window.$('#examTime').val();

            var url = e.currentTarget.form.action;
            this.dlgDownload.render({url: url + "?admissionCd=" + admissionCd + "&examTime=" + examTime + "&examDate=" + examDate});

            return false;
        },
        txtDownloadClicked: function (e) {
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
*/
// header.html에 툴바 없는 버전
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/data-report.html');
    var DlgDownload = require('../dist/dlg-download.js');

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    var BootstrapDialog = require('bootstrap-dialog');
    var admissions = [];

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.dlgDownload = new DlgDownload();
        },
        render: function () {
            this.$el.html(Template);
            var _this = this;

            $.ajax({
                url: 'model/reportToolbar.json',
                async: false,
                success: function (response) {
                    var flag = true;
                    var admissionNm = '<option value="">전체</option>';

                    for (var i = 0; i < response.length; i++) {
                        for (var j = 0; j < i; j++)
                            if (response[i].admissionNm == response[j].admissionNm) flag = false;
                        if (flag == true) {
                            admissionNm += '<option value="' + response[i].admissionCd + '">' + response[i].admissionNm + '</option>';
                            admissions.push({admissionCd: response[i].admissionCd, admissionNm: response[i].admissionNm});
                        }
                        flag = true;
                    }
                    _this.$('#admissionNm').html(admissionNm);
                }
            });

            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate()));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime()));

            $('.report').hide(); // 모든 버튼 및 타이틀을 가린다

            $('#admissionNm').change(function(e){
                $('.report').hide(); // 모든 버튼 및 타이틀을 가린다
                // $('#admissionNm')는 admissionCd를 value로 가짐
                var admissionCd = $('#admissionNm').val();
                var univCd = admissionCd.substr(0, 3); // 전형코드의 앞 3자리는 항상 학교를 의미함

                if (admissionCd == '') {
                    for (var i = 0; i < admissions.length; i++) {
                        $("[id='" + admissions[i].admissionCd.substr(0, 3) + "']").show();
                        $("[id='" + admissions[i].admissionCd + "']").show();
                    }
                }
                else if ($('#' + admissionCd)[0] != undefined && admissionCd != undefined) { // 해당 전형만의 산출물이 존재하는지 검사
                    $('#' + univCd).show(); // 해당 전형의 학교 전용이라는 것을 표시한다
                    $('#' + admissionCd).show(); // 있다면 보여주고
                }
                else $('#' + univCd).hide(); // 없다면 'OO대학교 전용' 이란 표시도 숨김*!/
            });
        },
        events: {
            'click .btn': 'buttonClicked',
            'click .txtDownload': 'txtDownloadClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #examDate': 'examDateChanged'
        },
        buttonClicked: function (e) {
            e.preventDefault();

            var param = {
                admissionNm: this.getAdmissionNm(this.$('#admissionNm').val()),
                examDate: this.$('#examDate').val(),
                examTime: this.$('#examTime').val()
            };

            var url = e.currentTarget.form.action;
            this.dlgDownload.render({url: url + "?admissionNm=" + param.admissionNm + "&examTime=" + param.examTime + "&examDate=" + param.examDate });

            return false;
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: this.getAdmissionNm(this.$('#admissionNm').val())
            };
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#examTime').html(this.getOptions(ToolbarModel.getExamTime(param)));
        },
        examDateChanged: function (e) {
            var param = {
                admissionNm: this.getAdmissionNm(this.$('#admissionNm').val()),
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
        },
        // $('#admissionNm).val()가 가지는 admissionCd로 실제 admissionNm을 구함
        getAdmissionNm: function(e){
            var admissionNm = '';

            for(var i = 0; i < admissions.length; i++){
                if(admissions[i].admissionCd == this.$('#admissionNm').val()){
                    admissionNm = admissions[i].admissionNm;
                }
            }

            return admissionNm;
        }
    });
});