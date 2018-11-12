/**
 * 
 */
const scriptShowLength = 20;	//設定欄位顯示內容最大長度
var STEP_NUM = 1;

$(document).ready(function() {
	$("#viewScriptModal").on("shown.bs.modal", function () {
    	setTimeout(function() {
    		$('#viewScriptModal_scriptContent').scrollTop(0);
    	}, 30);
    });
	
	$("#btnDelivery").click(function(e) {
		showDeliveryPanel();
	});
	
	$("#btnStepGoPrev").click(function(e) {
		goStep(-1);
	});
	
	$("#btnStepGoNext").click(function(e) {
		goStep(1);
	});
	
	$("#stepModal_searchDevice").keydown(function(e) {
		$("#stepModal_searchWaiting").show();
	});
	
	$("#stepModal_searchDevice").keyup(_.debounce(function(e) {
		searchDevice($(this).val());
	}, 500));
});

function showDeliveryPanel() {
	if ($(':radio:checked').length == 0) {
		alert("請先選擇腳本!!");
		return;
	}
	
	var scriptInfoId = $(':radio:checked').val();
	
	$.ajax({
		url : _ctx + '/delivery/getScriptInfo.json',
		data : {
			"scriptInfoId" : scriptInfoId
		},
		type : "POST",
		dataType : 'json',
		async: false,
		success : function(resp) {
			if (resp.code == '200') {
				
				window.sessionStorage.setItem(_DELIVERY_VAR_KEY_, resp.data.actionScriptVariable);
				window.sessionStorage.setItem(_DELIVERY_DEVICE_MENU_JSON_STR_, resp.data.groupDeviceMenuJsonStr);
				
				initStepPanel(1);
				
				STEP_NUM = 1;
				initStepSection();
				initStepBtn();
				initStepImg();
				initStepModalInput();
				
				/* **************************************************************************
				 * 派送Modal開啟 >> 紀錄腳本ID、代碼、名稱
				 * **************************************************************************/
				window.sessionStorage.setItem(_DELIVERY_SCRIPT_INFO_ID_, scriptInfoId);
				window.sessionStorage.setItem(_DELIVERY_SCRIPT_CODE_, resp.data.scriptCode);
				window.sessionStorage.setItem(_DELIVERY_SCRIPT_NAME_, resp.data.scriptName);
				window.sessionStorage.setItem(_DELIVERY_VAR_KEY_, resp.data.actionScriptVariable);
				
				$('#stepModal').modal({
					backdrop : 'static'
				});
				
			} else {
				alert(resp.message);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
}

function searchDevice(searchTxt) {
	$.ajax({
		url : _ctx + '/base/getGroupDeviceMenu.json',
		data : {
			"searchTxt" : searchTxt
		},
		type : "POST",
		dataType : 'json',
		async: true,
		beforeSend : function() {
			
		},
		complete : function() {
			$("#stepModal_searchWaiting").hide();
		},
		success : function(resp) {
			if (resp.code == '200') {
				
				$("#stepModal_chooseDevice option").remove();
				
				var groupName = "";
				var obj = $.parseJSON(resp.data.groupDeviceMenu);
				$.each(obj, function(key, value) {
					if (key.startsWith("GROUP")) {
						groupName = value;
						$("#stepModal_chooseDevice").append($("<option></option>").attr("class", "multi-option-topic")
																				  .attr("value", "")
																				  .attr("disabled", "disabled")
																				  .text(value));
						
					} else {
						var deviceKey = key.replace("DEVICE_", "");
						$("#stepModal_chooseDevice").append($("<option></option>").attr("class", "multi-option-item")
																				  .attr("value", deviceKey)
																				  .attr("data-group-name", groupName)
																				  .text(value));
					}
				});
				
			} else {
				alert(resp.message);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
}

function goStep(num) {
	var nextStep = parseInt(STEP_NUM) + parseInt(num);
	var nowStep = parseInt(STEP_NUM);
	
	var pass = false;
	if (parseInt(nextStep) > parseInt(nowStep)) {
		var success = checkStepRequirement(nowStep);
		
		if (!success) {
			return;
			
		} else {
			initStepPanel(nextStep);
		}
	}
	
	$("#step" + nextStep + "_section").show();
	$("#step" + nowStep + "_section").hide();
	
	STEP_NUM = parseInt(STEP_NUM) + parseInt(num);
	initStepBtn();
	initStepImg();
}

function checkStepRequirement(nowStep) {
	switch (nowStep) {
		case 1:
			//步驟1:檢核是否有選取設備
			return checkDeviceChoose();
			break;
			
		case 2:
			//步驟2:檢核必要變數值是否有填寫
			return checkVariable();
			break;
	}
}

function initStepPanel(nextStep) {
	switch (nextStep) {
		case 1:
			initStep1Panel();
			break;
		case 2:
			initStep2Panel();
			break;
		case 3:
			initStep3Panel();
			break;
	}
}

function initStep1Panel() {
	var variables = window.sessionStorage.getItem(_DELIVERY_VAR_KEY_);
	var menuJsonStr = window.sessionStorage.getItem(_DELIVERY_DEVICE_MENU_JSON_STR_);
	
	$('#stepModal_variable_show').text(variables);
	$("#stepModal_chooseDevice option").remove();
	
	var groupName = "";
	var obj = $.parseJSON(menuJsonStr);
	$.each(obj, function(key, value) {
		if (key.startsWith("GROUP")) {
			groupName = value;
			$("#stepModal_chooseDevice").append($("<option></option>").attr("class", "multi-option-topic")
																	  .attr("value", "")
																	  .attr("disabled", "disabled")
																	  .text(value));
			
		} else {
			var deviceKey = key.replace("DEVICE_", "");
			$("#stepModal_chooseDevice").append($("<option></option>").attr("class", "multi-option-item")
																	  .attr("value", deviceKey)
																	  .attr("data-group-name", groupName)
																	  .text(value));
		}
	});
}

function initStep2Panel() {
	var groupArray = JSON.parse(window.sessionStorage.getItem(_DELIVERY_DEVICE_GROUP_NAME_));
	var deviceArray = JSON.parse(window.sessionStorage.getItem(_DELIVERY_DEVICE_NAME_));
	
	if (groupArray.length != 0) {
		var count = groupArray.length;
		
		$("#step2_target_section").html("");
		
		for (var i=0; i<count; i++) {
			var seq = parseInt(i) + 1;
			var groupName = (i < groupArray.length) ? groupArray[i] : "N/A";
			var deviceName = (i < deviceArray.length) ? deviceArray[i] : "N/A";
			
			/* [Example]:
			 * <div class="row-group-dash">
	      	       <div class="form-group row">
		        	<label for="stepModal_group1" class="col-md-2 col-sm-2 col-form-label">群組1 :</label>
		        	<input type="text" class="form-control form-control-sm col-md-4 col-sm-4" id="stepModal_group1" value="群組1" readonly="readonly">
		        	<label for="stepModal_device1" class="col-md-2 col-sm-2 col-form-label">設備1 :</label>
		        	<input type="text" class="form-control form-control-sm col-md-4 col-sm-4" id="stepModal_device1" value="設備1" readonly="readonly">
		          </div>
		       </div>
			 */
			var target_el = $("<div></div>").attr("class", "row-group-dash")
					            .append(
					            	$("<div></div>").attr("class", "form-group row")
					            		.append(
							            	[$("<label></label>").attr("for", "stepModel_group"+seq).attr("class", "col-md-1 col-sm-2 col-form-label").text("群組"+seq+" :")],
							            	[$("<input>").attr("type", "text").attr("class", "form-control form-control-sm col-md-3 col-sm-4").attr("id", "stepModal_group"+seq).attr("value", groupName).attr("readonly", "readonly")],
							            	[$("<label></label>").attr("for", "stepModal_device"+seq).attr("class", "col-md-1 col-sm-2 col-form-label").text("設備"+seq+" :")],
							            	[$("<input>").attr("type", "text").attr("class", "form-control form-control-sm col-md-7 col-sm-4").attr("id", "stepModal_device"+seq).attr("value", deviceName).attr("readonly", "readonly")]
							            )
					            );
			
			$("#step2_target_section").append(target_el);
		}
	}
	
	var varKeys = window.sessionStorage.getItem(_DELIVERY_VAR_KEY_);
	console.log("varKeys: "+varKeys);
	if (varKeys != null && varKeys.trim().length != 0) {
		var varArray = varKeys.split(",");
		
		var count = varArray.length;
		
		$("#step2_variable_section").html("");
		
		/*
		 * <div class="form-group row">
	       	 <label for="stepModal_enterVarRemark" class="col-md-12 col-sm-2 col-form-label red bold">請輸入以下變數 :</label>
	       </div>
		 */
		var variable_el = $("<div></div>").attr("class", "form-group row")
								.append($("<label></label>").attr("for", "stepModal_enterVarRemark").attr("class", "col-md-12 col-sm-2 col-form-label red bold").text("請輸入以下變數 :"));
		
		$("#step2_variable_section").append(variable_el);

		for (var i=0; i<count; i++) {
			var varKey = varArray[i].trim();
			
			/* [Example]:
			 * <div class="form-group row">
			     <label for="stepModal_var_1" class="col-md-2 col-sm-2 col-form-label blue bold">vlan_id :</label>
			     <input type="text" class="form-control form-control-sm col-md-10 col-sm-12" id="stepModal_var_1" value="" name="stepModalVariables">
			   </div>
			 */
			variable_el = $("<div></div>").attr("class", "form-group row")
								.append(
									[$("<label></label>").attr("for", "stepModal_var_"+seq).attr("class", "col-md-2 col-sm-2 col-form-label blue bold").text(varKey+" :")],
									[$("<input>").attr("type", "text").attr("class", "form-control form-control-sm col-md-10 col-sm-12").attr("id", "stepModal_var_"+seq).attr("name", "stepModalVariables")]
								);
			
			$("#step2_variable_section").append(variable_el);
		}
	}
}

function initStep3Panel() {
	
}

function checkDeviceChoose() {
	var selectedObj = $("#stepModal_chooseDevice option:selected");
	
	if (selectedObj.length == 0) {
		alert("請選擇設備");
		return false;
		
	} else {
		var deviceId = [];
		var groupName = [];
		var deviceName = [];
		
		$.each(selectedObj, function(key, option) {
			deviceId.push($(option).val());
			deviceName.push($(option).text());
			groupName.push($(option).data('group-name'));
		});
		
		/* **************************************************************************
		 * 派送Step 1. >> 紀錄目標設備ID
		 * **************************************************************************/
		window.sessionStorage.setItem(_DELIVERY_DEVICE_ID_, JSON.stringify(deviceId));
		window.sessionStorage.setItem(_DELIVERY_DEVICE_NAME_, JSON.stringify(deviceName));
		window.sessionStorage.setItem(_DELIVERY_DEVICE_GROUP_NAME_, JSON.stringify(groupName));
		
		return true;
	}
}

function checkVariable() {
	var variables = $("input[name=stepModalVariables]");
	var success = true;
	
	if (variables.length > 0) {
		$.each(variables, function(key, input) {
			if (input.value.trim().length == 0) {
				alert("請輸入變數值");
				success = false
				return false;
			}
		});
	}
	
	return success;
}

function initStepImg() {
	var idx = parseInt(STEP_NUM) - 1;
	$(".step-img").removeClass('step-current');
	$(".step-img").eq(idx).addClass('step-current');
}

function initStepSection() {
	$("#step1_section").show();
	$("#step2_section").hide();
	$("#step3_section").hide();
}

function initStepModalInput() {
	$("#stepModal_searchDevice").val("");
	$("#stepModal_reason").val("");
}

function initStepBtn() {
	switch (STEP_NUM) {
		case 1:
			$("#btnStepGoPrev").hide();
			$("#btnStepGoNext").show();
			$("#btnStepGoFire").hide();
			break;
			
		case 2:
			$("#btnStepGoPrev").show();
			$("#btnStepGoNext").show();
			$("#btnStepGoFire").hide();
			break;
			
		case 3:
			$("#btnStepGoPrev").show();
			$("#btnStepGoNext").hide();
			$("#btnStepGoFire").show();
			break;
	}
}

function showFullScript(jObj) {
	var scriptName = jObj.parent().find('td').eq(2).text();
	var scriptContent = jObj.attr('content');
	
	$('#viewScriptModal_scriptName').val(scriptName);
	$('#viewScriptModal_scriptContent').html('<pre>'+scriptContent+'</pre>');
	$('#viewScriptModal').modal('show');
}

//查詢按鈕動作
function findData(from) {
	
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
	        	   if(data.actionScript != null && data.actionScript.length > scriptShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
	        	      $(row).children('td').eq(5).attr('onclick','javascript:showFullScript($(this));');
	        	      $(row).children('td').eq(5).addClass('cursor_zoom_in');
	        	   }
	        	   $(row).children('td').eq(5).attr('content', data.actionScript);
	        	   
	        	   if(data.checkScript != null && data.checkScript.length > scriptShowLength) { //當內容長度超出設定值，加上onclick事件(切換顯示部分or全部)
	        	      $(row).children('td').eq(7).attr('onclick','javascript:showFullScript($(this));');
	        	      $(row).children('td').eq(7).addClass('cursor_zoom_in');
	        	   }
	        	   $(row).children('td').eq(7).attr('content', data.checkScript);
	        	},
			"ajax" : {
				"url" : _ctx + '/delivery/getScriptListData.json',
				"type" : 'POST',
				"data" : function ( d ) {
					if ($('#queryFrom').val() == 'WEB') {
						d.queryScriptTypeCode = $("#queryScriptTypeCode").val();
					
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryScriptTypeCode = $("#queryScriptTypeCode_mobile").val();
					}
					
					return d;
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				}
			},
			"order" : [[2 , 'asc' ]],
			"pageLength" : 25,
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
			},
			"columns" : [
				{},{},
				{ "data" : "scriptName" },
				{ "data" : "scriptTypeName" },
				{ "data" : "systemVersion" , "className" : "center"},
				{},
				{ "data" : "actionScriptRemark" , "orderable" : false },
				{},
				{ "data" : "checkScriptRemark" , "orderable" : false }
			],
			"columnDefs" : [ 
				{
					"targets" : [0],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render" : function(data, type, row) {
								 var html = '<input type="radio" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value='+row.scriptInfoId+'>';
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
					"orderable": false,
					"render": function (data, type, row, meta) {
						if (row.actionScript != null && row.actionScript.length > scriptShowLength) {
							 return getPartialContentHtml(row.actionScript, scriptShowLength); 	//內容長度超出設定，僅顯示部分內容
						} else {
							return row.actionScript; 							//未超出設定則全部顯示
						}
					}
				},
				{
					"targets" : [7],
					"className" : "left",
					"searchable": true,
					"orderable": false,
					"render" : function(data, type, row) {
						if (row.checkScript != null && row.checkScript.length > scriptShowLength) {
							 return getPartialContentHtml(row.checkScript, scriptShowLength); 	//內容長度超出設定，僅顯示部分內容
						} else {
							return row.checkScript; 							//未超出設定則全部顯示
						}
					}
				}
			],
		});
	}
	
}