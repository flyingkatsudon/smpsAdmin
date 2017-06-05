define(function (require) {
    "use strict";

    require('bootstrap');
    require('sb-admin-2');

    var Backbone = require('backbone');

    var Router = Backbone.Router.extend({
        routes: {
            '': 'home',
            '*actions': 'defaultRoute'
        }, loadView: function (view) {
            this.view = view;
            this.view.render();
        }, home: function () {
            this.defaultRoute('home');
        }, defaultRoute: function (actions) {
            if (actions) {
                if (this.view) {
                    var prop;
                    var $view = this.view;

                    for (prop in $view) {
                        if ($view[prop] instanceof Backbone.View) {
                            if ($view[prop].close) $view[prop].close();
                            $view[prop].remove();
                        }
                    }
                    if (this.view.close) this.view.close();
                    this.view.remove();

                    $('body #wrapper').append('<div id="page-wrapper"></div>');
                }
                var _this = this;
                require(['js/app/' + actions], function (View) {
                    var view = new View({el: '#page-wrapper'});
                    _this.loadView(view);
                }, function (e) {
                    console.log(e);
                });
            }
        }
    });

    $('#side-menu.nav li a').click(function () {
        var hash = this.hash;
        if (hash && hash != '')
            router.navigate(this.hash, {trigger: true});
        return false;
    });

    $(window).bind('resize', function () {
        if (this.resizeTo) clearTimeout(this.resizeTo);
        this.resizeTo = setTimeout(function () {
            $(this).trigger('resizeEnd');
        }, 100);
    });

    var router = new Router();
    Backbone.history.start();

    // TODO: header.html에 툴바있는 버전

    $(window.document).ready(function () {
        headerToolbar('');
    });

    // 2017.02.16 시작 부분
    var Toolbar = require('./dist/toolbar.js');
    var ToolbarModel = require('./model/model-status-toolbar.js');
    var toolbar = new Toolbar();

    window.$('#admNm').change(function () {
        var param = {
            //admissionCd: window.$('#admNm').val(),
            admissionNm: getAdmissionNm(window.$('#admNm').val())
        };

        window.$('#tNm').html(toolbar.getOptions(ToolbarModel.getTypeNm(param)));
        window.$('#exDate').html(toolbar.getOptions(ToolbarModel.getExamDate(param)));
    });

    window.$('#tNm').change(function () {
        var param = {
            admissionNm: getAdmissionNm(window.$('#admNm').val()),
            typeNm: window.$('#tNm').val()
        };

        window.$('#exDate').html(toolbar.getOptions(ToolbarModel.getExamDate(param)));
    });

    // $('#admissionNm).val()가 가지는 admissionCd로 실제 admissionNm을 구함
    var getAdmissionNm = function (e) {
        var admissionNm = window.$('#admNm').val();

        for (var i = 0; i < admissions.length; i++) {
            if (admissions[i].admissionCd == e)
                admissionNm = admissions[i].admissionNm;
        }

        return admissionNm;
    };

    function headerToolbar(e) {

        var Toolbar = require('./dist/toolbar.js');
        var ToolbarModel = require('./model/model-status-toolbar.js');
        var toolbar = new Toolbar();

        $.ajax({
            url: 'model/reportToolbar.json',
            async: false,
            success: function (response) {
                var flag = true;
                window.admissions = [];

                /**
                 *      toolbar에 typeNm, examDate 관련 필터는 포함되어 있다.
                 *      admissionNm은 value가 admissionCd이기 때문에 따로 만듦
                 */

                var admissionNm = '<option value="">전체</option>';

                for (var i = 0; i < response.length; i++) {
                    for (var j = 0; j < i; j++)
                        if (response[i].admissionNm == response[j].admissionNm) flag = false;
                    if (flag == true) {
                        if (response[i].admissionCd == e)
                            admissionNm += '<option value="' + response[i].admissionCd + '" selected>' + response[i].admissionNm + '</option>';
                        else
                            admissionNm += '<option value="' + response[i].admissionCd + '">' + response[i].admissionNm + '</option>';
                        admissions.push({admissionCd: response[i].admissionCd, admissionNm: response[i].admissionNm});
                    }
                    flag = true;
                }

                window.$('#admNm').html(admissionNm);
                //window.$('#admNm').html(toolbar.getOptions(ToolbarModel.getAdmissionNm()));
                window.$('#tNm').html(toolbar.getOptions(ToolbarModel.getTypeNm()));
                window.$('#exDate').html(toolbar.getOptions(ToolbarModel.getExamDate()));

            }
        });
    }

    // 상단 필터 값 초기화
    window.param = {
        admissionCd: '',
        admissionNm: '',
        typeNm: '',
        examDate: '',
        filter: 'with', // header: 상단만, with: 상, 하단 동시에
        empty: true
    };

    // 상단 필터 선택하면 window.param의 값을 업데이트 한다 -> 각 페이지에서 필터로 쓰임
    $(window.$('#admNm, #tNm, #exDate')).change(function () {

        window.param = {
            admissionCd: window.$('#admNm').val(),
            admissionNm: getAdmissionNm(window.$('#admNm').val()),
            typeNm: window.$('#tNm').val(),
            examDate: window.$('#exDate').val(),
            filter: 'header',
            empty: false
        };

        if (param.admissionNm == '' && param.examDate == '' && param.typeNm == '') {
            window.param.filter = 'with';
            window.param.empty = true;
        }
    });
});