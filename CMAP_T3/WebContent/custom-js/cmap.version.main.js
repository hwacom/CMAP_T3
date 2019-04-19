/**
 * 
 */
var currentDiffPos = -1;
var diffPos = [];
var diffPos_offsetTopDeductNum = 66;
var remarkShowLength = 50;	//設定欄位顯示內容最大長度

$(document).ready(function() {
	initMenuStatus("toggleMenu_cm", "toggleMenu_cm_items", "cm_manage");
	
	//初始化設備選單
	changeDeviceMenu("queryDevice1", $("#queryGroup1").val());
    changeDeviceMenu("queryDevice2", $("#queryGroup2").val());
    
    //比對按鈕點擊事件
    $('#btnCompare').click(function (e) {
    	compareFile();
    });
    
    //刪除按鈕點擊事件
    $('#btnDelete').click(function (e) {
    	deleteData();
    });
    
    //[版本比對-Modal] >> 內容三區塊卷軸連動
    var combined = $("div[id$='compareModal_contentLeft']")
    				.add($("div[id$='compareModal_contentRight']"))
    				.add($("div[id$='compareModal_contentLineNum']"));
    
    	combined.on("scroll", function () {
        combined.scrollTop($(this).scrollTop());
    });
    
    //[版本比對-Modal] >> 卷軸滾動事件
    $('#compareModal_contentLineNum').scroll(function() {
    	var position = $('#compareModal_contentLineNum').scrollTop();
    	var offsetHeight = $('#compareModal_contentLineNum').prop("offsetHeight");
    	var height = $('#compareModal_contentLineNum').prop("scrollHeight");
    	
    	if (position == 0) {
    		currentDiffPos = -1;
    	} else if ((position+offsetHeight) == height) {
    		currentDiffPos = diffPos.length;
    	}
    });
    
  	//[版本比對-Modal] >> 開啟時將卷軸置頂
    $("#compareModal").on("shown.bs.modal", function () {
    	currentDiffPos = -1;
    	
    	setTimeout(function() {
    		$('#compareModal_contentLineNum').scrollTop(0);
    	}, 300);
    });
    
  	//[版本比對-Modal] >> 卷軸移動至最頂端
    $('#jumpToTop').click(function (e) {
    	$(".diffPos").css("background-color", ""); 
    	
        $('#compareModal_contentLineNum').animate({
            scrollTop: 0
        }, '500');
        
        currentDiffPos = -1;
        
        $('.diffPos').removeClass("compare-current-pos");
        
        changeCompareDesc(false);
    });
    
  	//[版本比對-Modal] >> 卷軸移動至前一個差異行數
    $('#jumpToPre').click(function (e) {
    	if (currentDiffPos <= 0) {
        	currentDiffPos = diffPos.length - 1;
        } else {
        	currentDiffPos -= 1;
        }
    	
    	console.log("diffPos[" + currentDiffPos + "] : " + $('.diffPos')[currentDiffPos].offsetTop);
        $('#compareModal_contentLineNum').animate({
            scrollTop: $('.diffPos')[currentDiffPos].offsetTop - diffPos_offsetTopDeductNum
        }, '500');
        
        
        $('.diffPos').removeClass("compare-current-pos");
        $('.diffPos').eq(currentDiffPos).addClass("compare-current-pos");
        
        changeCompareDesc(true);
    });
    
  	//[版本比對-Modal] >> 卷軸移動至下一個差異行數
    $('#jumpToNext').click(function (e) {
    	if ((currentDiffPos+1) >= diffPos.length) {
        	currentDiffPos = 0;
        } else {
        	currentDiffPos += 1;
        }
    	
    	console.log("diffPos[" + currentDiffPos + "] : " + $('.diffPos')[currentDiffPos].offsetTop);
        $('#compareModal_contentLineNum').animate({
            scrollTop: $('.diffPos')[currentDiffPos].offsetTop - diffPos_offsetTopDeductNum
        }, '500');
        
        $('.diffPos').removeClass("compare-current-pos");
        $('.diffPos').eq(currentDiffPos).addClass("compare-current-pos");
        
        changeCompareDesc(true);
    });
    
  	//[版本比對-Modal] >> 卷軸移動至最底部
    $('#jumpToBottom').click(function (e) {
    	$(".diffPos").css("background-color", ""); 
    	
    	var offsetHeight = $('#compareModal_contentLineNum').prop("offsetHeight");
    	var scrollHeight = $('#compareModal_contentLineNum').prop("scrollHeight");
    	
        $('#compareModal_contentLineNum').animate({
            scrollTop: scrollHeight - offsetHeight
        }, '500');
        
        $('.diffPos').removeClass("compare-current-pos");
        
        changeCompareDesc(false);
    });
    
    findData('WEB');
} );

function changeCompareDesc(showNow) {
	if (showNow) {
		$('#compareModal_summary').html("共 " + diffPos.length + " 個差異；第 " + (currentDiffPos+1) + " 個差異點");
	} else {
		$('#compareModal_summary').html("共 " + diffPos.length + " 個差異");
	}
}

//比對按鈕動作
function compareFile() {
	var checkedItem = $('input[name=chkbox]:checked');
	
	//比對只能勾選兩個項目
	if (checkedItem.length > 2) {
		alert("版本比對僅能勾選兩筆檔案進行，請重新選擇");
		
	} else {
		var checkedObjArray = new Array();
		var obj;
		for (var i=0; i<checkedItem.length; i++) {
			obj = new Object();
			obj.name = 'versionId';
			obj.value = checkedItem[i].value;
			checkedObjArray.push(obj);
		}
		
		$.ajax({
			url : _ctx + '/version/compare',
			data : JSON.stringify(checkedObjArray),
			headers: {
			    'Accept': 'application/json',
			    'Content-Type': 'application/json'
			},
			type : "POST",
			dataType : 'json',
			async: true,
			beforeSend : function() {
				showProcessing();
			},
			complete : function() {
				hideProcessing();
			},
			success : function(resp) {
				if (resp.code == '200') {
					
					$('#compareModal_contentLineNum').html(resp.data.versionLineNum);
					$('#compareModal_contentLeft').html(resp.data.contentLeft);
					$('#compareModal_contentRight').html(resp.data.contentRight);
					$('#viewModal_versionLeft').val(resp.data.versionLeft);
					$('#viewModal_versionRight').val(resp.data.versionRight);
					
					currentDiffPos = 0;
					
					if (resp.data.diffPos != "") {
						diffPos = resp.data.diffPos.split(",");
						$('#compareModal_summary').html("共 " + diffPos.length + " 個差異");
						
					} else {
						$('#compareModal_summary').html("無差異");
					}
					
					$('#compareModal').modal('show');
					
				} else {
					alert(resp.message);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				ajaxErrorHandler();
			}
		});
	}
}

//刪除按鈕動作
function deleteData() {
	var checkedItem = $('input[name=chkbox]:checked');
	
	if (checkedItem.length == 0) {
		alert("請選擇要刪除的項目");
		return;
	}
	
	confirm("確認刪除?", "deleteDataGO");
}

function deleteDataGO() {
	var checkedItem = $('input[name=chkbox]:checked');
	var checkedObjArray = new Array();
	var obj;
	for (var i=0; i<checkedItem.length; i++) {
		obj = new Object();
		obj.name = 'versionId';
		obj.value = checkedItem[i].value;
		checkedObjArray.push(obj);
	}
	
	$.ajax({
		url : _ctx + '/version/delete',
		data : JSON.stringify(checkedObjArray),
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
		},
		type : "POST",
		dataType : 'json',
		async: true,
		success : function(resp) {
			if (resp.code == '200') {
				clearDialog();
				alert(resp.message);
				
				findData($('#queryFrom').val());
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
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
			"pageLength"	: 10,
			"language" : {
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
	        "createdRow": function( row, data, dataIndex ) {
	        	   if(data.deviceName != null && data.deviceName.length > remarkShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
	        	      $(row).children('td').eq(3).attr('onclick','javascript:changeShowContent(this, '+remarkShowLength+');');
	        	      $(row).children('td').eq(3).addClass('cursor_zoom_in');
	        	   }
	        	   $(row).children('td').eq(3).attr('content', data.deviceName);
	        	},
			"ajax" : {
				"url" : _ctx + '/version/getVersionInfoData.json',
				"type" : 'POST',
				"data" : function ( d ) {
					if ($('#queryFrom').val() == 'WEB') {
						d.queryGroup1 = $("#queryGroup1").val(),
						d.queryGroup2 = $("#queryGroup2").val(),
						d.queryDevice1 = $("#queryDevice1").val(),
						d.queryDevice2 = $("#queryDevice2").val(),
						d.queryDateBegin1 = $("#queryExcuteDateBegin1").val(),
						d.queryDateEnd1 = $("#queryExcuteDateEnd1").val(),
						d.queryDateBegin2 = $("#queryExcuteDateBegin2").val(),
						d.queryDateEnd2 = $("#queryExcuteDateEnd2").val();
						d.queryConfigType = $("#queryConfigType").val();
						d.queryNewChkbox = $("#queryNewChkbox").prop("checked") ? true : false;
					
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryGroup1 = $("#queryGroup1_mobile").val(),
						d.queryGroup2 = $("#queryGroup2_mobile").val(),
						d.queryDevice1 = $("#queryDevice1_mobile").val(),
						d.queryDevice2 = $("#queryDevice2_mobile").val(),
						d.queryDateBegin1 = $("#queryExcuteDateBegin1_mobile").val(),
						d.queryDateEnd1 = $("#queryExcuteDateEnd1_mobile").val(),
						d.queryDateBegin2 = $("#queryExcuteDateBegin2_mobile").val(),
						d.queryDateEnd2 = $("#queryExcuteDateEnd2_mobile").val();
						d.queryConfigType = $("#queryConfigType_mobile").val();
						d.queryNewChkbox = $("#queryNewChkbox_mobile").prop("checked") ? true : false;
					}
					
					return d;
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				}
			},
			/*"order": [[6 , 'desc' ]],*/
			"initComplete": function(settings, json){
            },
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
			},
			"columns" : [
				{},{},
				{ "data" : "groupName" },
				{},
				{ "data" : "systemVersion" , "className" : "center"},
				{ "data" : "configType" },
				{},
				{}
			],
			"columnDefs" : [
				{
					"targets" : [0],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<input type="checkbox" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value='+row.versionId+'>';
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
									 return getPartialContentHtml(row.deviceName, remarkShowLength); //內容長度超出設定，僅顯示部分內容
								} else {
									return row.deviceName; 						//未超出設定則全部顯示
								}
						   	}
				},
				{
					"targets" : [6],
					"className" : "left",
					"searchable": true,
					"orderable": true,
					"render" : function(data, type, row) {
								 var html = '<a href="#" onclick="viewConfig(\''+row.versionId+'\')">'+row.configVersion+'</a>';
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