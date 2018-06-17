<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" flush="true"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Parity JSON RPC</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <!-- request form row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    JSON RPC Request
                </div>
                <!-- panel body -->
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form">
                                <div class="form-group">
                                    <label>URI</label>
                                    <input class="form-control" id="uri" value="http://localhost:8540">
                                </div>

                                <div class="form-group">
                                    <div id="module-select-div"></div>
                                </div>
                                <div class="form-group">
                                    <button type="button" class="btn btn-default btn-wiki" id="btn-module-wiki" style="display: none;">Module Wiki</button>
                                </div>

                                <div class="form-group">
                                    <div id="method-select-div"></div>
                                </div>
                                <div class="form-group">
                                    <button type="button" class="btn btn-default btn-wiki" id="btn-method-wiki" style="display: none;">Method Wiki</button>
                                </div>

                                <div class="form-group">
                                    <div class="checkbox">
                                        <label>
                                            <input type="checkbox" value="" id="json-result-pretty">Result Pretty
                                        </label>
                                    </div>
                                </div>
                            </form>
                        </div>

                        <div class="col-lg-6">
                            <form role="form">
                                <div class="form-group">
                                    <label>Id</label>
                                    <input class="form-control" id="json_request_id" value="1">
                                </div>
                                <div class="form-group">
                                    <label>Params</label>
                                    <p>e.g) ["0x1", true]</p>
                                    <textarea class="form-control" rows="10" id="json_request_param"></textarea>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-lg-6 col-lg-offset-6">
                            <button type="button" class="btn btn-outline btn-default btn-lg btn-block" id="btn-json-rpc-request">Request</button>
                        </div>
                    </div>
                </div> <!-- ./ panel body -->
            </div>
        </div>
    </div> <!-- ./ request form row -->

    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    JSON Result &nbsp;&nbsp;&nbsp;
                    <button type="button" class="btn btn-outline btn-default btn-xs" id="btn-json-result-clear">Clear all</button>
                    <%--<button type="button" class="btn btn-outline btn-default btn-xs result-collapse" data-type="open">open all</button>
                    <button type="button" class="btn btn-outline btn-default btn-xs result-collapse" data-type="close">close all</button>--%>
                </div>
                <!-- .panel-heading -->
                <div class="panel-body">
                    <div class="panel-group" id="accordion"></div>
                </div>
                <!-- .panel-body -->
            </div>
            <!-- /.panel -->
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <!-- json rpc tabs -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    PARITY JSON RPC APIS
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body" id="json-rpc-specs-tab-body">
                </div> <!-- /.panel-body -->

            </div> <!-- /.panel -->
        </div>
    </div> <!-- ./json rpc tabs -->
</div>

<script id="json-rpc-tabs-template" type="text/x-handlebars-template">
    <!-- Nav tabs -->
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tab-all" data-toggle="tab">ALL</a>
        {{#each .}}
        <li><a href="#tab-{{module}}" data-toggle="tab">{{module}}</a>
        {{/each}}
    </ul>

    <!-- Tab panes -->
    <div class="tab-content">
        <!-- all -->
        <div class="tab-pane fade in active" id="tab-all">
            {{#each .}}
            <h4>{{module}}</h4>
            <table border="0" style="width : 100%;">
                {{#each methods}}
                    {{#compare @index '%' 3 0}}<tr>{{/compare}}
                    <td style="width:33%;">
                        <a href="#" class="tab-method" data-module="{{../module}}" data-method="{{this}}">
                            {{this}}
                        </a>
                    </td>
                    {{#compare @index '%' 3 2}}</tr>{{/compare}}
                {{/each}}

                {{#compare methods.length '%' 3 1}}
                <td style="width:33%;"></td><td style="width:33%;"></td></tr>
                {{/compare}}
                {{#compare methods.length '%' 3 2}}
                <td style="width:33%;"></td>
                </tr>
                {{/compare}}
            </table>
            {{/each}}
        </div>

        <!-- each modules -->
        {{#each .}}
        <div class="tab-pane fade" id="tab-{{module}}">
            <h4>{{module}}</h4>
            <table border="0" style="width : 100%;">
            {{#each methods}}
                {{#compare @index '%' 3 0}}<tr>{{/compare}}
                <td>
                    <a href="#" class="tab-method" data-module="{{../module}}" data-method="{{this}}">
                        {{this}}
                    </a>
                </td>
                {{#compare @index '%' 3 2}}</tr>{{/compare}}
            {{/each}}
            </table>
        </div>
        {{/each}}
    </div>
</script>

<script id="module-select-template" type="text/x-handlebars-template">
    <label>Modules</label>
    <select class="form-control" id="module-select">
        <option value="">Select Module</option>
        {{#each .}}
        <option value="{{module}}" data-url="{{url}}">{{module}}</option>
        {{/each}}
    </select>
</script>

<script id="method-select-template" type="text/x-handlebars-template">
    <label>Methods</label>
    <select class="form-control" id="method-select">
        <option value="">Select Method</option>
        {{#each .}}
        <option value="{{this}}">{{this}}</option>
        {{/each}}
    </select>
</script>

<script id="json-result-template" type="text/x-handlebars-template">
    <div class="panel panel-default">
        <div class="panel-heading">
            <h4 class="panel-title">
                <a data-toggle="collapse" data-parent="#accordion" href="#collapse{{resultSeq}}">{{resultSeq}} - {{jsonRequest}}</a>
            </h4>
        </div>
        <div id="collapse{{resultSeq}}" class="panel-collapse collapse in">
            <div class="panel-body">
                <pre>{{jsonResult}}</pre>
            </div>
        </div>
    </div>
</script>

<script>
  $(function () {
    var jsonRpcSpecs, method, jsonResultSeq = 0;
    // divs
    var methodSelectDivObj = $('#method-select-div');
    var jsonResultRowDivObj = $('#json-result-row');
    var jsonResultDivObj = $('#accordion');

    // inputs
    var uriObj = $('#uri');
    var jsonResultPretty = $('#json-result-pretty');

    var jsonRequestIdObj = $('#json_request_id');
    var jsonRequstParamObj = $('#json_request_param');

    // buttons
    var moduleWikiObj = $('#btn-module-wiki');
    var methodWikiObj = $('#btn-method-wiki');

    // module select changed
    $(document).on('change', '#module-select', function () {
      var selected = $(this).val();
      if (!selected) {
        changeDisplay(moduleWikiObj, 'none');
        methodSelectDivObj.empty();
        moduleWikiObj.data('url', '');
        methodWikiObj.data('url', '');
        return;
      }

      var url = $(this).find(':selected').data('url');
      moduleWikiObj.text(selected.toUpperCase() + ' Wiki');
      moduleWikiObj.data('url', url);
      changeDisplay(moduleWikiObj, 'block');

      for (var i = 0; i < jsonRpcSpecs.length; i++) {
        if (jsonRpcSpecs[i].module == selected) {
          appendMethodsSelect(jsonRpcSpecs[i].methods);
          return;
        }
      }
    });

    // method select
    $(document).on('change', '#method-select', function () {
      var selected = $(this).val();
      method = selected;
      if (!selected) {
        changeDisplay(moduleWikiObj, 'none');
        changeDisplay(methodWikiObj, 'none');
        methodWikiObj.data('url', '');
        return;
      }

      var url = moduleWikiObj.data('url');
      url += '#' + selected.toLowerCase();

      methodWikiObj.text(selected + ' Wiki');
      methodWikiObj.data('url', url);
      changeDisplay(methodWikiObj, 'block');
    });

    // module wiki click
    $(document).on('click', '.btn-wiki', function () {
      var url = $(this).data('url');
      if (!!url) {
        window.open(url, '_blank');
      }
    });

    // request json rpc
    $(document).on('click', '#btn-json-rpc-request', function () {
      var jsonRequest = getJsonRequestData();
      console.log(jsonRequest);
      if (!jsonRequest.url) {
        alert('Invalid url');
        uriObj.focus();
        return;
      }

      if (!jsonRequest.method) {
        alert('Select method');
        return;
      }

      $.ajax({
        url    : '${context}/json-rpc/parity/request',
        type   : 'POST',
        headers: {
          "Content-Type"          : "application/json",
          "X-HTTP-Method-Override": "POST"
        },
        data   : JSON.stringify(jsonRequest),
        success: function (data) {
          $('.panel-collapse').each(function () {
            $(this).removeClass('in');
          });
          var panelData = {};
          panelData.jsonRequest = JSON.stringify(jsonRequest);
          panelData.jsonResult = data;
          panelData.resultSeq = jsonResultSeq++;
          handlebarsManager.printTemplate(panelData, jsonResultDivObj, $('#json-result-template'), 'append', null, null, false);
        },
        error  : function (jqxhr) {
          alert(jqxhr);
          console.log(jqxhr);
          changeDisplay(jsonResultRowDivObj, 'none');
        }
      });
    });

    // json rpc tab list click
    $(document).on('click', '.tab-method', function (e) {
      e.preventDefault();
      var selectedModule = $(this).data('module');
      var selectedMethod = $(this).data('method');

      $('#module-select').val(selectedModule).trigger('change');
      $('#method-select').val(selectedMethod).trigger('change');

      var offset = $(".page-header").offset();
      $('html, body').animate({scrollTop : offset.top}, 400);
    });

    $(document).on('click', ('#btn-json-result-clear'), function () {
      jsonResultSeq = 0;
      jsonResultDivObj.empty();
    });

    // TODO :: open or close all collapse
    /*$(document).on('click', '.result-collapse', function () {
      var type = $(this).data('type');
      if (type == 'open') {
        $('.panel-collapse').each(function () {
          $(this).addClass('in');
        });
      } else if (type == 'close') {
        $('.panel-collapse').each(function () {
          $(this).removeClass('in');
        });
      } else {
        return;
      }
    });*/

    (function () {
      $.getJSON("${context}/json-rpc/parity/specs", function (data) {
        jsonRpcSpecs = data;
        console.log(data);
        // display json rpc spec tabs
        handlebarsManager.printTemplate(jsonRpcSpecs, $('#json-rpc-specs-tab-body'), $('#json-rpc-tabs-template'), 'append', null, null, true);
        // display modules select box
        appendModuleSelect(data);
      });
    })();

    function changeDisplay(target, value) {
      target.css('display', value);
    }

    function appendModuleSelect() {
      handlebarsManager.printTemplate(jsonRpcSpecs, $('#module-select-div'), $('#module-select-template'), 'append', null, null, true);
    }

    function appendMethodsSelect(methods) {
      handlebarsManager.printTemplate(methods, methodSelectDivObj, $('#method-select-template'), 'append', null, null, true);
    }

    function getJsonRequestData() {
      var request = {};

      request.url = uriObj.val();
      request.method = method;
      if (jsonRequestIdObj.val()) {
        request.id = Number(jsonRequestIdObj.val());
      }
      paramValue = jsonRequstParamObj.val();
      if (paramValue) {
        request.params = JSON.parse(paramValue);
      }

      request.pretty = jsonResultPretty.is(":checked");
      console.log(request.pretty);
      return request;
    }
  });
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>