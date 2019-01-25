<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<section>

  <div class="container-fluid">
      <!-- [START]操作按鈕bar -->
      <div class="col-12 action-btn-bar">
        <div class="container-fluid">
        	<div class="row">
        		<div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnDelivery">派送</button>
		  	    </div>
        	</div>
        </div>
      </div>
      <!-- [END]操作按鈕bar -->
    </div>
    <!-- [END]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
    
  	<input type="hidden" id="onlySwitchPort" value="Y" />
  	<div class="container-fluid">
		<!-- 查詢結果TABLE區塊 -->
		<div class="row">
	  	<div class="col-sm-12 myTableSection" style="display:none;">
			<table id="resutTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  	<thead class="center">
		    	<tr>
			      <th scope="col" nowrap="nowrap">操作</th>
			      <th scope="col" nowrap="nowrap">序</th>
			      <th scope="col" nowrap="nowrap">腳本名稱</th>
			      <th scope="col" nowrap="nowrap">類別</th>
			      <th scope="col" nowrap="nowrap">系統版本</th>
			      <th scope="col" nowrap="nowrap">執行腳本內容</th>
			      <th scope="col" nowrap="nowrap">執行腳本備註</th>
			      <th scope="col" nowrap="nowrap">檢核腳本內容</th>
			      <th scope="col" nowrap="nowrap">檢核腳本備註</th>
			    </tr>
			  </thead>
			</table>
	  	</div>
		</div>
  	</div>
  	
  </div>

</section>

<!-- Modal [View] start -->
<div class="modal fade" id="viewScriptModal" tabindex="-1" role="dialog" aria-labelledby="viewScriptModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-mid" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="viewScriptModalLabel"><span id="msgModal_title">腳本內容預覽</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
     	<div class="form-group row">
        	<label for="viewScriptModal_scriptName" class="col-md-2 col-sm-12 col-form-label">腳本名稱 :</label>
    		<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="viewScriptModal_scriptName" readonly>
        </div>
        <div class="form-group row">
        	<label for="viewScriptModal_scriptContent" class="col-md-2 col-sm-12 col-form-label">腳本內容 :</label>
        	<div class="form-control form-control-sm col-md-10 col-sm-12 font script" id="viewScriptModal_scriptContent"></div>
        </div>
      </div>
      <div class="modal-footer">
      </div>
    </div>
  </div>
</div>
<!-- Modal [View] end -->

<!-- Modal [Step] start -->
<div class="modal fade" id="stepModal" tabindex="-1" role="dialog" aria-labelledby="stepModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="stepModalLabel"><span id="msgModal_title">供裝派送</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
      	<div class="row center">
      		<div class="step-img col-3 step step-current">
      			<span>Step 1.</span><br><span>選擇<spring:message code="device.name" /></span>
      		</div>
      		<div class="col-1 step-arrow">
      			<i class="fas fa-angle-double-right step-arrow-img"></i>
      		</div>
      		<div class="step-img col-3 step">
      			<span>Step 2.</span><br><span>輸入變數值</span>
      		</div>
      		<div class="col-1 step-arrow">
      			<i class="fas fa-angle-double-right step-arrow-img"></i>
      		</div>
      		<div class="step-img col-3 step">
      			<span>Step 3.</span><br><span>確認派送</span>
      		</div>
      	</div>
      	<div class="row center">
      		<div class="col-12">
      			<hr>
      		</div>
      	</div>
      	
      	<!-- [START] Step.1 -->
      	<div id="stepModal_scroll">
	      	<div id="step1_section" style="display: inline;">
	      	  	<div class="form-group row">
		        	<label for="stepModal_chooseDevice" class="col-md-4 col-sm-6 col-form-label bold">請選擇<spring:message code="device.name" />(按住Ctrl可複選) :</label>
		        	<label for="stepModal_searchDevice" class="col-md-1 col-sm-1 col-form-label bold right">搜尋</label>
		    		<input type="text" class="form-control form-control-sm col-md-5 col-sm-" id="stepModal_searchDevice">
		    		<img src="${pageContext.request.contextPath}/resources/images/loading.gif" id="stepModal_searchWaiting" alt="loading...">
		        </div>
		        <div class="form-group row">
		        	<select class="multi-select col-md-12 col-sm-12" id="stepModal_chooseDevice" size="10" multiple="multiple">
		    		</select>
		        </div>
		        <div class="form-group row">
		        	<label for="stepModal_remark" class="col-md-12 col-sm-12 col-form-label">
		        		<span id="stepModal_variable_description"></span>
		        		<span id="stepModal_variable_show"></span>
		        	</label>
		        </div>
		        <div class="form-group row">
		        	<label for="stepModal_reason" class="col-md-2 col-sm-12 col-form-label bold">供裝原因 :</label>
		        	<input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="stepModal_reason" name="stepModal_reason">
		        </div>
	      	</div>
	      	<!-- [END] Step.1 -->
	      	
	      	<!-- [START] Step.2 -->
	      	<div id="step2_section" style="display: none;">
	      	  <div class="form-group row">
		      	<label for="stepModal_enterVarRemark" class="col-md-12 col-sm-2 col-form-label bold">請確認派送對象並輸入變數值 :</label>
		      </div>
		      <div id="step2_target_section">
		      	<table id="step2_target_table" class="myTable">
		      	  <thead class="center bold">
		      	  	<tr>
			      	  	<th rowspan="3" width="3%">序</th>
			      	  	<th rowspan="3" width="12%"><spring:message code="group.name" /></th>
			      	  	<th rowspan="3" width="25%"><spring:message code="device.name" /></th>
			      	  	<th colspan="1" width="60%" id="step2_varKey_td">變數值</th>
		      	  	</tr>
		      	  	<tr>
		      	  		<td>interface_id</td>
		      	  	</tr>
		      	  </thead>
		      	  <tbody>
		      	  	<!-- 依據前一步驟勾選的設備動態增長 -->
		      	  </tbody>
		      	</table>
		      </div>
		      
		      <div class="row">
		      	<hr class="col-12">
		      </div>

	      	</div>
	      	<!-- [END] Step.2 -->
	      	
	      	<!-- [START] Step.3 -->
	      	<div id="step3_section" style="display: none;">
	      		<div class="form-group row">
		      		<div class="col-md-12 col-sm-12" id="stepModal_preview">
		      		  <!-- 派送前預覽區 -->
		      		</div>
		     	</div>
	      	</div>
	      	<!-- [END] Step.3 -->
      	</div>
      	
      </div>
      <div class="modal-footer">
      	<div class="col-12 row center">
      		<div class="col-2">
	      		<button type="button" class="btn btn-secondary" id="btnStepGoPrev" style="width: 100%;">上一步</button>
	      	</div>
	      	<div class="col-1"></div>
	      	<div class="col-2">
	      		<button type="submit" class="btn btn-success" id="btnStepGoNext" style="width: 100%;">下一步</button>
	      		<button type="button" class="btn btn-success" id="btnStepGoFire" style="width: 100%;">確認派送</button>
	      	</div>
      	</div>
      </div>
    </div>
  </div>
</div>
<!-- Modal [View] end -->

<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.delivery.main.min.js"></script>
