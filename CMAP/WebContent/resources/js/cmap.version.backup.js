/**
 * 
 */
const remarkShowLength = 20;	//設定欄位顯示內容最大長度

$(document).ready(function() {
	changeDeviceMenu("queryDevice", $("#queryGroup").val());
    
    //備份按鈕點擊事件
    $('#btnBackup').click(function (e) {
    	if (chkChecked()) {
    		$("#backupDialogModal").modal({
				backdrop : 'static'
			});
    		
    	} else {
    		alert('請勾選要備份的設備');
    	}
    });
    
    $('#btnConfirm').click(function (e) {
    	doBackup();
    });
});

function doBackup() {
	var checkedItem = $('input[name=chkbox]:checked');
	var configType = $('#queryConfigType').val();

	var checkedObjArray = new Array();
	var obj = new Object();
	obj.configType = configType;
	checkedObjArray.push(obj);
	
	for (var i=0; i<checkedItem.length; i++) {
		obj = new Object();
		obj.deviceListId = checkedItem[i].value;
		checkedObjArray.push(obj);
	}
	
	$.ajax({
		url : _ctx + '/version/backup/execute',
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
				alert(resp.message);
				findData($('#queryFrom').val());
				
				setTimeout(function(){
					$("#backupDialogModal").modal('hide');
					
				}, 500);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
}


function chkChecked() {
	var hasChecked = false;
	
	$('input[type=checkbox][name=chkbox]').each(function () {
        if (this.checked) {
        	hasChecked = true;
         	return false;
        }
	});
	
	return hasChecked;
	//$("#btnBackup").prop( "disabled", disabled );
}


//查詢按鈕動作
function findData(from) {
	$('#queryFrom').val(from);
	$('input[name=checkAll]').prop('checked', false);
	
	if (from == 'MOBILE') {
		$('#collapseExample').collapse('hide');
	}
	
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
	        	   if(data.deviceName != null && data.deviceName.length > remarkShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
	        	      $(row).children('td').eq(3).attr('onclick','javascript:changeShowRemarks(this);');
	        	      $(row).children('td').eq(3).addClass('cursor_zoom_in');
	        	   }
	        	   $(row).children('td').eq(3).attr('content', data.deviceName);
	        	},
			"ajax" : {
				"url" : _ctx + '/version/getDeviceListData.json',
				"type" : 'POST',
				"data" : function ( d ) {
					if ($('#queryFrom').val() == 'WEB') {
						d.queryGroup = $("#queryGroup").val(),
						d.queryDevice = $("#queryDevice").val()
					
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryGroup = $("#queryGroup_mobile").val(),
						d.queryDevice = $("#queryDevice_mobile").val()
					}
					
					return d;
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				}
			},
			/*"order": [[2 , 'asc' ]],*/
			/*
			"initComplete": function(settings, json) {
            },
            */
			"drawCallback" : function(settings) {
				$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust();
				$("div.dataTables_length").parent().removeClass('col-sm-12');
				$("div.dataTables_length").parent().addClass('col-sm-6');
				$("div.dataTables_filter").parent().removeClass('col-sm-12');
				$("div.dataTables_filter").parent().addClass('col-sm-6');
				
				$("div.dataTables_info").parent().removeClass('col-sm-12');
				$("div.dataTables_info").parent().addClass('col-sm-6');
				$("div.dataTables_paginate").parent().removeClass('col-sm-12');
				$("div.dataTables_paginate").parent().addClass('col-sm-6');
				
				bindTrEvent();
			},
			"columns" : [
				{},{},
				{ "data" : "groupName" },
				{},
				{ "data" : "systemVersion" , "className" : "center" },
				{},
				{ "data" : "configType" },
				{}
			],
			"columnDefs" : [
				{
					"targets" : [0],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<input type="checkbox" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value='+row.deviceListId+'>';
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
					"targets" : [3],
					"className" : "left",
					"searchable": true,
					"orderable": true,
					"render": function (data, type, row, meta) {
								if (row.deviceName != null && row.deviceName.length > remarkShowLength) {
									 return getPartialRemarksHtml(row.deviceName); //內容長度超出設定，僅顯示部分內容
								} else {
									return row.deviceName; 						//未超出設定則全部顯示
								}
						   	}
				},
				{
					"targets" : [5],
					"className" : "left",
					"searchable": true,
					"orderable": true,
					"render" : function(data, type, row) {
								 var html = "";
								 if (row.versionId != null) {
									 html = '<a href="#" onclick="viewConfig(\''+row.versionId+'\')">'+row.configVersion+'</a>';
								 }
								 return html;
							 }
				},
				{
					"targets" : [7],
					"className" : "center",
					"searchable": false,
					"orderable": true,
					"render" : function(data, type, row) {
								 return row.backupTimeStr;
							 }
				}
			],
		});
	}
}