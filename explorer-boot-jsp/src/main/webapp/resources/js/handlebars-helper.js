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