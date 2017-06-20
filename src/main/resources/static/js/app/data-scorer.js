define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/data-scorer.js');
    require('../toolbar/data-scorer.js');
    require('text!/tpl/data-scorer.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});