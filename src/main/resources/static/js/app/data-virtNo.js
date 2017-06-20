define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/data-virtNo.js');
    require('../grid/status-summary.js');
    require('../toolbar/data-virtNo.js');
    require('text!/tpl/data-virtNo.html');
    require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});