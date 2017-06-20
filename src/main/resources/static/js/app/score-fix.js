define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/score-fix.js');
    require('../toolbar/score-fix.js');
    require('text!/tpl/score-fix.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});