define(function (require) {
    "use strict";
    var Backbone = require('backbone');
    var LoadPage = require('../loadPage.js');

    // 초기에 그릴 때 필요, loadPage.js의 같은 부분은 필터 시 다시 그릴 때 require 된다
    require('../grid/status-dept.js');
    require('../grid/status-summary.js');
    require('../toolbar/status-dept.js');
    require('text!/tpl/status-dept.html');
    require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        initialize: function () {
            // require로 그림을 그린 후 grid는 다음으로 로딩한다 (모든 페이지가 같은 형식으로 만들어져 있다)
            new LoadPage({baseName: location.hash.substring(1, location.hash.length), param: window.param}).render();
        }
    });
});