<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<c:set var="context" value="${pageContext.request.contextPath}"/>

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Ethereum Explorer</title>

    <!-- Bootstrap Core CSS -->
    <link href="${context}/resources/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">

    <!-- MetisMenu CSS -->
    <link href="${context}/resources/vendor/metisMenu/metisMenu.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${context}/resources/dist/css/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="${context}/resources/vendor/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js"></script>
</head>

<body>

<div id="wrapper">
    <!-- Navigation -->
    <nav class="navbar navbar-default navbar-static-top" role="navigation" style="margin-bottom: 0">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">Ethereum Explorer by zaccoding</a>
        </div>
        <!-- /.navbar-header -->

        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                </ul>
            </div>
            <!-- /.sidebar-collapse -->
        </div>
        <!-- /.navbar-static-side -->
    </nav>

    <!-- jQuery -->
    <script src="${context}/resources/vendor/jquery/jquery.min.js"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="${context}/resources/vendor/bootstrap/js/bootstrap.min.js"></script>
    <!-- Metis Menu Plugin JavaScript -->
    <script src="${context}/resources/vendor/metisMenu/metisMenu.min.js"></script>
    <!-- Custom Theme JavaScript -->
    <script src="${context}/resources/dist/js/sb-admin-2.js"></script>

    <!-- handlebars -->
    <script src="${context}/resources/js/handlebars-helper.js"></script>

    <!-- moment -->
    <script src="${context}/resources/js/moment.js"></script>

    <script src="${context}/resources/js/bignumber.js"></script>

    <script id="side-menu-template" type="text/x-handlebars-template">
        <li class="sidebar-search">
            <div class="input-group custom-search-form">
                <input type="text" class="form-control" placeholder="Search...">
                <span class="input-group-btn">
                <button class="btn btn-default" type="button"><i class="fa fa-search"></i></button>
            </span>
            </div>
            <!-- /input-group -->
        </li>
        {{#each nodes}}
        <li>
            <a href="#"><span class="glyphicon glyphicon-chevron-right"></span>   {{nodeName}}<span class="fa arrow"></span></a>
            <ul class="nav nav-second-level">
                <li>
                    <a href="${context}/{{nodeName}}/blocks"><i class="fa fa-bold"></i>&nbsp; Blocks</a>
                </li>
                <li>
                    <a href="${context}/{{nodeName}}/accounts"><i class="fa fa-users"></i>&nbsp;Accounts</a>
                </li>
            </ul>
        </li>
        {{/each}}
        <li>
            <a href="#"><i class="fa fa-slack"></i> JSON-RPC<span class="fa arrow"></span></a>
            <ul class="nav nav-second-level">
                <li>
                    <a href="${context}/json-rpc/parity">Parity</a>
                </li>
            </ul>
        </li>
    </script>

    <script>
      (function () {
        $.ajax({
          url    : "/nodes",
          headers: {
            "Content-Type": "application/json"
          },
          method : 'GET',
          success: function (nodes) {
            var data = {};
            data.ctx = '${context}';
            data.nodes = nodes;
            console.log(nodes);
            handlebarsManager.printTemplate(data, $('#side-menu'), $('#side-menu-template'), 'append', null, null, true);
          },
          error  : function (jqxhr) {
            console.log(jqxhr);
            alert('error');
          }
        });
      })();
    </script>
