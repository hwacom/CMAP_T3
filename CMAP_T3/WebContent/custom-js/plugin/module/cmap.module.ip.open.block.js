/**
 * 
 */
var resultTable_blockedIpRecord;	//DataTable
var blockReasonShowLength = 30;
var openReasonShowLength = 30;
var _navAndMenuAndFooterHeight = 0;
var _deductHeight = 0;
var blockedIpTableHeight;

$(document).ready(function() {
	$("#btnIpOpen").click(function(e) {
		doConfirmIpOpenByBtn();
	});
});

function checkboxOnChangeEvent() {
	// 判斷CheckBox有無勾選，決定解鎖按鈕是否可按
	$('input[name=chkbox]').change(function (e) {
		if ($('input[name=chkbox]:checked').length > 0) {
			$('#btnIpOpen').attr('disabled', false);
		} else {
			$('#btnIpOpen').attr('disabled', true);
		}
	});
}

/**********************************************************************************************************
 *** 計算BlockedIpList區塊DataTable可呈顯的高度
 **********************************************************************************************************/
function calBlockedIpSectionHeight() {
	_navAndMenuAndFooterHeight = 0;
	_deductHeight = 0;
	
	//計算dataTable區塊高度，以window高度扣除navbar、footer和mobile版選單高度
	if ($('.mobile-menu').css('display') != 'none' && $('.mobile-menu').css('display') != undefined) {
		_navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight() + $('.mobile-menu').outerHeight();
	} else {
		_navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight();
	}
	
	if ($('.search-bar-large').css('display') != 'none' && $('.search-bar-large').css('display') != undefined) {
		_navAndMenuAndFooterHeight += $('.search-bar-large').outerHeight();
		_deductHeight = 110;
	} else {
		_deductHeight = 210;
	}
	
	if ($('.mainTable').css('display') != 'none' && $('.mainTable').css('display') != undefined) {
		_deductHeight += $('.mainTable').outerHeight();
	}
	
	if ($('#divBlockedTitle').css('display') != 'none' && $('#divBlockedTitle').css('display') != undefined) {
		_deductHeight += $('#divBlockedTitle').outerHeight();
	}
	
	blockedIpTableHeight = Math.round(window.innerHeight-_navAndMenuAndFooterHeight-_deductHeight);
	
	//避免手機裝置橫向狀態下高度縮太小無法閱讀資料，設定最小高度限制為165px
	blockedIpTableHeight = blockedIpTableHeight < 165 ? 165 : blockedIpTableHeight;
}

function doConfirmIpOpenByBtn() {
	var check = confirm("按下確認後將立即執行IP開通作業，請再次確認是否執行?");
	
	if (check) {
		doIpOpenByBtn();
	}
}

/**
 * 執行IP開通 by 「IP開通/封鎖」功能中的「解鎖」按鈕
 */
function doIpOpenByBtn() {
	var listId = new Array();
	
	var checkedItem = $('input[name=chkbox]:checked');
	for (var i=0; i<checkedItem.length; i++) {
		listId.push(checkedItem[i].value);
	}
	
	$.ajax({
		url : _ctx + '/delivery/doIpOpenByBtn.json',
		data : {
			"listId" : listId
		},
		type : "POST",
		async: true,
		beforeSend : function() {
			showProcessing();
		},
		complete : function() {
			hideProcessing();
		},
		success : function(resp) {
			if (resp.code == '200') {
				alert(resp.message);
				
				setTimeout(function() {
					$('#stepModal').modal('hide');
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

//查詢按鈕動作
function findBlockedIpRecordData() {
	calBlockedIpSectionHeight();

	if (typeof resultTable_blockedIpRecord !== "undefined") {
		//resultTable.clear().draw(); server-side is enabled.
		resultTable_blockedIpRecord.ajax.reload();
		
	} else {
		$("#divBlockedIpRecord").show();
		
		resultTable_blockedIpRecord = $('#resultTable_blockedIpRecord').DataTable(
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
			"scrollY"		: blockedIpTableHeight,
			"scrollCollapse": true,
			"language" : {
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
	        "createdRow": function( row, data, dataIndex ) {
	        	   if(data.blockReason != null && data.blockReason.length > blockReasonShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
	        	      $(row).children('td').eq(5).attr('onclick','javascript:showFullScript($(this));');
	        	      $(row).children('td').eq(5).addClass('cursor_zoom_in');
	        	   }
	        	   $(row).children('td').eq(5).attr('content', data.blockReason);
	        	},
			"ajax" : {
				"url" : _ctx + '/delivery/getBlockedIpData.json',
				"type" : 'POST',
				"data" : function ( d ) {
					if ($('#queryFrom').val() == 'WEB') {
						d.queryGroupId = $("#queryGroup").val()
						
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryGroupId = $("#queryGroup_mobile").val()
					}
					return d;
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				}
			},
			"order" : [[3 , 'desc' ]],
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
				
				bindTrEventOnlyCheckbox();
				checkboxOnChangeEvent();
				
				var pathname = window.location.pathname;
				var lastPath = pathname.substring(pathname.lastIndexOf('/'), pathname.length);
				if (lastPath === "/ipOpenBlock") {
					$('[data-field="status"]').hide();
					$('[data-field="openTime"]').hide();
					$('[data-field="openReason"]').hide();
					$('[data-field="openBy"]').hide();
				}
				
				$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust();
			},
			"rowCallback": function( row, data ) {
				$('td:eq(1)', row).attr('data-field', 'seq');
				$('td:eq(2)', row).attr('data-field', 'groupName');
				$('td:eq(3)', row).attr('data-field', 'ipAddress');
				$('td:eq(4)', row).attr('data-field', 'status');
				$('td:eq(5)', row).attr('data-field', 'blockTime');
				$('td:eq(6)', row).attr('data-field', 'blockReason');
				$('td:eq(7)', row).attr('data-field', 'blockBy');
				$('td:eq(8)', row).attr('data-field', 'openTime');
				$('td:eq(9)', row).attr('data-field', 'openReason');
				$('td:eq(10)', row).attr('data-field', 'openBy');
			},
			"columns" : [
				{},{},
				{ "data" : "groupName" , "className" : "left" },
				{ "data" : "ipAddress" , "className" : "left" },
				{ "data" : "statusFlag" , "className" : "center" },
				{ "data" : "blockTimeStr" , "className" : "center" },
				{},
				{ "data" : "blockBy" , "className" : "center" },
				{ "data" : "openTimeStr" , "className" : "center" },
				{},
				{ "data" : "openBy" , "className" : "center" },
			],
			"columnDefs" : [ 
				{
					"targets" : [0],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<input type="checkbox" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value='+row.listId+'>';
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
					"targets" : [6],
					"className" : "left",
					"searchable": true,
					"orderable": false,
					"render" : function(data, type, row) {
						if (row.blockReason != null && row.blockReason.length > blockReasonShowLength) {
							 return getPartialContentHtml(row.blockReason, blockReasonShowLength); 	//內容長度超出設定，僅顯示部分內容
						} else {
							return row.blockReason; 							//未超出設定則全部顯示
						}
					}
				}
				,
				{
					"targets" : [9],
					"className" : "left",
					"searchable": true,
					"orderable": false,
					"render" : function(data, type, row) {
						if (row.openReason != null && row.openReason.length > openReasonShowLength) {
							 return getPartialContentHtml(row.openReason, openReasonShowLength); 	//內容長度超出設定，僅顯示部分內容
						} else {
							return row.openReason; 							//未超出設定則全部顯示
						}
					}
				}
			],
		});
	}
}