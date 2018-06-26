<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../common/header.jsp" flush="true"/>
<c:set var="context" value="${pageContext.request.contextPath}"/>
<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
                <h1 class="page-header">Ethereum Blocks</h1>
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
                    Block list
                    <div class="pull-right">
                        <i class="fa fa-refresh" id="btn-refresh" style="cursor:pointer;"></i>
                    </div>
                </div>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="table-responsive">
                        <div class="form-inline">
                            <div class="pull-left">
                                Show
                                <select id="btn-page-length">
                                    <option value="10">10</option>
                                    <option value="20">20</option>
                                    <option value="30">30</option>
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
                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Hash</th>
                                <th>Difficulty</th>
                                <th>Miner</th>
                                <th>Usage</th>
                                <th>Time</th>
                                <th>#Tx</th>
                                <th>#Uncles</th>
                            </tr>
                            </thead>
                            <tbody id="block-info-body">
                            </tbody>
                        </table>
                        <div class="pull-right">
                            <button type="button" class="btn btn-default btn-page" data-diff="-1">Prev</button>
                            <button type="button" class="btn btn-default btn-page" data-diff="1">Next</button>
                        </div>
                    </div>
                    <!-- /.table-responsive -->
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div><!-- /.col-lg-12 -->
    </div> <!-- /.row -->
</div>

<script id="new-block-alert-template" type="text/x-handlebars-template">
    <div class="row">
        <div class="col-lg-12">
            <div id="new-block-notification-div">
                <div class="alert alert-success alert-dismissable">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
                    New block imported # <strong>{{number}}</strong>
                </div>
            </div>
        </div>
    </div>
</script>

<script id="new-block-template" type="text/x-handlebars-template">
    <div class="col-lg-12">
        <div class="panel panel-default" id="best-block-div">
            <div class="panel-heading">
                Best block : #<strong>{{block.number}}</strong>
            </div>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <tbody>
                        <tr>
                            <th style="width: 10%;">Hash:</th>
                            <th style="width : 40%;">{{block.hash}}</th>
                            <th style="width: 10%;">Difficulty:</th>
                            <th style="width : 40%;">{{#displayDifficulty block.difficulty}}{{/displayDifficulty}}</th>
                        </tr>
                        <tr>
                            <th>Miner:</th>
                            <th>{{block.miner}}</th>
                            <th>Reward:</th>
                            <th>TODO REWARD</th>
                        </tr>
                        <tr>
                            <th>Tx Fees:</th>
                            <th>TODO TX FEE</th>
                            <th>Tx / Uncles:</th>
                            <th>{{block.txCount}} / {{#getLength block.uncles 0}}{{/getLength}}</th>
                        </tr>
                        <tr>
                            <th>Gas Limit:</th>
                            <th>{{block.gasLimit}}</th>
                            <th>Gas Usage:</th>
                            <th>{{#displayGasUsage block}}{{/displayGasUsage}}</th>
                        </tr>
                        <tr>
                            <th>Time:</th>
                            <th>{{#displayTimestamp block.timestamp 'long'}}{{/displayTimestamp}}</th>
                            <th>Size:</th>
                            <th>{{block.size}} bytes</th>
                        </tr>
                        <tr>
                            <th>SealFields:</th>
                            <th colspan="100%">{{#displaySealfields block.sealFields}}{{/displaySealfields}}</th>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <!-- best block alert-->
                <div class="table-responsive" id="new-block-alert-div">
                </div>
            </div>
        </div>
    </div>
</script>

<script id="block-infos-template" type="text/x-handlebars-template">
    {{#each .}}
    <tr>
        <td><a href="block/{{number}}">{{number}}</a></td>
        <td><a href="block/{{hash}}">{{hash}}</a></td>
        <td>{{#displayDifficulty difficulty}}{{/displayDifficulty}}</td>
        <td>{{miner}}</td>
        <td>{{#displayGasUsage this}}{{/displayGasUsage}}</td>
        <td>{{#displayTimestamp timestamp 'short'}}{{/displayTimestamp}}</td>
        <td>{{txCount}}</td>
        <td>{{#getLength uncles 0}}{{/getLength}}</td>
    </tr>
    {{/each}}
</script>

<script>
  $(function () {
    var start = 1;
    var length = 10;
    var subscribeError = false;

    var newBlockAlertDiv = $('#new-block-alert-div');
    var newBlockDiv = $('#new-block-div');
    var blockInfoBody = $('#block-info-body');

    $(document).on('click', '.btn-page', function () {
      var diff = Number($(this).data('diff'));
      var tempStart = start + diff;
      if (tempStart < 1) {
        return;
      }
      start = tempStart;
      displayBlockInfos();
    });

    $(document).on('change', '#btn-page-length', function () {
      length = $(this).val();
      displayBlockInfos();
    });

    $(document).on('click', '#btn-refresh', function () {
      start = 1;
      displayBlockInfos();
    });

    (function () {
      var url = '${context}/blocks/is-subscribe';
      $.getJSON(url, function (data) {
        if (data == true) {
          subscribeNewBlocks();
        }
      });
      displayBlockInfos();
    })();

    function subscribeNewBlocks() {
      if (subscribeError) {
        // TODO :: add subscribe error
        return;
      }

      $.ajax({
        url     : '/blocks/subscribe',
        headers : {
          "Content-Type": "application/json"
        },
        success : function (block) {
          console.log('success', block);
          displayNewBlock(block);
        },
        error   : function (jqxhr) {
          subscribeError = true;
          console.log(jqxhr);
        },
        complete: subscribeNewBlocks
      });
    }

    function displayNewBlock(data) {
      handlebarsManager.printTemplate(data, newBlockDiv, $('#new-block-template'), 'append', null, null, true);
      handlebarsManager.printTemplate(data.block, $('#new-block-alert-div'), $('#new-block-alert-template'), 'append', null, null, true);
      $('#new-block-alert-div').fadeOut(3000, function () {
        newBlockAlertDiv.empty();
      });
    }

    function displayBlockInfos() {
      var url = '${context}/blocks/data?start=' + start + '&length=' + length;
      $.getJSON(url, function (data) {
        console.log(data);
        handlebarsManager.printTemplate(data, blockInfoBody, $('#block-infos-template'), 'append', null, null, true);
      });
    }
  });
</script>


<jsp:include page="../common/footer.jsp" flush="true"/>