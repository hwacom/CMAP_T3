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
      	<form>
      		<div class="container-fluid">
	      	  <div class="form-group row">
	    	    <div class="col-lg-3 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 20%">群組</span>
	    	    	<form:select path="queryGroup1" id="queryGroup" style="width: 75%" onchange="changeDeviceMenu('queryDevice', this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${group1List}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-3 group-field-other">
					<span class="font-weight-bold" style="width: 20%">設備</span>
	    	    	<form:select path="queryDevice1" id="queryDevice" style="width: 75%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device1List}" />
                    </form:select>
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
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnBackup">備份</button>
		  	    </div>
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
		  		<form>
		      	  <div class="form-group row">
		      	  	<label for="group_1" class="col-sm-2 col-form-label">群組</label>
				    <div class="col-sm-10">
				      <form:select path="queryGroup1" id="queryGroup_mobile" class="form-control form-control-sm" onchange="changeDeviceMenu('queryDevice_mobile', this.value)">
	                  	<form:option value="" label="=== ALL ===" />
	                    <form:options items="${group1List}" />
	                  </form:select>
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	  	<label for="device_1" class="col-sm-2 col-form-label">設備</label>
		    	    <div class="col-sm-10">
		    	      <form:select path="queryDevice1" id="queryDevice_mobile" class="form-control form-control-sm">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device1List}" />
                      </form:select>
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
		      <th scope="col" nowrap="nowrap">群組名稱</th>
		      <th scope="col" nowrap="nowrap">設備名稱</th>
		      <th scope="col" nowrap="nowrap">系統版本</th>
		      <th scope="col" nowrap="nowrap">最新備份版本號</th>
		      <th scope="col" nowrap="nowrap">備份類型</th>
		      <th scope="col" nowrap="nowrap">最新備份時間</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
	
  </div>

</section>

<!-- Modal [Backup_dialog] start -->
<div class="modal fade" id="backupDialogModal" tabindex="-1" role="dialog" aria-labelledby="backupDialogModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
      
        <h5 class="modal-title" id="backupDialogModalLabel"><span id="msgModal_title">操作確認</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      </div>
      <div class="modal-body">
        <form role="form" id="formEdit" name="formEdit">
            <div class="box-body">
            	<div class="form-group">
                  <label for="queryConfigType" class="control-label">請選擇要備份的組態檔類型:<span class="pull-right" style="color: red;">＊ </span></label>
                  <form:select path="queryConfigType" id="queryConfigType">
                      <form:option value="" label="兩者都要" />
                      <form:options items="${configTypeList}" />
                  </form:select>
                </div>                              
            </div>
        </form>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn btn-default" id="btnClose" data-dismiss="modal">關閉</button>
      	<button type="button" class="btn btn-success" id="btnConfirm">確認</button>
	  </div>
    </div>
  </div>
</div>
<!-- Modal [Backup_dialog] end -->

<script src="${pageContext.request.contextPath}/resources/js/cmap.version.backup.js"></script>