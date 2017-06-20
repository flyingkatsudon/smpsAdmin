define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/data-physical.js');
    require('../toolbar/data-physical.js');
    require('text!/tpl/data-physical.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});