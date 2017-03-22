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
    window.$('#admNm').change(function () {
        window.admNm = getAdmissionNm(window.$('#admNm').val());

        var Toolbar = require('./dist/toolbar.js');
        var ToolbarModel = require('./model/model-status-toolbar.js');
        var toolbar = new Toolbar();

        var param = {
            admissionNm: window["admNm"]
        };

        window.$('#exDate').html(toolbar.getOptions(ToolbarModel.getExamDate(param)));

        return window.admNm;
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
        $.ajax({
            url: 'model/reportToolbar.json',
            async: false,
            success: function (response) {
                /*var flag = true;
                 window.admissions = [];

                 var admissionNm = '<option value="">전체</option>';
                 var examDate = '<option value="">전체</option>';

                 for (var i = 0; i < response.length; i++) {
                 for (var j = 0; j < i; j++)
                 if (response[i].admissionNm == response[j].admissionNm) flag = false;
                 if (flag == true) {
                 if(response[i].admissionCd == e)
                 admissionNm += '<option value="' + response[i].admissionCd + '" selected>' + response[i].admissionNm + '</option>';
                 else
                 admissionNm += '<option value="' + response[i].admissionCd + '">' + response[i].admissionNm + '</option>';
                 admissions.push({admissionCd: response[i].admissionCd, admissionNm: response[i].admissionNm});

                 examDate += '<option value="' + response[i].examDate + '">' + response[i].examDate + '</option>';
                 }
                 flag = true;
                 }

                 window.$('#admissionNm').html(admissionNm);
                 window.$('#examDate').html(examDate);

                 console.log(response);*/
                renderToolbar(response, e);
            }
        });

    }

    function renderToolbar(response, e) {
        var flag = true;
        window.admissions = [];

        var admissionNm = '<option value="">전체</option>';
        var typeNm = '<option value="">전체</option>';
        var examDate = '<option value="">전체</option>';

        for (var i = 0; i < response.length; i++) {
            for (var j = 0; j < i; j++)
                if (response[i].admissionNm == response[j].admissionNm) flag = false;
            if (flag == true) {
                if (response[i].admissionCd == e)
                    admissionNm += '<option value="' + response[i].admissionCd + '" selected>' + response[i].admissionNm + '</option>';
                else
                    admissionNm += '<option value="' + response[i].admissionCd + '">' + response[i].admissionNm + '</option>';
                admissions.push({admissionCd: response[i].admissionCd, admissionNm: response[i].admissionNm});

                //examDate += '<option value="' + response[i].examDate + '">' + response[i].examDate + '</option>';
            }
            flag = true;
        }

        window.$('#admNm').html(admissionNm);
        window.$('#tNm').html(typeNm);
        window.$('#exDate').html(examDate);
    }

    window.param = '';
    $(window.$('#admNm, #tNm, #exDate')).change(function () {
        window.param = {
            admissionNm: window["admNm"],
            typeNm: window.$('#tNm').val(),
            examDate: window.$('#exDate').val()
        };

        if (param.admissionNm == '' && param.examDate == '' && param.typeNm == '')
            window.param = '';

        console.log(window.param);
        console.log($('.hm-ui-grid'));
    });
});