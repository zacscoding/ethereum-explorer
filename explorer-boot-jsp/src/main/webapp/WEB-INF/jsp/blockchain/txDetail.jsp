<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" flush="true"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header" id="page-title">
                Transaction :
            </h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <!-- tx detail row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h5>Transaction Detail</h5>
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="col-lg-12" id="tx-detail-div">
                    </div>
                </div> <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div><!-- /.col-lg-12 -->
    </div> <!-- ./ tx detail row -->
</div>

<script id="tx-info-template" type="text/x-handlebars-template">
    <div class="row">
        <div class="table-responsive">
            <table class="table table-hover">
                <tbody>
                <tr>
                    <th style="width: 20%;">Hash:</th>
                    <td style="width : 60%;">{{hash}}</td>
                </tr>
                <tr>
                    <th>Status</th>
                    <td>{{#displayTxStatus this}}{{/displayTxStatus}}</td>
                </tr>
                <tr>
                    <th>Block</th>
                    <td>
                        {{#if pending}}
                        Waiting for block
                        {{else}}
                        {{blockNumber}} | {{confirms}} Confirms
                        {{/if}}
                    </td>
                </tr>
                <tr>
                    <th>Time</th>
                    <td>{{#displayTimestamp timestamp 'long'}} {{/displayTimestamp}}</td>
                </tr>
                <tr>
                    <th>From</th>
                    <td>{{from}}</td>
                </tr>
                <tr>
                    <th>To</th>
                    <td>{{to}}</td>
                </tr>
                <tr>
                    <th>Nonce</th>
                    <td>{{nonce}}</td>
                </tr>
                <tr>
                    <th>Value</th>
                    <td>{{value}} ETH</td>
                </tr>
                <tr>
                    <th>Fee</th>
                    <td>{{txPrice}} ETH</td>
                </tr>
                <tr>
                    <th>Gas Price</th>
                    <td>{{gasPrice}}</td>
                </tr>
                <tr>
                    <th>Gas Used</th>
                    <td>{{gas}}</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</script>
<script>
  $(function () {
    var href = window.location.href;
    var delimiter = '/tx/';
    var keyword = href.substr(href.indexOf(delimiter) + delimiter.length);
    var blockTime;

    (function () {
      $('#page-title').append('<small>' + keyword + '</small>');
      getTx();
    })();

    function getTx() {
      $.ajax({
        url    : window.location.href,
        headers: {
          "Content-Type": "application/json"
        },
        method : 'POST',
        success: function (tx) {
          console.log('success', tx);
          handlebarsManager.printTemplate(tx, $('#tx-detail-div'), $('#tx-info-template'), 'append', null, null, true);
          if (tx.pending) {
            if (!blockTime) {
              $.getJSON('/blocks/time', function (time) {
                console.log(time);
                blockTime = time;
                setTimeout(function () {
                  getTx();
                }, blockTime);
              });
            } else {
              setTimeout(function () {
                getTx();
              }, blockTime);
            }
          }
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