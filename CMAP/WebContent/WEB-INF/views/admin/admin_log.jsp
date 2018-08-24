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

<script>
	var isModify = false;
	var resutTable_errorLog;		//DataTable
	var resutTable_jobLog;			//DataTable
	const remarkShowLength = 50;	//設定欄位顯示內容最大長度
	
	$(document).ready(function() {
		//查詢按鈕(Web)點擊事件
	    $('#btnSearchErrorLog_web').click(function (e) {
	    	findErrorLogData('WEB');
	    });
		
	    $('#btnSearchJobLog_web').click(function (e) {
	    	findJobLogData('WEB');
	    });
	  	
	  	//查詢按鈕(Mobile)點擊事件
	    $('#btnSearchErrorLog_mobile').click(function (e) {
	    	findErrorLogData('MOBILE');
	    });
	  	
	    $('#btnSearchJobLog_mobile').click(function (e) {
	    	findJobLogData('MOBILE');
	    });
	});
	
	function viewDetail(key) {
		$.ajax({
			url : '${pageContext.request.contextPath}/admin/log/getDetailInfo.json',
			data : {
				logType : key.split("@~")[0],
				logId : key.split("@~")[1],
			},
			type : "POST",
			dataType : 'json',
			async: false,
			success : function(resp) {
				if (resp.code == '200') {
					$("#detailsModal").modal();
					
					$("#viewDetailTitle").html(key.split("@~")[0] + "明細");
					$("#viewDetail").html(resp.data.details);
					
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
	function findErrorLogData(from) {
		$("#queryFrom").val(from);
		
		if (from == "MOBILE") {
			$("#collapseExample").collapse("hide");
		}
		
		$("#divErrorLog").show();
		$("#divJobLog").hide();
			
		if (typeof resutTable_errorLog !== "undefined") {
			//resutTable.clear().draw(); server-side is enabled.
			resutTable_errorLog.ajax.reload();
			
  		} else {
  			resutTable_errorLog = $("#resutTable_errorLog").DataTable(
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
		        "createdRow": function( row, data, dataIndex ) {
		        	   if(data.message.length > remarkShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
		        	      $(row).children('td').eq(4).attr('onclick','javascript:changeShowRemarks(this);');
		        	   }
		        	   $(row).children('td').eq(4).attr('content', data.message);
		        	},
				"ajax" : {
					"url" : "${pageContext.request.contextPath}/admin/log/getErrorLog.json",
					"type" : "POST",
					"data" : function ( d ) {},
					"error" : function(xhr, ajaxOptions, thrownError) {
						ajaxErrorHandler();
					}
				},
				"order": [[1 , "desc" ]],
				"pageLength": 100,
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
					{},
					{ "data" : "entryDateStr" },
					{ "data" : "logger" },
					{ "data" : "logLevel" },
					{},
					{}
				],
				"columnDefs" : [
					{
						"targets" : [0],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render": function (data, type, row, meta) {
							       	return meta.row + meta.settings._iDisplayStart + 1;
							   	}
					},
					{
						"targets" : [4],
						"className" : "left",
						"searchable": true,
						"orderable": true,
						"render": function (data, type, row, meta) {
									if (row.message.length > remarkShowLength) {
										 return getPartialRemarksHtml(row.message); //內容長度超出設定，僅顯示部分內容
									} else {
										return row.message; 						//未超出設定則全部顯示
									}
							   	}
					},
					{
						"targets" : [5],
						"className" : "left",
						"searchable": false,
						"orderable": false,
						"render" : function(data, type, row) {
									 var html = '<a href="#" onclick="viewDetail(\'ERROR@~'+row.logId+'\')">查看明細</a>';
									 return html;
								 }
					}
				],
			});
  		}
	}
	
	function findJobLogData(from) {
		$("#queryFrom").val(from);
		
		if (from == "MOBILE") {
			$("#collapseExample").collapse("hide");
		}
		
		$("#divErrorLog").hide();
		$("#divJobLog").show();
			
		if (typeof resutTable_jobLog !== "undefined") {
			//resutTable.clear().draw(); server-side is enabled.
			resutTable_jobLog.ajax.reload();
			
  		} else {
  			resutTable_jobLog = $("#resutTable_jobLog").DataTable(
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
					"url" : "${pageContext.request.contextPath}/admin/log/getJobLog.json",
					"type" : "POST",
					"data" : function ( d ) {},
					"error" : function(xhr, ajaxOptions, thrownError) {
						ajaxErrorHandler();
					}
				},
				"order": [[1 , "desc" ]],
				"pageLength": 100,
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
					{},
					{ "data" : "startTimeStr" },
					{ "data" : "jobGroup" },
					{},
					{},
					{ "data" : "recordsNum" },
					{ "data" : "remark" },
					{ "data" : "endTimeStr" },
					{ "data" : "spendTimeInSeconds" },
					{ "data" : "cronExpression" },
					{ "data" : "prevFireTimeStr" },
					{ "data" : "nextFireTimeStr" },
					{ "data" : "logId" },
				],
				"columnDefs" : [
					{
						"targets" : [0],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render": function (data, type, row, meta) {
							       	return meta.row + meta.settings._iDisplayStart + 1;
							   	}
					},
					{
						"targets" : [3],
						"className" : "left",
						"searchable": true,
						"orderable": true,
						"render": function(data, type, row) {
							 var html = '<a href="#" onclick="viewDetail(\'JOB@~'+row.logId+'\')">'+row.jobName+'</a>';
							 return html;
						 }
					},
					{
						"targets" : [4],
						"className" : "left",
						"searchable": true,
						"orderable": true,
						"render": function(data, type, row) {
							 var html = '';
								 if (row.result === 'FAILED') {
									 html = '<font color="red">'+row.result+'</font>';
								 } else if (row.result === 'SUCCESS') {
									 html = '<font color="green">'+row.result+'</font>';
								 } else {
									 html = '<font color="purple">'+row.result+'</font>';
								 }
							 return html;
						 }
					}
				],
			});
  		}
	}

</script>
