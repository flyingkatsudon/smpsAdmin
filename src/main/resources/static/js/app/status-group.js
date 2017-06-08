define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/status-group.js');
    require('../grid/status-summary.js');
    require('../toolbar/status-group.js');
    require('text!/tpl/status-group.html');
    require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});