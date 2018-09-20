<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<!DOCTYPE html>
<section>

  <div class="container-fluid">
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div class="row search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
		<div class="container-fluid">
      	  <div class="form-group row">
			<div class="col-lg-2" style="padding-top: 5px;">
    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearchErrorLog_web">SYS_ERROR_LOG</button>
    	    </div>
    	    <div class="col-lg-2" style="padding-top: 5px;">
    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearchJobLog_web">SYS_JOB_LOG</button>
    	    </div>
      	  </div>
      	</div>

      </div>
      <!-- [END]查詢欄位bar -->
      <!-- [START]操作按鈕bar -->
      <div class="col-12 action-btn-bar">
        <div class="container-fluid">
        	<div class="row">
        	</div>
        </div>
      </div>
      <!-- [END]操作按鈕bar -->
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
			  <div class="form-group row">
	    	    <div class="col-sm-12">
			      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearchErrorLog_mobile">SYS_ERROR_LOG</button>
			    </div>
			  </div>
			  <div class="form-group row">
	    	    <div class="col-sm-12">
			      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearchJobLog_mobile">SYS_JOB_LOG</button>
			    </div>
			  </div>
		  	</div>
		  </div>
	  </div>
	</div>
	<!-- [END]查詢欄位 for 中小型解析度螢幕 -->
	
	<!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div id="divErrorLog" class="col-sm-12 myTableSection" style="display:none;">
		<table id="resutTable_errorLog" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">異常時間</th>
		      <th scope="col" nowrap="nowrap">Logger</th>
		      <th scope="col" nowrap="nowrap">異常等級</th>
		      <th scope="col" nowrap="nowrap">Message</th>
		      <th scope="col" nowrap="nowrap">Exception</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	  
	  <div id="divJobLog" class="col-sm-12 myTableSection" style="display:none;">
		<table id="resutTable_jobLog" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">啟動時間</th>
		      <th scope="col" nowrap="nowrap">JOB群組</th>
		      <th scope="col" nowrap="nowrap">JOB名稱</th>
		      <th scope="col" nowrap="nowrap">執行結果</th>
		      <th scope="col" nowrap="nowrap">異動筆數</th>
		      <th scope="col" nowrap="nowrap">備註</th>
		      <th scope="col" nowrap="nowrap">結束時間</th>
		      <th scope="col" nowrap="nowrap">執行秒數</th>
		      <th scope="col" nowrap="nowrap">排程表示式</th>
		      <th scope="col" nowrap="nowrap">前次啟動時間</th>
		      <th scope="col" nowrap="nowrap">下次啟動時間</th>
		      <th scope="col" nowrap="nowrap">JID</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
	
	
  </div>
  
</section>

<!-- Modal [details] start -->
<div class="modal fade" id="detailsModal" tabindex="-1" role="dialog" aria-labelledby="detailsModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
      
        <h5 class="modal-title" id="detailsModalLabel"><span id="msgModal_title"><span id="viewDetailTitle"></span></span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      </div>
      <div class="modal-body">
    	<div class="form-group row">
        	<div class="col-12">
          		<div class="form-control form-control-sm" id="viewDetail" style="height: 550px; overflow: auto; white-space: pre; background-color: #1f3d55; color: white"></div>
          	</div>
        </div>        
      </div>
    </div>
  </div>
</div>
<!-- Modal [details] end -->

<script src="${pageContext.request.contextPath}/resources/js/cmap.admin.log.js"></script>