define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/check-item.js');
    require('../toolbar/check-item.js');
    require('text!/tpl/check-item.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});