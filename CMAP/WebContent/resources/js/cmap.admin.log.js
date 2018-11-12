/**
 * 
 */
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
		url : _ctx + '/admin/log/getDetailInfo.json',
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
			"pagingType" 	: "full_numbers",
			"processing" 	: true,
			"scrollX"		: true,
			"scrollY"		: dataTableHeight,
			"scrollCollapse": true,
			"language" : {
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
	        "createdRow": function( row, data, dataIndex ) {
	        	   if(data.message != null && data.message.length > remarkShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
	        	      $(row).children('td').eq(4).attr('onclick','javascript:changeShowContent(this, '+remarkShowLength+');');
	        	      $(row).children('td').eq(4).addClass('cursor_zoom_in');
	        	   }
	        	   $(row).children('td').eq(4).attr('content', data.message);
	        	},
			"ajax" : {
				"url" : _ctx + "/admin/log/getErrorLog.json",
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
				feather.replace();
			},
			"columns" : [
				{},
				{ "data" : "entryDateStr" , "class" : "center" },
				{ "data" : "logger" },
				{ "data" : "logLevel" , "class" : "center" },
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
								if (row.message != null && row.message.length > remarkShowLength) {
									 return getPartialContentHtml(row.message, remarkShowLength); //內容長度超出設定，僅顯示部分內容
								} else {
									return row.message; 						//未超出設定則全部顯示
								}
						   	}
				},
				{
					"targets" : [5],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<a href="#" onclick="viewDetail(\'ERROR@~'+row.logId+'\')"><span data-feather="zoom-in"></span></a>';
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
			"pagingType" 	: "full_numbers",
			"processing" 	: true,
			"scrollX"		: true,
			"scrollY"		: dataTableHeight,
			"scrollCollapse": true,
			"language" : {
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
			"ajax" : {
				"url" : _ctx + "/admin/log/getJobLog.json",
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