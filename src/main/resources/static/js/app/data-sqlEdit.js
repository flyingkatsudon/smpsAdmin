define(function (require) {
    "use strict";

    require('jqgrid');

    var Backbone = require('backbone');
    var Template = require('text!/tpl/data-sqlEdit.html');
    var List = require('../grid/data-sqlEdit.js');
    var DlgDownload = require('../dist/dlg-download.js');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.list = new List({el: '.hm-ui-grid', parent: this, colModel: [{name: ' '}]}).render();
        }, events: {
            'click .btn-grid': 'search',
            'click .btn-excel' : 'excel'
        },excel : function(){
            var _this = this;
            new DlgDownload({
                url: 'data/sqlEdit.xlsx',
                data: {
                    sql: _this.$('#sql').val()
                }
            }).render();
        }, search: function () {
            var _this = this;
            $.ajax({
                type: "GET",
                url: 'data/sqlEdit.json',
                data: {
                    sql: _this.$('#sql').val()
                },
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (content) {
                    var colModel = [];
                    for (var name in content[0]) {
                        colModel.push({name: name});
                    }
                    if (_this.list.$grid != undefined) {
                        $('#alertmod_' + _this.list.$grid[0].p.id).remove();
                        $(window).unbind('resizeEnd.jqGrid' + _this.list.cid);
                        _this.list.unbind();
                    }
                    _this.list = new List({el: '.hm-ui-grid', parent: _this, colModel: colModel}).render();

                    _this.list.$grid.jqGrid('setGridParam', {data : content}).trigger('reloadGrid');
                },
                error: function (xhr, status, error) {
                    var response = JSON.parse(xhr.responseText);
                    alert("code:" + xhr.status + "\nexception:" + response.exception + "\nmessage:" + response.message);
                }
            });
        }
    });
});