define(function (require) {
    "use strict";

    require('jqgrid');

    var Backbone = require('backbone');
    var Template = require('text!/tpl/data-sqlEdit.html');
    var List = require('../grid/data-sqlEdit.js');
    var DlgDownload = require('../dist/dlg-download.js');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.list = new List({el: '.hm-ui-grid', parent: this, colModel: [{name: ' '}]}).render();

            var interview1 = 'select CONVERT(YEAR(CURDATE()) + 1, CHAR(4)) as "입학년도"\n'
                + '\t\t, exam_map.examinee_cd as "수험번호"\n'
                + '\t\t, if(exam_map.virt_no is null, 1, 0) as "결시여부"\n'
                + '\t\t, if(a.scorer_nm is null, "%", a.scorer_nm) as "평가위원"\n'
                + '\t\t, if(a.i is null, "%", a.i) as "항목코드"\n'
                + '\t\t, if(a.s is null, "", a.s) as "평가값"\n'
                + '\tfrom exam_map\n'
                + '\tleft join (select sc.exam_cd, virt_no, scorer_nm\n'
                + '\t\t\t\t, right(concat("00", item_no),2) as i\n'
                + '\t\t\t\t, case item.item_no\n'
                + '\t\t\t\t\twhen 1 then score01\n'
                + '\t\t\t\t\twhen 2 then score02\n'
                + '\t\t\t\t\twhen 3 then score03\n'
                + '\t\t\t\t\twhen 4 then score04\n'
                + '\t\t\t\t\twhen 5 then score05\n'
                + '\t\t\t\t\twhen 6 then score06\n'
                + '\t\t\t\t\twhen 7 then score07\n'
                + '\t\t\t\t\twhen 8 then score08\n'
                + '\t\t\t\t\twhen 9 then score09\n'
                + '\t\t\t\t\twhen 10 then score10\n'
                + '\t\t\t\tend as s\n'
                + '\t\t\tfrom (\n'
                + '\t\t\t\tselect score.exam_cd, score.virt_no\n'
                + '\t\t\t\t\t\t, if(score01 REGEXP "^-?[0-9\.]+$" > 0, score01 + exam.adjust, score01) as score01\n'
                + '\t\t\t\t\t\t, if(score02 REGEXP "^-?[0-9\.]+$" > 0, score02 + exam.adjust, score02) as score02\n'
                + '\t\t\t\t\t\t, if(score03 REGEXP "^-?[0-9\.]+$" > 0, score03 + exam.adjust, score03) as score03\n'
                + '\t\t\t\t\t\t, if(score04 REGEXP "^-?[0-9\.]+$" > 0, score04 + exam.adjust, score04) as score04\n'
                + '\t\t\t\t\t\t, if(score05 REGEXP "^-?[0-9\.]+$" > 0, score05 + exam.adjust, score05) as score05\n'
                + '\t\t\t\t\t\t, if(score06 REGEXP "^-?[0-9\.]+$" > 0, score06 + exam.adjust, score06) as score06\n'
                + '\t\t\t\t\t\t, if(score07 REGEXP "^-?[0-9\.]+$" > 0, score07 + exam.adjust, score07) as score07\n'
                + '\t\t\t\t\t\t, if(score08 REGEXP "^-?[0-9\.]+$" > 0, score08 + exam.adjust, score08) as score08\n'
                + '\t\t\t\t\t\t, if(score09 REGEXP "^-?[0-9\.]+$" > 0, score09 + exam.adjust, score09) as score09\n'
                + '\t\t\t\t\t\t, if(score10 REGEXP "^-?[0-9\.]+$" > 0, score10 + exam.adjust, score10) as score10\n'
                + '\t\t\t\t\t\t, if(@pVirtNo = score.virt_no AND @pExamCd = score.exam_cd, @rownum:=@rownum + 1, @rownum:=1) scorer_nm\n'
                + '\t\t\t\t\t\t, (@pVirtNo :=score.virt_no) pVirtNo\n'
                + '\t\t\t\t\t\t, (@pExamCd :=score.exam_cd) pExamCd\n'
                + '\t\t\t\tfrom score\n'
                + '\t\t\t\tinner join exam on score.exam_cd = exam.exam_cd\n'
                + '\t\t\t\tinner join (select @pExamCd := "", @pVirtNo := "", @rnum := 0) t\n'
                + '\t\t\t\torder by exam_cd, virt_no, score.scorer_nm) sc\n'
                + '\tinner join item on sc.exam_cd = item.exam_cd) a\n'
                + '\ton exam_map.exam_cd = a.exam_cd and exam_map.virt_no = a.virt_no\n'
                + '\tinner join exam on exam.exam_cd = exam_map.exam_cd\n'
                + '\tinner join admission on admission.admission_cd = exam.admission_cd\n'
                + '\twhere admission.admission_cd="';

            var interview2 = '"\n\torder by examinee_cd, scorer_nm, i';

            var lawTemplate = 'select CONVERT(YEAR(CURDATE()) + 1, CHAR(4)) as "입학년도"\n'
                + '\t\t, exam_map.examinee_cd as "수험번호"\n'
                + '\t\t, if(a.s is null, 1, 0) as "결시여부"\n'
                + '\t\t, if(b.scorer_cd is null, "%", b.scorer_cd) as "평가위원"\n'
                + '\t\t, if(a.i is null, "%", a.i) as "항목코드"\n'
                + '\t\t, if(a.s is null, "", a.s) as "평가값"\n'
                + '\tfrom exam_map\n'
                + '\tinner join exam on exam.exam_cd = exam_map.exam_cd\n'
                + '\tinner join admission on admission.admission_cd = exam.admission_cd\n'
                + '\tleft join (select distinct score.exam_cd, virt_no, if(score.total_score = "F", null, scorer_nm) as scorer_nm\n'
                + '\t\t\t, if(score.total_score = "F", null, right(concat("00", item_no),2)) as i\n'
                + '\t\t\t, if(score.total_score = "F", null,\n'
                + '\t\t\tcase item.item_no\n'
                + '\t\t\t\t\twhen 1 then if(score01 REGEXP "^-?[0-9\.]+$" > 0, score01 + exam.adjust, score01)\n'
                + '\t\t\t\t\twhen 2 then if(score02 REGEXP "^-?[0-9\.]+$" > 0, score02 + exam.adjust, score02)\n'
                + '\t\t\t\t\twhen 3 then if(score03 REGEXP "^-?[0-9\.]+$" > 0, score03 + exam.adjust, score03)\n'
                + '\t\t\t\t\twhen 4 then if(score04 REGEXP "^-?[0-9\.]+$" > 0, score04 + exam.adjust, score04)\n'
                + '\t\t\t\t\twhen 5 then if(score05 REGEXP "^-?[0-9\.]+$" > 0, score05 + exam.adjust, score05)\n'
                + '\t\t\t\t\twhen 6 then if(score06 REGEXP "^-?[0-9\.]+$" > 0, score06 + exam.adjust, score06)\n'
                + '\t\t\t\t\twhen 7 then if(score07 REGEXP "^-?[0-9\.]+$" > 0, score07 + exam.adjust, score07)\n'
                + '\t\t\t\t\twhen 8 then if(score08 REGEXP "^-?[0-9\.]+$" > 0, score08 + exam.adjust, score08)\n'
                + '\t\t\t\t\twhen 9 then if(score09 REGEXP "^-?[0-9\.]+$" > 0, score09 + exam.adjust, score09)\n'
                + '\t\t\t\t\twhen 10 then if(score10 REGEXP "^-?[0-9\.]+$" > 0, score10 + exam.adjust, score10)\n'
                + '\t\t\tend) as s\n'
                + '\tfrom score\n'
                + '\tinner join exam on score.exam_cd = exam.exam_cd\n'
                + '\tinner join item on score.exam_cd = item.exam_cd) a\n'
                + '\ton exam_map.exam_cd = a.exam_cd and exam_map.virt_no = a.virt_no\n'
                + '\tleft join (\t\tselect "A024769" scorer_cd, "김재봉" scorer_nm\n'
                + '\t\t\t\t\tunion all select "A033093", "오윤"\n'
                + '\t\t\t\t\tunion all select "A025984", "이호영"\n'
                + '\t\t\t\t\tunion all select "A022389", "김종우"\n'
                + '\t\t\t\t\tunion all select "A033094", "송호영"\n'
                + '\t\t\t\t\tunion all select "A006066", "정문식"\n'
                + '\t\t\t\t\tunion all select "A044791", "박선아"\n'
                + '\t\t\t\t\tunion all select "A043704", "이광걸"\n'
                + '\t\t\t) b\n'
                + '\ton a.scorer_nm = b.scorer_nm\n'
                + '\twhere admission.admission_cd = "HYU401"'
                + '\norder by examinee_cd, scorer_cd, i';

            var lawAbsentList = 'SELECT DISTINCT CONVERT(YEAR(CURDATE()) + 1, CHAR(4)) as "입학년도"\n'
                + '\t\t, exam_map.examinee_cd as "수험번호"\n'
                + '\t\t, examinee.examinee_nm as "성명"\n'
                + '\t\t, "" as "비고"\n'
                + '\tFROM exam_map\n'
                + '\tINNER JOIN examinee ON examinee.examinee_cd = exam_map.examinee_cd\n'
                + '\tINNER JOIN score ON score.exam_cd = exam_map.exam_cd AND score.virt_no = exam_map.virt_no\n'
                + '\tinner join exam on exam.exam_cd = exam_map.exam_cd\n'
                + '\tinner join admission on admission.admission_cd = exam.admission_cd\n'
                + '\twhere score.total_score = "F" and admission.admission_cd = "HYU401"';

            var medicalTemplate = 'select CONVERT(YEAR(CURDATE()) + 1, CHAR(4)) as "입학년도"\n'
                + '\t\t, exam_map.examinee_cd as "수험번호"\n'
                + '\t\t, if(a.s is null, 1, 0) as "결시여부"\n'
                + '\t\t, if(b.scorer_cd is null, "%", b.scorer_cd) as "평가위원"\n'
                + '\t\t, if(a.i is null, "%", a.i) as "항목코드"\n'
                + '\t\t, if(a.s is null, "", a.s) as "평가값"\n'
                + '\tfrom exam_map\n'
                + '\tinner join exam ON exam.exam_cd = exam_map.exam_cd\n'
                + '\tinner join admission ON admission.admission_cd = exam.admission_cd\n'
                + '\tleft join (select distinct score.exam_cd, virt_no, if(score.total_score = "F", null, scorer_nm) as scorer_nm\n'
                + '\t\t\t, if(score.total_score = "F", null, right(concat("00", item_no),2)) as i\n'
                + '\t\t\t, if(score.total_score = "F", null\n'
                + '\t\t\t, case item.item_no\n'
                + '\t\t\t\twhen 1 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score01 = devi.devi_nm)\n'
                + '\t\t\t\twhen 2 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score02 = devi.devi_nm)\n'
                + '\t\t\t\twhen 3 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score03 = devi.devi_nm)\n'
                + '\t\t\t\twhen 4 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score04 = devi.devi_nm)\n'
                + '\t\t\t\twhen 5 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score05 = devi.devi_nm)\n'
                + '\t\t\t\twhen 6 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score06 = devi.devi_nm)\n'
                + '\t\t\t\twhen 7 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score07 = devi.devi_nm)\n'
                + '\t\t\t\twhen 8 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score08 = devi.devi_nm)\n'
                + '\t\t\t\twhen 9 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score09 = devi.devi_nm)\n'
                + '\t\t\t\twhen 10 then (select max_score from devi where item.devi_cd = devi.fk_devi_cd and score10 = devi.devi_nm)\n'
                + '\t\t\tend) as s\n'
                + '\tfrom score\n'
                + '\tinner join item on score.exam_cd = item.exam_cd\n'
                + '\tinner join devi on devi.devi_cd = item.devi_cd) a\n'
                + '\ton exam_map.exam_cd = a.exam_cd and exam_map.virt_no = a.virt_no\n'
                + '\tleft join (           select "A004168" scorer_cd, "고현철" scorer_nm\n'
                + '\t\t\t\tunion all select "A013682", "임태호"\n'
                + '\t\t\t\tunion all select "A023782", "한진욱"\n'
                + '\t\t\t\tunion all select "A044991", "남진우"\n'
                + '\t\t\t\tunion all select "A032902", "김현영"\n'
                + '\t\t\t\tunion all select "A040358", "호정규"\n'
                + '\t\t\t\tunion all select "A004545", "노재근"\n'
                + '\t\t\t\tunion all select "A005066", "신인철"\n'
                + '\t\t\t\tunion all select "A022556", "정진환"\n'
                + '\t\t\t\tunion all select "A018464", "최동호"\n'
                + '\t\t\t\tunion all select "A007731", "강영종"\n'
                + '\t\t\t\tunion all select "A038943", "최제민"\n'
                + '\t\t) b on a.scorer_nm = b.scorer_nm\n'
                + '\twhere admission.admission_cd = "HYU402"\n'
                + '\torder by examinee_cd, scorer_cd, i';

            $('[name=HYU]').click(function () {
                $('#sql').val(interview1 + $(this)[0].id + interview2);
            });

            $('.lawTemplate').click(function () {
                $('#sql').val(lawTemplate);
            });

            $('[name=lawAbsentList]').click(function () {
                $('#sql').val(lawAbsentList);
            });

            $('[name=medicalTemplate]').click(function () {
                $('#sql').val(medicalTemplate);
            });


        }, events: {
            'click .btn-grid': 'search',
            'click .btn-excel': 'excel'
        }, excel: function () {
            var _this = this;

            if($('#sql').val() == ''){
                responseDialog.notify({msg: '쿼리가 비어있습니다. 버튼을 눌러 쿼리를 가져와 실행하세요'});
                return false;
            }

            new DlgDownload({
                url: 'data/sqlEdit.xlsx',
                data: {
                    sql: _this.$('#sql').val()
                }
            }).render();
        }, search: function () {
            var _this = this;

            if($('#sql').val() == ''){
                responseDialog.notify({msg: '쿼리가 비어있습니다. 버튼을 눌러 쿼리를 가져오세요'});
                return false;
            }

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

                    _this.list.$grid.jqGrid('setGridParam', {data: content}).trigger('reloadGrid');
                },
                error: function (xhr, status, error) {
                    var response = JSON.parse(xhr.responseText);
                    alert("code:" + xhr.status + "\nexception:" + response.exception + "\nmessage:" + response.message);
                }
            });
        }
    });
});