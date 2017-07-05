/**
 * Created by eugene on 2017-07-05.
 */
define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    var dialogDetail = require('text!tpl/examInfo-detail.html');

    require('jquery-simple-datetimepicker');

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'examNm', label: '평가명'},
                {name: 'period', label: '구분'},
                {name: 'hallDate', label: '일자'},

                {name: 'examCd', hidden: true},
                {name: 'adjust', hidden: true},
                {name: 'barcodeType', hidden: true},
                // {name: 'examNm', label: '평가명'},
                {name: 'examineeLen', hidden: true},
                {name: 'isAbsence', hidden: true},
                {name: 'isClosedView', hidden: true},
                {name: 'isHorizontal', hidden: true},
                {name: 'isMgrAuto', hidden: true},
                {name: 'isMove', hidden: true},
                {name: 'itemCnt', hidden: true},
                {name: 'keypadType', hidden: true},
                // {name: 'period', label: '평가구분'},
                {name: 'printContent1', hidden: true},
                {name: 'printContent2', hidden: true},
                {name: 'printSign', hidden: true},
                {name: 'printTitle1', hidden: true},
                {name: 'printTitle2', hidden: true},
                {name: 'scorerCnt', hidden: true},
                {name: 'totScore', hidden: true},
                // {name: 'typeNm', label: '계열'},
                {name: 'virtNoDigits', hidden: true},
                {name: 'virtNoType', hidden: true},
                {name: 'fkExamCd', hidden: true},
                {name: 'admissionCd', hidden: true},

                // {name: 'admissionNm', label: '전형'},

                // {name: 'hallDate', label: '평가일자'},
                {name: 'virtNoEnd', hidden: true},
                {name: 'virtNoStart', hidden: true},
                {name: 'hallCd', hidden: true},

                {name: 'headNm', hidden: true},
                {name: 'bldgNm', hidden: true},
                {name: 'hallNm', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'status/getExamInfo',
                    colModel: colModel,
                    // loadComplete: function (data) {
                    //     console.log(data);
                    // }
                    // onCellSelect: function (rowid, index, contents, event) {
                    //     var colModel = $(this).jqGrid('getGridParam', 'colModel');
                    //     var rowData = $(this).jqGrid('getRowData', rowid);
                    //
                    //     BootstrapDialog.show({
                    //         title: '<h3>' + rowData.admissionNm + ' / ' + rowData.attendNm + '</h3>',
                    //         size: 'size-wide',
                    //         closable: false,
                    //         onshown: function (dialogRef) {
                    //
                    //             var body = dialogRef.$modalBody;
                    //             body.append(dlgDetail);
                    //
                    //             $('#basicPart1').append('<div style="margin:3% 0 0 3%;">' + '시험구분' + '<input type="text" id="atNm" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.attendNm + '"></div>');
                    //             $('#basicPart1').append('<div style="margin:3% 0 0 3%;">' + '계&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;열' + '<input type="text" id="typeNm" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.typeNm + '"></div>');
                    //             $('#basicPart1').append('<div style="margin:3% 0 0 3%;">' + '수험번호' + '<input type="text" id="attendLen" style="width: 10%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.attendLen + '">&nbsp;자리</div>');
                    //
                    //             $('#basicPart2').append('<div style="margin:3% 0 0 3%;">' + '시험일자' + '<input type="text" id="attendDate" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.attendDate + '"></div>');
                    //             $('#basicPart2').append('<div style="margin:3% 0 0 3%;">' + '시험시간' + '<input type="text" id="attendTime" style="text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.attendTime + '"></div>');
                    //
                    //             $('#detailPart1').append(
                    //                 '<div style="margin:4% 0 5% 3%;">' + '답안지 교체'
                    //                 + '<input style="margin: 0 2% 0 15%;" type="radio" id="changePaper" name="isPaperChange" value=true>사용'
                    //                 + '<input style="margin: 0 2% 0 10%;" type="radio" id="notChangePaper" name="isPaperChange" value=false>미사용</div>');
                    //
                    //             switch (rowData.isPaperChange) {
                    //                 case 'true':
                    //                     $('#changePaper').attr('checked', 'checked');
                    //                     break;
                    //                 case 'false':
                    //                     $('#notChangePaper').attr('checked', 'checked');
                    //                     break;
                    //                 default:
                    //                     $('#notChangePaper').attr('checked', 'checked');
                    //                     break;
                    //             }
                    //
                    //             $('#detailPart1').append(
                    //                 '<div style="margin:6% 0 0 3%;">' + '조&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;사용'
                    //                 + '<input style="margin: 0 2% 0 15%;" type="radio" id="useGroup" name="isUseGroup" value=true>사용'
                    //                 + '<input style="margin: 0 2% 0 10%;" type="radio" id="notUseGroup" name="isUseGroup" value=false>미사용</div>');
                    //
                    //             switch (rowData.isUseGroup) {
                    //                 case 'true':
                    //                     $('#useGroup').attr('checked', 'checked');
                    //                     break;
                    //                 case 'false':
                    //                     $('#notUseGroup').attr('checked', 'checked');
                    //                     break;
                    //                 default:
                    //                     $('#notUseGroup').attr('checked', 'checked');
                    //                     break;
                    //             }
                    //
                    //
                    //             $('#detailPart2').append('<div style="margin:2% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;매수<input type="text" id="paperCnt" style="width: 10%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.paperCnt + '">&nbsp;장</div>');
                    //             $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지 자리수<input type="text" id="paperLen" style="width: 10%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.paperLen + '">&nbsp;자리</div>');
                    //
                    //             if(rowData.paperHeader == null) rowData.paperHeader = '';
                    //             $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;헤더<input type="text" id="paperHeader" style="width: 20%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.paperHeader + '"></div>');
                    //
                    //             $('#attendDate').appendDtpicker({
                    //                 autodateOnStart: false,
                    //                 dateOnly: true,
                    //                 dateFormat: 'YYYY-MM-DD'
                    //             });
                    //
                    //             $('#attendTime').appendDtpicker({
                    //                 timeOnly: true,
                    //                 autodateOnStart: false
                    //             });
                    //         },
                    //         buttons: [
                    //             {
                    //                 id: 'modiyfy',
                    //                 label: '변경 내용 저장',
                    //                 cssClass: 'btn-primary',
                    //                 action: function (dialog) {
                    //
                    //                     var param = {
                    //                         admissionCd: rowData.admissionCd,
                    //                         admissionNm: rowData.admissionNm,
                    //                         attendCd: rowData.attendCd,
                    //                         attendNm: $('#atNm').val(),
                    //                         typeNm: $('#typeNm').val(),
                    //                         attendLen: $('#attendLen').val(),
                    //                         atDate: $('#attendDate').val(),
                    //                         atTime: $('#attendTime').val(),
                    //                         isPaperChange: $('input[name=isPaperChange]:checked').val(),
                    //                         isUseGroup: $('input[name=isUseGroup]:checked').val(),
                    //                         paperCnt: $('#paperCnt').val(),
                    //                         paperLen: $('#paperLen').val(),
                    //                         paperHeader: $('#paperHeader').val()
                    //                     };
                    //
                    //                     if(param.attendNm == '' || param.typeNm == '' || param.atDate == '' || param.atTime == ''
                    //                         || param.paperCnt == '' || param.paperLen == '' || param.attendLen == ''){
                    //                         $('#basicPart2').append('<div style="margin:10% 0 0 0; text-align:center"><h4 style="margin-top: 1%; color:crimson">빈 항목을 확인하세요</h4></div>');
                    //
                    //                         return false;
                    //                     }
                    //
                    //
                    //                     if(param.paperHeader == '') param.paperHeader = null;
                    //
                    //                     $.ajax({
                    //                         url: 'status/modifyAttend',
                    //                         type: 'POST',
                    //                         contentType: "application/json; charset=utf-8",
                    //                         data: JSON.stringify(param),
                    //                         success: function(response){
                    //                             console.log(response);
                    //                             BootstrapDialog.show({
                    //                                 title: '<h3>시험 정보 관리</h3>',
                    //                                 message: '<label><h4>변경되었습니다</h4></label>',
                    //                                 buttons:[{
                    //                                     label: '닫기',
                    //                                     action: function(dialog){
                    //
                    //                                         $('#search').trigger('click');
                    //                                         dialog.close();
                    //                                     }
                    //                                 }]
                    //                             });
                    //
                    //                             $('#search').trigger('click');
                    //                         }
                    //                     });
                    //                     dialog.close();
                    //                 }
                    //             }, {
                    //                 label: '닫기',
                    //                 action: function (dialog) {
                    //                     dialog.close();
                    //                 }
                    //             }
                    //         ]
                    //     }); // dialog
                    // }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            return this;
        }
    });
});