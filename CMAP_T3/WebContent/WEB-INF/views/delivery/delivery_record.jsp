<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<section>

  <div id="content" class="main">
  	<p class="content-title"><spring:message code="func.provision.record" /></p>
  	
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div id="search-bar-large" class="section search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
      	<form>
      		<div class="container-fluid">
	      	  <div class="form-group row">
	    	    <div class="col-3 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 25%"><spring:message code="group.name" /></span>
	    	    	<form:select path="group" id="queryGroup" style="width: 70%" onchange="changeDeviceMenu('queryDevice', this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${groupList}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-3 group-field-other">
					<span class="font-weight-bold" style="width: 25%"><spring:message code="device.name" /></span>
					<form:select path="device" id="queryDevice" style="width: 70%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${deviceList}" />
                    </form:select>
				</div>
	    	    <div class="col-4 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 20%"><spring:message code="execute.date" /></span>
	    	    	<input type="date" id="queryExcuteDateBegin" style="width: 35%" placeholder="yyyy-mm-dd">
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryExcuteDateEnd" style="width: 35%" placeholder="yyyy-mm-dd">
	    	    </div>
	    	    <div class="col-1" style="padding-left: 15px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web"><spring:message code="inquiry" /></button>
	    	    </div>
	      	  </div>
	      	</div>
		</form>
      </div>
      <!-- [END]查詢欄位bar -->
    </div>
    <!-- [END]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
    
    <!-- 查詢欄位 for 中小型解析度螢幕 -->
    <!-- 
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
		      	  	<label for="group_1" class="col-sm-2 col-form-label"><spring:message code="group.name" /></label>
				    <div class="col-sm-10">
				      <form:select path="group" id="queryGroup_mobile" class="form-control form-control-sm" onchange="changeDeviceMenu('queryDevice_mobile', this.value)">
	                  	<form:option value="" label="=== ALL ===" />
	                    <form:options items="${groupList}" />
	                  </form:select>
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	  	<label for="device_1" class="col-sm-2 col-form-label"><spring:message code="device.name" /></label>
		    	    <div class="col-sm-10">
		    	      <form:select path="device" id="queryDevice_mobile" class="form-control form-control-sm">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${deviceList}" />
                      </form:select>
				    </div>
				  </div>
				  <div class="form-group row">
				  	<label for="bkdate_begin_1" class="col-sm-2 col-form-label"><spring:message code="execute.date" /></label>
				  	<div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateBegin_mobile">
				    </div>
				    <div class="col-sm-1">~</div>
				    <div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateEnd_mobile">
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
	 -->
	
	<!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div class="col-12 myTableSection" style="display:none;">
		<table id="resultTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap"><spring:message code="seq" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="execute.time" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="user" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="group.name" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="device.name" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="system.version" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="execute.script" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="provision.reason" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="execute.result" /></th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
  </div>
  
</section>

<!-- Modal [Provision Log] start -->
<div class="modal fade" id="viewProvisionLogModal" tabindex="-1" role="dialog" aria-labelledby="viewProvisionLogModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="viewProvisionLogModalLabel"><span id="msgModal_title"><spring:message code="provision.execute.result.detail" /></span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
     	<div class="form-group row">
        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="execute.time" /> :</span></div>
    		<div class="col-11"><span class="yellow" id="viewModal_beginTime"></span></div>
        </div>
        <div class="form-group row">
        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="user" /> :</span></div>
    		<div class="col-11"><span class="yellow" id="viewModal_userName"></span></div>
        </div>
        <div class="form-group row">
        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="group.name" /> :</span></div>
    		<div class="col-11"><span class="yellow" id="viewModal_groupName"></span></div>
        </div>
        <div class="form-group row">
        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="device.name" /> :</span></div>
    		<div class="col-11"><span class="yellow" id="viewModal_deviceName"></span></div>
        </div>
        <div class="form-group row">
        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="system.version" /> :</span></div>
    		<div class="col-11"><span class="yellow" id="viewModal_systemVersion"></span></div>
        </div>
        <div class="form-group row">
        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="script.name" /> :</span></div>
    		<div class="col-11"><span class="yellow" id="viewModal_scriptName"></span></div>
        </div>
        <div class="form-group row">
        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="provision.reason" /> :</span></div>
    		<div class="col-11"><span class="yellow" id="viewModal_reason"></span></div>
        </div>
        <div class="form-group row">
        	<div class="col-1 bold"><span class="log-item-title"><spring:message code="execute.result" /> :</span></div>
    		<div class="col-11"><span class="yellow" id="viewModal_result"></span></div>
        </div>
        <div>
        	<hr>
        </div>
        <div class="form-group row">
        	<div class="form-group textarea-section textarea" id="viewModal_provisionLog" style="height: 38vh;"></div>
        </div>
      </div>
      <div class="modal-footer">
      </div>
    </div>
  </div>
</div>
<!-- Modal [Provision Log] end -->
	
<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.delivery.record.min.js"></script>
