define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/status-dept.js');
    require('../grid/status-summary.js');
    require('../toolbar/status-dept.js');
    require('text!/tpl/status-dept.html');
    require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});