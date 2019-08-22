/**
 * 
 */
var scriptShowMaxLine = 2;

$(document).ready(function() {
	initMenuStatus("toggleMenu_cm", "toggleMenu_cm_items", "cm_script");
			
	findData('WEB');
});

//查看腳本內容
function showScriptContent(scriptInfoId, type) {
	var obj = new Object();
	obj.scriptInfoId = scriptInfoId;
	obj.type = type;
	
	$.ajax({
		url : _ctx + '/script/view',
		data : JSON.stringify(obj),
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
		},
		type : "POST",
		async: true,
		/*
		beforeSend : function() {
			showProcessing();
		},
		complete : function() {
			hideProcessing();
		},
		*/
		success : function(resp) {
			if (resp.code == '200') {
				$('#viewScriptModal_scriptName').val(resp.data.script);
				$('#viewScriptModal_scriptContent').html("<pre>" + resp.data.content + "</pre>");
				
				$('#viewScriptModal').modal('show');
				
			} else {
				alert(resp.message);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
}

function getPartialContent(content) {
	var retVal = '';
	if (content != null && content != '') {
		var line = content.split('<br>');
		
		if (line.length > scriptShowMaxLine) {
			for (var i=0; i<scriptShowMaxLine; i++) {
				retVal += line[i];
				
				if (i != scriptShowMaxLine - 1) {
					retVal += '<br>';
				}
			}
			
			retVal += '&nbsp;&nbsp;<a href="javascript:void(0);" >...(顯示)</a>';
			
		} else {
			retVal = content;
		}
	}
	
	return retVal;
}

//查詢按鈕動作
function findData(from) {
	$('#queryFrom').val(from);
	$('input[name=checkAll]').prop('checked', false);
	
	if (from == 'MOBILE') {
		$('#collapseExample').collapse('hide');
	}
	
	if (typeof resultTable !== "undefined") {
		resultTable.clear().draw();
		resultTable.ajax.reload();
		
	} else {
		$(".myTableSection").show();
		
		resultTable = $('#resultTable').DataTable(
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
			"pageLength"	: 10,
			"language" : {
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
			"ajax" : {
				"url" : _ctx + '/script/getScriptData.json',
				"type" : 'POST',
				"data" : function ( d ) {
					if ($('#queryFrom').val() == 'WEB') {
						d.queryScriptTypeCode = $("#queryScriptType").val();
					
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryScriptTypeCode = $("#queryScriptType_mobile").val();
					}
					
					return d;
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				}
			},
			"order": [[3 , 'asc' ]],
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
				
				initCheckedItems();
				bindTrEvent();
			},
			"createdRow": function( row, data, dataIndex ) {
				if (data.enableModify == false) {
					$(row).children('td').eq(0).addClass('hide');
				}
				
				if(data.actionScript != null) { //加上onclick事件(查看script完整內容)
					$(row).children('td').eq(5).attr('onclick','javascript:showScriptContent(\''+data.scriptInfoId+'\', \'A\');');
					$(row).children('td').eq(5).addClass('cursor_zoom_in');
				}
				/*
				if(data.checkScript != null) { //加上onclick事件(查看script完整內容)
					$(row).children('td').eq(7).attr('onclick','javascript:showScriptContent(\''+data.scriptInfoId+'\', \'C\');');
					$(row).children('td').eq(7).addClass('cursor_zoom_in');
				}
				*/
        	},
			"columns" : [
				{},{},
				{ "data" : "scriptName" },
				{ "data" : "scriptTypeName" , "className" : "center" },
				{ "data" : "systemVersion" , "className" : "center" },
				{},
				{ "data" : "actionScriptRemark"},
				/*
				{},
				{ "data" : "checkScriptRemark"},
				*/
				{ "data" : "createTimeStr" , "className" : "center" },
				{ "data" : "updateTimeStr" , "className" : "center" }
			],
			"columnDefs" : [
				{
					"targets" : [0],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<input type="radio" id="radiobox" name="radiobox" onclick="resetTrBgColor();changeTrBgColor(this)" value='+row.scriptTypeId+'>';
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
					"targets" : [5],
					"className" : "left",
					"searchable": true,
					"orderable": true,
					"render": function (data, type, row, meta) {
								return getPartialContent(row.actionScript); //當內容行數超出設定，僅顯示部分內容
						   	}
				},
				/*
				{
					"targets" : [7],
					"className" : "left",
					"searchable": true,
					"orderable": true,
					"render": function (data, type, row, meta) {
								return getPartialContent(row.checkScript); //當內容行數超出設定，僅顯示部分內容
						   	}
				}
				*/
			],
		});
	}
}