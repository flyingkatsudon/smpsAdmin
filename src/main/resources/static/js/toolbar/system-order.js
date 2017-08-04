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

                this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(tmp)));

                this.disableFilter();
            } else {
                this.makeToolbar(this.param);
            }

            return this;
        },
        selected: function (o) {
            if (o == '') return '<option value="">전체</option>';
            else return '<option value="' + o + '" selected>' + o + '</option>';
        },
        disableFilter: function () {
            this.$('#admissionNm').attr('disabled', true);
            this.$('#admissionNm').css('background', '#fbf7f7');
            this.$('#admissionNm').css('color', 'graytext');
            this.$('#admissionNm').css('cursor', 'not-allowed');

            this.$('#typeNm').attr('disabled', true);
            this.$('#typeNm').css('background', '#fbf7f7');
            this.$('#typeNm').css('color', 'graytext');
            this.$('#typeNm').css('cursor', 'not-allowed');

            this.$('#examDate').attr('disabled', true);
            this.$('#examDate').css('background', '#fbf7f7');
            this.$('#examDate').css('color', 'graytext');
            this.$('#examDate').css('cursor', 'not-allowed');
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
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm()));
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #typeNm': 'typeNmChanged',
            'change #examDate': 'examDateChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            if (this.parent) {
                this.parent.search({
                    admissionNm: this.$('#admissionNm').val(),
                    typeNm: this.$('#typeNm').val(),
                    examDate: this.$('#examDate').val(),
                    groupNm: this.$('#groupNm').val(),
                    groupOrder: this.$('#groupOrder').val(),
                    debateNm: this.$('#debateNm').val(),
                    debateOrder: this.$('#debateOrder').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm(param)));
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
        },
        typeNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: e.currentTarget.value
            };
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
        },
        examDateChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                examDate: e.currentTarget.value
            };
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
        }
    });
});