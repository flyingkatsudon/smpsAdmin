define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/data-scorerH.js');
    require('../toolbar/data-scorerH.js');
    require('text!/tpl/data-scorerH.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});