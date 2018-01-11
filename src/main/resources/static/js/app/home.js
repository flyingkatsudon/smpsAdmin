define(function (require) {
    "use strict";
    var Backbone = require('backbone');
    var LoadPage = require('../loadPage.js');

    // 초기에 그릴 때 필요, loadPage.js의 같은 부분은 필터 시 다시 그릴 때 require 된다
    require('../grid/data-virtNo.js');
    require('../grid/status-summary.js');
    require('../toolbar/data-virtNo.js');
    require('text!/tpl/data-virtNo.html');
    require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        initialize: function () {
            /*
             *  Home 화면에서는 location.bash가 존재하지 않아서
             *  아래와 같이 location.bash명을 직접 보내줘야 함
             */
            new LoadPage({baseName: 'data-virtNo', param: window.param}).render();
        }
    });
});