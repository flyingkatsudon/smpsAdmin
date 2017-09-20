define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    var dlgDetail = require('text!tpl/account-detail.html');

    var GetUrl = require('./../getUrl.js');
    var JSON = '.json';

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parents;
            this.param = options.param;
            this.baseName = options.baseName;

            var colModel = [
                {name: 'userId', label: '계정'},
                {name: 'password', label: '비밀번호'},
                {name: 'admissions', label: '전형'},
                {name: 'role', label: '구분'},
                {name: 'roleName', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: new GetUrl({baseName: this.baseName, suffix: JSON, param: this.param}).getUrl(),
                    colModel: colModel,
                    loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 0; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.roleName == 'ROLE_ADMIN') $(this).setRowData(ids[i], false, {background: "#FFD8D8"});
                        }
                    },
                    onCellSelect: function (rowid, index, contents, event) {
                        var colModel = $(this).jqGrid('getGridParam', 'colModel');
                        var rowdata = $(this).jqGrid('getRowData', rowid);

                        $.ajax({
                            url: 'system/admission',
                            success: function (admissions) {
                                BootstrapDialog.show({
                                    title: '<h3>' + rowdata.userId + ' / ' + rowdata.role + '</h3>',
                                    size: 'size-wide',
                                    closable: false,
                                    onshown: function (dialogRef) {

                                        var body = dialogRef.$modalBody;
                                        body.append(dlgDetail);

                                        /**
                                         * 관리자일때는 각 전형별 권한이 필요하지 않으므로 숨긴다
                                         */
                                        if (rowdata.roleName != 'ROLE_ADMIN') {

                                            $('#line').show();
                                            $('#permission').show();

                                        }

                                        $.ajax({
                                            url: 'system/accountDetail?userId=' + rowdata.userId,
                                            //async: false,
                                            success: function (response) {
                                                // 권한 정보를 일단 그린다
                                                for (var i = 0; i < admissions.length; i++) {
                                                    if (i % 4 == 0) $('#admission').append('<br>');

                                                    // 계정이 권한을 갖는 전형은 체크박스로 표시
                                                    // flag: true 해당 계정이 가지는 체크박스에 표시
                                                    var flag = false;
                                                    for (var j = 0; j < response.length; j++) {
                                                        if (admissions[i].admissionCd == response[j].admissionCd) {
                                                            flag = true;
                                                        }
                                                    }

                                                    if (flag)
                                                        $('#admission').append('<div style="width: 25%; float: left"><input style="margin-right: 10%" type="checkbox" id="' + i + '" ' +
                                                            'name="admissionCd" value=' + admissions[i].admissionCd + ' checked ><label for=' + i + '>' + admissions[i].admissionNm + '</label></div>');
                                                    else
                                                        $('#admission').append('<div style="width: 25%; float: left"><input style="margin-right: 10%" type="checkbox" id="' + i + '" ' +
                                                            'name="admissionCd" value=' + admissions[i].admissionCd + '><label style="font-weight: normal" for=' + i + '>' + admissions[i].admissionNm + '</label></div>');
                                                }

                                                // 계정 정보 변경
                                                $('#account').append('<div style="margin:3% 0 0 3%; width:94%; float:left">' + 'I&nbsp;&nbsp;D' + '<input type="text" style="border-radius: 10px; padding: 1%; margin-left: 10%; background: #e4e4e4; color: #727272" value="' + rowdata.userId + '" disabled>' +
                                                    '<h5 style="margin:1% 15% 0 0; color:crimson; float: right">아이디는 변경할 수 없습니다</h5></div>');
                                                $('#account').append('<div style="margin:3% 0 0 3%; width:47%; float:left">' + 'PW' + '<input id="pw" type="text" style="border-radius: 10px; padding: 2%; margin-left: 20%" value="' + rowdata.password + '"></div>');
                                                $('#account').append(
                                                    '<div style="margin:3% 0 0 3%; width:47%; float:left">' + '권한'
                                                    + '<input style="margin: 0 2% 0 15%;" type="radio" id="admin" name="role" value="ROLE_ADMIN">' + '관리자'
                                                    + '<input style="margin: 0 2% 0 10%;" type="radio" id="user" name="role" value="ROLE_USER">' + '사용자'
                                                    + '<input style="margin: 0 2% 0 10%;" type="radio" id="guest" name="role" value="ROLE_GUEST">' + '기타' + '</div>');

                                                switch (rowdata.roleName) {
                                                    case 'ROLE_ADMIN':
                                                        $('#admin').attr('checked', 'checked');
                                                        break;
                                                    case 'ROLE_USER':
                                                        $('#user').attr('checked', 'checked');
                                                        break;
                                                    case 'ROLE_GUEST':
                                                        $('#guest').attr('checked', 'checked');
                                                        break;
                                                    default:
                                                        $('#guest').attr('checked', 'checked');
                                                        break;
                                                }

                                                $('#user').click(function () {
                                                    $('#permission').fadeIn(500);
                                                    $('#line').fadeIn(100);
                                                });

                                                $('#admin').click(function () {
                                                    $('#permission').fadeOut(200);
                                                    $('#line').fadeOut(200);
                                                });

                                                $('#guest').click(function () {
                                                    $('#permission').fadeIn(500);
                                                    $('#line').fadeIn(100);
                                                });
                                            }
                                        });
                                    },
                                    buttons: [
                                        {
                                            id: 'modiyfy',
                                            label: '변경 내용 저장',
                                            cssClass: 'btn-primary',
                                            action: function (dialog) {

                                                /**
                                                 * 계정별 전형 정보 일괄 삭제
                                                 * 권한을 관리자로 주면 user_admission 비움
                                                 */

                                                // 비밀번호 입력여부 검사
                                                if($('#pw').val() == '') return false;


                                                // 권한을 갖는 전형 입력 전에 모두 삭제
                                                $.ajax({url: 'system/delAdm?userId=' + rowdata.userId});

                                                // 계정별 전형 정보 저장
                                                var tmp = [];

                                                $('input[name=admissionCd]:checked').each(function () {
                                                    tmp.push({admissionCd: $(this).val()});
                                                });

                                                // TODO: json 객체로 넘기도록 바꿔야
                                                if (tmp.length != 0) {
                                                    for (var i = 0; i < tmp.length; i++) {
                                                        $.ajax({url: 'system/mod?userId=' + rowdata.userId + '&admissionCd=' + tmp[i].admissionCd + '&password=' + $('#pw').val()});
                                                    }
                                                }

                                                // 선택한 권한이 관리자이면 모든 전형 삭제
                                                if($('input[name=role]:checked').val() == 'ROLE_ADMIN') $.ajax({url: 'system/delAdm?userId=' + rowdata.userId});

                                                $.ajax({
                                                    url: 'system/mod?userId=' + rowdata.userId + '&password=' + $('#pw').val() + '&roleName=' + $('input[name=role]:checked').val(),
                                                    success: function(){
                                                        $('#search').trigger('click');
                                                        dialog.close();

                                                    }
                                                });
                                            }
                                        }, {
                                            label: '계정 삭제',
                                            action: function (dialog) {
                                                dialog.close();
                                                BootstrapDialog.show({
                                                    title: '<h3>' + rowdata.userId + ' / ' + rowdata.role + '</h3>',
                                                    message: '<h4 style="font-weight: normal">' + rowdata.userId + ' 계정을 삭제합니다. 계속하시겠습니까?' + '<h4>',
                                                    closable: false,
                                                    size: 'size-wide',
                                                    buttons: [
                                                        {
                                                            id: 'delete',
                                                            label: '삭제',
                                                            cssClass: 'btn-primary',
                                                            action: function (dialog) {
                                                                // 삭제 액션
                                                                $.ajax({
                                                                    url: 'system/delAccount?userId=' + rowdata.userId,
                                                                    success: function () {
                                                                        $('#search').trigger('click');
                                                                        dialog.close();
                                                                    }
                                                                });
                                                            } // action
                                                        }, {
                                                            label: '닫기',
                                                            action: function (dialog) {
                                                                $('#search').trigger('click');
                                                                dialog.close();
                                                            }
                                                        }]
                                                });
                                            }
                                        }, {
                                            label: '닫기',
                                            action: function (dialog) {
                                                $('#search').trigger('click');
                                                dialog.close();
                                            }
                                        }
                                    ]
                                }); // dialog
                            } // success
                        }); // ajax
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            // this.$grid.closest('.ui-jqgrid-bdiv').css('overflow-x', 'auto');
            // this.addExcel('data/examinee.xlsx');
            return this;
        }
    });
});