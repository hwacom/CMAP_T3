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
	      	  <div class="form-group row">
	    	    <div class="col-lg-3 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 25%"><spring:message code="group.name" /></span>
	    	    	<!-- 
					// TODO
                    -->
	    	    </div>
	    	    <div class="col-lg-3 group-field-other">
					<span class="font-weight-bold" style="width: 25%"><spring:message code="device.name" /></span>
					<!-- 
					// TODO
                    -->
				</div>
	    	    <div class="col-lg-4 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 20%">執行日期</span>
	    	    	<input type="date" id="queryExcuteDateBegin" style="width: 35%">
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryExcuteDateEnd" style="width: 35%">
	    	    </div>
	    	    <div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web">查詢</button>
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
	     	查詢條件 ▼
	  </button>
	</div>
	<div class="row search-bar-small">
	  <div class="col-sm-12 collapse" id="collapseExample" style="padding-top: 10px">
		  <div class="card card-body">
		  	<div class="col-12">
		  		<form>
		      	  <div class="form-group row">
		      	  	<label for="group_1" class="col-sm-2 col-form-label"><spring:message code="group.name" /></label>
		      	  	<!-- 
					// TODO
                    -->
		    	  </div>
		    	  <div class="form-group row">
		    	  	<label for="device_1" class="col-sm-2 col-form-label"><spring:message code="device.name" /></label>
		    	  	<!-- 
					// TODO
                    -->
				  </div>
				  <div class="form-group row">
				  	<label for="bkdate_begin_1" class="col-sm-2 col-form-label">執行日期</label>
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
				      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_mobile">查詢</button>
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
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">執行時間</th>
		      <th scope="col" nowrap="nowrap">使用者</th>
		      <th scope="col" nowrap="nowrap"><spring:message code="group.name" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="device.name" /></th>
		      <th scope="col" nowrap="nowrap">系統版本</th>
		      <th scope="col" nowrap="nowrap">執行腳本</th>
		      <th scope="col" nowrap="nowrap">供裝原因</th>
		      <th scope="col" nowrap="nowrap">執行結果</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
  </div>
  
</section>

<!-- Modal [Compare] start -->
<div class="modal fade" id="viewProvisionLogModal" tabindex="-1" role="dialog" aria-labelledby="viewProvisionLogModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-mid" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="viewProvisionLogModalLabel"><span id="msgModal_title">供裝紀錄執行結果明細</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
     	<div class="form-group row">
        	<label for="viewModal_beginTime" class="col-md-2 col-sm-12 col-form-label">執行時間 :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_beginTime" readonly>
        </div>
        <div class="form-group row">
        	<label for="viewModal_userName" class="col-md-2 col-sm-12 col-form-label">使用者 :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_userName" readonly>
        </div>
        <div class="form-group row">
        	<label for="viewModal_groupName" class="col-md-2 col-sm-12 col-form-label"><spring:message code="group.name" /> :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_groupName" readonly>
        </div>
        <div class="form-group row">
        	<label for="viewModal_deviceName" class="col-md-2 col-sm-12 col-form-label"><spring:message code="device.name" /> :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_deviceName" readonly>
        </div>
        <div class="form-group row">
        	<label for="viewModal_systemVersion" class="col-md-2 col-sm-12 col-form-label">系統版本 :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_systemVersion" readonly>
        </div>
        <div class="form-group row">
        	<label for="viewModal_scriptName" class="col-md-2 col-sm-12 col-form-label">腳本名稱 :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_scriptName" readonly>
        </div>
        <div class="form-group row">
        	<label for="viewModal_reason" class="col-md-2 col-sm-12 col-form-label">供裝原因 :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_reason" readonly>
        </div>
        <div class="form-group row">
        	<label for="viewModal_result" class="col-md-2 col-sm-12 col-form-label">執行結果 :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewModal_result" readonly>
        </div>
        <div>
        	<hr>
        </div>
        <div class="form-group row">
        	<div class="form-control form-control-sm col-12 script" id="viewModal_provisionLog"></div>
        </div>
      </div>
      <div class="modal-footer">
      </div>
    </div>
  </div>
</div>
<!-- Modal [Compare] end -->
	
<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.module.wifi.poller.min.js"></script>
