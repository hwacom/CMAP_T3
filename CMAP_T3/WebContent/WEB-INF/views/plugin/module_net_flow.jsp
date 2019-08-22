<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<section>
  <input type="hidden" id="pageLength" name="pageLength" value="${pageLength }" />
  
  <div id="content" class="container-fluid">
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div id="search-bar-large" class="row search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
      	<form>
      		<div class="container-fluid">
	      	  <div class="form-group row" style="margin-bottom: -.5rem;">
	    	    <div class="col-lg-3 group-field-other">
	    	    	<label for="queryGroup" class="font-weight-bold must" style="width: 20%"><spring:message code="group.name" /></label>
	    	    	<form:select path="queryGroup" id="queryGroup" style="width: 75%">
                        <c:if test="${fn:length(groupList) gt 1}">
                        	<form:option value="" label="=== ALL ===" />
                        </c:if>
                        <form:options items="${groupList}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-2 group-field-other">
	    	    	<label for="queryDateBegin" class="font-weight-bold must" style="width: 20%"><spring:message code="date" /></label>
	    	    	<input type="date" id="queryDateBegin" style="width: 75%">
	    	    	<!-- 
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryDateEnd" style="width: 35%">
	    	    	 -->
	    	    </div>
	    	    <div class="col-lg-3 group-field-other">
	    	    	<label for="queryTimeBegin" class="font-weight-bold must" style="width: 14%"><spring:message code="time" /></label>
	    	    	<input type="time" id="queryTimeBegin" style="width: 38%">
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="time" id="queryTimeEnd" style="width: 38%">
	    	    </div>
	    	    <div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web">
	    	    		<spring:message code="btn.query" />
	    	    	</button>
	    	    </div>
	    	    <div class="col-lg-2 group-field-other">
	    	    	<input type="text" id="timeoutMsg" disabled="disabled" style="width: 100%">
	    	    </div>
	    	  </div>
	    	  <div class="form-group row" style="margin-bottom: -.2rem;">
	    	    <div class="col-lg-3 group-field-other">
					<label for="query_SourceIp" class="font-weight-bold" style="width: 35%"><spring:message code="net.flow.source.ip" /></label>
					<input type="text" id="query_SourceIp" class="input-ip" style="width: 60%">
				</div>
				<div class="col-lg-3 group-field-other">
					<label for="query_DestinationIp" class="font-weight-bold" style="width: 45%"><spring:message code="net.flow.destination.ip" /></label>
					<input type="text" id="query_DestinationIp" class="input-ip" style="width: 50%">
				</div>
				<div class="col-lg-3 group-field-other">
					<label for="query_SenderIp" class="font-weight-bold" style="width: 35%"><spring:message code="net.flow.sender.ip" /></label>
					<input type="text" id="query_SenderIp" class="input-ip" style="width: 60%">
				</div>
			  </div>
			  <div class="form-group row" style="margin-bottom: -.2rem;">
			  	<div class="col-lg-3 group-field-other">
					<label for="query_SourcePort" class="font-weight-bold" style="width: 35%"><spring:message code="net.flow.source.port" /></label>
					<input type="text" id="query_SourcePort" class="input-port" style="width: 60%">
				</div>
				<div class="col-lg-3 group-field-other">
					<label for="query_DestinationPort" class="font-weight-bold" style="width: 45%"><spring:message code="net.flow.destination.port" /></label>
					<input type="text" id="query_DestinationPort" class="input-port" style="width: 50%">
				</div>
				<div class="col-lg-3 group-field-other" id="div_TotalFlow" style="display: none">
					<span id="result_TotalFlow" class="warning bold"></span>
					<img src="${pageContext.request.contextPath}/resources/images/Processing_4.gif" id="searchWaiting" class="img_searchWaiting" alt="loading..." style="display: none">
				</div>
			  </div>
			  <!-- 
	    	  <div class="form-group row" style="margin-bottom: -.2rem;">
	    	    <div class="col-lg-3 group-field-other">
					<label for="queryIp" class="font-weight-bold must" style="width: 25%">IP</label>
					<input type="text" id="queryIp" class="input-ip" style="width: 70%" placeholder="(後模糊<spring:message code="inquiry" />)">
				</div>
				<div class="col-lg-3 group-field-other">
					<label for="queryPort" class="font-weight-bold must" style="width: 25%">Port</label>
					<input type="text" id="queryPort" class="input-port" style="width: 70%" placeholder="(完全比對)">
				</div>
				<div class="col-lg-3 group-field-other">
					<label for="queryMac" class="font-weight-bold must" style="width: 25%">MAC</label>
					<input type="text" id="queryMac" class="input-mac" style="width: 70%" placeholder="(後模糊<spring:message code="inquiry" />)">
				</div>
	    	    <div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web">
	    	    		<spring:message code="btn.query" />
	    	    	</button>
	    	    </div>
	      	  </div>
	      	   -->
	      	</div>
		</form>
      </div>
      <!-- [END]查詢欄位bar -->
    </div>
    <!-- [END]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
    
    <!-- 查詢欄位 for 中小型解析度螢幕 -->
    <div id="search-bar-small-btn" class="row search-bar-small-btn">
  	  <button id="mobileMenuBtn" class="btn btn-success col-sm-12" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
	     	<spring:message code="query.condition" /> ▼
	  </button>
	</div>
	<div class="row search-bar-small">
	  <div class="col-sm-12 collapse" id="collapseExample" style="padding-top: 10px">
		  <div class="card card-body">
		  	<div class="col-12">
		  		<form>
		  		  <!-- 
		      	  <div class="form-group row">
		      	  	<label for="queryGroup_mobile" class="col-sm-2 col-form-label"><spring:message code="group.name" /></label>
		      	  	<form:select path="queryGroup" id="queryGroup_mobile" class="col-sm-10 form-control form-control-sm">
                        <c:if test="${fn:length(groupList) gt 1}">
                        	<form:option value="" label="=== ALL ===" />
                        </c:if>
                        <form:options items="${groupList}" />
                    </form:select>
		    	  </div>
		    	   -->
		    	  <div class="form-group row">
		    	  	<label for="query_SourceIp_mobile" class="col-sm-2 col-form-label"><spring:message code="net.flow.source.ip" /></label>
		    	  	<input type="text" class="col-sm-10 form-control form-control-sm" id="query_SourceIp_mobile">
				  </div>
				  <div class="form-group row">
		    	  	<label for="query_SourcePort_mobile" class="col-sm-2 col-form-label"><spring:message code="net.flow.source.port" /></label>
		    	  	<input type="text" class="col-sm-10 form-control form-control-sm input-port" id="query_SourcePort_mobile">
				  </div>
				  <div class="form-group row">
		    	  	<label for="query_DestinationIp_mobile" class="col-sm-2 col-form-label"><spring:message code="net.flow.destination.ip" /></label>
		    	  	<input type="text" class="col-sm-10 form-control form-control-sm" id="query_DestinationIp_mobile">
				  </div>
				  <div class="form-group row">
		    	  	<label for="query_DestinationPort_mobile" class="col-sm-2 col-form-label"><spring:message code="net.flow.destination.port" /></label>
		    	  	<input type="text" class="col-sm-10 form-control form-control-sm input-port" id="query_DestinationPort_mobile">
				  </div>
				  <div class="form-group row">
		    	  	<label for="query_SenderIp_mobile" class="col-sm-2 col-form-label"><spring:message code="net.flow.sender.ip" /></label>
		    	  	<input type="text" class="col-sm-10 form-control form-control-sm" id="query_SenderIp_mobile">
				  </div>
				  <div class="form-group row">
		    	  	<label for="queryMac_mobile" class="col-sm-2 col-form-label"><spring:message code="net.flow.mac" /></label>
		    	  	<input type="text" class="col-sm-10 form-control form-control-sm" id="queryMac_mobile">
				  </div>
				  <div class="form-group row">
				  	<label for="queryDateBegin_mobile" class="col-sm-2 col-form-label"><spring:message code="execute.date" /></label>
				  	<div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryDateBegin_mobile">
				    </div>
				    <div class="col-sm-1">~</div>
				    <div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryDateEnd_mobile">
				    </div>
		    	  </div>
				  <div class="form-group row">
		    	    <div class="col-sm-12">
				      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_mobile"><spring:message code="inquiry" /></button>
				    </div>
				  </div>
				</form>
		  	</div>
		  </div>
	  </div>
	</div>
	
	<!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div class="col-sm-12 myTableSection" style="display:none;">
		<table id="resultTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap"><spring:message code="seq" /></th>
		      <!-- <th scope="col" nowrap="nowrap"><spring:message code="group.name" /></th> -->
		      <c:forEach var="fName" items="${TABLE_FIELD}">
		        <th scope="col" nowrap="nowrap">${fName }</th>
		      </c:forEach>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
  </div>
  
</section>

<!-- Modal [View IP_Address 來源/目的 port] start -->
<div class="modal fade" id="viewIpMappingPortModal" tabindex="-1" role="dialog" aria-labelledby="viewIpMappingPortLabel" aria-hidden="true">
  <div class="modal-dialog modal-mid" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="viewIpMappingPortLabel"><span id="msgModal_title">IP_Address 來源/目的 port</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
     	<div class="form-group row">
        	<label for="viewIpMappingPortModal_groupName" class="col-md-2 col-sm-12 col-form-label"><spring:message code="group.name" /> :</label>
    		<div class="form-control form-control-sm col-md-10 col-sm-12" id="viewIpMappingPortModal_groupName"></div>
        </div>
        <div class="form-group row">
        	<label for="viewIpMappingPortModal_deviceName" class="col-md-2 col-sm-12 col-form-label"><spring:message code="device.name" /> :</label>
        	<div class="form-control form-control-sm col-md-10 col-sm-12" id="viewIpMappingPortModal_deviceName"></div>
        </div>
        <div class="form-group row">
        	<label for="viewIpMappingPortModal_deviceModel" class="col-md-2 col-sm-12 col-form-label"><spring:message code="device.model" /> :</label>
    		<div class="form-control form-control-sm col-md-10 col-sm-12" id="viewIpMappingPortModal_deviceModel"></div>
        </div>
        <div class="form-group row">
        	<label for="viewIpMappingPortModal_ipAddress" class="col-md-2 col-sm-12 col-form-label"><spring:message code="ip.address" /> :</label>
    		<div class="form-control form-control-sm col-md-10 col-sm-12" id="viewIpMappingPortModal_ipAddress"></div>
        </div>
        <div class="form-group row">
        	<label for="viewIpMappingPortModal_portName" class="col-md-2 col-sm-12 col-form-label"><spring:message code="port.name" /> :</label>
    		<div class="form-control form-control-sm col-md-10 col-sm-12" id="viewIpMappingPortModal_portName"></div>
        </div>
        <div class="form-group row">
        	<label for="viewIpMappingPortModal_showMsg" class="col-md-2 col-sm-12 col-form-label"><spring:message code="message" /> :</label>
    		<div class="form-control form-control-sm col-md-10 col-sm-12" id="viewIpMappingPortModal_showMsg"></div>
        </div>
        <div class="form-group row">
        	<label for="viewIpMappingPortModal_country" class="col-md-2 col-sm-12 col-form-label"><spring:message code="country" /> :</label>
    		<div class="form-control form-control-sm col-md-10 col-sm-12" id="viewIpMappingPortModal_country" style="background-color: #dbeded;"></div>
        </div>
      </div>
      <div class="modal-footer">
      </div>
    </div>
  </div>
</div>
<!-- Modal [View IP_Address 來源/目的 port] end -->

<c:set var="val"><spring:message code="group.name"/></c:set>

<script>
	var msg_chooseGroup = '<spring:message code="please.choose" /><spring:message code="group.name" />';
	var msg_chooseDate = '<spring:message code="please.choose" /><spring:message code="date" />';
</script>
<script src="${pageContext.request.contextPath}/resources/js/custom/min/plugin/module/cmap.module.net.flow.min.js"></script>
