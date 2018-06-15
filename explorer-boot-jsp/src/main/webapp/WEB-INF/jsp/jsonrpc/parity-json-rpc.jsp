<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="../common/header.jsp" flush="true"/>

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
                    Server Information
                </div>
                <!-- panel body -->
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-6">
                            <form role="form">
                                <div class="form-group">
                                    <label>URI</label>
                                    <input class="form-control" id="uri" value="http://192.168.5.77:8540">
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

    <!-- json request result -->
    <div class="row" id="json-result-row" style="display:none;">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    JsonRequestResult
                </div>
                <!-- panel body -->
                <div class="panel-body">
                    <div class="row">
                        <div class="col-lg-9">
                            <button type="button" class="btn btn-default">Expand</button>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12" id="json-result-div"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- ./ json request result -->
</div>

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
                <a data-toggle="collapse" data-parent="#accordion" href="#collapse{{resultSeq}}">{{jsonRequest}}</a>
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

    $(document).on('click', '.result-collapse', function () {
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
    });

    (function () {
      $.getJSON("${context}/json-rpc/parity/specs", function (data) {
        jsonRpcSpecs = data;
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