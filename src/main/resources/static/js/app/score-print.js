define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/score-print.js');
    require('../toolbar/score-print.js');
    require('text!/tpl/score-print.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});