<%--
https://blackrockdigital.github.io/startbootstrap-sb-admin-2/pages/index.html
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" flush="true"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Ethereum light explorer</h1>
        </div>
        <!-- /.col-lg-12 -->
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Ethereum Client`s
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body" id="eth-nodes-body-div">
                </div><!-- /.panel-body -->
            </div><!-- /.panel -->
        </div>
    </div>
</div>

<script id="eth-nodes-template" type="text/x-handlebars-template">
    <div class="table-responsive">
        <table class="table">
            <thead>
            <tr>
                <th>#</th>
                <th>Node Name</th>
                <th>Client Version</th>
            </tr>
            </thead>
            <tbody>
            {{#each .}}
            <tr class="{{#displayBootstrapClasses @index}}{{/displayBootstrapClasses}}">
                <td>{{@index}}</td>
                <td><a href="${context}/{{nodeName}}/blocks">{{nodeName}}</a></td>
                <td>{{clientVersion}}</td>
            </tr>
            {{/each}}
            </tbody>
        </table>
    </div>
</script>

<script>
  $(function () {
    (function () {
      displayEthNodes();
    })();

    function displayEthNodes() {
      $.ajax({
        url    : "/nodes",
        headers: {
          "Content-Type": "application/json"
        },
        method : 'GET',
        success: function (nodes) {
          handlebarsManager.printTemplate(nodes, $('#eth-nodes-body-div'), $('#eth-nodes-template'), 'append', null, null, true);
        },
        error  : function (jqxhr) {
          console.log(jqxhr);
          alert('error');
        }
      });
    }
  });
</script>


<jsp:include page="../common/footer.jsp" flush="true"/>