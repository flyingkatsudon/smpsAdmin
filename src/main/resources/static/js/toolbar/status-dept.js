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
            this.param = o.param;
        },
        render: function () {

            // header.html의 필터를 사용할 때 = 상단 필터가 비어있지 않다면
            if (!this.param.empty) {
                this.$('#admissionNm').html(this.selected(this.param.admissionNm));
                this.$('#typeNm').html(this.selected(this.param.typeNm));
                this.$('#examDate').html(this.selected(this.param.examDate));

                var tmp = {
                    admissionNm: this.$('#admissionNm').val(),
                    typeNm: this.$('#typeNm').val(),
                    examDate: this.$('#examDate').val()
                };

                this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(tmp)));

                this.disableFilter('on');
            } else {
                this.makeToolbar(this.param);
            }

            return this;
        },
        selected: function (o) {
            if (o == '') return '<option value="">전체</option>';
            else return '<option value="' + o + '" selected>' + o + '</option>';
        },
        disableFilter: function (option) {
            if (option == 'on') {
                this.$('#admissionNm').attr('disabled', true);
                this.$('#admissionNm').css('background', '#fbf7f7');
                this.$('#admissionNm').css('color', 'graytext');

                this.$('#typeNm').attr('disabled', true);
                this.$('#typeNm').css('background', '#fbf7f7');
                this.$('#typeNm').css('color', 'graytext');

                this.$('#examDate').attr('disabled', true);
                this.$('#examDate').css('background', '#fbf7f7');
                this.$('#examDate').css('color', 'graytext');
            } else {
                this.$('#admissionNm').attr('disabled', false);
                this.$('#admissionNm').css('background', '');
                this.$('#admissionNm').css('color', '');

                this.$('#typeNm').attr('disabled', false);
                this.$('#typeNm').css('background', '#fbf7f7');
                this.$('#typeNm').css('color', '');

                this.$('#examDate').attr('disabled', false);
                this.$('#examDate').css('background', '');
                this.$('#examDate').css('color', '');
            }
        },
        makeToolbar: function (o) {
            // 상단필터 사용 안할 시
            if (o.empty) {
                this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
                this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
                this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate()));
            } else {
                if (o.admissionNm == '') {
                    this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
                    this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
                    this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate()));

                    if (o.type == '') {
                        this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
                        if (o.examDate == '')
                            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate()));
                    }
                } else {
                    if (o.type == '') {
                        this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
                        if (o.examDate == '')
                            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate()));
                    }
                }

            }
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));

            // etc
            this.disableFilter('off');
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #typeNm': 'typeNmChanged',
            'change #examDate': 'examDateChanged',
            'change #deptNm': 'deptNmChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            if (this.parent) {
                this.parent.search({
                    admissionNm: this.$('#admissionNm').val(),
                    typeNm: this.$('#typeNm').val(),
                    examDate: this.$('#examDate').val(),
                    deptNm: this.$('#deptNm').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm(param)));
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
        },
        typeNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: e.currentTarget.value
            };
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
        },
        examDateChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                examDate: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
        }
    });
});