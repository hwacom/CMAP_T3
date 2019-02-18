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
    <link href="${pageContext.request.contextPath}/resources/css/jquery-scrollbar/jquery.scrollbar.css" rel="stylesheet">
	
	<!-- Core Javascript -->
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/dist/jquery.validate.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/dist/additional-methods.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/dist/localization/messages_zh_TW.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/popper/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/jquery-ui/jquery-ui.min.js"></script>
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
	
    <nav class="navbar navbar-dark fixed-top flex-md-nowrap p-0 shadow navbar-bg">
      <a href="${pageContext.request.contextPath}/index">
      	<img class="img" src="${pageContext.request.contextPath}/resources/images/Logo_icon.png" width="auto" height="30" style="padding-top: 3px" />
  		<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/Logo_word.png" width="auto" height="23" style="padding-top: 3px" />
 		<!--
 		<img class="img" src="${pageContext.request.contextPath}/resources/images/aptg_logo_icon.png" width="auto" height="30" style="padding-top: 3px" />
		<img class="img web-only" src="${pageContext.request.contextPath}/resources/images/aptg_logo_word.png" width="auto" height="23" style="padding-top: 3px" />
 		-->
 		<span class="font-weight-bold title-font" style="color:#000079"><spring:message code="cmap.title" /></span>	
      </a>
      <ul class="navbar-nav">
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="${pageContext.request.contextPath}/logout"><span data-feather="log-out"></span></a>
        </li>
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="#" data-container="body" data-toggle="popover" data-placement="bottom" title="<spring:message code="user.account" />" data-content="${userInfo }"><span data-feather="user"></span></a>
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
              <%
              	if (Env.SHOW_MENU_TREE_PRTG.equals(__SHOW__)) {
              %>
              <li class="nav-item">
                <a class="nav-link toggleMenuLink" id="toggleMenu_prtg" href="#">
                  <span data-feather="layout"></span>
                  	<span><spring:message code="menu.monitor" />&nbsp;<span id="toggleMenu_prtg_icon" data-feather="chevron-down"></span></span>
                </a>
                <ul aria-expanded="false" id="toggleMenu_prtg_items" class="collapse">
                    <li class="subMenu-item">
                    	<a id="mp_index" href="#" onclick="closeTabAndGo('${pageContext.request.contextPath}/prtg/index')">
                    	  <span data-feather="home"></span>
                    		<span><spring:message code="func.prtg.index" /></span>
                    	</a>
                    </li>
                    <li class="subMenu-item">
                    	<a id="mp_dashboard" href="#" onclick="closeTabAndGo('${pageContext.request.contextPath}/prtg/dashboard')">
                    	  <span data-feather="grid"></span>
                    	  	<span><spring:message code="func.prtg.dashboard" /></span>
                    	</a>
                    </li>
                   	<li class="subMenu-item">
                    	<a id="mp_netFlowSummary" href="#" onclick="closeTabAndGo('${pageContext.request.contextPath}/prtg/netFlowSummary')">
                    	  <span data-feather="activity"></span>
                    	  	<span><spring:message code="func.prtg.net.flow.statistics" /></span>
                    	</a>
                    </li>
                </ul>
	          </li>
	          <%
              	}
              %>
	          <sec:authorize access="hasAnyRole('ROLE_ADMIN', 'ROLE_USER')">
	          	<li class="nav-item">
	                <a class="nav-link toggleMenuLink" id="toggleMenu_cm" href="#">
	                  <span data-feather="file-text"></span>
	                  	<span><spring:message code="menu.cm.manage" />&nbsp;<span id="toggleMenu_cm_icon" data-feather="chevron-down"></span></span>
	                </a>
	                <ul aria-expanded="false" id="toggleMenu_cm_items" class="collapse">
	                    <li class="subMenu-item">
	                    	<a id="cm_manage" href="${pageContext.request.contextPath}/version/manage">
	                    	  <span data-feather="file-text"></span>
	                    		<span><spring:message code="func.version.manage" /></span>
	                    	</a>
	                    </li> 
	                    <li class="subMenu-item">
			                <a id="cm_backup" href="${pageContext.request.contextPath}/version/backup">
			                  <span data-feather="download"></span>
			                  	<span><spring:message code="func.version.backup" /></span>
			                </a>
			            </li>
			            <li class="subMenu-item">
			                <a id="cm_restore" href="${pageContext.request.contextPath}/version/restore">
			                  <span data-feather="upload"></span>
			                  	<span><spring:message code="func.version.restore" /></span>
			                </a>
			            </li>
			            <%
			            if (Env.SHOW_MENU_ITEM_CM_SCRIPT.equals(__SHOW__)) {
			            %>
			            <li class="subMenu-item">
			                <a id="cm_script" href="${pageContext.request.contextPath}/script">
			                  <span data-feather="code"></span>
			                  	<span><spring:message code="func.script.manage" /></span>
			                </a>
			            </li>
			            <%
			            	}
			            %>
			            <li class="subMenu-item">
			                <a id="cm_delivery" href="${pageContext.request.contextPath}/delivery">
			                  <span data-feather="send"></span>
			                  	<span><spring:message code="func.provision.delivery" /></span>
			                </a>
			            </li>
			            <li class="subMenu-item">
			                <a id="cm_record" href="${pageContext.request.contextPath}/delivery/record">
			                  <span data-feather="search"></span>
			                  	<span><spring:message code="func.provision.record" /></span>
			                </a>
			            </li>
	                </ul>
	              </li>
	          </sec:authorize>
	          
	          <!-- 異常告警 START -->
              <%
              if (Env.SHOW_MENU_TREE_ABNORMAL_ALARM.equals(__SHOW__)) {
              %>
	              <li class="nav-item">
	                <a class="nav-link toggleMenuLink" id="toggleMenu_abnormalAlarm" href="#">
	                  <span data-feather="alert-triangle"></span>
	                  	<span><spring:message code="menu.abnormal.alarm" />&nbsp;<span id="toggleMenu_abnormalAlarm_icon" data-feather="chevron-down"></span></span>
	                </a>
	                <ul aria-expanded="false" id="toggleMenu_abnormalAlarm_items" class="collapse">
	                	<%
			            	if (Env.SHOW_MENU_ITEM_IP_CONFLICT.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_wifi" href="${pageContext.request.contextPath}/prtg/ipConflict">
	                    	  <span data-feather="minimize-2"></span>
	                    		<span><spring:message code="func.prtg.ip.conflict" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_UNAUTHORIZED_DHCP.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_netflow" href="${pageContext.request.contextPath}/prtg/unauthorizedDHCP">
	                    	  <span data-feather="user-x"></span>
	                    	  	<span><spring:message code="func.prtg.unauthorized.dhcp.device" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_LOOP_LOOP.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_switchPort" href="${pageContext.request.contextPath}/prtg/loopLoop">
	                    	  <span data-feather="refresh-cw"></span>
	                    	  	<span><spring:message code="func.prtg.loop.loop" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_DEVICE_FAILURE.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_switchPort" href="${pageContext.request.contextPath}/prtg/deviceFailure">
	                    	  <span data-feather="help-circle"></span>
	                    	  	<span><spring:message code="func.prtg.device.failure" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_ABNORMAL_TRAFFIC.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_switchPort" href="${pageContext.request.contextPath}/prtg/abnormalTraffic">
	                    	  <span data-feather="trending-up"></span>
	                    	  	<span><spring:message code="func.prtg.abnormal.traffic" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_OTHER_EXCEPTION.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_switchPort" href="${pageContext.request.contextPath}/prtg/otherException">
	                    	  <span data-feather="zap-off"></span>
	                    	  	<span><spring:message code="func.prtg.other.exception" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
	                    %>
	                </ul>
		          </li>
	          <%
              	}
              %>
              <!-- 異常告警 END -->
              
              <%
              if (Env.SHOW_MENU_TREE_PLUGIN.equals(__SHOW__)) {
              %>
	              <li class="nav-item">
	                <a class="nav-link toggleMenuLink" id="toggleMenu_plugin" href="#">
	                  <span data-feather="alert-octagon"></span>
	                  	<span><spring:message code="menu.security" />&nbsp;<span id="toggleMenu_plugin_icon" data-feather="chevron-down"></span></span>
	                </a>
	                <ul aria-expanded="false" id="toggleMenu_plugin_items" class="collapse">
	                	<%
			            	if (Env.SHOW_MENU_ITEM_PLUGIN_WIFI_POLLER.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_wifi" href="${pageContext.request.contextPath}/plugin/module/wifiPoller">
	                    	  <span data-feather="wifi"></span>
	                    		<span><spring:message code="func.plugin.wifi.manage" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_PLUGIN_NET_FLOW.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_netflow" href="${pageContext.request.contextPath}/plugin/module/netFlow">
	                    	  <span data-feather="shuffle"></span>
	                    	  	<span><spring:message code="func.plugin.net.flow" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_PLUGIN_SWITCH_PORT.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_switchPort" href="${pageContext.request.contextPath}/delivery/switchPort">
	                    	  <span data-feather="shield-off"></span>
	                    	  	<span><spring:message code="func.plugin.switch.port" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_IP_OPEN_BLOCK.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_switchPort" href="${pageContext.request.contextPath}/delivery/ipOpenBlock">
	                    	  <span data-feather="check-square"></span>
	                    	  	<span><spring:message code="func.ip.open.block" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
			            	if (Env.SHOW_MENU_ITEM_MAC_OPEN_BLOCK.equals(__SHOW__)) {
			            %>
	                    <li class="subMenu-item">
	                    	<a id="cm_switchPort" href="${pageContext.request.contextPath}/delivery/macOpenBlock">
	                    	  <span data-feather="at-sign"></span>
	                    	  	<span><spring:message code="func.mac.open.block" /></span>
	                    	</a>
	                    </li>
	                    <%
			            	}
	                    %>
	                </ul>
		          </li>
	          <%
              	}
              %>
              <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
              	  <li class="nav-item">
	                <a class="nav-link toggleMenuLink" id="toggleMenu_admin" href="#">
	                  <span data-feather="settings"></span>
	                  	<span><spring:message code="menu.backend" />&nbsp;<span id="toggleMenu_admin_icon" data-feather="chevron-down"></span></span>
	                </a>
	                <ul aria-expanded="false" id="toggleMenu_admin_items" class="collapse">
	                    <li class="subMenu-item">
	                    	<a id="bk_env" href="${pageContext.request.contextPath}/admin/env/main">
	                    	  <span data-feather="command"></span> 
	                    		<span><spring:message code="func.sys.env.manage" /></span>
	                    	</a>
	                    </li>
	                    <li class="subMenu-item">
	                    	<a id="bk_script" href="${pageContext.request.contextPath}/admin/script/main">
	                    	  <span data-feather="hash"></span> 
	                    	  	<span><spring:message code="func.default.script.manage" /></span>
	                    	</a>
	                    </li>
	                    <li class="subMenu-item">
	                    	<a id="bk_job" href="${pageContext.request.contextPath}/admin/job/main">
	                    	  <span data-feather="check-square"></span> 
	                    	  	<span><spring:message code="func.job.manage" /></span>
	                    	</a>
	                    </li>
	                    <li class="subMenu-item">
	                    	<a id="bk_log" href="${pageContext.request.contextPath}/admin/log/main">
	                    	  <span data-feather="alert-triangle"></span> 
	                    	  	<span><spring:message code="func.sys.log.inquiry" /></span>
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
	          	<span style="color: white;padding-top:9px;position: fixed;z-index: 999;background-color: #344e6a;width: 12px;height: 2.75rem;margin-top: -3px"></span>
	          </div>
	          	<span class="p-2"><a href="${pageContext.request.contextPath}/version/manage"><spring:message code="func.version.manage" /></a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/version/backup"><spring:message code="func.version.backup" /></a></span>
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
        
        <!-- Modal [View 組態內容] start -->
		<div class="modal fade" id="viewModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-mid" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLabel"><span id="msgModal_title"><spring:message code="config.content.preview" /></span></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		     	<div class="form-group row">
		        	<label for="viewModal_group" class="col-md-2 col-sm-12 col-form-label"><spring:message code="group.name" /> :</label>
		    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_group" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_device" class="col-md-2 col-sm-12 col-form-label"><spring:message code="device.name" /> :</label>
		    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_device" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_version" class="col-md-2 col-sm-12 col-form-label"><spring:message code="config.version" /> :</label>
		    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_version" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_content" class="col-md-2 col-sm-12 col-form-label"><spring:message code="config.content" /> :</label>
		        	<!-- <textarea class="form-control col-md-9 col-sm-12" id="viewModal_content" rows="10" readonly></textarea> -->
		        	<div class="form-control form-control-sm col-md-10 col-sm-12 script" id="viewModal_content"></div>
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
		  <div class="modal-dialog modal-mid" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="viewScriptModalLabel"><span id="msgModal_title"><spring:message code="script.content.preview" /></span></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		     	<div class="form-group row">
		        	<label for="viewScriptModal_scriptName" class="col-md-2 col-sm-12 col-form-label"><spring:message code="script.name" /> :</label>
		    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewScriptModal_scriptName" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewScriptModal_scriptContent" class="col-md-2 col-sm-12 col-form-label"><spring:message code="script.content" /> :</label>
		        	<div class="form-control form-control-sm col-md-10 col-sm-12 font script" id="viewScriptModal_scriptContent"></div>
		        </div>
		      </div>
		      <div class="modal-footer">
		      </div>
		    </div>
		  </div>
		</div>
		<!-- Modal [View 腳本內容] end -->
        
        <footer role="footer" class="ml-sm-auto col-md-10 footer">
        	<span class="copyright"><spring:message code="contact.us" /> | Copyright &copy; 2018-2019 HwaCom Systems Inc. All Rights Reserved.</span>	
        </footer>
        
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
	<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.main.min.js"></script>
	
</body>
</html>