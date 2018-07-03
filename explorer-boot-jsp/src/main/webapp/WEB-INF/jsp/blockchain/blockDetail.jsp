<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" flush="true"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header" id="page-title">
                Block :
            </h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>

    <!-- best block div -->
    <div class="row" id="new-block-div"></div>

    <!-- block list -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h5>Block Detail</h5>
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="col-lg-12" id="block-detail-div">
                    </div>
                </div> <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div><!-- /.col-lg-12 -->
    </div> <!-- /.row -->
</div>

<script id="block-info-template" type="text/x-handlebars-template">
    {{#if .}}
    <div class="row">
        <div class="table-responsive">
            <table class="table table-hover">
                <tbody>
                <tr>
                    <th style="width: 10%;">Hash:</th>
                    <td style="width : 40%;">{{block.hash}}</td>
                    <th style="width: 10%;">Difficulty:</th>
                    <td style="width : 40%;">{{#displayDifficulty block.difficulty}}{{/displayDifficulty}}</td>
                </tr>
                <tr>
                    <th>Miner:</th>
                    <td>{{block.miner}}</td>
                    <th>Total Tx Fees</th>
                    <td>{{block.txFees}} ETH</td>
                </tr>
                <tr>
                    <th>Tx / Uncles:</th>
                    <td>{{block.txCount}} / {{#getLength block.uncles 0}}{{/getLength}}</td>
                    <th>Nonce or Step:</th>
                    <td>
                        {{#if block.nonce}}
                        {{block.nonce}}
                        {{else}}
                        {{block.sealFields.[0]}}
                        {{/if}}
                    </td>
                </tr>
                <tr>
                    <th>Gas Limit:</th>
                    <td>{{block.gasLimit}}</td>
                    <th>Gas Usage:</th>
                    <td>{{#displayGasUsage block}}{{/displayGasUsage}}</td>
                </tr>
                <tr>
                    <th>Time:</th>
                    <td>{{#displayTimestamp block.timestamp 'long'}}{{/displayTimestamp}}</td>
                    <th>Size:</th>
                    <td>{{block.size}} bytes</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- tab row -->
    <div class="row">
        <!-- Nav tabs -->
        <ul class="nav nav-tabs">
            <li class="active"><a href="#tx-tab" data-toggle="tab"># {{#getLength txns 0}}{{/getLength}} Txns</a>
            </li>
            <li><a href="#details-tab" data-toggle="tab">Details</a>
            </li>
        </ul>

        <!-- Tab panes -->
        <div class="tab-content">
            <div class="tab-pane fade in active" id="tx-tab">
                {{#if txns}}
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                        <th>Hash</th>
                        <th>Type</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Value</th>
                        <th>Fee</th>
                        <th>Gas Price</th>
                        </thead>
                        <tbody>
                        {{#each txns}}
                        <tr>
                            <td><a href="/tx/{{hash}}">{{hash}}</a></td>
                            <td>{{#displayTxType this}}{{/displayTxType}}</td>
                            <td><a href="#">{{from}}</a></td>
                            <td><a href="#">{{to}}</a></td>
                            <td>{{value}} ETH</td>
                            <td>{{txPrice}}ETH</td>
                            <td>{{gasPrice}}</td>
                        </tr>
                        {{/each}}
                        </tbody>
                    </table>
                </div>
                {{/if}}
            </div>
            <div class="tab-pane fade" id="details-tab">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th style="width: 20%;">Uncles Hash</th>
                            <td>{{block.sha3Uncles}}</td>
                        </tr>
                        <tr>
                            <th>Receipt Root Hash</th>
                            <td>{{block.receiptRoot}}</td>
                        </tr>
                        <tr>
                            <th>State Root Hash</th>
                            <td>{{block.stateRoot}}</td>
                        </tr>
                        <tr>
                            <th>Tranactions Root Hash</th>
                            <td>{{block.transactionsRoot}}</td>
                        </tr>
                        <tr>
                            <th>Mix Hash or Signature</th>
                            <td>
                                {{#if block.mixHash}}
                                {{block.mixHash}}
                                {{else}}
                                {{block.sealFields.[1]}}
                                {{/if}}
                            </td>
                        </tr>
                        <tr>
                            <th>Uncles</th>
                            <td>{{block.uncles}}</td>
                        </tr>
                        <tr>
                            <th>Bloom Data</th>
                            <td>{{block.logsBloom}}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div><!-- ./ tab row -->
    {{else}}
    <div class="row">
        <h4>No Result</h4>
    </div>

    {{/if}}
</script>

<script>
  $(function () {
    (function () {
      getBlock();
    })();

    function getBlock() {
      var href = window.location.href;
      var keyword = href.substr(href.indexOf('/block/') + 7);

      $('#page-title').append('<small>' + keyword + '</small>');

      $.ajax({
        url    : window.location.href,
        headers: {
          "Content-Type": "application/json"
        },
        method : 'POST',
        success: function (block) {
          console.log('success', block);
          handlebarsManager.printTemplate(block, $('#block-detail-div'), $('#block-info-template'), 'append', null, null, true);
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