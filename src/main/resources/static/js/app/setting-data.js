define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var _ = require('underscore');
    var Template = require('text!tpl/setting-data.html');
    var BootstrapDialog = require('bootstrap-dialog');
    var List = require('../grid/system-download.js');
    var dlgUniv = require('text!tpl/dlg-univ.html');
    var examList = [];

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    var SettingExamInfo = require('./../grid/setting-data.js');
    var ExamInfoDataToolbar = require('./../toolbar/setting-data.js');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;

        }, render: function () {
            this.$el.html(Template);
            this.uploadForm('#frmUploadItem');
            this.uploadForm('#frmUploadHall');
            this.uploadForm('#frmUploadExaminee');
            this.uploadForm('#frmUploadVirtNo');
            this.uploadForm('#frmUploadScore');

            // 평가(시험) 정보 관리 메뉴
            this.toolbar = new ExamInfoDataToolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new SettingExamInfo({el: '#examInfo', parent: this}).render();

        }, search: function (o) {
            this.list.search(o);
        }, uploadForm: function (id) {
            this.$(id).ajaxForm({
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            responseDialog.notify('파일을 선택하세요');
                            return false;
                        }
                    }
                    responseDialog.notify({msg:'<div style="cursor: wait">업로드 중 입니다. 창이 사라지지 않으면 관리자에게 문의하세요</div>', closable: false});

                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                },
                success: function (response) {
                    responseDialog.notify({msg: response});
                }
            });
        }, events: {
            'click #download': 'downloadClicked',
            'click #reset': 'resetClicked',
            'click #init': 'initClicked',
            'click #fill': 'fillClicked'
        }, downloadClicked: function (e) {
            var dialog = new BootstrapDialog({
                title: '',
                message: '',
                size: 'size-wide',
                closable: false,
                onshown: function (dialogRef) {
                    var header = dialogRef.$modalHeader;
                    $.ajax({
                        url: 'system/server.json',
                        async: false,
                        success: function (res) {
                            var tmp = _.template(dlgUniv);
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
                onhidden: function (dialogRef) {
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
                                url: $grid.getGridParam('postData').url,
                                list: []
                            };

                            var rows = $grid.getGridParam('selarrrow');
                            for (var i = 0; i < rows.length; i++) {
                                var rowdata = $grid.jqGrid('getRowData', rows[i]);
                                param.list.push({
                                    examCd: rowdata['examCd']
                                });
                            }

                            // 데이터 전송
                            if (param && param.list.length > 0) {
                                $.ajax({
                                    url: 'system/download',
                                    type: 'POST',
                                    data: JSON.stringify(param),
                                    contentType: 'application/json',
                                    success: function (response) {
                                        responseDialog.notify({msg:response});
                                    },
                                    error: function (response, status, error) {
                                        responseDialog.notify({msg: response.responseJSON});
                                    }
                                });
                            } else {
                                responseDialog.notify({msg: '<h5 style="margin-left:10%">데이터를 선택해주세요</h5>'});
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

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '15%');
            dialog.open();

        }, resetClicked: function (e) {
            var _this = this;
            var dialog = new BootstrapDialog({
                message: '<h5 style="margin-left:10%">삭제하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?</h5>',
                buttons: [
                    /*{
                     label: '사진포함',
                     cssClass: 'btn-primary',
                     action: function () {
                     BootstrapDialog.closeAll();
                     BootstrapDialog.show({
                     title: '서버 데이터 관리',
                     message: '진행 중입니다. 잠시만 기다려주세요.',
                     closable: false
                     });
                     _this.reset(true);
                     }
                     },*/
                    {
                        label: '사진 미포함',
                        action: function () {
                            _this.reset(false);
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

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.open();
        },
        reset: function (o) {
            var _this = this;

            $.ajax({
                url: 'system/reset',
                data: {
                    photo: o
                },
                success: function (response) {
                    responseDialog.notify({msg: response});
                }, error: function (response){
                    responseDialog.notify({msg: response.responseJSON});
                }
            });
        },
        initClicked: function (e) {
            var text = '<select id="examCd"><option value="">전체</option>';

            var _this = this;
            var dialog = new BootstrapDialog({
                message: '<h5 style="margin-left:10%">초기화할 시험을 선택하세요&nbsp;&nbsp;&nbsp;&nbsp;' + this.getExamList(text) + '</h5>',
                closable: true,
                buttons: [
                    {
                        label: '계속',
                        cssClass: 'btn-primary',
                        action: function (dialog) {
                            dialog.close();

                            var examCd = $('#examCd').val();
                            var examNm = _this.getExamNm(examCd);

                            var innerDialog = new BootstrapDialog({
                                title: '<h4>' + examNm + '</h4>',
                                message: '<h5 style="margin-left:10%">시험의 가번호와 점수를 초기화 하시겠습니까?</h5>',
                                closable: false,
                                buttons: [
                                    {
                                        label: '초기화',
                                        cssClass: 'btn-primary',
                                        action: function () {
                                            $.ajax({
                                                url: 'system/init?examCd=' + examCd,
                                                success: function (response) {
                                                    responseDialog.notify({msg: response});
                                                }
                                            });
                                        }
                                    }, {
                                        label: '닫기',
                                        action: function (dialog) {
                                            dialog.close();
                                        }
                                    }
                                ]
                            });

                            innerDialog.realize();
                            innerDialog.getModalDialog().css('margin-top', '20%');
                            innerDialog.getModalHeader().hide();
                            innerDialog.open();
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

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.open();

        }, fillClicked: function (e) {
            var text = '<select id="examCd"><option value="">선택하세요</option>';

            var dialog = new BootstrapDialog({
                title: '<h4>가번호 / 답안지 번호 / 점수</h4>',
                message: '<h5 style="margin-left: 10%">입력할 시험을 선택하세요&nbsp;&nbsp;&nbsp;&nbsp;' + this.getExamList(text) + '</h5>',
                closable: true,
                buttons: [
                    {
                        label: '가번호',
                        cssClass: 'btn-primary',
                        action: function () {

                            var examCd = $('#examCd').val();

                            if (examCd == '') {
                                responseDialog.notify({msg: '시험을 선택하세요'});
                                return false;
                            }

                            $.ajax({
                                url: 'data/fillVirtNo.json?examCd=' + examCd,
                                success: function (response) {
                                    responseDialog.notify({msg: response});
                                }
                            });
                        }
                    },
                    {
                        label: '답안지 번호',
                        cssClass: 'btn-primary',
                        action: function () {

                            var examCd = $('#examCd').val();

                            if (examCd == '') {
                                responseDialog.notify({msg: '시험을 선택하세요'});
                                return false;
                            }

                            $.ajax({
                                url: 'data/fillEvalCd.json?examCd=' + examCd,
                                success: function (response) {
                                    responseDialog.notify({msg: response});
                                },
                                error: function (response) {
                                    responseDialog.notify({msg: response.responseJSON});
                                }
                            });
                        }
                    },
                    {
                        label: '점수',
                        cssClass: 'btn-success',
                        action: function () {
                            dialog.close();

                            var examCd = $('#examCd').val();
                            var examNm = '';

                            for (var i = 0; i < examList.length; i++) {
                                if (examList[i].examCd == examCd)
                                    examNm = examList[i].examNm;
                            }

                            if (examCd == '') {
                                alert('시험을 선택하세요');
                                return false;
                            }

                            var innerDialog = new BootstrapDialog({
                                title: '<h4>' + examNm + '</h4>',
                                message: '<h5 style="margin-left: 10%">점수를 일괄 입력합니다<input type="text" id="score" style="border-radius: 10px; padding: 1%; margin-left: 5%">&nbsp;점</h5>',
                                closable: true,
                                buttons: [{
                                    label: '입력',
                                    cssClass: 'btn-success',
                                    action: function (dialog) {
                                        var score = $('#score').val();
                                        if (score > 100 || score < 0) {
                                            var dialog = new BootstrapDialog({
                                                message: '<h5 style="margin-left: 10%">0&nbsp;이상 100&nbsp;이하의 점수를 입력하세요</h5>'
                                            });

                                            dialog.realize();
                                            dialog.getModalDialog().css('margin-top', '25%');
                                            dialog.getModalHeader().hide();
                                            dialog.open();

                                            $('#score').focus();
                                            return false;
                                        }
                                        $.ajax({
                                            url: 'data/fillScore.json?examCd=' + examCd + '&score=' + score,
                                            success: function (response) {
                                                responseDialog.notify({msg: response});
                                            },
                                            error: function (response) {
                                                responseDialog.notify({msg: response.responseJSON});
                                            }
                                        });
                                    }
                                }, {
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }]
                            });

                            innerDialog.realize();
                            innerDialog.getModalDialog().css('margin-top', '20%');
                            innerDialog.open();
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

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.open();

        }, getExamList: function (text) {
            $.ajax({
                url: 'data/examInfo.json',
                async: false,
                success: function (response) {
                    for (var i = 0; i < response.length; i++) {
                        text += '<option value="' + response[i].examCd + '">' + response[i].examNm + '</option>';
                        examList.push({examCd: response[i].examCd, examNm: response[i].examNm});
                    }
                    text += '</select>';
                }
            });
            return text;
        }, getExamNm: function (examCd) {

            var examNm = '데이터 초기화';

            for (var i = 0; i < examList.length; i++) {
                if (examList[i].examCd == examCd)
                    examNm = examList[i].examNm;
            }

            return examNm;

        }
    });
});