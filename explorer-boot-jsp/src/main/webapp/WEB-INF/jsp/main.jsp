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
<div id="blocks">
</div>

</body>

<script>
  $(function () {
    var blockDiv = $('#blocks');
    var error = false;

    $.get('http://localhost:8080/last-block', function (block) {
      console.log('#Receive block : ', block);
      printBlock(block);
      subscribe();
    });

    function subscribe() {
      if (error) {
        return;
      }

      $.ajax({
        url     : 'http://localhost:8080/subscribe',
        headers : {
          "Content-Type": "application/json"
        },
        success : function (block) {
          console.log('success', block);
          printBlock(block);
        },
        error   : function (jqxhr) {
          alert('error');
          error = true;
          console.log(jqxhr);
        },
        complete: subscribe
      });
    }

    function printBlock(blockDto) {
      var html = '<h4>Receive block : ' + blockDto.block.number + '</h4> <br/>';
      blockDiv.append(html);
    }
  });
</script>

</html>