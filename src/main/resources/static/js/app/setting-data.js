define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var _ = require('underscore');
    var Template = require('text!tpl/setting-data.html');
    var BootstrapDialog = require('bootstrap-dialog');
    var List = require('../grid/system-download.js');
    var comboBox = require('text!tpl/dlg-univ.html');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;

        }, render: function () {
            this.$el.html(Template);
            this.uploadForm('#frmUploadDevi');
            this.uploadForm('#frmUploadItem');
            this.uploadForm('#frmUploadHall');
            this.uploadForm('#frmUploadExaminee');
            this.uploadForm('#frmUploadScore');
        }, uploadForm: function (id) {
            this.$(id).ajaxForm({
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            this.error();
                            return false;
                        }
                    }
                    BootstrapDialog.closeAll();
                    BootstrapDialog.show({
                        title: '파일 업로드',
                        message: '업로드 중입니다. 잠시만 기다려주십시오.',
                        closable: false
                    });
                },
                error: function () {
                    BootstrapDialog.closeAll();
                    BootstrapDialog.show({
                        title: '파일 업로드',
                        message: '양식 파일을 확인하세요.',
                        closable: true
                    });
                },
                success: function () {
                    BootstrapDialog.closeAll();
                    BootstrapDialog.show({
                        title: '파일 업로드',
                        message: '업로드가 완료되었습니다.',
                        closable: true
                    });
                }
            });
        }, events: {
            'click #download': 'downloadClicked',
            'click #reset': 'resetClicked',
            'click #init': 'initClicked'
        }, downloadClicked: function (e) {
            BootstrapDialog.show({
                title: '',
                message: '학교를 선택하세요.',
                size: 'size-wide',
                closable: false,
                onshown: function (dialogRef) {
                    var header = dialogRef.$modalHeader;
                    $.ajax({
                        url: 'system/server.json',
                        async: false,
                        success: function (res) {
                            var tmp = _.template(comboBox);
                            header.append(tmp({list: res}));
                        }
                    });

                    dialogRef.list = new List({el: dialogRef.$modalBody}).render();

                    $(header, '#univ').change(function () {
                        var url = $("#univ option:selected").val();
                        dialogRef.list.$grid.setGridParam({
                            postData: {
                                url: url
                            }
                        }).trigger('reloadGrid');
                    });
                },
                onhidden : function(dialogRef){
                    dialogRef.list.close();
                },
                buttons: [
                    {
                        label: '다운로드',
                        cssClass: 'btn-primary',
                        action: function (dialogRef) {
                            // 화면상에 선택된 줄 가져오기
                            var $grid = dialogRef.list.$grid;

                            var param = {
                                url : $grid.getGridParam('postData').url,
                                list: []
                            };

                            var rows = $grid.getGridParam('selarrrow');
                            for (var i = 0; i < rows.length; i++) {
                                var rowdata = $grid.jqGrid('getRowData', rows[i]);
                                param.list.push({
                                    examCd: rowdata['exam.examCd'],
                                    hallCd: rowdata['hall.hallCd']
                                });
                            }

                            // 데이터 전송
                            if (param && param.list.length > 0) {
                                BootstrapDialog.show({
                                    title: '서버 데이터 관리',
                                    message: '진행 중입니다. 잠시만 기다려주세요.',
                                    closable: false
                                });
                                $.ajax({
                                    url: 'system/download',
                                    type: 'POST',
                                    data: JSON.stringify(param),
                                    contentType: 'application/json',
                                    success: function (response) {
                                        BootstrapDialog.closeAll();
                                        BootstrapDialog.show({
                                            title: '서버 데이터 관리',
                                            message: '데이터가 정상적으로 처리되었습니다.',
                                            closable: true,
                                            buttons: [
                                                {
                                                    label: '확인',
                                                    action: function (dialog) {
                                                        dialog.close();
                                                    }
                                                }
                                            ]
                                        });
                                    },
                                    error: function (response, status, error) {
                                        BootstrapDialog.closeAll();
                                        BootstrapDialog.show({
                                            title: '서버 데이터 관리',
                                            message: response.responseText,
                                            closable: true
                                        });
                                    }
                                });
                            } else {
                                BootstrapDialog.show({
                                    title: '서버 데이터 관리',
                                    message: '데이터를 선택해주세요!',
                                    closable: true
                                });
                            }
                        }
                    },
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });
        }, resetClicked: function (e) {
            BootstrapDialog.show({
                title: '서버 데이터 관리',
                message: '삭제 하시겠습니까?',
                closable: true,
                buttons: [
                    {
                        label: '사진포함',
                        cssClass: 'btn-primary',
                        action: function () {
                            BootstrapDialog.closeAll();
                            BootstrapDialog.show({
                                title: '서버 데이터 관리',
                                message: '진행 중입니다. 잠시만 기다려주세요.',
                                closable: false
                            });
                            $.ajax({
                                url: 'system/reset.photo',
                                success: function (data) {
                                    BootstrapDialog.closeAll();
                                    BootstrapDialog.show({
                                        title: '서버 데이터 관리',
                                        message: '완료되었습니다.',
                                        closable: true,
                                        buttons: [{
                                            label: '확인',
                                            action: function (dialog) {
                                                dialog.close();
                                            }
                                        }]
                                    });
                                }
                            });
                        }
                    },
                    {
                        label: '사진 미포함',
                        action: function () {
                            BootstrapDialog.closeAll();
                            BootstrapDialog.show({
                                title: '서버 데이터 관리',
                                message: '진행 중입니다. 잠시만 기다려주세요.',
                                closable: false
                            });
                            $.ajax({
                                url: 'system/reset.none',
                                success: function (data) {
                                    BootstrapDialog.closeAll();
                                    BootstrapDialog.show({
                                        title: '서버 데이터 관리',
                                        message: '완료되었습니다.',
                                        closable: true,
                                        buttons: [{
                                            label: '확인',
                                            action: function (dialog) {
                                                dialog.close();
                                            }
                                        }]
                                    });
                                }
                            });
                        }
                    },
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });
        }, initClicked: function (e) {
            BootstrapDialog.show({
                title: '서버 데이터 관리',
                message: '가번호 및 점수 데이터를 초기화 하시겠습니까?',
                closable: true,
                buttons: [
                    {
                        label: '초기화',
                        cssClass: 'btn-primary',
                        action: function () {
                            BootstrapDialog.closeAll();
                            BootstrapDialog.show({
                                title: '서버 데이터 관리',
                                message: '진행 중입니다. 잠시만 기다려주세요.',
                                closable: false
                            });
                            $.ajax({
                                url: 'system/init',
                                success: function (data) {
                                    BootstrapDialog.closeAll();
                                    BootstrapDialog.show({
                                        title: '서버 데이터 관리',
                                        message: '완료되었습니다.',
                                        closable: true,
                                        buttons: [{
                                            label: '확인',
                                            action: function (dialog) {
                                                dialog.close();
                                            }
                                        }]
                                    });
                                }
                            });
                        }
                    },
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });
        }
    });
});