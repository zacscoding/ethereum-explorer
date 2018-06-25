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
    throw new Error("Handlerbars Helper 'compare' doesn't know the operator "
        + operator);
  }

  result = operators[operator](lvalue, rvalue);

  if (result) {
    return options.fn(this);
  } else {
    return options.inverse(this);
  }
});

Handlebars.registerHelper('displayGasUsage', function (blockInfo) {
  return (blockInfo.gasUsed / blockInfo.gasLimit * 100) + '%';
});

Handlebars.registerHelper('displayDifficulty', function (difficulty) {
  if (!difficulty) {
    return '';
  }

  // TODO :: CONVERT UNIT OR COMMA
  return difficulty.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
});

Handlebars.registerHelper('getLength', function (data, defaultValue) {
  if (!data) {
    return defaultValue;
  }

  return data.length;
});

Handlebars.registerHelper('displayTimestamp', function (timestamp, type) {
  if (!timestamp || timestamp == 0) {
    return '-';
  }

  if (type == 'short') {
    return moment.unix(timestamp).fromNow();
  } else if (type == 'long') {
    return new Handlebars.SafeString(''+ moment.unix(timestamp).format('YYYY.MM.DD HH:mm:ss') + '+UTC  ('+ moment.unix(timestamp).fromNow() + ')');
  }
});

Handlebars.registerHelper('displaySealfields', function (sealFields) {
  if (!sealFields) {
    return '';
  }
  var ret = '';

  for (var i = 0; i < sealFields.length; i++) {
    ret += sealFields[i] + '<br/>';
  }

  return ret;
});