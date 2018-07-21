<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" flush="true"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Ethereum Accounts</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <!-- account list -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Account List
                    <div class="pull-right">
                        <i class="fa fa-refresh" id="btn-refresh" style="cursor:pointer;"></i>
                    </div>
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <table class="table table-striped">
                        <div class="form-inline">
                            <div class="pull-left">
                                Show
                                <select id="btn-page-length">
                                    <option value="10" selected>10</option>
                                    <option value="30">30</option>
                                    <option value="50">50</option>
                                </select>
                                entries
                            </div>
                            <div class="pull-right">
                                <button type="button" class="btn btn-default btn-page" data-diff="-1">Prev</button>
                                <button type="button" class="btn btn-default btn-page" data-diff="1">Next</button>
                            </div>
                        </div>
                </div>

                <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                        <thead>
                        <tr>
                            <th>Address</th>
                            <th>Balance(ETH)</th>
                        </tr>
                        </thead>
                        <tbody id="accounts-info-body">

                        </tbody>
                    </table>
                    <div class="pull-right">
                        <button type="button" class="btn btn-default btn-page" data-diff="-1">Prev</button>
                        <button type="button" class="btn btn-default btn-page" data-diff="1">Next</button>
                    </div>
                </div> <!-- /.table-responsive -->
            </div> <!-- /.panel-body -->
        </div> <!-- /.panel -->
    </div><!-- /.col-lg-12 -->
</div>
<!-- /.row -->
</div>

<script id="account-infos-template" type="text/x-handlebars-template">
    {{#if this}}
    {{#each .}}
    <tr>
        <td style="width: 40%;">{{address}}</td>
        <td>{{balance}} ETH</td>
    </tr>
    {{/each}}
    {{else}}
    <tr>
        <td colspan="100%" align="center"><h4>No results</h4></td>
    </tr>
    {{/if}}
</script>

<script>
  $(function () {
    var start = 1;
    var length = 10;
    var prevStart = start;
    var prevLength = length;
    var nodeName;

    $(document).on('click', '.btn-page', function () {
      var diff = Number($(this).data('diff'));
      var tempStart = start + diff;
      if (tempStart < 1) {
        return;
      }
      start = tempStart;
      displayAccountInfos();
    });

    $(document).on('change', '#btn-page-length', function () {
      start = 1;
      length = $(this).val();
      displayAccountInfos();
    });

    $(document).on('click', '#btn-refresh', function () {
      start = 1;
      displayAccountInfos();
    });

    (function () {
      nodeName = window.location.pathname.split('/')[1];
      displayAccountInfos();
    })();

    function displayAccountInfos() {
      var url = '${context}/' + nodeName + '/accounts/data?start=' + start + '&length=' + length;
      console.log(start, length, prevStart, prevLength);
      $.ajax({
        url    : url,
        headers: {
          "Content-Type": "application/json"
        },
        method : 'GET',
        success: function (data) {
          console.log(data);
          if (data && data.length > 0) {
            prevStart = start;
            prevLength = length;
          } else {
            start = prevStart;
            length = prevLength;
          }
          handlebarsManager.printTemplate(data, $('#accounts-info-body'), $('#account-infos-template'), 'append', null, null, true);
        },
        error  : function (jqxhr) {
          console.log(jqxhr);
          alert('error');
          start = prevStart;
          length = prevLength;
        }
      });
    }
  });
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>