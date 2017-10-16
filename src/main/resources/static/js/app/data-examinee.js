define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Common = require('./common.js');

    require('../grid/data-examinee.js');
    require('../toolbar/data-examinee.js');
    require('text!/tpl/data-examinee.html');

    return Backbone.View.extend({
        initialize: function () {
            new Common().render();
        }
    });
});