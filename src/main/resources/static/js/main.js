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
});