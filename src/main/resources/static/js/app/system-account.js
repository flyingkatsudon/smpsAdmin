define(function (require) {
    "use strict";
    var Backbone = require('backbone');
    var LoadPage = require('../loadPage.js');

    // 초기에 그릴 때 필요, loadPage.js의 같은 부분은 필터 시 다시 그릴 때 require 된다
    require('../grid/system-account.js');
    require('../grid/system-account.js');
    require('../toolbar/system-account.js');
    require('text!/tpl/system-account.html');

    var AddAcount = require('../addAccount.js');

    return Backbone.View.extend({
        initialize: function () {
            new LoadPage({baseName: location.hash.substring(1, location.hash.length), param: window.param}).render();

            $('#add').click(function(){
                new AddAcount().render();
            });
        }
    });
});