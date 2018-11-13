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
	
	window.sessionStorage.clear();
	
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
				/* **************************************************************************
				 * 派送Modal開啟前 >> 紀錄群組設備選單內容、腳本變數Key
				 * **************************************************************************/
				window.sessionStorage.setItem(_DELIVERY_DEVICE_MENU_JSON_STR_, resp.data.groupDeviceMenuJsonStr);
				window.sessionStorage.setItem(_DELIVERY_VAR_KEY_, resp.data.actionScriptVariable);
				
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
				
				$('#stepModal').modal({
					backdrop : 'static'
				});
				
			} else {
				alert(resp.message);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		},
		complete : function() {
			console.log("_DELIVERY_SCRIPT_INFO_ID_ : " + window.sessionStorage.getItem(_DELIVERY_SCRIPT_INFO_ID_));
			console.log("_DELIVERY_SCRIPT_CODE_ : " + window.sessionStorage.getItem(_DELIVERY_SCRIPT_CODE_));
			console.log("_DELIVERY_SCRIPT_NAME_ : " + window.sessionStorage.getItem(_DELIVERY_SCRIPT_NAME_));
			console.log("_DELIVERY_DEVICE_MENU_JSON_STR_ : " + window.sessionStorage.getItem(_DELIVERY_DEVICE_MENU_JSON_STR_));
			console.log("_DELIVERY_DEVICE_ID_ : " + window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_));
			console.log("_DELIVERY_DEVICE_GROUP_NAME_ : " + window.sessionStorage.getItem(_DELIVERY_DEVICE_GROUP_NAME_));
			console.log("_DELIVERY_DEVICE_NAME_ : " + window.sessionStorage.getItem(_DELIVERY_DEVICE_NAME_));
			console.log("_DELIVERY_REASON_ : " + window.sessionStorage.getItem(_DELIVERY_REASON_));
			console.log("_DELIVERY_VAR_KEY_ : " + window.sessionStorage.getItem(_DELIVERY_VAR_KEY_));
			console.log("_DELIVERY_VAR_VALUE_ : " + window.sessionStorage.getItem(_DELIVERY_VAR_VALUE_));
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
	
	console.log("_DELIVERY_SCRIPT_INFO_ID_ : " + window.sessionStorage.getItem(_DELIVERY_SCRIPT_INFO_ID_));
	console.log("_DELIVERY_SCRIPT_CODE_ : " + window.sessionStorage.getItem(_DELIVERY_SCRIPT_CODE_));
	console.log("_DELIVERY_SCRIPT_NAME_ : " + window.sessionStorage.getItem(_DELIVERY_SCRIPT_NAME_));
	console.log("_DELIVERY_DEVICE_MENU_JSON_STR_ : " + window.sessionStorage.getItem(_DELIVERY_DEVICE_MENU_JSON_STR_));
	console.log("_DELIVERY_DEVICE_ID_ : " + window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_));
	console.log("_DELIVERY_DEVICE_GROUP_NAME_ : " + window.sessionStorage.getItem(_DELIVERY_DEVICE_GROUP_NAME_));
	console.log("_DELIVERY_DEVICE_NAME_ : " + window.sessionStorage.getItem(_DELIVERY_DEVICE_NAME_));
	console.log("_DELIVERY_REASON_ : " + window.sessionStorage.getItem(_DELIVERY_REASON_));
	console.log("_DELIVERY_VAR_KEY_ : " + window.sessionStorage.getItem(_DELIVERY_VAR_KEY_));
	console.log("_DELIVERY_VAR_VALUE_ : " + window.sessionStorage.getItem(_DELIVERY_VAR_VALUE_));
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
			
		case 3:
			//步驟3:檢核必要參數內容是否有缺
			return checkDeliveryParameters();
			break;
	}
}

function initStepPanel(nextStep) {
	switch (nextStep) {
		case 1:
			initStep1Panel();	//Step 1.選擇設備
			break;
		case 2:
			initStep2Panel();	//Step 2.輸入變數
			break;
		case 3:
			initStep3Panel();	//Step 3.預覽內容
			break;
		case 4:
			doDelivery();		//Step 4.確認派送
			break;
	}
}

function initStep1Panel() {
	var variables = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_VAR_KEY_));
	var menuJsonStr = window.sessionStorage.getItem(_DELIVERY_DEVICE_MENU_JSON_STR_);
	
	console.log(variables === '');
	if (variables === '') {
		$("#stepModal_variable_description").text("此腳本不需輸入變數");
		$("#stepModal_variable_description").removeClass("red bold");
		
	} else {
		$("#stepModal_variable_description").text("※ 此腳本需輸入變數值 : ");
		$("#stepModal_variable_description").addClass("red bold");
		$('#stepModal_variable_show').text(variables);
	}
	
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
		
		$("#step2_target_section").empty();
		
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
	var varKeyArray = (varKeys != null) ? $.parseJSON(varKeys) : null;
	
	if (varKeyArray != null && varKeyArray.length != 0) {
		
		var count = varKeyArray.length;
		
		$("#step2_variable_section").empty();
		
		/*
		 * <div class="form-group row">
	       	 <label for="stepModal_enterVarRemark" class="col-md-12 col-sm-2 col-form-label red bold">請輸入以下變數 :</label>
	       </div>
		 */
		var variable_el = $("<div></div>").attr("class", "form-group row")
								.append($("<label></label>").attr("for", "stepModal_enterVarRemark").attr("class", "col-md-12 col-sm-2 col-form-label red bold").text("請輸入以下變數 :"));
		
		$("#step2_variable_section").append(variable_el);

		for (var i=0; i<count; i++) {
			var varKey = varKeyArray[i].trim();
			
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
	const scriptName = window.sessionStorage.getItem(_DELIVERY_SCRIPT_NAME_);
	const groupName = window.sessionStorage.getItem(_DELIVERY_DEVICE_GROUP_NAME_);
	const deviceName = window.sessionStorage.getItem(_DELIVERY_DEVICE_NAME_);
	const varKey = window.sessionStorage.getItem(_DELIVERY_VAR_KEY_);
	const varValue = window.sessionStorage.getItem(_DELIVERY_VAR_VALUE_);
	
	/*
	<span class="preview-topic">腳本名稱 :</span> 客製2-開VLAN<br>
	<hr class="bg_yellow">
	<span class="preview-topic">派送對象 :</span><br>
	  <ul class="preview-ul">
	    <li><span class="preview-li">第一組</span>
		    <ul>
		      <li>群組1: T1</li>
		      <li>設備1: A-1F-N1D-BR1</li>
		    </ul>
	    </li>
	    <li><span class="preview-li">第二組</span>
		    <ul>
		      <li>群組2: T1</li>
		      <li>設備2: A-1F-N1D-BR2</li>
		    </ul>
	    </li>
	  </ul>
	<hr class="bg_yellow">
	<span class="preview-topic">變數內容 :</span><br>
	<ul class="preview-ul">
	  <li>vlan_id: (預設系統流水)</li>
	  <li>vlan_name: public_vlan</li>
	  <li>interface_id: (預設系統流水)</li>
	</ul>
	 */
	
	var itemTopic = $("<span></span>").attr("class", "preview-topic");
	var br = $("<br>");
	var hr = $("<hr>").attr("class", "bg_yellow");
	var target_ul_el = $("<ul></ul>").attr("class", "preview-ul");
	var groupArray = $.parseJSON(groupName);
	var deviceArray = $.parseJSON(deviceName);
	
	for (var i=0; i<groupArray.length; i++) {
		var target_li_el = $("<li></li>").append(
				[$("<span></span>").attr("class", "preview-li").text("第" + (i+1) + "組")],
				[$("<ul></ul>").append(
					[$("<li></li>").text("群組" + (i+1) + " : " + groupArray[i])],
					[$("<li></li>").text("設備" + (i+1) + " : " + deviceArray[i])]
				 )]
			);
		
		target_ul_el.append(target_li_el);
	}
	
	var variable_ul_el = $("<ul></ul>").attr("class", "preview-ul");
	var keyArray = $.parseJSON(varKey);
	var valueArray = $.parseJSON(varValue);
	
	for (var i=0; i<keyArray.length; i++) {
		var variable_li_el = $("<li></li>").text(keyArray[i] + " : " + valueArray[i]);
		variable_ul_el.append(variable_li_el);
	}
	
	if (keyArray.length == 0) {
		variable_ul_el = $("<span></span>").text("此腳本不需輸入變數");
	}
	
	$("#stepModal_preview").append(
		[itemTopic.clone().text("腳本名稱 :")],
		[$("<span></span>").text(scriptName)],
		[br.clone()],
		[hr.clone()],
		[itemTopic.clone().text("派送對象 :")],
		[br.clone()],
		[target_ul_el],
		[hr.clone()],
		[itemTopic.clone().text("變數內容 :")],
		[variable_ul_el]
	);
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
		 * 派送Step 1. >> 紀錄目標設備ID、供裝原因 
		 * **************************************************************************/
		window.sessionStorage.setItem(_DELIVERY_DEVICE_ID_, JSON.stringify(deviceId));
		window.sessionStorage.setItem(_DELIVERY_DEVICE_NAME_, JSON.stringify(deviceName));
		window.sessionStorage.setItem(_DELIVERY_DEVICE_GROUP_NAME_, JSON.stringify(groupName));
		window.sessionStorage.setItem(_DELIVERY_REASON_, $("#stepModal_reason").val());
		
		return true;
	}
}

function checkVariable() {
	var variables = $("input[name=stepModalVariables]");
	var success = true;
	
	var inputVar = [];
	if (variables.length > 0) {
		$.each(variables, function(key, input) {
			if (input.value.trim().length == 0) {
				alert("請輸入變數值");
				success = false
				return false;
				
			} else {
				inputVar.push(input.value);
			}
		});
	}
	
	/* **************************************************************************
	 * 派送Step 2. >> 紀錄輸入的變數值
	 * **************************************************************************/
	window.sessionStorage.setItem(_DELIVERY_VAR_VALUE_, JSON.stringify(inputVar));
	
	return success;
}

function checkDeliveryParameters() {
	//先做初步基本檢核
	const scriptInfoId = window.sessionStorage.getItem(_DELIVERY_SCRIPT_INFO_ID_);
	const scriptCode = window.sessionStorage.getItem(_DELIVERY_SCRIPT_CODE_);
	const deviceId = window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_);
	const varKey = window.sessionStorage.getItem(_DELIVERY_VAR_KEY_);
	const varValue = window.sessionStorage.getItem(_DELIVERY_VAR_VALUE_);

	if ((scriptInfoId == null || (scriptInfoId != null && scriptInfoId.trim().length == 0))
		|| (scriptCode == null || (scriptCode != null && scriptCode.trim().length == 0))
		|| (deviceId == null || (deviceId != null && deviceId.trim().length == 0))
		|| (varKey == null || (varKey != null && varKey.trim().length == 0))
		|| (varValue == null || (varValue != null && varValue.trim().length == 0))
	) {
		alert("派送所需參數內容檢核有異常，系統將自動重整畫面，並請再重新操作；若此異常訊息仍再次出現，請通知系統維護人員，謝謝");
		location.reload();
		return false;
	}
}

function doDelivery() {
	const scriptInfoId = window.sessionStorage.getItem(_DELIVERY_SCRIPT_INFO_ID_);
	const scriptCode = window.sessionStorage.getItem(_DELIVERY_SCRIPT_CODE_);
	const deviceId = window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_);
	const varKey = window.sessionStorage.getItem(_DELIVERY_VAR_KEY_);
	const varValue = window.sessionStorage.getItem(_DELIVERY_VAR_VALUE_);
	
	var ps = {
		"scriptInfoId" : scriptInfoId,
		"scriptCode" : scriptCode,
		"deviceId" : deviceId,
		"varKey" : varKey,
		"varValue" : varValue
	};
	
	$.ajax({
		url : _ctx + '/delivery/doDelivery.json',
		data : {
			"ps" : JSON.stringify(ps)
		},
		type : "POST",
		dataType : 'json',
		async: true,
		beforeSend : function() {
			$("#processing").show();
		},
		complete : function() {
			$("#processing").hide();
		},
		success : function(resp) {
			if (resp.code == '200') {
				
				
			} else {
				alert(resp.message);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
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