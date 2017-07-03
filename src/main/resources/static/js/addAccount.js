define(function (require) {
    "use strict";

    var GridBase = require('./dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    var dlgDetail = require('text!tpl/account-detail.html');
    var check = true; // 아이디 중복 체크

    return GridBase.extend({
        render: function () {
            $.ajax({
                url: 'system/admission',
                success: function (admissions) {
                    BootstrapDialog.show({
                        title: '<h3>계정을 추가합니다</h3>',
                        // message: '<h1>' + rowdata.userId + '</h1>',
                        size: 'size-wide',
                        closable: false,
                        onshown: function (dialogRef) {

                            var body = dialogRef.$modalBody;
                            body.append(dlgDetail);

                            // TODO: 전체 선택 및 해제
                            /*
                             $('#admission').append('<div style="width: 25%; float: left"><input style="margin-right: 10%" type="checkbox" id="all" name="all">' +
                             '<label style="font-weight: normal" for="all">전체</label></div><br>');
                             */

                            // 권한 정보 변경
                            for (var i = 0; i < admissions.length; i++) {
                                if (i % 4 == 0) $('#admission').append('<br>');
                                $('#admission').append('<div style="width: 25%; float: left"><input style="margin-right: 10%" type="checkbox" id="' + i + '" ' +
                                    'name="admissionCd" value=' + admissions[i].admissionCd + '><label style="font-weight: normal" for=' + i + '>' + admissions[i].admissionNm + '</label></div>');
                            }

                            // 계정 정보 변경
                            $('#account').append('<div style="margin:1% 0 1% 3%; width:47%; float:left">' + 'I&nbsp;&nbsp;D' + '<input id="userId" type="text" style="border-radius: 10px; padding: 1%; margin-left: 20%;"></div>');
                            $('#account').append('<div id="msg" style="margin:1% 0 1% 3%; width:47%; float:left; vertical-align: middle"></div>');

                            $('#account').append('<div style="margin:3% 0 1% 3%; width:47%;">' + 'PW' + '<input id="pw" type="text" style="border-radius: 10px; padding: 1%; margin-left: 20%"></div>');
                            $('#account').append(
                                '<div style="margin:3% 0 0 3%; width:47%;">' + '권한'
                                + '<input style="margin: 0 2% 0 15%;" type="radio" id="admin" name="role" value="ROLE_ADMIN">' + '관리자'
                                + '<input style="margin: 0 2% 0 10%;" type="radio" id="user" name="role" value="ROLE_USER">' + '사용자'
                                + '<input style="margin: 0 2% 0 10%;" type="radio" id="guest" name="role" value="ROLE_GUEST">' + '기타' + '</div>');

                            $('#userId').keyup(function () {
                                var tmp = $('#userId').val();

                                $.ajax({
                                    type: 'GET',
                                    url: '/system/account',
                                    success: function (response) {
                                        for (var i = 0; i < response.content.length; i++) {
                                            if (response.content[i].userId == tmp) {
                                                $('#msg').html('<h4 style="margin-top: 1%; color:crimson">사용할 수 없는 아이디입니다</h4>');
                                                check = false;
                                                return this;
                                            }
                                            if (tmp == '') {
                                                $('#msg').html('');
                                                check = false;
                                            }
                                            else if (tmp == 'api' || tmp == 'admin') {
                                                $('#msg').html('<h4 style="margin-top: 1%; color:crimson">사용할 수 없는 아이디입니다</h4>');
                                                check = false;
                                            }
                                            else{
                                                $('#msg').html('<h4 style="margin-top: 1%; color:dodgerblue">사용할 수 있는 아이디 입니다</h4>');
                                                check = true;
                                            }
                                        }
                                    }
                                });
                            });

                            $('#user').click(function () {
                                $('#permission').fadeIn(500);
                                $('#line').fadeIn(100);
                            });

                            $('#admin').click(function () {
                                $('#line').hide();
                                $('#permission').hide();
                            });

                            $('#guest').click(function () {
                                $('#permission').fadeIn(500);
                                $('#line').fadeIn(100);
                            });
                        },
                        buttons: [
                            {
                                id: 'add',
                                label: '계정 추가',
                                cssClass: 'btn-primary',
                                action: function (dialog) {

                                    // 권한 정보 저장
                                    var tmp = [];

                                    $('input[name=admissionCd]:checked').each(function () {
                                        tmp.push({admissionCd: $(this).val()});
                                    });

                                    if (check == true && ($('#userId').val() != '' && $('#pw').val() != '')) {

                                        $('#msg').html('<h4 style="margin-top: 1%; color:#0b58a2">확인</h4>');

                                        $.ajax({
                                            url: 'system/addAccount?userId=' + $('#userId').val() + '&password=' + $('#pw').val() + '&roleName=' + $('input[name=role]:checked').val(),
                                            async: false,
                                            success: function () {
                                                if($('input[name=role]:checked').val() != 'ROLE_ADMIN') {
                                                    for (var i = 0; i < tmp.length; i++) {
                                                        $.ajax({
                                                            url: 'system/mod?userId=' + $('#userId').val() + '&admissionCd=' + tmp[i].admissionCd + '&password=' + $('#pw').val()
                                                        });
                                                    }
                                                }
                                            }
                                        });

                                        $('#search').trigger('click');

                                    } else {
                                        $('#msg').html('<h4 style="margin-top: 1%; color:crimson">아이디 중복이나 기타 항목을 확인하세요</h4>');
                                        return false;
                                    }

                                    $('#search').trigger('click');
                                    dialog.close();
                                }
                            },
                            {
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
    });
});