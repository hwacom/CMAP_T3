<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="url" content="${pageContext.request.requestURL}" />
	<meta name="uri" content="${pageContext.request.requestURI}" />
	<title>Forbidden</title>

	<!-- Favicon icon -->
    <!-- 桃機 -->
    <!-- 
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/images/icon.ico">
     -->
    
    <!-- 台鐵 -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/images/taiwan_railway.ico">

	<!-- dataTable -->
	<link href="${pageContext.request.contextPath}/resources/DataTables/datatables.min.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/resources/css/main_TRA.css" rel="stylesheet">
	
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>  
</head>
<body>
	<div id="content" class="container-fluid">
		<div class="row">
			<div class="col-12 center" style="color: red;"><h1>403 forbidden.</h1></div>
		</div>
		<div class="row">
			<div class="col-12 center" id="countdown" style="color: yellow;"></div>
		</div>
	</div>
	
	<!-- JavaScript part -->
	<script type="text/javascript">
		var _url = $("meta[name='url']").attr("content");
		var _uri = $("meta[name='uri']").attr("content");
		var redirUrl = _url.replace(_uri, "");
		
	    // Total seconds to wait
	    var seconds = 3;
	    
	    function countdown() {
	        seconds = seconds - 1;
	        if (seconds == 0) {
	            // Chnage your redirection link here
	            window.location = redirUrl;
	        } else {
	            // Update remaining seconds
	            document.getElementById("countdown").innerHTML = "(" + seconds + "秒後將導向登入頁)";
	            // Count down using javascript
	            window.setTimeout("countdown()", 1000);
	        }
	    }
	    
	    // Run countdown function
	    countdown();
	    
	</script>
</body>
</html>