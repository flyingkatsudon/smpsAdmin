define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.param = o.param;
        },
        render: function (e) {

            if (this.param != undefined) {
                this.param.admissionNm = $('#admissionNm').val();
                this.param.typeNm = $('#typeNm').val();
                this.param.examDate = $('#examDate').val();
                this.param.examNm = $('#examNm').val();
                this.param.deptNm = $('#deptNm').val();
                this.param.majorNm = $('#majorNm').val();
                this.param.headNm = $('#headNm').val();
                this.param.bldgNm = $('#bldgNm').val();
                this.param.hallNm = $('#hallNm').val();
            }

            var _this = this;
            $.ajax({
                url: 'status/all',
                data: this.param,
                success: function (response) {
                    _this.$('#examineeCnt').html(response.examineeCnt);
                    _this.$('#attendCnt').html(response.attendCnt);
                    _this.$('#absentCnt').html(response.absentCnt);
                    if (response.attendPer == undefined) _this.$('#attendPer').html('0.00%');
                    else _this.$('#attendPer').html(response.attendPer + "%");
                    if (response.absentPer == undefined) _this.$('#absentPer').html('0.00%');
                    else _this.$('#absentPer').html(response.absentPer + "%");
                }
            });
        }
    });
});