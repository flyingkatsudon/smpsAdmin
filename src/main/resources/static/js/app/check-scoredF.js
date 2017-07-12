define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');


    require('../grid/check-scoredF.js');
    require('../toolbar/check-scoredF.js');
    require('text!/tpl/check-scoredF.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});