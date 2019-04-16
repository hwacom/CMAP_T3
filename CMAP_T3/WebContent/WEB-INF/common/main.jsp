<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="com.cmap.Env" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglib.jsp" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title><spring:message code="cmap.title" /></title>
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="description" content="">
    <meta name="author" content="">
    <meta name="ctx" content="${pageContext.request.contextPath}" />
    <meta name="timeout" content="${timeout}" />
    
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/images/icon.ico">
    
    <!-- Bootstrap Core CSS -->
    <!-- <link href="${pageContext.request.contextPath}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet"> -->
    <!-- JQuery-UI -->
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.structure.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.theme.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-scrollbar/jquery.scrollbar.css" rel="stylesheet">
    
    <!-- dataTable -->
	<link href="${pageContext.request.contextPath}/resources/DataTables/datatables.min.css" rel="stylesheet">
    <!-- <link href="${pageContext.request.contextPath}/resources/css/blog.css" rel="stylesheet"> -->
    <link href="${pageContext.request.contextPath}/resources/css/fontawesome/all.css" rel="stylesheet">
    
    <link href="${pageContext.request.contextPath}/resources/css/main_T3.css" rel="stylesheet">
	
	<!-- Core Javascript -->
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/dist/jquery.validate.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/dist/additional-methods.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/dist/localization/messages_zh_TW.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/popper/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-ui/jquery-ui.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-scrollbar/jquery.scrollbar.min.js"></script>
    <!-- Icons -->
    <script src="${pageContext.request.contextPath}/resources/js/feather-icons/feather.min.js"></script>
	<!-- dataTable -->
	<!-- <script src="${pageContext.request.contextPath}/resources/js/dataTable/jquery.dataTables.min.js"></script> -->
	<script src="${pageContext.request.contextPath}/resources/DataTables/datatables.min.js"></script>
	<!-- Underscore -->
	<script src="${pageContext.request.contextPath}/resources/js/underscore/underscore-min.js"></script>
	<!-- blockUI -->
	<script src="${pageContext.request.contextPath}/resources/js/blockUI/jquery.blockUI.js"></script>
	<!-- cleave -->
	<script src="${pageContext.request.contextPath}/resources/js/cleave/cleave.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/modernizr/modernizr-custom.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/common.min.js"></script>
	<!-- ImageMapster -->
	<script src="${pageContext.request.contextPath}/resources/js/imagemapster/jquery.imagemapster.js"></script>
	
	<script>
		$(function () {
		  $('[data-toggle="popover"]').popover()
		})
	</script>
</head>

<%
	final String __SHOW__ = "Y";
%>

<body>
	<div class="loader"></div>
	<div class="mask" style="display: none;"></div>
	<div class="processing" style="display: none;"></div>
	
	<!-- [START] Top bar -->
	<nav class="navbar">
		<div class="nav">
			<img class="logo" src="${pageContext.request.contextPath}/resources/images/hwacom_white.png">
		</div>
		<div class="nav system-title">
			<a href="${pageContext.request.contextPath}/index"><spring:message code="cmap.title" /></a>
		</div>
		
		<div class="nav reverse navbar-item"><i class="fas fa-sign-out-alt"></i><a href="${pageContext.request.contextPath}/logout"><spring:message code="btn.logout" /></a></div>
		<div class="nav reverse"><i class="fas fa-user"></i><span id="nav_user">${userInfo }</span></div>
		<div class="nav reverse navbar-time"><span id="nav_timer"></span></div>
		<div class="nav reverse navbar-date"><span id="nav_date"></span></div>
	</nav>
	<!-- [END] Top bar -->

	<!-- [START] Menu -->
	<aside class="menu">
		<div data-ul-id="cm1" class="ul-bg"></div>
		<div class="ul-title">
			<spring:message code="menu.cm.manage" />
		</div>
		<ul>
			<li data-li-id="cm_manage"><a id="cm_manage" href="${pageContext.request.contextPath}/version/manage"><span><spring:message code="func.version.manage" /></span></a></li>
		    <li data-li-id="cm_backup"><a id="cm_backup" href="${pageContext.request.contextPath}/version/backup"><span><spring:message code="func.version.backup" /></span></a></li>
		    <li data-li-id="cm_restore"><a id="cm_restore" href="${pageContext.request.contextPath}/version/restore"><span><spring:message code="func.version.restore" /></span></a></li>
		</ul>
		<div data-ul-id="cm2" class="ul-bg"></div>
		<div class="ul-title">
			<spring:message code="menu.provision" />
		</div>
		<ul>
		    <li data-li-id="cm_delivery"><a id="cm_delivery" href="${pageContext.request.contextPath}/delivery"><span><spring:message code="func.provision.delivery" /></span></a></li>
		    <%
              if (Env.SHOW_MENU_ITEM_CM_SCRIPT.equals(__SHOW__)) {
            %>
		    	<li data-li-id="cm_script"><a id="cm_script" href="${pageContext.request.contextPath}/script"><span><spring:message code="func.script.manage" /></span></a></li>
		    <%
              }
            %>
		    <li data-li-id="cm_record"><a id="cm_record" href="${pageContext.request.contextPath}/delivery/record"><span><spring:message code="func.provision.record" /></span></a></li>
		</ul>
		<div data-ul-id="ot" class="ul-bg"></div>
		<div class="ul-title">
			其他系統
		</div>
		<ul>
			<!-- 
			<li data-li-id="ot_A"><a id="ot_A" href="${pageContext.request.contextPath}/others/sys/A"><span>建物管理系統</span></a></li>
			<li data-li-id="ot_B"><a id="ot_B" href="${pageContext.request.contextPath}/others/sys/B"><span>機場收入管理系統</span></a></li>
			 -->
			<li data-li-id="ot_C"><a id="ot_C" href="${pageContext.request.contextPath}/others/sys/C"><span>私有雲平台</span></a></li>
			<li data-li-id="ot_D"><a id="ot_D" href="${pageContext.request.contextPath}/others/sys/D"><span>統一威脅管理平台</span></a></li>
			<li data-li-id="ot_CCTV"><a id="ot_CCTV" href="${pageContext.request.contextPath}/others/sys/CCTV"><span>CCTV</span></a></li>
			<li data-li-id="ot_E"><a id="ot_E" href="${pageContext.request.contextPath}/others/sys/E"><span>無線網路網管</span></a></li>
			<li data-li-id="ot_F"><a id="ot_F" href="${pageContext.request.contextPath}/others/sys/F"><span>PRTG</span></a></li>
			<li data-li-id="ot_G"><a id="ot_G" href="${pageContext.request.contextPath}/others/sys/G"><span>Prime Network</span></a></li>
		</ul>
		<div data-ul-id="bk" class="ul-bg"></div>
		<div class="ul-title">
			<spring:message code="menu.backend" />
		</div>
		<ul>
			<li data-li-id="bk_env"><a id="bk_env" href="${pageContext.request.contextPath}/admin/env/main"><span><spring:message code="func.sys.env.manage" /></span></a></li>
			<li data-li-id="bk_job"><a id="bk_job" href="${pageContext.request.contextPath}/admin/job/main"><span><spring:message code="func.job.manage" /></span></a></li>
			<li data-li-id="bk_log"><a id="bk_log" href="${pageContext.request.contextPath}/admin/log/main"><span><spring:message code="func.sys.log.inquiry" /></span></a></li>
		</ul>
	</aside>
	<!-- [END] Menu -->
    
    <!-- [START] Content -->
    <main role="main" class="content">
		<decorator:body />
    </main>
	<!-- [START] Content -->
	
	<input type="hidden" id="queryFrom" name="queryFrom" />
        
    <!-- Modal [View 組態內容] start -->
	<div class="modal fade" id="viewModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel"><span id="msgModal_title"><spring:message code="config.content.preview" /></span></h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	     	<div class="form-group row">
	        	<label for="viewModal_group" class="col-1 col-form-label"><spring:message code="group.name" /> :</label>
	    		<input type="text" class="form-control form-control-sm col-11" id="viewModal_group" readonly>
	        </div>
	        <div class="form-group row">
	        	<label for="viewModal_device" class="col-1 col-form-label"><spring:message code="device.name" /> :</label>
	    		<input type="text" class="form-control form-control-sm col-11" id="viewModal_device" readonly>
	        </div>
	        <div class="form-group row">
	        	<label for="viewModal_version" class="col-1 col-form-label"><spring:message code="config.version" /> :</label>
	    		<input type="text" class="form-control form-control-sm col-11" id="viewModal_version" readonly>
	        </div>
	        <div class="form-group row">
	        	<label for="viewModal_content" class="col-1 col-form-label"><spring:message code="config.content" /> :</label>
	        	<!-- <textarea class="form-control col-md-9 col-sm-12" id="viewModal_content" rows="10" readonly></textarea> -->
	        	<div class="form-group col-11 textarea-section textarea script" style="min-height: 300px; margin-left: 8px; border-radius: 0.25rem; border: 1px solid #ced4da;" id="viewModal_content"></div>
	        </div>
	        
	      </div>
	      <div class="modal-footer">
	      </div>
	    </div>
	  </div>
	</div>
	<!-- Modal [View 組態內容] end -->
	
	<!-- Modal [View 腳本內容] start -->
	<div class="modal fade" id="viewScriptModal" tabindex="-1" role="dialog" aria-labelledby="viewScriptModalLabel" aria-hidden="true">
	  <div class="modal-dialog modal-lg" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="viewScriptModalLabel"><span id="msgModal_title"><spring:message code="script.content.preview" /></span></h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	     	<div class="form-group row">
	        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="script.name" /> :</span></div>
	    		<div class="col-11 yellow" id="viewScriptModal_scriptName"></div>
	        </div>
	        <div class="form-group row">
	        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="script.content" /> :</span></div>
	        </div>
	        <div class="form-group row">
	        	<div class="form-group textarea-section textarea script">
	        		<pre id="viewScriptModal_scriptContent"></pre>
	        	</div>
	        </div>
	      </div>
	      <div class="modal-footer">
	      </div>
	    </div>
	  </div>
	</div>
	<!-- Modal [View 腳本內容] end -->
	
	<!-- setup details pane template -->
	<div id="details-pane" style="display: none;">
		<h3 class="title"></h3>
	  	<div class="content"></div>
	</div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.main.min.js"></script>
	
</body>
</html>