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
	            
	            <input type="hidden" id="jobKeyName" name="jobKeyName">
	            <input type="hidden" id="jobKeyGroup" name="jobKeyGroup">
	            
              </div>
			</div>
			<div class="modal-footer">
        		<button type="button" class="btn btn-default" id="btnClose" data-dismiss="modal">關閉</button>
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
            
          </div>
		</div>
		<div class="modal-footer">
       		<button type="button" class="btn btn-default" id="btnClose" data-dismiss="modal">關閉</button>
		</div>
      </div>
    </div>
  </div>
</div>
<!-- Modal [JobDetails] end -->

<script>
	var isModify = false;
	
	$(document).ready(function() {
		
		$("#btnAdd").click(function() {
			isModify = false;
			uncheckAll();
			initModal();
			$("#addModifyModal").modal({
				backdrop : 'static'
			});
		});
		
		$("#btnPause").click(function() {
			jobAction('pause');
		});
		
		$("#btnResume").click(function() {
			jobAction('resume');
		});

		$("#btnDelete").click(function() {
			const confirmMsg = confirm("確認是否刪除?");
			
			if (confirmMsg) {
				jobAction('delete');
			}
		});
		
		$("#btnModify").click(function() {
			isModify = true;
			jobAction('modify');
			changeSchedView($("#inputSchedType").val());
		});
		
		$("#btnSave").click(function() {
			saveJob(isModify);
		});
		
		$("#btnExcute").click(function() {
			jobAction('excute');
		});
		
		$("#inputSchedType").change(function() {
			changeSchedView($(this).val());
		});
	});
	
	function changeSchedView(schedType) {
		$("div[id^=sec_]").hide();
		
		if (schedType !== '') {
			if (schedType.indexOf('sysCheck') != -1) {
				$("#sec_sysCheck").show();
			} else {
				$("#sec_"+schedType).show();
			}
		}
	}
	
	function jobAction(action) {
		var checkedItem = $('input[name=chkbox]:checked');
		
		if (isModify && checkedItem.length != 1) {
			alert('修改僅允許勾選一項，請重新選擇');
			uncheckAll();
			return;
		}

		var checkedObjArray = new Array();
		var obj = new Object();
		
		for (var i=0; i<checkedItem.length; i++) {
			obj = new Object();
			obj.keyVal = checkedItem[i].value;
			checkedObjArray.push(obj);
		}
		
		$.ajax({
			url : '${pageContext.request.contextPath}/admin/job/'+action,
			data : JSON.stringify(checkedObjArray),
			headers: {
			    'Accept': 'application/json',
			    'Content-Type': 'application/json'
			},
			type : "POST",
			dataType : 'json',
			async: false,
			success : function(resp) {
				if (resp.code == '200') {
					if (action == 'modify') {
						$("#inputJobName").val(resp.data.inputJobName);
						$("#inputJobGroup").val(resp.data.inputJobGroup);
						
						$("#jobKeyName").val(resp.data.inputJobName);
						$("#jobKeyGroup").val(resp.data.inputJobGroup);
						
						$("#inputCronExpression").val(resp.data.inputCronExpression);
						$("#inputGroupIds").val(resp.data.inputGroupIds);
						$("#inputDeviceIds").val(resp.data.inputDeviceIds);
						$("#inputPriority").val(resp.data.inputPriority);
						$("#inputDescription").val(resp.data.inputDescription);
						
						$("#inputFtpName").val(resp.data.inputFtpName);
						$("#inputFtpHost").val(resp.data.inputFtpHost);
						$("#inputFtpPort").val(resp.data.inputFtpPort);
						$("#inputFtpAccount").val(resp.data.inputFtpAccount);
						$("#inputFtpPassword").val(resp.data.inputFtpPassword);
						
						$("#inputSysCheckSql").val(resp.data.inputSysCheckSql);
						
						$("#inputMisFirePolicy option").filter(function() {
						    return $(this).val() == resp.data.inputMisFirePolicy; 
						}).prop('selected', true);
						
						$("#inputSchedType option").filter(function() {
						    return $(this).val() == resp.data.inputSchedType; 
						}).prop('selected', true);
						
						$("#inputConfigType option").filter(function() {
						    return $(this).val() == resp.data.inputConfigType; 
						}).prop('selected', true);
						
						$("#inputSchedType").attr('disabled', 'disabled');
						$("#inputJobName").attr('disabled', 'disabled');
						$("#inputJobGroup").attr('disabled', 'disabled');
						
						$("#sec_"+resp.data.inputSchedType).show();
						
						$("#addModifyModal").modal({
							backdrop : 'static'
						});
						
					} else {
						alert(resp.message);
						findData('WEB');
					}
					
				} else {
					alert(resp.message);
				}
				
				if (resp.code == '403') {
					uncheckAll();
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				ajaxErrorHandler();
			}
		});
	}
	
	function saveJob(isModify) {
		var disabled = $("#formEdit").find(':input:disabled').removeAttr('disabled');
		var serialized = JSON.stringify($("#formEdit").serializeObject());
		disabled.attr('disabled','disabled');
		
		$.ajax({
			url : '${pageContext.request.contextPath}/admin/job/save',
			data : serialized,
			contentType : 'application/json; charset=utf-8',
			/*
			headers: {
			    'Accept': 'application/json',
			    'Content-Type': 'application/json'
			},
			*/
			type : 'POST',
			dataType : 'json',
			async: false,
			success : function(resp) {
				if (resp.code == '200') {
					alert(resp.message);
					findData('WEB');
					
					setTimeout(function(){
						$('#addModifyModal').modal('hide');
						
					}, 500);
					
				} else {
					alert(resp.message);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				ajaxErrorHandler();
			}
		});
	}
	
	function viewDetail(key) {
		$.ajax({
			url : '${pageContext.request.contextPath}/admin/job/getJobDetails.json',
			data : {
				jobKeyName : key.split("@~")[0],
				jobKeyGroup : key.split("@~")[1],
			},
			type : "POST",
			dataType : 'json',
			async: false,
			success : function(resp) {
				if (resp.code == '200') {
					$("#jobDetailsModal").modal();
					
					$("#viewDetailSchedTypeName").val(resp.data.schedTypeName);
					$("#viewDetailConfigType").val(resp.data.configType);
					$("#viewDetailGroupIds").val(resp.data.groupId);
					$("#viewDetailDeviceIds").val(resp.data.deviceId);
					
					$("#viewDetailFtpName").val(resp.data.ftpName);
					$("#viewDetailFtpHost").val(resp.data.ftpHost);
					$("#viewDetailFtpPort").val(resp.data.ftpPort);
					$("#viewDetailFtpAccount").val(resp.data.ftpAccount);
					$("#viewDetailFtpPassword").val(resp.data.ftpPassword);
					
					$("#viewDetailSysCheckSql").val(resp.data.sysCheckSqls);
					
					const schedType = resp.data.schedType;
					
					$("div[id^=sec_detail_]").hide();
					
					if (schedType !== '') {
						if (schedType.indexOf('sysCheck') != -1) {
							$("#sec_detail_sysCheck").show();
						} else {
							$("#sec_detail_"+schedType).show();
						}
						
						$("#sec_detail_common").show();
					}
					
				} else {
					alert(resp.message);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				ajaxErrorHandler();
			}
		});
	}
	
	//查詢按鈕動作
	function findData(from) {
		$("#queryFrom").val(from);
		$("input[name=checkAll]").prop("checked", false);
		
		if (from == "MOBILE") {
			$("#collapseExample").collapse("hide");
		}
		
		if (typeof resutTable !== "undefined") {
			//resutTable.clear().draw(); server-side is enabled.
			resutTable.ajax.reload();
			
  		} else {
  			$(".myTableSection").show();
  			
  			resutTable = $("#resutTable").DataTable(
			{
				"autoWidth" 	: true,
				"paging" 		: true,
				"bFilter" 		: true,
				"ordering" 		: true,
				"info" 			: true,
				"serverSide" 	: true,
				"bLengthChange" : true,
				"pagingType" 	: "full",
				"processing" 	: true,
				"scrollX"		: true,
				"scrollY"		: dataTableHeight,
				"scrollCollapse": true,
				"language" : {
		    		"url" : "${pageContext.request.contextPath}/resources/js/dataTable/i18n/Chinese-traditional.json"
		        },
				"ajax" : {
					"url" : "${pageContext.request.contextPath}/admin/job/getJobInfo.json",
					"type" : "POST",
					"data" : function ( d ) {},
					"error" : function(xhr, ajaxOptions, thrownError) {
						ajaxErrorHandler();
					}
				},
				"order": [[7 , "desc" ]],
				/*
				"initComplete": function(settings, json){
	            },
	            */
				"drawCallback" : function(settings) {
					$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust();
					$("div.dataTables_length").parent().removeClass("col-sm-12");
					$("div.dataTables_length").parent().addClass("col-sm-6");
					$("div.dataTables_filter").parent().removeClass("col-sm-12");
					$("div.dataTables_filter").parent().addClass("col-sm-6");
					
					$("div.dataTables_info").parent().removeClass("col-sm-12");
					$("div.dataTables_info").parent().addClass("col-sm-6");
					$("div.dataTables_paginate").parent().removeClass("col-sm-12");
					$("div.dataTables_paginate").parent().addClass("col-sm-6");
					
					bindTrEvent();
				},
				"columns" : [
					{},{},{},
					{ "data" : "jobGroup" },
					{ "data" : "jobName" },
					{ "data" : "priority" },
					{ "data" : "triggerState" },
					{ "data" : "_preFireTime" },
					{ "data" : "_nextFireTime" },
					{ "data" : "misfireInstr" },
					{ "data" : "cronExpression" },
					{ "data" : "timeZoneId" },
					{ "data" : "jobClassName" },
					{},
					{ "data" : "description" }
				],
				"columnDefs" : [
					{
						"targets" : [0],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render" : function(data, type, row) {
									 var html = '<input type="checkbox" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value="'+row.jobName+'@~'+row.jobGroup+'">';
									 return html;
								 }
					},
					{
						"targets" : [1],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render": function (data, type, row, meta) {
							       	return meta.row + meta.settings._iDisplayStart + 1;
							   	}
					},
					{
						"targets" : [2],
						"className" : "left",
						"searchable": false,
						"orderable": false,
						"render": function (data, type, row, meta) {
							       	return row.schedTypeName;
							   	}
					},
					{
						"targets" : [13],
						"className" : "left",
						"searchable": false,
						"orderable": false,
						"render" : function(data, type, row) {
									 var html = '<a href="#" onclick="viewDetail(\''+row.jobName+'@~'+row.jobGroup+'\')">查看明細</a>';
									 return html;
								 }
					}
				],
			});
  		}
	}

</script>
