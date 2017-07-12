define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/check-scorer.js');
    require('../toolbar/check-scorer.js');
    require('text!/tpl/check-scorer.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});