/**
 * 
 */
var reasonShowLength = 20;	//設定欄位顯示內容最大長度
var STEP_NUM = 1;

$(document).ready(function() {
	initMenuStatus("toggleMenu_cm", "toggleMenu_cm_items", "cm_record");
	
	$("#viewProvisionLogModal").on("shown.bs.modal", function () {
    	currentDiffPos = -1;
    	
    	setTimeout(function() {
    		$('#viewModal_provisionLog').scrollTop(0);
    	}, 300);
    });
});

//查詢按鈕動作
function findData(from) {
	$('#queryFrom').val(from);
	
	if (typeof resutTable !== "undefined") {
		//resutTable.clear().draw(); server-side is enabled.
		resutTable.ajax.reload();
		
	} else {
		$(".myTableSection").show();
		
		resutTable = $('#resutTable').DataTable(
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
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
	        "createdRow": function( row, data, dataIndex ) {
	        	   if(data.deliveryReason != null && data.deliveryReason.length > reasonShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
	        	      $(row).children('td').eq(7).attr('onclick','javascript:showFullScript($(this));');
	        	      $(row).children('td').eq(7).addClass('cursor_zoom_in');
	        	   }
	        	   $(row).children('td').eq(7).attr('content', data.deliveryReason);
	        	},
			"ajax" : {
				"url" : _ctx + '/delivery/getDeliveryRecordData.json',
				"type" : 'POST',
				"data" : function ( d ) {
					if ($('#queryFrom').val() == 'WEB') {
						d.queryGroup = $("#queryGroup").val();
						d.queryDevice = $("#queryDevice").val();
						d.queryDateBegin = $("#queryExcuteDateBegin").val();
						d.queryDateEnd = $("#queryExcuteDateEnd").val();
					
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryGroup = $("#queryGroup_mobile").val();
						d.queryDevice = $("#queryDevice_mobile").val();
						d.queryDateBegin = $("#queryExcuteDateBegin_mobile").val();
						d.queryDateEnd = $("#queryExcuteDateEnd_mobile").val();
					}
					
					return d;
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				}
			},
			"order" : [[1 , 'desc' ]],
			"pageLength" : 100,
			/*
			"initComplete": function(settings, json){
            },
            */
			"drawCallback" : function(settings) {
				//$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust();
				$("div.dataTables_length").parent().removeClass('col-sm-12');
				$("div.dataTables_length").parent().addClass('col-sm-6');
				$("div.dataTables_filter").parent().removeClass('col-sm-12');
				$("div.dataTables_filter").parent().addClass('col-sm-6');
				
				$("div.dataTables_info").parent().removeClass('col-sm-12');
				$("div.dataTables_info").parent().addClass('col-sm-6');
				$("div.dataTables_paginate").parent().removeClass('col-sm-12');
				$("div.dataTables_paginate").parent().addClass('col-sm-6');
				
				bindTrEvent();
				initCheckedItems();
				unfoldMobileMenu();
			},
			"columns" : [
				{},
				{ "data" : "deliveryBeginTime" },
				{ "data" : "createBy" },
				{ "data" : "groupName" },
				{ "data" : "deviceName" },
				{ "data" : "systemVersion" , "className" : "center" },
				{ "data" : "scriptName" },
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
					"targets" : [7],
					"className" : "left",
					"searchable": true,
					"orderable": false,
					"render" : function(data, type, row) {
						if (row.deliveryReason != null && row.deliveryReason.length > reasonShowLength) {
							 return getPartialContentHtml(row.deliveryReason, reasonShowLength); 	//內容長度超出設定，僅顯示部分內容
						} else {
							return row.deliveryReason; 							//未超出設定則全部顯示
						}
					}
				},
				{
					"targets" : [8],
					"className" : "center",
					"searchable": true,
					"orderable": true,
					"render" : function(data, type, row) {
						var html = "";
						switch (row.deliveryResult) {
							case "SUCCESS":
								html = '<button name="btn_viewLog" class="btn btn-secondary" onclick="viewProvisionLog(this)" data-step-id="' + row.logStepId + '">成功</button>';
								break;
								
							case "NO_DIFFERENT":
								html = '<button name="btn_viewLog" class="btn btn-success" onclick="viewProvisionLog(this)" data-step-id="' + row.logStepId + '">無差異</a>';
								break;
								
							case "ERROR":
								html = '<button name="btn_viewLog" class="btn btn-danger" onclick="viewProvisionLog(this)" data-step-id="' + row.logStepId + '">失敗</a>';
								break;
						}
						return html;
					}
				}
			],
		});
	}
}

function viewProvisionLog(obj) {
	var logStepId = $(obj).data("step-id");
	var beginTime = $(obj).closest("tr").find("td").eq(1).text();
	var userName = $(obj).closest("tr").find("td").eq(2).text();
	var groupName = $(obj).closest("tr").find("td").eq(3).text();
	var deviceName = $(obj).closest("tr").find("td").eq(4).text();
	var systemVersion = $(obj).closest("tr").find("td").eq(5).text();
	var scriptName = $(obj).closest("tr").find("td").eq(6).text();
	var reason = $(obj).closest("tr").find("td").eq(7).text();
	var result = $(obj).text();
	
	$.ajax({
		url : _ctx + '/delivery/viewProvisionLog.json',
		data : {
			"logStepId" : logStepId
		},
		type : "POST",
		dataType : 'json',
		async: true,
		success : function(resp) {
			if (resp.code == '200') {
				var ps = {
					"beginTime" : beginTime,
					"userName" : userName,
					"groupName" : groupName,
					"deviceName" : deviceName,
					"systemVersion" : systemVersion,
					"scriptName" : scriptName,
					"reason" : reason,
					"result" : result,
					"log" : resp.data.log
				}
				var provisionLog = resp.data.log != null ? (resp.data.log).replace(/(\r\n|\r|\n)/g, "<br>") : "異常無紀錄";
				
				$("#viewModal_beginTime").val(beginTime);
				$("#viewModal_userName").val(userName);
				$("#viewModal_groupName").val(groupName);
				$("#viewModal_deviceName").val(deviceName);
				$("#viewModal_systemVersion").val(systemVersion);
				$("#viewModal_scriptName").val(scriptName);
				$("#viewModal_reason").val(reason);
				$("#viewModal_result").val(result);
				$("#viewModal_provisionLog").html(provisionLog);
				
				$("#viewProvisionLogModal").modal('show');
				
			} else {
				alert(resp.message);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
}