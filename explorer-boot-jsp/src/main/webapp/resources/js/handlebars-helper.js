var handlebarsManager = (function () {
  var printTemplate = function (data, target, templateObject, type, prefixHtml, suffixHtml, empty) {

    if (empty) {
      target.empty();
    }

    var template = Handlebars.compile(templateObject.html());
    var html = '';

    if (prefixHtml) {
      html += prefixHtml;
    }

    html += template(data);

    if (suffixHtml) {
      html += suffixHtml;
    }

    if (type == 'html') {
      target.html(html);
    }
    else if (type == 'append') {
      target.append(html);
    }
  };

  return {
    printTemplate: printTemplate
  }
})();

Handlebars.registerHelper('compare', function (lvalue, operator, rvalue, expected, options) {

  //{{#compare @index '%' 10}}
  var operators, result;

  if (arguments.length < 3) {
    throw new Error("Handlerbars Helper 'compare' needs 2 parameters");
  }

  if (options === undefined) {
    options = rvalue;
    rvalue = operator;
    operator = "===";
  }

  if (expected === undefined) {
    expected = 0;
  }

  operators = {
    '=='    : function (l, r) {
      return l == r;
    },
    '==='   : function (l, r) {
      return l === r;
    },
    '!='    : function (l, r) {
      return l != r;
    },
    '!=='   : function (l, r) {
      return l !== r;
    },
    '<'     : function (l, r) {
      return l < r;
    },
    '>'     : function (l, r) {
      return l > r;
    },
    '<='    : function (l, r) {
      return l <= r;
    },
    '>='    : function (l, r) {
      return l >= r;
    },
    'typeof': function (l, r) {
      return typeof l == r;
    },
    '%'     : function (l, r) {
      return l % r == expected;
    }
  };

  if (!operators[operator]) {
    throw new Error("Handlerbars Helper 'compare' doesn't know the operator " + operator);
  }

  result = operators[operator](lvalue, rvalue);

  if (result) {
    return options.fn(this);
  } else {
    return options.inverse(this);
  }
});