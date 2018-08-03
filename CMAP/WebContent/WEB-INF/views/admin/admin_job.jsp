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
	
	<!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div class="col-sm-12 myTableSection" style="display:none;">
		<table id="resutTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap">操作&nbsp;<input type="checkbox" id="checkAll" name="checkAll" /></th>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">排程名稱</th>
		      <th scope="col" nowrap="nowrap">Job群組</th>
		      <th scope="col" nowrap="nowrap">Job名稱</th>
		      <th scope="col" nowrap="nowrap">Trigger群組</th>
		      <th scope="col" nowrap="nowrap">Tirgger名稱</th>
		      <th scope="col" nowrap="nowrap">優先度</th>
		      <th scope="col" nowrap="nowrap">狀態</th>
		      <th scope="col" nowrap="nowrap">前次觸發時間</th>
		      <th scope="col" nowrap="nowrap">下次觸發時間</th>
		      <th scope="col" nowrap="nowrap">Loss次數</th>
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
            <div class="box-body">
            	<div class="form-group">
                  <label for="jobName" class="control-label">排程名稱<span class="pull-right" style="color: red;">＊ </span></label>
                  <input type="text" class="form-control" id="jobName" name="jobName" placeholder="排程名稱">
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

<script>
	$(document).ready(function() {
		
		$("#btnAdd").click(function() {
			$("#addModifyModal").modal();
		});
		
		$("#btnPause").click(function() {
			jobAction('pause')
		});
		
		$("#btnResume").click(function() {
			jobAction('resume')
		});

		$("#btnDelete").click(function() {
			jobAction('delete')
		});
		
		$("#btnModify").click(function() {
			saveJob()
		});
		
		$("#btnSave").click(function() {
			saveJob()
		});
	});
	
	function jobAction(action) {
		var checkedItem = $('input[name=chkbox]:checked');

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
			type : "POST",
			dataType : 'json',
			async: false,
			success : function(resp) {
				if (resp.code == '200') {
					alert(resp.message);
					findData('WEB');
					
				} else {
					alert(resp.message);
				}
			},

			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.status+" :: "+thrownError);
			}
		});
	}
	
	function saveJob() {
		var deviceListIdArray = new Array();
		deviceListIdArray.push("40283a8164f431e90164f432bb8d0003");
		deviceListIdArray.push("40283a8164f431e90164f432bb8f0005");
		deviceListIdArray.push("40283a8164f431e90164f432bb8f0004");
		
		$.ajax({
			url : '${pageContext.request.contextPath}/admin/job/save',
			//data : $('#formEdit').serialize(),
			data : function(d) {
				d.inputSchedType = 'backupConfig',
				d.inputJobName = 'backupJob_1',
				d.inputJobGroup = 'backupGroup',
				d.inputCronExpression = '0 0/30 * * * ?',
				d.inputClassName = 'JobBackupConfig',
				d.inputDeviceListIds = deviceListIdArray,
				d.inputConfigType = ''
				return d;
			},
			type : 'POST',
			dataType : 'json',
			async: false,
			success : function(resp) {
				if (resp.code == '200') {
					alert('排程新增成功');
					findData('WEB');
					
					setTimeout(function(){
						$('#addModifyModal').modal('hide');
						
					}, 500);
					
				} else {
					alert(resp.message);
				}
			},

			error : function(xhr, ajaxOptions, thrownError) {
				alert(xhr.status+" :: "+thrownError);
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
					"data" : function ( d ) {}
				},
				"order": [[3 , "asc" ]],
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
				},
				"columns" : [
					{},{},
					{ "data" : "schedName" },
					{ "data" : "jobGroup" },
					{ "data" : "jobName" },
					{ "data" : "triggerGroup" },
					{ "data" : "triggerName" },
					{ "data" : "priority" },
					{ "data" : "triggerState" },
					{ "data" : "_preFireTime" },
					{ "data" : "_nextFireTime" },
					{ "data" : "misFireInstr" },
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
						"targets" : [15],
						"className" : "left",
						"searchable": true,
						"orderable": true,
						"render" : function(data, type, row) {
									 var html = '<a href="#" onclick="viewDetail()">查看明細</a>';
									 return html;
								 }
					}
				],
			});
  		}
	}

</script>
