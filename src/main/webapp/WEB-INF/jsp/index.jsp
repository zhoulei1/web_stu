<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>Paypayzhu JAVA Demo</title>
<script type="text/javascript"  src="../js/jquery-3.3.1.min.js"></script>
</head>
<!-- Jquery files -->
<script type="text/javascript">
$(function(){
    $.post(
            "/data",
            {
                name : $("#id1").val()

            },
            function(data){
            	$("#id1").val(data.name)
            }, "json"
        );
});
</script>   
<body>
<input id="id1"/>
</body>
</html>