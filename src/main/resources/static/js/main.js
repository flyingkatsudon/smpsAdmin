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

    // TODO: header.html에 툴바를 만들 때
/*
    $(window.document).ready(function(){
        headerToolbar('');
    });

    // 2016.12.22 시작 부분
    window.$('#admissionCd').change(function(){

    });

    function headerToolbar(e){
        console.log(e);
        $.ajax({
            url: 'model/reportToolbar.json',
            async: false,
            success: function (response) {
                var flag = true;
                window.admissions = [];

                var admissionCd = '<option value="">전체</option>';
                var examDate = '<option value="">전체</option>';
                var examTime = '<option value="">전체</option>';

                for (var i = 0; i < response.length; i++) {
                    for (var j = 0; j < i; j++)
                        if (response[i].admissionNm == response[j].admissionNm) flag = false;
                    if (flag == true) {
                        if(response[i].admissionCd == e)
                            admissionCd += '<option value="' + response[i].admissionCd + '" selected>' + response[i].admissionNm + '</option>';
                        else
                            admissionCd += '<option value="' + response[i].admissionCd + '">' + response[i].admissionNm + '</option>';
                        admissions.push({admissionCd: response[i].admissionCd, admissionNm: response[i].admissionNm});

                        examDate += '<option value="' + response[i].examDate + '">' + response[i].examDate + '</option>';
                        examTime += '<option value="' + response[i].examTime + '">' + response[i].examTime + '</option>';
                    }
                    flag = true;
                }

                window.$('#admissionCd').html(admissionCd);
                window.$('#examDate').html(examDate);
                window.$('#examTime').html(examTime);
            }
        });
    }
*/
});