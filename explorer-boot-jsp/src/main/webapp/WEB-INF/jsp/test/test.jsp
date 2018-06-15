<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<jsp:include page="../common/header.jsp" flush="true"/>

<div id="page-wrapper">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">Sample Content</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
</div>

<script>
  $(function () {
    alert('${context}');
  });
</script>

<jsp:include page="../common/footer.jsp" flush="true"/>
