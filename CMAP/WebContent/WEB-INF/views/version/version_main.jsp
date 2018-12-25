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
	      	  	<div class="col-lg-1 group-field-other center">
	      	  		<span class="font-weight-bold group-title">第1組</span>
	      	  	</div>
	    	    <div class="col-lg-2 group-field-left">
	    	    	<span class="font-weight-bold" style="width: 25%"><spring:message code="group.name" /></span>
	    	    	<form:select path="queryGroup1" id="queryGroup1" style="width: 70%" onchange="changeDeviceMenu('queryDevice1', this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${group1List}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-2 group-field-middle">
					<span class="font-weight-bold" style="width: 25%"><spring:message code="device.name" /></span>
					<form:select path="queryDevice1" id="queryDevice1" style="width: 70%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device1List}" />
                    </form:select>
				</div>
	    	    <div class="col-lg-4 group-field-right">
	    	    	<span class="font-weight-bold" style="width: 20%">備份日期</span>
	    	    	<input type="date" id="queryExcuteDateBegin1" style="width: 35%" placeholder="yyyy-mm-dd">
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryExcuteDateEnd1" style="width: 35%" placeholder="yyyy-mm-dd">
	    	    </div>
	    	    <div class="col-lg-2 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 25%">分類</span>
					<form:select path="queryConfigType" id="queryConfigType" style="width: 70%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${configTypeList}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-1" style="padding-top: 5px;">
	    	    	<input type="checkbox" id="queryNewChkbox" value="" style="vertical-align:middle;" checked="checked">&nbsp;
	    	    	<label for="queryNewChkbox" class="font-weight-bold">最新版</label>
	    	    </div>
	      	  </div>
	      	  
	      	  <div class="form-group row">
	      	  	<div class="col-lg-1 group-field-other center">
	      	  		<span class="font-weight-bold group-title">第2組</span>
	      	  	</div>
	    	    <div class="col-lg-2 group-field-left">
	    	    	<span class="font-weight-bold" style="width: 25%"><spring:message code="group.name" /></span>
	    	    	<form:select path="queryGroup2" id="queryGroup2" style="width: 70%" onchange="changeDeviceMenu('queryDevice2', this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${group2List}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-2 group-field-middle">
					<span class="font-weight-bold" style="width: 25%"><spring:message code="device.name" /></span>
					<form:select path="queryDevice2" id="queryDevice2" style="width: 70%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device2List}" />
                    </form:select>
				</div>
	    	    <div class="col-lg-4 group-field-right">
	    	    	<span class="font-weight-bold" style="width: 20%">備份日期</span>
	    	    	<input type="date" id="queryExcuteDateBegin2" style="width: 35%" placeholder="yyyy-mm-dd">
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryExcuteDateEnd2" style="width: 35%" placeholder="yyyy-mm-dd">
	    	    </div>
	    	    <div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web">查詢</button>
	    	    </div>
	      	  </div>
	      	</div>
		</form>
      </div>
      <!-- [END]查詢欄位bar -->
      <!-- [START]操作按鈕bar -->
      <div class="col-12 action-btn-bar">
        <div class="container-fluid">
        	<div class="row">
        		<div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnCompare">比對</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnDelete">刪除</button>
		  	    </div>
        	</div>
        </div>
        <!-- [END]操作按鈕bar -->
      </div>
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
		      	  	<div class="col-sm-12">
		      	  		<span class="font-weight-bold group-title">第1組</span>
		      	  	</div>
		      	  </div>
		      	  <div class="form-group row">
		      	  	<label for="group_1" class="col-sm-2 col-form-label"><spring:message code="group.name" /></label>
				    <div class="col-sm-10">
				      <form:select path="queryGroup1" id="queryGroup1_mobile" class="form-control form-control-sm" onchange="changeDeviceMenu('queryDevice1_mobile', this.value)">
	                  	<form:option value="" label="=== ALL ===" />
	                    <form:options items="${group1List}" />
	                  </form:select>
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	  	<label for="device_1" class="col-sm-2 col-form-label"><spring:message code="device.name" /></label>
		    	    <div class="col-sm-10">
		    	      <form:select path="queryDevice1" id="queryDevice1_mobile" class="form-control form-control-sm">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device1List}" />
                      </form:select>
				    </div>
				  </div>
				  <div class="form-group row">
				  	<label for="bkdate_begin_1" class="col-sm-2 col-form-label">備份日期</label>
				  	<div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateBegin1_mobile">
				    </div>
				    <div class="col-sm-1">~</div>
				    <div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateEnd1_mobile">
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		      	  	<div class="col-sm-12">
		      	  		<span class="font-weight-bold group-title">第2組</span>
		      	  	</div>
		      	  </div>
		      	  <div class="form-group row">
		      	  	<label for="group_2" class="col-sm-2 col-form-label"><spring:message code="group.name" /></label>
				    <div class="col-sm-10">
				      <form:select path="queryGroup2" id="queryGroup2_mobile" class="form-control form-control-sm" onchange="changeDeviceMenu('queryDevice2_mobile', this.value)">
	                  	<form:option value="" label="=== ALL ===" />
	                    <form:options items="${group2List}" />
	                  </form:select>
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	  	<label for="device_2" class="col-sm-2 col-form-label"><spring:message code="device.name" /></label>
		    	    <div class="col-sm-10">
		    	      <form:select path="queryDevice2" id="queryDevice2_mobile" class="form-control form-control-sm">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device2List}" />
                      </form:select>
				    </div>
				  </div>
				  <div class="form-group row">
				  	<label for="bkdate_begin_2" class="col-sm-2 col-form-label">備份日期</label>
				  	<div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateBegin2_mobile">
				    </div>
				    <div class="col-sm-1">~</div>
				    <div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateEnd2_mobile">
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	    <label for="device_2" class="col-sm-2 col-form-label">分類</label>
		    	    <div class="col-sm-10">
		    	      <form:select path="queryConfigType" id="queryConfigType_mobile" class="form-control form-control-sm">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${configTypeList}" />
                      </form:select>
				    </div>
				  </div>
				  <div class="form-group row">
					<div class="col-sm-12">
					  <input id="queryNewChkbox_mobile" type="checkbox" value="" style="vertical-align:middle;" checked="checked"> 最新版本
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
		      <th scope="col" nowrap="nowrap">操作&nbsp;<input type="checkbox" id="checkAll" name="checkAll" /></th>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap"><spring:message code="group.name" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="device.name" /></th>
		      <th scope="col" nowrap="nowrap">系統版本</th>
		      <th scope="col" nowrap="nowrap">組態類型</th>
		      <th scope="col" nowrap="nowrap">版本號</th>
		      <th scope="col" nowrap="nowrap">備份時間</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
  </div>
  
</section>

<!-- Modal [Compare] start -->
<div class="modal fade" id="compareModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-xxg" role="document">
    <div class="modal-content">
      <div class="modal-header">
      
        <h5 class="modal-title" id="exampleModalLabel"><span id="msgModal_title">版本比對</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      </div>
      <div class="modal-body">
        <div class="form-group row">
        	<div class="col-1"></div>
        	<label for="viewModal_version" class="col-md-1 col-sm-12 col-form-label">版號 :</label>
    		<input type="text" class="form-control form-control-sm col-md-4 col-sm-12" id="viewModal_versionLeft" readonly>
    		<label for="viewModal_version" class="col-md-1 col-sm-12 col-form-label">版號 :</label>
    		<input type="text" class="form-control form-control-sm col-md-4 col-sm-12" id="viewModal_versionRight" readonly>
        </div>
        <div class="form-group row">
        	<div class="form-control form-control-sm col-1 compare-line script" id="compareModal_contentLineNum"></div>
        	<div class="form-control form-control-sm col-5 compare-content nowrap script" id="compareModal_contentLeft"></div>
        	<div class="form-control form-control-sm col-5 compare-content nowrap script" id="compareModal_contentRight"></div>
        	<div class="col-1">
        		<span data-feather="chevrons-up" class="feather-compare" id="jumpToTop"></span>
        		<span data-feather="chevron-up" class="feather-compare" id="jumpToPre"></span>
        		<span data-feather="chevron-down" class="feather-compare" id="jumpToNext"></span>
        		<span data-feather="chevrons-down" class="feather-compare" id="jumpToBottom"></span>
        	</div>
        	<div class="col-12 center">
        		<span id="compareModal_summary"></span>
        	</div>
        </div>
      </div>
      <div class="modal-footer">
        <!-- <button type="button" class="btn btn-secondary" data-dismiss="modal">關閉</button> -->
      </div>
    </div>
  </div>
</div>
<!-- Modal [Compare] end -->

<script src="${pageContext.request.contextPath}/resources/js/cmap.version.main.js"></script>
	