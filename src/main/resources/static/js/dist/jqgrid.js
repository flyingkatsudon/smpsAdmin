define(function (require) {
    "use strict";

    require('jqgrid');

    var DlgDownload = require('./dlg-download.js');
    var BootstrapDialog = require('bootstrap-dialog');
    var Backbone = require('backbone');
    var uuid = require('uuid');

    $.extend($.jgrid, {
        defaults: {
            cmTemplate: {
                align: 'center'
            },
            styleUI: 'Bootstrap',
            caption: '',
            colModel: [],
            //forceFit: true,
            sortable: true,
            autoencode: true,
            ignoreCase: true,
            gridview: true,
            hidegrid: false,
            jsonReader: {
                repeatitems: false
                , total: 'totalPages'
                , page: function (obj) {
                    return obj.number + 1;
                }
                , records: function (obj) {
                    return obj.totalElements;
                }
                , root: 'content'
            },
            multiSort: false,
            height: 'auto',
            rownumbers: true,
            rowNum: 20,
            rownumWidth: 50,
            rowList: [1, 10, 20, 30, 50, 100],
            datatype: 'local',
            ajaxGridOptions: {contentType: 'application/json;charset=UTF-8'},
            gridComplete: function () {
                $(window).trigger('resize');
            },
            prmNames: {search: null, nd: null, rows: 'size'},
            serializeGridData: function (postData) {
                if (postData.sidx.length) postData.sort = postData.sidx + ',' + postData.sord;

                postData.page--;

                delete postData.filters;
                delete postData.sidx;
                delete postData.sord;

                return postData;
            },
            loadError: function (jqXHR, status, error) {
                if (jqXHR.status == '401' ||
                    jqXHR.status == '302'
                ) {
                    $('<div title="세션종료">로그인 정보가 없습니다.<br/>다시 로그인 해주세요.</div>').dialog({
                        modal: true,
                        close: function () {
                            $(this).dialog('destroy').remove();
                            location.reload(true);
                        },
                        buttons: {
                            '확인': function () {
                                $(this).dialog('close');
                            }
                        }
                    });
                } else {
                    alert(error);
                }
            }
        },
        add: {},
        edit: {},
        del: {},
        nav: {
            search: false,
            view: false,
            add: false,
            edit: false,
            del: false,
            refresh: false
        }
    });

    return Backbone.View.extend({
        initialize: function (options) {
            this.options = $.extend(true, {}, $.jgrid, options);
            this.$grid = $(document.createElement('table')).attr('id', uuid.uuid());
            this.$pager = $(document.createElement('div')).attr('id', uuid.uuid());
            var _this = this;
            $(window).bind('resizeEnd.jqGrid' + this.cid, function () {
                _this.$grid.jqGrid('setGridWidth', _this.$el.width());
            });
        }, render: function () {
            this.$el.empty().append(this.$grid, this.$pager);
            this.options.defaults.pager = this.$pager;
            this.$grid.jqGrid(this.options.defaults);
            this.$grid.jqGrid('navGrid', this.$grid.getGridParam('pager'), this.options.nav, this.options.edit, this.options.add, this.options.del, this.options.search, this.options.view);
            this.$('.ui-jqgrid .ui-jqgrid-bdiv').css('overflow-x', 'hidden');
            this.$grid.parents('div.ui-jqgrid-bdiv').css('min-height', '100px');

            this.$grid.jqGrid('setGridWidth', this.$el.width());

            if (this.options.defaults.url) this.$grid.jqGrid('setGridParam', {datatype: 'json'}).trigger('reloadGrid');
            return this;
        }, addExcel: function (url) {
            if (url && url != '') {
                var _this = this;
                this.$grid.jqGrid('navButtonAdd', _this.$grid.getGridParam('pager'), {
                    caption: '엑셀내보내기',
                    onClickButton: function (e) {
                        new DlgDownload({
                            url: url,
                            data: _this.$grid.getGridParam('postData')
                        }).render();
                        return false;
                    }
                });
                return this;
            }
        }, addPdf: function (url) {
                if (url && url != '') {
                    var _this = this;
                    this.$grid.jqGrid('navButtonAdd', _this.$grid.getGridParam('pager'), {
                        caption: '내보내기',
                        onClickButton: function (e) {
                            var step1 = new BootstrapDialog({
                                title: false,
                                closable: true,
                                onshown: function (dialogRef) {
                                    var body = dialogRef.$modalBody;

                                    // 평가표 설정값
                                    var detail4Html = '<div style="padding: 2% 0 0 2%">&nbsp;상위&nbsp;제목&nbsp;<input type="text" id="mainTitle" class="sheet" value=""/></div>';
                                    detail4Html += '<div style="padding: 2% 0 0 2%">&nbsp;하위&nbsp;제목&nbsp;<input type="text" id="subTitle" class="sheet"  value=""/></div>';

                                    body.append(detail4Html);
                                }, // onShown
                                buttons: [{
                                    label: '출력',
                                    cssClass: 'btn-primary',
                                    action: function () {

                                        _this.param.mainTitle = $('#mainTitle').val();
                                        _this.param.subTitle = $('#subTitle').val();

                                        new DlgDownload({
                                            url: url,
                                            data: _this.param
                                        }).render();
                                    }
                                }] // buttons
                            }); // BootstrapDialog

                            step1.realize();
                            step1.getModalDialog().css('width', '70%');
                            step1.open();

                            return false;
                        }
                    });
                    return this;
                }
        }, search: function (o) {
            var postData = this.$grid[0].p.postData;
            this.$grid[0].p.postData = $.extend(postData, o);

            this.$grid.trigger('reloadGrid', [{page: 1, current: true}]);
        }, close: function () {
            if (this.$grid != undefined) $('#alertmod_' + this.$grid[0].p.id).remove();
            $(window).unbind('resizeEnd.jqGrid' + this.cid);
            this.unbind();
            this.remove();
        }
    });
});