define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/data-report.html');
    var DlgDownload = require('../dist/dlg-download.js');

    var BootstrapDialog = require('bootstrap-dialog');

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    var toolbar = new Toolbar();

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        initialize: function (o) {
            // this.el = o.el;
            // this.parent = o.parent;

            if (o.param == undefined) this.param = window.param;
            else this.param = o.param;

            this.dlgDownload = new DlgDownload();
        },
        render: function () {
            // this.$el.html(Template);
            $('#page-wrapper').html(Template);

            $('.init').hide();
            var _this = this;

            _this.innerToolbar(this.param);
            _this.viewButton(this.param);
        },
        innerToolbar: function (o) {

            if (o.empty) {
                  $('.report').hide(); // 모든 버튼을 가린다

                $('#deptNm').html(toolbar.getOptions(ToolbarModel.getDeptNm()));
                $('#majorNm').html(toolbar.getOptions(ToolbarModel.getMajorNm()));
            } else {
                var param = {
                    admissionNm: o.admissionNm,
                    typeNm: o.typeNm,
                    examDate: o.examDate
                };

                $('#deptNm').html(toolbar.getOptions(ToolbarModel.getDeptNm(param)));
                $('#majorNm').html(toolbar.getOptions(ToolbarModel.getMajorNm(param)));
            }
        },
        viewButton: function (o) {
            var admissions = window.admissions;
            var admissionCd = o.admissionCd;

            if (o.admissionCd == undefined) admissionCd = '';

            var univCd = admissionCd.substr(0, 3); // 전형코드의 앞 3자리는 항상 학교를 의미함

            $('.report').hide(); // 모든 버튼 및 타이틀을 가린다

            // '전체'를 선택하면 모든 항목을 보여줌
            if (admissionCd == '') {
                for (var i = 0; i < admissions.length; i++) {
                    //    $('.init').show();
                    $("[id='" + admissions[i].admissionCd.substr(0, 3) + "']").show();
                    $("[id='" + admissions[i].admissionCd + "']").show();
                }
            }
            else if ($('#' + admissionCd)[0] != undefined && admissionCd != undefined) { // 해당 전형만의 산출물이 존재하는지 검사
                $('.init').show();
                $('#' + univCd).show(); // 해당 전형의 학교 전용이라는 것을 표시한다
                $('#' + admissionCd).show(); // 있다면 보여주고
            }
            else $('#' + univCd).hide(); // 없다면 'OO대학교 전용' 이란 표시도 숨김
        },
        events: {
            'click .btn': 'buttonClicked',
            'click #txtDownload': 'txtDownloadClicked',
            'change #deptNm': 'deptNmChanged'
        },
        deptNmChanged: function (e) {

            var param = {
                admissionNm: this.param.admissionNm,
                examDate: this.param.examDate,
                deptNm: e.currentTarget.value
            };

            this.$('#majorNm').html(toolbar.getOptions(ToolbarModel.getMajorNm(param)));
        },
        buttonClicked: function (e) {
            e.preventDefault();

            var url = e.currentTarget.form.action;
            var tmp = '';

            var param = {
                admissionNm: window.param.admissionNm,
                typeNm: $('#tNm').val(),
                examDate: $('#exDate').val(),
                deptNm: $('#deptNm').val(),
                majorNm: $('#majorNm').val()
            };

            // url 생성
            for (var obj in param) {

                if (obj != 'admissionNm') tmp += '&';
                else tmp += '?';

                tmp += obj + '=' + param[obj];
            }

            this.dlgDownload.render({
                url: url + tmp
            });

            return false;
        },
        txtDownloadClicked: function (e) {
            BootstrapDialog.show({
                title: '텍스트파일 다운로드',
                message: '다운로드 중입니다',
                closable: true,
                onshow: function (dialog) {
                    $.ajax({
                        url: 'data/physical.txt',
                        success: function (response) {
                            responseDialog.notify({msg: response});
                        }
                    });
                }
            });
        },
        // $('#admissionNm).val()가 가지는 admissionCd로 실제 admissionNm을 구함
        /*getAdmissionNm: function(e){
         var admissionNm = '';

         for(var i = 0; i < admissions.length; i++){
         if(admissions[i].admissionCd == e){
         admissionNm = admissions[i].admissionNm;
         }
         }

         return admissionNm;
         }*/
    });
});