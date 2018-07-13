<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglib.jsp" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>CMAP</title>
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="description" content="">
    <meta name="author" content="">
    
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
    
    <!-- Bootstrap Core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- materialize.css -->
    <!-- <link href="${pageContext.request.contextPath}/resources/css/materialize.min.css" rel="stylesheet"> -->
    <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/blog.css" rel="stylesheet">
	<!-- dataTable -->
	<link href="${pageContext.request.contextPath}/resources/DataTables/datatables.min.css" rel="stylesheet">
	
	<!-- Core Javascript -->
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/popper/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap/bootstrap.min.js"></script>
    <!-- Icons -->
    <script src="${pageContext.request.contextPath}/resources/js/feather-icons/feather.min.js"></script>
	<!-- dataTable -->
	<!-- <script src="${pageContext.request.contextPath}/resources/js/dataTable/jquery.dataTables.min.js"></script> -->
	<script src="${pageContext.request.contextPath}/resources/DataTables/datatables.min.js"></script>
	<!-- Underscore -->
	<script src="${pageContext.request.contextPath}/resources/js/underscore/underscore-min.js"></script>
	<!-- blockUI -->
	<script src="${pageContext.request.contextPath}/resources/js/blockUI/jquery.blockUI.js"></script>
	
</head>

<body>
    <nav class="navbar navbar-dark fixed-top flex-md-nowrap p-0 shadow navbar-bg">
      <a href="#">
      	<img class="img" src="${pageContext.request.contextPath}/resources/images/hwacom.png" width="100" height="40" />
      	<span class="font-weight-bold title-font" style="color:#1C2269">組態設定管理系統</span>	
      </a>
      <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="${pageContext.request.contextPath}/logout"><span data-feather="log-out"></span></a>
        </li>
      </ul>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block sidebar sidebar-bg">
          <div class="sidebar-sticky">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/version/manage">
                  <span data-feather="file-text"></span>
                  	<span>版本管理</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/version/backup">
                  <span data-feather="download"></span>
                  	<span>版本備份</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/version/recover">
                  <span data-feather="upload"></span>
                  	<span>版本還原</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/script">
                  <span data-feather="code"></span>
                  	<span>腳本管理</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/delivery">
                  <span data-feather="cast"></span>
                  	<span>供裝派送</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/record">
                  <span data-feather="search"></span>
                  	<span>供裝紀錄</span>
                </a>
              </li>
            </ul>
          </div>
        </nav>

        <!-- ============================================================== -->
        <!-- End Page wrapper  -->
        <!-- ============================================================== -->
        <div class="mobile-menu nav-scroller py-1 mb-2">
	        <nav class="nav d-flex justify-content-between">
	          <div>
	          	<span style="color: white;padding-top:9px;position: fixed;z-index: 999;background-color: #344e6a;width: 12px;height: 2.75rem;margin-top: -3px"><</span>
	          </div>
	          	<span class="p-2"><a href="${pageContext.request.contextPath}/version/manage">版本管理</a></span>
	            <span class="p-2" style="color:white">&nbsp;&nbsp;|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/version/backup">版本備份</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/version/recover">版本還原</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/script">腳本管理</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/delivery">供裝派送</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/record">供裝紀錄</a></span>
	          <div style="z-index: 9999;margin-right: 11px;margin-top: -3px;position: fixed;left: calc(100% - 15px);">
	          	<span style="color: white;padding-top:9px;background-color: #344e6a;width: 12px;height: 2.75rem;float: right">></span>
	          </div>
	        </nav>
	    </div>
		    
        <main role="main" class="ml-sm-auto col-md-10">
			<decorator:body />
        </main>
        
        <footer role="footer" class="ml-sm-auto col-md-10 footer">
        	<span class="copyright">聯絡我們 | Copyright &copy; 2018-2019 HwaCom Systems Inc. All Rights Reserved.</span>	
        </footer>
        
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    
    <script>
    	feather.replace();
      	
    	$(document).ready(function() {
    		// get current URL path and assign 'active' class
			var pathname = window.location.pathname;
    		
    		if (pathname == '/CMAP/' || pathname == '/CMAP/index') {
    			$('.nav > .nav-item > a[href="/CMAP/version/manage"]').addClass('active');
    		} else {
    			$('.nav > .nav-item > a[href="'+pathname+'"]').addClass('active');
    		}
		});

    </script>

</body>
</html>