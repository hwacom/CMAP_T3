<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<section>

  <div id="content" class="container-fluid">
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div id="search-bar-large" class="row search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
      	<form>
      		<div class="container-fluid">
	      	  <div class="form-group row" style="margin-bottom: -.5rem;">
	    	    <div class="col-lg-3 group-field-other">
	    	    	<label for="queryIpAddr" class="font-weight-bold" style="width: 35%">IP Address</label>
	    	    	<input type="text" id="queryIpAddr" style="width: 60%">
	    	    </div>
	    	    <div class="col-lg-3 group-field-other">
	    	    	<label for="queryGroup" class="font-weight-bold" style="width: 35%"><spring:message code="group.name" /></label>
	    	    	<form:select path="queryGroup" id="queryGroup" style="width: 60%">
                        <c:if test="${fn:length(groupList) gt 1}">
                        	<form:option value="" label="=== ALL ===" />
                        </c:if>
                        <form:options items="${groupList}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-3 group-field-other">
	    	    	<label for="queryDateBegin" class="font-weight-bold" style="width: 35%"><spring:message code="date" /></label>
	    	    	<input type="date" id="queryDateBegin" style="width: 60%">
	    	    	<!-- 
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryDateEnd" style="width: 35%">
	    	    	 -->
	    	    </div>
	    	    <div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web">
	    	    		<spring:message code="btn.query" />
	    	    	</button>
	    	    </div>
	    	  </div>
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
		      	  <div class="form-group row">
		      	  	<label for="queryGroup_mobile" class="col-sm-2 col-form-label"><spring:message code="group.name" /></label>
		      	  	<form:select path="queryGroup" id="queryGroup_mobile" class="col-sm-10 form-control form-control-sm">
                        <c:if test="${fn:length(groupList) gt 1}">
                        	<form:option value="" label="=== ALL ===" />
                        </c:if>
                        <form:options items="${groupList}" />
                    </form:select>
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
		<table id="resutTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap"><spring:message code="seq" /></th>
		      <th scope="col" nowrap="nowrap">異動描述<!-- <spring:message code="device.name" />--></th>
		      <th scope="col" nowrap="nowrap">異動時間<!-- <spring:message code="device.name" />--></th>
		      <th scope="col" nowrap="nowrap">IP Address<!-- <spring:message code="device.name" />--></th>
		      <th scope="col" nowrap="nowrap">Mac Address<!-- <spring:message code="device.name" />--></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="group.name" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="device.name" /></th>
		      <th scope="col" nowrap="nowrap">設備IP<!-- <spring:message code="device.name" />--></th>
		      <th scope="col" nowrap="nowrap">Ports<!-- <spring:message code="device.name" />--></th>
		      <th scope="col" nowrap="nowrap">Vlan<!-- <spring:message code="device.name" />--></th>
		    </tr>
		  </thead>
		  <tbody>
			<tr><td class="center">1</td><td class="left">D. Mac+Port異動</td><td class="left">2019/02/21 14:55</td><td class="center">120.104.236.101</td><td class="center">1c87.2cca.b334</td><td class="left">031. 田美國小</td><td class="left">TMES-C2960L-SW4.mlc.edu.tw</td><td class="center">120.104.236.88</td><td class="center">Te0/4</td><td class="center">Vlan2</td></tr>
		  </tbody>
		</table>
	  </div>
	</div>
  </div>
  
</section>

<script src="${pageContext.request.contextPath}/resources/js/custom/min/plugin/module/cmap.module.ip.record.min.js"></script>
