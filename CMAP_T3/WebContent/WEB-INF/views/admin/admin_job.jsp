<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<section>

  <div id="content" class="main">
  	<p class="content-title"><spring:message code="func.job.manage" /></p>
  	
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div id="search-bar-large" class="section search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
		<div class="container-fluid">
      	  <div class="form-group row">
			<div class="col-1">
    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web"><spring:message code="inquiry" /></button>
    	    </div>
      	  </div>
      	</div>

      </div>
      <!-- [END]查詢欄位bar -->
      <!-- [START]操作按鈕bar -->
      <div class="col-12 action-btn-bar">
        <div class="container-fluid">
        	<div class="row">
        		<div class="col-1 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnAdd"><spring:message code="btn.add" /></button>
		  	    </div>
		  	    <div class="col-1 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnModify"><spring:message code="btn.modify" /></button>
		  	    </div>
		  	    <div class="col-1 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnExcute"><spring:message code="btn.execute.immediately" /></button>
		  	    </div>
		  	    <div class="col-1 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnPause"><spring:message code="btn.pause" /></button>
		  	    </div>
		  	    <div class="col-1 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnResume"><spring:message code="btn.restart" /></button>
		  	    </div>
		  	    <div class="col-1 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnDelete"><spring:message code="btn.delete" /></button>
		  	    </div>
        	</div>
        </div>
      </div>
      <!-- [END]操作按鈕bar -->
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
			  <div class="form-group row">
	    	    <div class="col-sm-12">
			      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_mobile"><spring:message code="inquiry" /></button>
			    </div>
			  </div>
		  	</div>
		  </div>
	  </div>
	</div>
	 -->
	<!-- [END]查詢欄位 for 中小型解析度螢幕 -->
	
	<!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div class="col-12 myTableSection" style="display:none;">
		<table id="resultTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap"><spring:message code="action" />&nbsp;<input type="checkbox" id="checkAll" name="checkAll" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="seq" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="schedule" /><spring:message code="type" /></th>
		      <th scope="col" nowrap="nowrap">Job群組</th>
		      <th scope="col" nowrap="nowrap">Job名稱</th>
		      <th scope="col" nowrap="nowrap">優先度</th>
		      <th scope="col" nowrap="nowrap">狀態</th>
		      <th scope="col" nowrap="nowrap">前次觸發時間</th>
		      <th scope="col" nowrap="nowrap">下次觸發時間</th>
		      <th scope="col" nowrap="nowrap">
		      	<span class="d-inline-block" tabindex="0" data-toggle="tooltip" title="<p style='text-align: left'>1: 錯過啟動時間，立即啟動<br/>2: 錯過啟動時間，不做處理<br/>-1: 忽略</p>">
		      		Miss策略
		      	</span>
		      </th>
		      <th scope="col" nowrap="nowrap">排程時間</th>
		      <th scope="col" nowrap="nowrap">時區</th>
		      <th scope="col" nowrap="nowrap">Job對象</th>
		      <th scope="col" nowrap="nowrap">參數明細</th>
		      <th scope="col" nowrap="nowrap">備註</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
	
	
  </div>
  
</section>

<spring:message code="please.choose" var="i18n_pleaseChoose"/>

<!-- Modal [Add/Modify] start -->
<div class="modal fade" id="addModifyModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
      
        <h5 class="modal-title" id="exampleModalLabel"><span id="msgModal_title">新增/維護排程</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      </div>
      <div class="modal-body">
        <form role="form" id="formEdit" name="formEdit">
        	<div class="card card-body">
        	  <div class="col-12">
            	<div class="form-group row">
                	<label for="inputSchedType" class="col-2 col-form-label"><spring:message code="schedule" /><spring:message code="type" /><span class="pull-right" style="color: red;">＊ </span></label>
                  	<div class="col-10">
                  		<form:select path="inputSchedType" id="inputSchedType" name="inputSchedType" class="form-control form-control-sm">
		                  	<form:option value="" label="=== ${i18n_pleaseChoose }  ===" />
		                  	<form:options items="${inputSchedType}" />
		                </form:select>
                  	</div>
                </div>                              
	           	<div class="form-group row">
	            	<label for="inputJobName" class="col-2 col-form-label">排程名稱<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-10">
	            		<input type="text" class="form-control form-control-sm" id="inputJobName" name="inputJobName" placeholder="排程名稱">
	            	</div>
	            </div>                              
	           	<div class="form-group row">
	            	<label for="inputJobGroup" class="col-2 col-form-label">群組名稱<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-10">
	                	<input type="text" class="form-control form-control-sm" id="inputJobGroup" name="inputJobGroup" placeholder="群組名稱">
	                </div>
	            </div>
	            <div class="form-group row">
	            	<label for="inputDescription" class="col-2 col-form-label">備註說明</label>
	            	<div class="col-10">
	                	<input type="text" class="form-control form-control-sm" id="inputDescription" name="inputDescription" placeholder="備註說明">
	                </div>
	            </div>
	            <div class="form-group row">
	            	<label for="inputCronExpression" class="col-2 col-form-label">週期表示式<span class="pull-right" style="color: red;">＊ </span> <span data-feather="info" class="cron-info" data-type="JOB_CRON" data-title="週期表示式說明"></span></label>
	            	<div class="col-10">
	                	<input type="text" class="form-control form-control-sm" id="inputCronExpression" name="inputCronExpression" placeholder="example: 0 0/30 * * * ?">
	                </div>
	            </div>
	            <div class="form-group row">
	            	<label for="inputPriority" class="col-2 col-form-label">優先度<span class="pull-right" style="color: red;">＊ </span> <span data-feather="info" class="cron-info" data-type="TEXT" data-title="優先度說明" data-text="當多個觸發器在一個相同的時間內觸發，並且調度引擎中的資源有限的情況下，那麼具有較高優先順序的觸發器先觸發。"></span></label>
	            	<div class="col-10">
	                	<form:select path="inputPriority" id="inputPriority" name="inputPriority" class="form-control form-control-sm">
	                		<form:option value="" label="=== ${i18n_pleaseChoose }  ===" />
	                		<form:options items="${inputPriority}" />
	                	</form:select>
	                </div>
	            </div>
	            <div class="form-group row">
	            	<label for="inputMisFirePolicy" class="col-2 col-form-label">排程策略<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-10">
	                	<form:select path="inputMisFirePolicy" id="inputMisFirePolicy" name="inputMisFirePolicy" class="form-control form-control-sm">
		                  	<form:option value="" label="=== ${i18n_pleaseChoose }  ===" />
		                  	<form:options items="${inputMisFirePolicy}" />
		                </form:select>
	                </div>
	            </div>
	            
	            <hr />
	            
	            <!-- 組態檔備份 -->
	            <div id="sec_backupConfig" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputFtpNames" class="col-12 col-form-label"><span style="color:yellow">*** 備份目標設備 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputConfigType" class="col-2 col-form-label">備份範圍<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-10">
		                	<form:select path="inputConfigType" id="inputConfigType" name="inputConfigType" class="form-control form-control-sm">
			                  	<form:option value="" label="=== ALL ===" />
			                  	<form:options items="${inputConfigType}" />
			                </form:select>
		                </div>
		            </div>
		            <div class="form-group row">
		            	<label for="inputDeviceListIds" class="col-2 col-form-label">Group_ID<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-3" style="padding-left: 7px;">
		            		<textarea rows="5" class="form-control form-control-sm" style="height: 120px;" id="inputGroupIds" name="inputGroupIds" placeholder="(1行1筆資料)"></textarea>
		                </div>
		                <label for="inputDeviceListIds" class="col-2 col-form-label">Device_ID<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-3" style="padding-left: 7px;">
		            		<textarea rows="5" class="form-control form-control-sm" style="height: 120px;" id="inputDeviceIds" name="inputDeviceIds" placeholder="(1行1筆資料)"></textarea>
		                </div>
		            </div>
	            </div>
	            <!-- [END]組態檔備份 -->
	            
	            <!-- 組態檔異地備援(FTP) -->
	            <div id="sec_uploadBackupConfigFile2FTP" style="display: none">
	              <div class="form-group row">
	              	<label for="inputFtpNames" class="col-12 col-form-label"><span style="color:yellow">*** FTP相關設定 ***</span></label>
	              </div>
	              <div class="form-group row">
	              	<label for="inputFtpNames" class="col-2 col-form-label">FTP Name:</label>
	            	<div class="col-10">
	            		<input type="text" class="form-control form-control-sm" id="inputFtpName" name="inputFtpName" placeholder="(選填)"></input>
	                </div>
	              </div>
	              <div class="form-group row">
	                <label for="inputFtpHosts" class="col-2 col-form-label">FTP Host:<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-10">
	            		<input type="text" class="form-control form-control-sm" id="inputFtpHost" name="inputFtpHost" placeholder="Host or IP"></input>
	                </div>
	              </div>
	              <div class="form-group row">
	                <label for="inputFtpPorts" class="col-2 col-form-label">FTP Port:</label>
	            	<div class="col-10">
	            		<input type="text" class="form-control form-control-sm" id="inputFtpPort" name="inputFtpPort" placeholder="(選填；預設值=21)"></input>
	                </div>
	              </div>
	              <div class="form-group row">
	                <label for="inputFtpAccounts" class="col-2 col-form-label">Account:<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-10">
	            		<input type="text" class="form-control form-control-sm" id="inputFtpAccount" name="inputFtpAccount" placeholder="(FTP登入帳號)"></input>
	                </div>
	              </div>
	              <div class="form-group row">
	                <label for="inputFtpPasswords" class="col-2 col-form-label">Password:<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-10">
	            		<input type="password" class="form-control form-control-sm" id="inputFtpPassword" name="inputFtpPassword" placeholder="(FTP登入密碼)"></input>
	                </div>
	              </div>
	            </div>
	            <!-- [END]組態檔異地備援(FTP) -->
	            
	            <!-- 系統檢核 -->
	            <div id="sec_sysCheck" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputSysCheckNames" class="col-12 col-form-label"><span style="color:yellow">*** 系統檢核設定 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputSysCheckSql" class="col-2 col-form-label">SQLs<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-10" style="padding-left: 7px;">
		            		<textarea rows="5" class="form-control form-control-sm" style="height: 120px;" id="inputSysCheckSql" name="inputSysCheckSql" placeholder="多道SQL以「;」分隔"></textarea>
		                </div>
		            </div>
	            </div>
	            <!-- [END]系統檢核 -->
	            
	            <!-- Data Poller -->
	            <div id="sec_dataPoller" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputDataPollerNames" class="col-12 col-form-label"><span style="color:yellow">*** Data Poller 參數設定 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputDataPollerSettingId" class="col-2 col-form-label">Setting IDs<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-10">
		            		<input type="text" class="form-control form-control-sm" id="inputDataPollerSettingId" name="inputDataPollerSettingId" placeholder="多組ID以「,」區隔；區間以「ID1~ID2」表示"></input>
		                </div>
		            </div>
	            </div>
	            <!-- [END]Data Poller -->
	            
	            <!-- 本地檔案操作 -->
	            <div id="sec_localFileOperation" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputLocalFileOperationNames" class="col-12 col-form-label"><span style="color:yellow">*** 本地檔案操作 參數設定 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputLocalFileOperationSettingId" class="col-2 col-form-label">Setting IDs<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-10">
		            		<input type="text" class="form-control form-control-sm" id="inputLocalFileOperationSettingId" name="inputLocalFileOperationSettingId" placeholder="多組ID以「,」區隔；區間以「ID1~ID2」表示"></input>
		                </div>
		            </div>
	            </div>
	            <!-- [END]本地檔案操作 -->
	            
	            <!-- MAIL Sender -->
	            <div id="sec_mailSender" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputDataPollerNames" class="col-12 col-form-label"><span style="color:yellow">*** Mail Sender 參數設定 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputMailSenderSettingId" class="col-2 col-form-label">Setting IDs<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-10">
		            		<input type="text" class="form-control form-control-sm" id="inputMailSenderSettingId" name="inputMailSenderSettingId" placeholder="多組ID以「,」區隔；區間以「ID1~ID2」表示"></input>
		                </div>
		            </div>
	            </div>
	            <!-- [END]MAIL Sender -->
	            
	            <!-- IP/MAC/Port Poller -->
	            <div id="sec_ipMacPortMappingPoller" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputDataPollerNames" class="col-12 col-form-label"><span style="color:yellow">*** IP/MAC/Port Poller 參數設定 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputIpMacPortMappingPollerGroupId" class="col-2 col-form-label">Group IDs<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-10">
		            		<input type="text" class="form-control form-control-sm" id="inputIpMacPortMappingPollerGroupId" name="inputIpMacPortMappingPollerGroupId" placeholder="多組ID以「,」區隔；區間以「ID1~ID2」表示"></input>
		                </div>
		            </div>
	            </div>
	            <!-- [END]IP/MAC/Port Poller -->
	            
	            <input type="hidden" id="jobKeyName" name="jobKeyName">
	            <input type="hidden" id="jobKeyGroup" name="jobKeyGroup">
	            
	            <div class="container-fluid">
		      		<div class="row center">
			      		<div class="col-2">
			      			<button type="button" class="btn btn-secondary" id="btnClose" data-dismiss="modal"><spring:message code="btn.cancel" /></button>
			      		</div>
			      		<div class="col-1"></div>
			      		<div class="col-2">
			      			<button type="button" class="btn btn-success" id="btnSave"><spring:message code="btn.save" /></button>
			      		</div>
			      	</div>
		      	</div>
              </div>
			</div>
        </form>
      </div>
      <div class="modal-footer">
	  </div>
    </div>
  </div>
</div>
<!-- Modal [Add/Modify] end -->

<!-- Modal [JobDetails] start -->
<div class="modal fade" id="jobDetailsModal" tabindex="-1" role="dialog" aria-labelledby="jobDetailsModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
      
        <h5 class="modal-title" id="jobDetailsModalLabel"><span id="msgModal_title">查看Job參數明細</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      </div>
      <div class="modal-body">
       	<div class="card card-body">
       	  <div class="col-12">
       	  
       	  	<div id="sec_detail_common" style="display: none">
       	  		<div class="form-group row">
	            	<label for="viewDetailSchedTypeName" class="col-3 col-form-label">Sched_Type:</label>
	            	<div class="col-9">
	            		<input type="text" class="form-control form-control-sm w-90p" id="viewDetailSchedTypeName" readonly>
	            	</div>
	            </div>           
	        </div>
	        
       	  	<!-- 組態檔備份 -->
       	  	<div id="sec_detail_backupConfig" style="display: none">
       	  		<div class="form-group row">
	            	<label for="viewDetailConfigType" class="col-3 col-form-label">Config_Type:</label>
	            	<div class="col-9">
	            		<input type="text" class="form-control form-control-sm w-90p" id="viewDetailConfigType" readonly>
	            	</div>
	            </div>     
	            <div class="form-group row">
	            	<label for="viewDetailGroupIds" class="col-3 col-form-label">Group_ID:</label>
	            	<div class="col-9" style="padding-left: 7px;">
	            		<textarea rows="5" class="form-control form-control-sm w-90p" id="viewDetailGroupIds" style="height: 120px;" readonly></textarea>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailDeviceIds" class="col-3 col-form-label">Device_ID:</label>
	            	<div class="col-9" style="padding-left: 7px;">
	            		<textarea rows="5" class="form-control form-control-sm w-90p" id="viewDetailDeviceIds" style="height: 120px;" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]組態檔備份 -->
	            
            <!-- 組態檔異地備援(FTP) -->
            <div id="sec_detail_uploadBackupConfigFile2FTP" style="display: none">
            	<div class="form-group row">
	            	<label for="viewDetailConfigType" class="col-3 col-form-label">Config_Type:</label>
	            	<div class="col-9">
	            		<input type="text" class="form-control form-control-sm w-90p" id="viewDetailConfigType" readonly>
	            	</div>
	            </div>     
	            <div class="form-group row">
	            	<label for="viewDetailFtpName" class="col-3 col-form-label">FTP_Name:</label>
	            	<div class="col-9">
	            		<input type="text" class="form-control form-control-sm w-90p" id="viewDetailFtpName" readonly>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailFtpHost" class="col-3 col-form-label">FTP_Host:</label>
	            	<div class="col-9">
	            		<input type="text" class="form-control form-control-sm w-90p" id="viewDetailFtpHost" readonly>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailFtpPort" class="col-3 col-form-label">FTP_Port:</label>
	            	<div class="col-9">
	            		<input type="text" class="form-control form-control-sm w-90p" id="viewDetailFtpPort" readonly>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailFtpAccount" class="col-3 col-form-label">Account:</label>
	            	<div class="col-9">
	            		<input type="text" class="form-control form-control-sm w-90p" id="viewDetailFtpAccount" readonly>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailFtpPassword" class="col-3 col-form-label">Password:</label>
	            	<div class="col-9">
	            		<input type="password" class="form-control form-control-sm w-90p" id="viewDetailFtpPassword" readonly>
	                </div>
	            </div>
            </div>
            <!-- [END]組態檔異地備援(FTP) -->
            
            <!-- 系統檢核 -->
       	  	<div id="sec_detail_sysCheck" style="display: none">
	            <div class="form-group row">
	            	<label for="viewDetailSysCheckSql" class="col-3 col-form-label">SQL:</label>
	            	<div class="col-9" style="padding-left: 7px;">
	            		<textarea rows="5" class="form-control form-control-sm w-90p" id="viewDetailSysCheckSql" style="height: 120px;" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]系統檢核 -->
       	  	
       	  	<!-- Data Poller -->
       	  	<div id="sec_detail_dataPoller" style="display: none">
	            <div class="form-group row">
	            	<label for="viewDataPollerSettingId" class="col-3 col-form-label">Data_Poller_Setting.ID:</label>
	            	<div class="col-9" style="padding-left: 7px;">
	            		<textarea rows="5" class="form-control form-control-sm w-90p" id="viewDataPollerSettingId" style="height: 120px;" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]Data Poller -->
       	  	
       	  	<!-- IP Traffic Statistics -->
       	  	<div id="sec_detail_ipTrafficStatistics" style="display: none">
	            <div class="form-group row">
	            	<label for="viewDataPollerSettingId" class="col-md-12 col-sm-12 col-form-label">無參數設定</label>
	            </div>
       	  	</div>
       	  	<!-- [END]IP Traffic Statistics -->
       	  	
       	  	<!-- 本地檔案操作 -->
       	  	<div id="sec_detail_localFileOperation" style="display: none">
	            <div class="form-group row">
	            	<label for="viewLocalFileOperationSettingId" class="col-3 col-form-label">Job_File_Operation_Setting.ID:</label>
	            	<div class="col-9" style="padding-left: 7px;">
	            		<textarea rows="5" class="form-control form-control-sm w-90p" id="viewLocalFileOperationSettingId" style="height: 120px;" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]本地檔案操作 -->
            
       	  	<!-- Mail Sender -->
       	  	<div id="sec_detail_mailSender" style="display: none">
	            <div class="form-group row">
	            	<label for="viewMailSenderSettingId" class="col-3 col-form-label">Mail_Sender_Setting.ID:</label>
	            	<div class="col-9" style="padding-left: 7px;">
	            		<textarea rows="5" class="form-control form-control-sm w-90p" id="viewMailSenderSettingId" style="height: 120px;" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]Mail Sender -->
       	  	
       	  	<!-- IP/MAC/Port Poller -->
       	  	<div id="sec_detail_ipMacPortMappingPoller" style="display: none">
	            <div class="form-group row">
	            	<label for="viewIpMacPortMappingPollerGroupId" class="col-3 col-form-label">IP/MAC/Port_Poller_Group.ID:</label>
	            	<div class="col-9" style="padding-left: 7px;">
	            		<textarea rows="5" class="form-control form-control-sm w-90p" id="viewIpMacPortMappingPollerGroupId" style="height: 120px;" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]IP/MAC/Port Poller -->
            
          </div>
		</div>
      </div>
      <div class="modal-footer">
	  </div>
    </div>
  </div>
</div>
<!-- Modal [JobDetails] end -->

<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.admin.job.min.js"></script>