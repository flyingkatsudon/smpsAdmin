define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/check-scoredCnt.js');
    require('../toolbar/check-scoredCnt.js');
    require('text!/tpl/check-scoredCnt.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});