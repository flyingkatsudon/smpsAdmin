define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/status-major.js');
    require('../grid/status-summary.js');
    require('../toolbar/status-major.js');
    require('text!/tpl/status-major.html');
    require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});