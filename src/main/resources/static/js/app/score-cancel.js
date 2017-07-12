define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/score-cancel.js');
    require('../toolbar/score-cancel.js');
    require('text!/tpl/score-cancel.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});