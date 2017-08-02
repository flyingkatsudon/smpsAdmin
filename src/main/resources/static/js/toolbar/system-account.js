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
        },
        makeToolbar: function (o) {
            // 상단필터 사용 안할 시
            if (o.empty) {
                this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            } else {
                if (o.admissionNm == '') {
                    this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
                }
            }
        },
        events: {
            'click #search': 'searchClicked'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;

            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    roleName: _this.$('#roleName').val()
                });
            }
        }
    });
});