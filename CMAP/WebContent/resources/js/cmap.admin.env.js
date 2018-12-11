/**
 * 
 */
const remarkShowLength = 20;	//設定欄位顯示內容最大長度
	
$(document).ready(function() {
	initMenuStatus("toggleMenu_admin", "toggleMenu_admin_items", "bk_env");
	
	$("#btnRefreshAll").click(function() {
		envAction('refreshAll');
	});
	
	$("#btnModify").click(function() {
		changeModifyView();
	});
	
	$("#btnDelete").click(function() {
		envAction('delete');
	});
	
	$("#btnModifySubmit").click(function() {
		envAction('save');
	});
	
	$("#btnModifyCancel").click(function() {
		findData($("#queryFrom").val());
	});
});

function initActionBar() {
	$("#modifyActionBar").hide();
	$("#defaultActionBar").show();
	$(".dataTable").find("thead").eq(0).find("tr > th").css( 'pointer-events', 'auto' );
	
	$("select[name=resutTable_length]").removeAttr('disabled'); //開放分頁筆數選單
	$("input[type=search]").removeAttr('disabled'); //開放內文搜尋輸入框
}

function toggleActionBar() {
	$("#modifyActionBar").toggle();
	$("#defaultActionBar").toggle();
}

function changeModifyView() {
	var checkedItem = $('input[name=chkbox]:checked');
	
	if (checkedItem.length == 0) {
		alert('請先勾選欲修改的項目');
		return;
	}
	
	for (var i=0; i<checkedItem.length; i++) {
		
		var hasInnerText = (document.getElementsByTagName("body")[0].innerText !== undefined) ? true : false;
		
		$('input[name=chkbox]:checked:eq('+i+')').attr('disabled','disabled'); //關閉列表勾選框
		$('input[name=chkbox]:checked:eq('+i+')').parents("tr").children().eq(2).html(
			//切換「備註」欄位為輸入框
			function() {
				var html = '<input type="text" name="modifySettingRemark" value="' + $(this).text() +'" class="form-control form-control-sm" style="min-width: 200px" />';
				return html;
			}
		);
		$('input[name=chkbox]:checked:eq('+i+')').parents("tr").children().eq(3).html(
				//切換「參數名稱」欄位為輸入框
				function() {
					var html = '<input type="text" name="modifySettingName" value="' + $(this).text() +'" class="form-control form-control-sm" style="min-width: 200px" readonly/>';
					return html;
				}
			);
		$('input[name=chkbox]:checked:eq('+i+')').parents("tr").children().eq(5).html(
			//切換「參數值(DB)」欄位為輸入框
			function() {
				var html = '<input type="text" name="modifySettingValue" value="' + $(this).text() +'" class="form-control form-control-sm" style="min-width: 200px" />';
				return html;
			}
		);
	}
	
	toggleActionBar(); //切換action bar按鈕
	$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust(); //重繪表頭寬度
	$(".dataTable").find("thead").eq(0).find("tr > th").removeClass("sorting sorting_asc sorting_desc"); //移除表頭排序箭頭圖示
	$(".dataTable").find("thead").eq(0).find("tr > th").css( 'pointer-events', 'none' ); //移除表頭排序功能
	$("select[name=resutTable_length]").attr('disabled','disabled'); //關閉分頁筆數選單
	$("input[type=search]").attr('disabled','disabled'); //關閉內文搜尋輸入框
	$("li[id^=resutTable_]").addClass('disabled'); //關閉右下角分頁按鈕
}

function envAction(action) {
	var obj = new Object();
	
	var settingIds = $("input[name='chkbox']:checked").map(function() {
     	return $(this).val();
     }).get();
	
	obj.settingIds = settingIds;
	
	if (action == "save") {
		var modifySettingName = $("input[name='modifySettingName']").map(function() {
						        	return $(this).val();
						        }).get();
		var modifySettingValue = $("input[name='modifySettingValue']").map(function() {
						         	return $(this).val();
						         }).get();
		var modifySettingRemark = $("input[name='modifySettingRemark']").map(function() {
						         	 return $(this).val();
						          }).get();
		
		obj.modifySettingName = modifySettingName;
		obj.modifySettingValue = modifySettingValue;
		obj.modifySettingRemark = modifySettingRemark;
		
	} else if (action == "delete") {
		var checkedItem = $('input[name=chkbox]:checked');
		
		if (checkedItem.length == 0) {
			alert('請先勾選欲刪除的項目');
			return;
		}
	}
	
	$.ajax({
		url : _ctx + '/admin/env/'+action,
		data : JSON.stringify(obj),
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
				findData($("#queryFrom").val());
				
			} else {
				alert('envAction > success > else :: resp.code: '+resp.code);
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
	
	initActionBar();
	
	if (typeof resutTable !== "undefined") {
		//resutTable.clear().draw(); server-side is enabled.
		resutTable.ajax.reload();
		
	} else {
		$(".myTableSection").show();
		
		resutTable = $("#resutTable").DataTable(
		{
			"autoWidth" 	: true,
			"paging" 		: false,
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
	        	   if(data.settingRemark != null && data.settingRemark.length > remarkShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
	        	      $(row).children('td').eq(2).attr('onclick','javascript:changeShowContent(this, '+remarkShowLength+');');
	        	      $(row).children('td').eq(2).addClass('cursor_zoom_in');
	        	   }
	        	   $(row).children('td').eq(2).attr('content', data.settingRemark);
	        	},
			"ajax" : {
				"url" : _ctx + "/admin/env/getEnvConfig.json",
				"type" : "POST",
				"data" : function ( d ) {},
				"dataSrc" : function (json) {
					$("#diffMsg").text(json.msg);
					return json.data;
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				}
			},
			"order": [[3 , "asc" ]],
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
				{ "data" : "settingName" },
				{},
				{ "data" : "settingValue" },
				{ "data" : "_settingValue" },
				{ "data" : "createTimeStr" },
				{ "data" : "createBy" },
				{ "data" : "updateTimeStr" },
				{ "data" : "updateBy" },
			],
			"columnDefs" : [
				{
					"targets" : [0],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<input type="checkbox" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value="'+row.settingId+'">';
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
					"searchable": true,
					"orderable": true,
					"render": function (data, type, row, meta) {
						if (row.settingRemark != null && row.settingRemark.length > remarkShowLength) {
							 return getPartialContentHtml(row.settingRemark, remarkShowLength); 	//內容長度超出設定，僅顯示部分內容
						} else {
							return row.settingRemark; 							//未超出設定則全部顯示
						}
				   	}
				},
				{
					"targets" : [4],
					"className" : "center",
					"searchable": true,
					"orderable": true,
					"render": function (data, type, row, meta) {
								var html = row.same;
								if (!row.same) {
									html = '<font color="red">'+row.same+'</font>';
								}
						       	return html;
						   	}
				}
			],
		});
	}
}