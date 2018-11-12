/**
 * 
 */
var resutTable;

$(document).ready(function() {
	changeDeviceMenu("queryDevice1", $("#queryGroup1").val());
	
	//查詢按鈕(Web)點擊事件
    $('#btnSearch_web').click(function (e) {
    	findData('WEB');
    });
  	
  	//查詢按鈕(Mobile)點擊事件
    $('#btnSearch_mobile').click(function (e) {
    	findData('MOBILE');
    });
});

function resetTrBgColor() {
	$('tbody > tr').removeClass('mySelected');
}

//查詢按鈕動作
function findData(from) {
	$('#queryFrom').val(from);
	$('input[name=checkAll]').prop('checked', false);
	
	if (from == 'MOBILE') {
		$('#collapseExample').collapse('hide');
	}
	
	if (typeof resutTable !== "undefined") {
		resutTable.clear().draw();
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
			"ajax" : {
				"url" : _ctx + '/version/getDeviceListData.json',
				"type" : 'POST',
				"data" : function ( d ) {
					d.maxCountByDevice = true;
					
					if ($('#queryFrom').val() == 'WEB') {
						d.queryGroup1 = $("#queryGroup1").val(),
						d.queryDevice1 = $("#queryDevice1").val()
					
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryGroup1 = $("#queryGroup1_mobile").val(),
						d.queryDevice1 = $("#queryDevice1_mobile").val()
					}
					
					return d;
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				}
			},
			"order": [[2 , 'asc' ]],
			/*
			"initComplete": function(settings, json){
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
				initCheckedItems();
			},
			"columns" : [
				{},{},
				{ "data" : "groupName" },
				{ "data" : "deviceName" },
				{ "data" : "systemVersion" , "className" : "center" }
			],
			"columnDefs" : [
				{
					"targets" : [0],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<input type="radio" id="radiobox" name="radiobox" onclick="resetTrBgColor();changeTrBgColor(this)" value='+row.versionId+'>';
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
				}
			],
		});
	}
}