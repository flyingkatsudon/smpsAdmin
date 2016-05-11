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
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #deptNm': 'deptNmChanged',
            'change #majorNm': 'majorNmChanged',
            'change #scorerNm': 'scorerNmChanged',
            'change #bldgNm': 'bldgNmChanged',
            'change #hallNm': 'hallNmChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    deptNm: _this.$('#deptNm').val(),
                    majorNm: _this.$('#majorNm').val(),
                    scorerNm : _this.$('#scorerNm').val(),
                    virtNo : _this.$('#virtNo').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm : _this.$('#hallNm').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        deptNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: e.currentTarget.value
            };
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        majorNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: e.currentTarget.value
            };
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        scorerNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                scorerNm: e.currentTarget.value
            };
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        bldgNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                scorerNm: this.$('#scorerNm').val(),
                bldgNm: e.currentTarget.value
            };
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        }
    });
});