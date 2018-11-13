<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglib.jsp" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>組態設定管理系統</title>
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="description" content="">
    <meta name="author" content="">
    <meta name="ctx" content="${pageContext.request.contextPath}" />
    
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/images/icon.ico">
    
    <!-- Bootstrap Core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- JQuery-UI -->
    <!-- <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.min.css" rel="stylesheet"> -->
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.structure.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.theme.min.css" rel="stylesheet">
    <!-- dataTable -->
	<link href="${pageContext.request.contextPath}/resources/DataTables/datatables.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/blog.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/fontawesome/all.css" rel="stylesheet">
	
	<!-- Core Javascript -->
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/popper/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-ui/jquery-ui.min.js"></script>
    <!-- Icons -->
    <script src="${pageContext.request.contextPath}/resources/js/feather-icons/feather.min.js"></script>
	<!-- dataTable -->
	<!-- <script src="${pageContext.request.contextPath}/resources/js/dataTable/jquery.dataTables.min.js"></script> -->
	<script src="${pageContext.request.contextPath}/resources/DataTables/datatables.min.js"></script>
	<!-- Underscore -->
	<script src="${pageContext.request.contextPath}/resources/js/underscore/underscore-min.js"></script>
	<!-- blockUI -->
	<script src="${pageContext.request.contextPath}/resources/js/blockUI/jquery.blockUI.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/common.js"></script>
	
	<script>
		$(function () {
		  $('[data-toggle="popover"]').popover()
		})
	</script>
</head>

<body>
	<div class="loader"></div>
	<div class="mask" style="display: none;"></div>
	<div class="processing" style="display: none;"></div>
	
    <nav class="navbar navbar-dark fixed-top flex-md-nowrap p-0 shadow navbar-bg">
      <a href="${pageContext.request.contextPath}/index">
      	<img class="img" src="${pageContext.request.contextPath}/resources/images/aptg_logo_1.png" width="auto" height="40" />
      	<!-- <img class="img web-only" src="${pageContext.request.contextPath}/resources/images/Logo_word.png" width="auto" height="40" /> -->
      	<span class="font-weight-bold title-font" style="color:#000079">組態設定管理系統</span>	
      </a>
      <ul class="navbar-nav">
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="${pageContext.request.contextPath}/logout"><span data-feather="log-out"></span></a>
        </li>
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="#" data-container="body" data-toggle="popover" data-placement="bottom" title="使用者帳號" data-content="${userInfo }"><span data-feather="user"></span></a>
        </li>
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="#" onclick="toggleMenu()"><span id="menu-icon" data-feather="menu"></span></a>
        </li>
      </ul>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="web-menu col-md-2 d-none d-md-block sidebar sidebar-bg">
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
                <a class="nav-link" href="${pageContext.request.contextPath}/version/restore">
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
                  <span data-feather="send"></span>
                  	<span>供裝派送</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/record">
                  <span data-feather="search"></span>
                  	<span>供裝紀錄</span>
                </a>
              </li>
              
              <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
              	  <li class="nav-item">
	                <a class="nav-link toggleMenuLink" id="toggleMenu_admin" href="#">
	                  <span data-feather="settings"></span>
	                  	<span>後臺管理&nbsp;<span id="toggleMenu_admin_icon" data-feather="chevron-down"></span></span>
	                </a>
	                <ul aria-expanded="false" id="toggleMenu_admin_items" class="collapse">
	                    <li class="nav-item subMenu-item">
	                    	<a href="${pageContext.request.contextPath}/admin/env/main">
	                    		<span data-feather="command"></span> 系統參數維護
	                    	</a>
	                    </li>
	                    <li class="nav-item subMenu-item">
	                    	<a href="${pageContext.request.contextPath}/admin/script/main">
	                    		<span data-feather="hash"></span> 預設腳本維護
	                    	</a>
	                    </li>
	                    <li class="nav-item subMenu-item">
	                    	<a href="${pageContext.request.contextPath}/admin/job/main">
	                    		<span data-feather="check-square"></span> 排程設定維護
	                    	</a>
	                    </li>
	                    <li class="nav-item subMenu-item">
	                    	<a href="${pageContext.request.contextPath}/admin/log/main">
	                    		<span data-feather="alert-triangle"></span> 系統LOG查詢
	                    	</a>
	                    </li>
	                </ul>
	              </li>
              </sec:authorize>
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
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/version/backup">版本備份</a></span>
	            <!-- 未完成版本先mark
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/version/recover">版本還原</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/script">腳本管理</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/delivery">供裝派送</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/record">供裝紀錄</a></span>
	             -->
	          <div style="z-index: 9999;margin-right: 11px;margin-top: -3px;position: fixed;left: calc(100% - 15px);">
	          	<span style="color: white;padding-top:9px;background-color: #344e6a;width: 12px;height: 2.75rem;float: right">></span>
	          </div>
	        </nav>
	    </div>
		    
        <main role="main" class="ml-sm-auto col-md-10">
			<decorator:body />
        </main>
        
        <input type="hidden" id="queryFrom" name="queryFrom" />
        
        <!-- Modal [View] start -->
		<div class="modal fade" id="viewModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-mid" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLabel"><span id="msgModal_title">版本內容預覽</span></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		     	<div class="form-group row">
		        	<label for="viewModal_group" class="col-md-2 col-sm-12 col-form-label">群組</label>
		    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_group" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_device" class="col-md-2 col-sm-12 col-form-label">設備</label>
		    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_device" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_version" class="col-md-2 col-sm-12 col-form-label">版本號碼</label>
		    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_version" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_content" class="col-md-2 col-sm-12 col-form-label">版本內容</label>
		        	<!-- <textarea class="form-control col-md-9 col-sm-12" id="viewModal_content" rows="10" readonly></textarea> -->
		        	<div class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_content" style="height: 300px;overflow: auto;"></div>
		        </div>
		        
		      </div>
		      <div class="modal-footer">
		      </div>
		    </div>
		  </div>
		</div>
		<!-- Modal [View] end -->
        
        <footer role="footer" class="ml-sm-auto col-md-10 footer">
        	<span class="copyright">聯絡我們 | Copyright &copy; 2018-2019 HwaCom Systems Inc. All Rights Reserved.</span>	
        </footer>
        
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
	<script src="${pageContext.request.contextPath}/resources/js/cmap.main.js"></script>
	
</body>
</html>