define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/status-hall.js');
    require('../grid/status-summary.js');
    require('../toolbar/status-hall.js');
    require('text!/tpl/status-hall.html');
    require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});