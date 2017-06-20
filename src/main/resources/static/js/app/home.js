define(function (require) {
    "use strict";
    var Backbone = require('backbone');
/*
    var Summary = require('../grid/status-summary.js');
    var Template = require('text!/tpl/home.html');
*/

    var Common = require('./common.js');

    require('../grid/data-virtNo.js');
    require('../grid/status-summary.js');
    require('../toolbar/data-virtNo.js');
    require('text!/tpl/data-virtNo.html');
    require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        initialize: function () {
            /**
             *  Home에서는 location.bash가 존재하지 않아서
             *  아래와 같이 location.bast명을 직접 보내줘야 함
             */
            new Common('data-virtNo').render();
        }
    });
});