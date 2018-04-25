<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Test: hello</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>

</body>

<script>
  $(function () {
    $.ajax({
      url     : '/blocks/null/10',
      type    : "GET",
      dataType: "json",
      success : function (data) {
        console.log('Receive blocks', data);
      },
      error   : function (jqxhr) {
        console.log(jqxhr);
      }
    });
  });
</script>

</html>