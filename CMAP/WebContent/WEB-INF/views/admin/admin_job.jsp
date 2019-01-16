<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<section>

  <div class="container-fluid">
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div class="row search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
		<div class="container-fluid">
      	  <div class="form-group row">
			<div class="col-lg-2" style="padding-top: 5px;">
    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web">查詢</button>
    	    </div>
      	  </div>
      	</div>

      </div>
      <!-- [END]查詢欄位bar -->
      <!-- [START]操作按鈕bar -->
      <div class="col-12 action-btn-bar">
        <div class="container-fluid">
        	<div class="row">
        		<div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnAdd">新增</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnModify">修改</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnExcute">立即執行</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-dark btn-sm" style="width: 100%" id="btnPause">暫停</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-info btn-sm" style="width: 100%" id="btnResume">重啟</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnDelete">刪除</button>
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
			  <div class="form-group row">
	    	    <div class="col-sm-12">
			      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_mobile">查詢</button>
			    </div>
			  </div>
		  	</div>
		  </div>
	  </div>
	</div>
	<!-- [END]查詢欄位 for 中小型解析度螢幕 -->
	
	<!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div class="col-sm-12 myTableSection" style="display:none;">
		<table id="resutTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap">操作&nbsp;<input type="checkbox" id="checkAll" name="checkAll" /></th>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">排程類別</th>
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
                	<label for="inputSchedType" class="col-md-2 col-sm-3 col-form-label">排程類別<span class="pull-right" style="color: red;">＊ </span></label>
                  	<div class="col-md-10 col-sm-9">
                  		<form:select path="inputSchedType" id="inputSchedType" name="inputSchedType" class="form-control form-control-sm">
		                  	<form:option value="" label="=== 請選擇  ===" />
		                  	<form:options items="${inputSchedType}" />
		                </form:select>
                  	</div>
                </div>                              
	           	<div class="form-group row">
	            	<label for="inputJobName" class="col-md-2 col-sm-3 col-form-label">排程名稱<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="inputJobName" name="inputJobName" placeholder="排程名稱">
	            	</div>
	            </div>                              
	           	<div class="form-group row">
	            	<label for="inputJobGroup" class="col-md-2 col-sm-3 col-form-label">群組名稱<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-md-10 col-sm-9">
	                	<input type="text" class="form-control form-control-sm" id="inputJobGroup" name="inputJobGroup" placeholder="群組名稱">
	                </div>
	            </div>
	            <div class="form-group row">
	            	<label for="inputDescription" class="col-md-2 col-sm-3 col-form-label">備註說明</label>
	            	<div class="col-md-10 col-sm-9">
	                	<input type="text" class="form-control form-control-sm" id="inputDescription" name="inputDescription" placeholder="備註說明">
	                </div>
	            </div>
	            <div class="form-group row">
	            	<label for="inputCronExpression" class="col-md-2 col-sm-3 col-form-label">週期表示式<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-md-10 col-sm-9">
	                	<input type="text" class="form-control form-control-sm" id="inputCronExpression" name="inputCronExpression" placeholder="example: 0 0/30 * * * ?">
	                </div>
	            </div>
	            <div class="form-group row">
	            	<label for="inputPriority" class="col-md-2 col-sm-3 col-form-label">優先度<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-md-10 col-sm-9">
	                	<input type="text" class="form-control form-control-sm" id="inputPriority" name="inputPriority" placeholder="優先度" value="5">
	                </div>
	            </div>
	            <div class="form-group row">
	            	<label for="inputMisFirePolicy" class="col-md-2 col-sm-3 col-form-label">排程策略<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-md-10 col-sm-9">
	                	<form:select path="inputMisFirePolicy" id="inputMisFirePolicy" name="inputMisFirePolicy" class="form-control form-control-sm">
		                  	<form:option value="" label="=== 請選擇  ===" />
		                  	<form:options items="${inputMisFirePolicy}" />
		                </form:select>
	                </div>
	            </div>
	            
	            <hr />
	            
	            <!-- 組態檔備份 -->
	            <div id="sec_backupConfig" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputFtpNames" class="col-12 col-form-label"><span style="color:blue">*** 備份目標設備 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputConfigType" class="col-md-2 col-sm-3 col-form-label">備份範圍<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-md-10 col-sm-9">
		                	<form:select path="inputConfigType" id="inputConfigType" name="inputConfigType" class="form-control form-control-sm">
			                  	<form:option value="" label="=== ALL ===" />
			                  	<form:options items="${inputConfigType}" />
			                </form:select>
		                </div>
		            </div>
		            <div class="form-group row">
		            	<label for="inputDeviceListIds" class="col-md-2 col-sm-3 col-form-label">Group_ID<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-md-4 col-sm-3">
		            		<textarea rows="5" class="form-control form-control-sm" id="inputGroupIds" name="inputGroupIds" placeholder="(1行1筆資料)"></textarea>
		                </div>
		                <label for="inputDeviceListIds" class="col-md-2 col-sm-3 col-form-label">Device_ID<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-md-4 col-sm-3">
		            		<textarea rows="5" class="form-control form-control-sm" id="inputDeviceIds" name="inputDeviceIds" placeholder="(1行1筆資料)"></textarea>
		                </div>
		            </div>
	            </div>
	            <!-- [END]組態檔備份 -->
	            
	            <!-- 組態檔異地備援(FTP) -->
	            <div id="sec_uploadBackupConfigFile2FTP" style="display: none">
	              <div class="form-group row">
	              	<label for="inputFtpNames" class="col-12 col-form-label"><span style="color:blue">*** FTP相關設定 ***</span></label>
	              </div>
	              <div class="form-group row">
	              	<label for="inputFtpNames" class="col-md-2 col-sm-3 col-form-label">FTP Name:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="inputFtpName" name="inputFtpName" placeholder="(選填)"></input>
	                </div>
	              </div>
	              <div class="form-group row">
	                <label for="inputFtpHosts" class="col-md-2 col-sm-3 col-form-label">FTP Host:<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="inputFtpHost" name="inputFtpHost" placeholder="Host or IP"></input>
	                </div>
	              </div>
	              <div class="form-group row">
	                <label for="inputFtpPorts" class="col-md-2 col-sm-3 col-form-label">FTP Port:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="inputFtpPort" name="inputFtpPort" placeholder="(選填；預設值=21)"></input>
	                </div>
	              </div>
	              <div class="form-group row">
	                <label for="inputFtpAccounts" class="col-md-2 col-sm-3 col-form-label">Account:<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="inputFtpAccount" name="inputFtpAccount" placeholder="(FTP登入帳號)"></input>
	                </div>
	              </div>
	              <div class="form-group row">
	                <label for="inputFtpPasswords" class="col-md-2 col-sm-3 col-form-label">Password:<span class="pull-right" style="color: red;">＊ </span></label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="password" class="form-control form-control-sm" id="inputFtpPassword" name="inputFtpPassword" placeholder="(FTP登入密碼)"></input>
	                </div>
	              </div>
	            </div>
	            <!-- [END]組態檔異地備援(FTP) -->
	            
	            <!-- 系統檢核 -->
	            <div id="sec_sysCheck" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputSysCheckNames" class="col-12 col-form-label"><span style="color:blue">*** 系統檢核設定 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputSysCheckSql" class="col-md-2 col-sm-3 col-form-label">SQLs<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-md-10 col-sm-9">
		            		<textarea rows="5" class="form-control form-control-sm" id="inputSysCheckSql" name="inputSysCheckSql" placeholder="多道SQL以「;」分隔"></textarea>
		                </div>
		            </div>
	            </div>
	            <!-- [END]系統檢核 -->
	            
	            <!-- Data Poller -->
	            <div id="sec_dataPoller" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputDataPollerNames" class="col-12 col-form-label"><span style="color:blue">*** Data Poller 參數設定 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputDataPollerSettingId" class="col-md-2 col-sm-3 col-form-label">Setting IDs<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-md-10 col-sm-9">
		            		<input type="text" class="form-control form-control-sm" id="inputDataPollerSettingId" name="inputDataPollerSettingId" placeholder="多組ID以「,」區隔；區間以「ID1~ID2」表示"></input>
		                </div>
		            </div>
	            </div>
	            <!-- [END]Data Poller -->
	            
	            <!-- 本地檔案操作 -->
	            <div id="sec_localFileOperation" style="display: none">
	            	<div class="form-group row">
		              	<label for="inputLocalFileOperationNames" class="col-12 col-form-label"><span style="color:blue">*** 本地檔案操作 參數設定 ***</span></label>
		            </div>
		            <div class="form-group row">
		            	<label for="inputLocalFileOperationSettingId" class="col-md-2 col-sm-3 col-form-label">Setting IDs<span class="pull-right" style="color: red;">＊ </span></label>
		            	<div class="col-md-10 col-sm-9">
		            		<input type="text" class="form-control form-control-sm" id="inputLocalFileOperationSettingId" name="inputLocalFileOperationSettingId" placeholder="多組ID以「,」區隔；區間以「ID1~ID2」表示"></input>
		                </div>
		            </div>
	            </div>
	            <!-- [END]本地檔案操作 -->
	            
	            <input type="hidden" id="jobKeyName" name="jobKeyName">
	            <input type="hidden" id="jobKeyGroup" name="jobKeyGroup">
	            
              </div>
			</div>
			<div class="modal-footer">
        		<button type="button" class="btn btn-secondary" id="btnClose" data-dismiss="modal">關閉</button>
        		<button type="button" class="btn btn-success" id="btnSave">保存</button>
			</div>
        </form>
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
	            	<label for="viewDetailSchedTypeName" class="col-md-2 col-sm-3 col-form-label">Sched_Type:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="viewDetailSchedTypeName" readonly>
	            	</div>
	            </div>           
	        </div>
	        
       	  	<!-- 組態檔備份 -->
       	  	<div id="sec_detail_backupConfig" style="display: none">
       	  		<div class="form-group row">
	            	<label for="viewDetailConfigType" class="col-md-2 col-sm-3 col-form-label">Config_Type:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="viewDetailConfigType" readonly>
	            	</div>
	            </div>     
	            <div class="form-group row">
	            	<label for="viewDetailGroupIds" class="col-md-2 col-sm-3 col-form-label">Group_ID:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<textarea rows="5" class="form-control form-control-sm" id="viewDetailGroupIds" readonly></textarea>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailDeviceIds" class="col-md-2 col-sm-3 col-form-label">Device_ID:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<textarea rows="5" class="form-control form-control-sm" id="viewDetailDeviceIds" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]組態檔備份 -->
	            
            <!-- 組態檔異地備援(FTP) -->
            <div id="sec_detail_uploadBackupConfigFile2FTP" style="display: none">
            	<div class="form-group row">
	            	<label for="viewDetailConfigType" class="col-md-2 col-sm-3 col-form-label">Config_Type:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="viewDetailConfigType" readonly>
	            	</div>
	            </div>     
	            <div class="form-group row">
	            	<label for="viewDetailFtpName" class="col-md-2 col-sm-3 col-form-label">FTP_Name:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="viewDetailFtpName" readonly>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailFtpHost" class="col-md-2 col-sm-3 col-form-label">FTP_Host:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="viewDetailFtpHost" readonly>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailFtpPort" class="col-md-2 col-sm-3 col-form-label">FTP_Port:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="viewDetailFtpPort" readonly>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailFtpAccount" class="col-md-2 col-sm-3 col-form-label">Account:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="text" class="form-control form-control-sm" id="viewDetailFtpAccount" readonly>
	                </div>
	            </div>
	            <div class="form-group row">
	                <label for="viewDetailFtpPassword" class="col-md-2 col-sm-3 col-form-label">Password:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<input type="password" class="form-control form-control-sm" id="viewDetailFtpPassword" readonly>
	                </div>
	            </div>
            </div>
            <!-- [END]組態檔異地備援(FTP) -->
            
            <!-- 系統檢核 -->
       	  	<div id="sec_detail_sysCheck" style="display: none">
	            <div class="form-group row">
	            	<label for="viewDetailSysCheckSql" class="col-md-2 col-sm-3 col-form-label">SQL:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<textarea rows="5" class="form-control form-control-sm" id="viewDetailSysCheckSql" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]系統檢核 -->
       	  	
       	  	<!-- Data Poller -->
       	  	<div id="sec_detail_dataPoller" style="display: none">
	            <div class="form-group row">
	            	<label for="viewDataPollerSettingId" class="col-md-2 col-sm-3 col-form-label">Data_Poller_Setting.ID:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<textarea rows="5" class="form-control form-control-sm" id="viewDataPollerSettingId" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]Data Poller -->
       	  	
       	  	<!-- 本地檔案操作 -->
       	  	<div id="sec_detail_localFileOperation" style="display: none">
	            <div class="form-group row">
	            	<label for="viewLocalFileOperationSettingId" class="col-md-2 col-sm-3 col-form-label">Job_File_Operation_Setting.ID:</label>
	            	<div class="col-md-10 col-sm-9">
	            		<textarea rows="5" class="form-control form-control-sm" id="viewLocalFileOperationSettingId" readonly></textarea>
	                </div>
	            </div>
       	  	</div>
       	  	<!-- [END]本地檔案操作 -->
            
          </div>
		</div>
		<div class="modal-footer">
       		<button type="button" class="btn btn-secondary" id="btnClose" data-dismiss="modal">關閉</button>
		</div>
      </div>
    </div>
  </div>
</div>
<!-- Modal [JobDetails] end -->

<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.admin.job.min.js"></script>