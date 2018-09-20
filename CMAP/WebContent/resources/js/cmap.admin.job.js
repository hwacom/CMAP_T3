/**
 * 
 */
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
		url : _ctx + '/admin/job/'+action,
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
		url : _ctx + '/admin/job/save',
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
		url : _ctx + '/admin/job/getJobDetails.json',
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
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
			"ajax" : {
				"url" : _ctx + "/admin/job/getJobInfo.json",
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
				feather.replace();
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
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<a href="#" onclick="viewDetail(\''+row.jobName+'@~'+row.jobGroup+'\')"><span data-feather="zoom-in"></span></a>';
								 return html;
							 }
				}
			],
		});
	}
}