/**
 * json 안의 공백문자나 null이 존재할 경우 제외한다.
 */
(function ($) {
    $.emptyFilter = function (obj) {
        var isArray = obj instanceof Array;
        for (var k in obj) {
            if (obj[k] === null || obj[k] === '') {
                isArray ? obj.splice(k, 1) : delete obj[k];
            } else if (typeof obj[k] == "object") {
                $.emptyFilter(obj[k]);
            }
        }
        return obj;
    };
})(jQuery);