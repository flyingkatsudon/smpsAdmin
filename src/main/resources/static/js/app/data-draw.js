define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/data-draw.js');
    require('../toolbar/data-draw.js');
    require('text!/tpl/data-draw.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});