/**
 * 
 */
var scriptShowLength = 20;	//設定欄位顯示內容最大長度
var STEP_NUM = 1;

$(document).ready(function() {
	if ($("#onlySwitchPort").val() == "Y") {
		initMenuStatus("toggleMenu_plugin", "toggleMenu_plugin_items", "cm_switchPort");
		findData('WEB');
		
	} else {
		initMenuStatus("toggleMenu_cm", "toggleMenu_cm_items", "cm_delivery");
	}
	
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
		e.preventDefault();
		goStep(1);
	});
	
	$("#btnStepGoFire").click(function(e) {
		doDelivery();
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
		async: true,
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
		/*
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
			console.log("_DELIVERY_GROUP_ID_ : " + window.sessionStorage.getItem(_DELIVERY_GROUP_ID_));
		}
		*/
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
				
				var groupId = "";
				var groupName = "";
				var obj = $.parseJSON(resp.data.groupDeviceMenu);
				$.each(obj, function(key, value) {
					if (key.startsWith("GROUP")) {
						groupId = key.split("_")[1];
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
																				  .attr("data-group-id", groupId)
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
	
	/*
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
	console.log("_DELIVERY_GROUP_ID_ : " + window.sessionStorage.getItem(_DELIVERY_GROUP_ID_));
	*/
}

function checkStepRequirement(nowStep) {
	$(".required").removeClass("required");
	
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
	}
}

function initStep1Panel() {
	var variables = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_VAR_KEY_));
	var menuJsonStr = window.sessionStorage.getItem(_DELIVERY_DEVICE_MENU_JSON_STR_);
	
	if (variables === '') {
		$("#stepModal_variable_description").text("此腳本不需輸入變數");
		$("#stepModal_variable_description").removeClass("red bold");
		
	} else {
		$("#stepModal_variable_description").text("※ 此腳本需輸入變數值 : ");
		$("#stepModal_variable_description").addClass("red bold");
		$('#stepModal_variable_show').text(variables.join(", "));
	}
	
	$("#stepModal_chooseDevice option").remove();
	
	var groupId = "";
	var groupName = "";
	var obj = $.parseJSON(menuJsonStr);
	$.each(obj, function(key, value) {
		if (key.startsWith("GROUP")) {
			groupId = key.replace("GROUP_", "");
			groupName = value;
			$("#stepModal_chooseDevice").append($("<option></option>").attr("class", "multi-option-topic")
																	  .attr("value", "")
																	  .attr("data-group-id", groupId)
																	  .attr("disabled", "disabled")
																	  .text(value));
			
		} else {
			var deviceKey = key.replace("DEVICE_", "");
			$("#stepModal_chooseDevice").append($("<option></option>").attr("class", "multi-option-item")
																	  .attr("value", deviceKey)
																	  .attr("data-group-name", groupName)
																	  .attr("data-group-id", groupId)
																	  .text(value));
		}
	});
}

function initStep2Panel() {
	var groupArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_GROUP_ID_));
	var deviceArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_));
	var varKeyArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_VAR_KEY_));
	
	$.ajax({
		url : _ctx + '/delivery/getVariableSetting.json',
		data : {
			"groupArray" : JSON.stringify(groupArray),
			"deviceArray" : JSON.stringify(deviceArray),
			"varKeyArray" : JSON.stringify(varKeyArray)
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
			if (resp.code != '200') {
				alert(resp.message);
			}
			
			drawStep2Panel(resp.data.info, resp.data.symbol);
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
}

function drawStep2Panel(deviceVarMap, symbol) {
	var groupIdArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_GROUP_ID_));
	var deviceIdArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_));
	var groupArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_DEVICE_GROUP_NAME_));
	var deviceArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_DEVICE_NAME_));
	var varKeyArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_VAR_KEY_));
	
	if (groupArray.length != 0) {
		var count = groupArray.length;
		var keyCount = (varKeyArray == null || (varKeyArray != null && varKeyArray.length == 0)) ? 1 : varKeyArray.length;
		
		//--------------- 建立THEAD動態增加VAR KEY欄位TD ---------------//
		//$("#step2_target_section").empty();
		//$("#step2_target_table > thead").empty();
		$("#step2_target_table > thead").find("tr:gt(0)").remove();
		
		var thead_var_tr_el = $("<tr></tr>");
		var thead_var_inpt_tr_el = $("<tr></tr>");
		if (varKeyArray != null && varKeyArray.length != 0) {
			var keyCount = (varKeyArray.length == 0) ? 1 : varKeyArray.length;
			
			var idx = 3;
			$.each(varKeyArray, function(key, value) {
				var input = $("<input>").attr("class", "form-control form-control-sm fill-all-inpt").attr("name", "fillAllInpt").attr("data-idx", idx).attr("placeholder", "(全部填入)");
				
				thead_var_tr_el.append($("<td></td>").attr("class", "var-td").text(value));
				thead_var_inpt_tr_el.append($("<td></td>").attr("class", "var-td").append($(input).clone()));
				idx++;
			});
			
			$("#step2_varKey_td").attr("colspan", keyCount);
			
		} else {
			thead_var_tr_el.append($("<td></td>").attr("class", "var-td").attr("rowspan", 2).text("不須輸入"));
		}
		
		$("#step2_target_table > thead").append(thead_var_tr_el);
		
		if (varKeyArray != null && varKeyArray.length != 0) {
			$("#step2_target_table > thead").append(thead_var_inpt_tr_el);
			$(":input[name=fillAllInpt]").on("keyup", function(e) {
				var inpt = e.target;
				fillAllRow($(inpt).data("idx"), $(inpt).val())
			});
		}
		
		//--------------- 建立TBODY動態增加設備TR ---------------//
		$("#step2_target_table > tbody").empty();
		
		for (var i=0; i<count; i++) {
			var seq = parseInt(i) + 1;
			var groupName = (i < groupArray.length) ? groupArray[i] : "N/A";
			var deviceName = (i < deviceArray.length) ? deviceArray[i] : "N/A";
			var groupId = (i < groupIdArray.length) ? groupIdArray[i] : 0;
			var deviceId = (i < deviceIdArray.length) ? deviceIdArray[i] : 0;
			var mapKey = groupId + symbol + deviceId;
			
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
			/*
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
			*/
			/*
			 * <tr>
	      	  		<td>1</td>
	      	  		<td>第二航廈(T2)</td>
	      	  		<td>192.168.1.5 (2F旅客中心) {2F_Center} [Cisco Device Cisco IOS]</td>
	      	  	</tr>
			 */
			var target_el = $("<tr></tr>")
					            .append(
					            	[$("<td></td>").attr("class", "center").attr("width", "5%").text(seq)],
					            	[$("<td></td>").attr("width", "10%").text(groupName)],
					            	[$("<td></td>").attr("width", "20%").text(deviceName)]
					            );
			
			if (varKeyArray != null && varKeyArray.length != 0) {
				var tdInput = $("<td></td>").append($("<input>").attr("type", "text").attr("class", "form-control form-control-sm").attr("name", "input_var").attr("data-idx", i));
				
				$.each(varKeyArray, function(key, value) {
					
					if (!isEmpty(deviceVarMap)) {
						var deviceMap = deviceVarMap[mapKey];
						
						if (deviceMap != undefined) {
							var varValueList = deviceMap[value];
							
							if (varValueList != undefined && varValueList.length > 0) {
								var tdSelect = $("<td></td>");
								var select = $("<select></select>").attr("class", "form-control form-control-sm").attr("name", "input_var").attr("data-idx", i);
								
								$.each(varValueList, function(idx, info) {
									var option = $("<option></option>").attr("value", info.infoValue).text(info.infoValue);
									select.append(option);
								});
								
								tdSelect.append(select);
								target_el.append(tdSelect);
								
							} else {
								//若該設備抓無此變數的無客製變數明細，則預設為輸入框
								target_el.append($(tdInput).clone());
							}
						}
						
					} else {
						//若無客製變數設定，則預設為輸入框
						target_el.append($(tdInput).clone());
					}
				});
			}
			
			//$("#step2_target_section").append(target_el);
			$("#step2_target_table > tbody").append(target_el);
		}
		
		$(":input[name=input_var]").on("change", function(e) {
			var inpt = e.target;

			if ($(inpt).val() != '' && $(inpt).hasClass("required")) {
				$(inpt).removeClass("required");
			}
		});
		
		$("#step2_target_table > tbody").animate({
			scrollTop: 0
		}, 500, 'easeOutBounce');
	}
	
	/*
	var varKeys = window.sessionStorage.getItem(_DELIVERY_VAR_KEY_);
	var varKeyArray = (varKeys != null) ? $.parseJSON(varKeys) : null;
	
	if (varKeyArray != null && varKeyArray.length != 0) {
		
		var count = varKeyArray.length;
		
		$("#step2_variable_section").empty();
		
		/*
		 * <div class="form-group row">
	       	 <label for="stepModal_enterVarRemark" class="col-md-12 col-sm-2 col-form-label red bold">請輸入以下變數 :</label>
	       </div>
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
			variable_el = $("<div></div>").attr("class", "form-group row")
								.append(
									[$("<label></label>").attr("for", "stepModal_var_"+seq).attr("class", "col-md-2 col-sm-2 col-form-label blue bold").text(varKey+" :")],
									[$("<input>").attr("type", "text").attr("class", "form-control form-control-sm col-md-10 col-sm-12").attr("id", "stepModal_var_"+seq).attr("name", "stepModalVariables")]
								);
			
			$("#step2_variable_section").append(variable_el);
		}
	}
*/
}

function initStep3Panel() {
	$("#stepModal_preview").empty();
	
	var scriptName = window.sessionStorage.getItem(_DELIVERY_SCRIPT_NAME_);
	var groupName = window.sessionStorage.getItem(_DELIVERY_DEVICE_GROUP_NAME_);
	var deviceName = window.sessionStorage.getItem(_DELIVERY_DEVICE_NAME_);
	var varKey = window.sessionStorage.getItem(_DELIVERY_VAR_KEY_);
	var varValue = window.sessionStorage.getItem(_DELIVERY_VAR_VALUE_);
	
	var keyArray = $.parseJSON(varKey);
	var keyCount = keyArray.length;
	
	var itemTopic = $("<span></span>").attr("class", "preview-topic");
	var br = $("<br>");
	var hr = $("<hr>").attr("class", "bg_yellow");
	var target_table_el = $("<table></table>").attr("id", "step3_target_table").attr("class", "myTable center");
	var target_thead_el = $("<thead></thead>").attr("class", "bold")
								.append($("<tr></tr>")
												.append($("<td></td>").attr("rowspan", 2).attr("width", "5%").text("序"))
												.append($("<td></td>").attr("rowspan", 2).attr("width", "15%").text("群組名稱"))
												.append($("<td></td>").attr("rowspan", 2).attr("width", "20%").text("設備名稱"))
												.append($("<td></td>").attr("colspan", keyCount).attr("width", "60%").text("變數值"))
										);
	
	var var_key_tr = $("<tr></tr>");
	$.each(keyArray, function(key, value) {
		var_key_tr.append($("<td></td>").attr("class", "var-td").text(value));
	});
	
	target_thead_el.append(var_key_tr);
	
	var target_tbody_el = $("<tbody></tbody>");
	
	target_table_el.append(target_thead_el).append(target_tbody_el);
	
	var groupArray = $.parseJSON(groupName);
	var deviceArray = $.parseJSON(deviceName);
	var valueArray = $.parseJSON(varValue);
	
	for (var i=0; i<groupArray.length; i++) {
		
		var target_tr_el = $("<tr></tr>").append(
												[$("<td></td>").attr("width", "5%").text(i+1)],
												[$("<td></td>").attr("width", "15%").text(groupArray[i])],
												[$("<td></td>").attr("width", "20%").text(deviceArray[i])]
											);
		
		var values = valueArray[i];
		
		$.each(values, function(key, value) {
			target_tr_el.append($("<td></td>").text(value));
			
			target_table_el.append(target_tr_el);
		});
	}
	
	/*
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
	*/
	
	$("#stepModal_preview").append(
		[itemTopic.clone().text("腳本名稱 : ")],
		[$("<span></span>").text(" "+scriptName)],
		[br.clone()],
		[hr.clone()],
		[itemTopic.clone().text("派送對象與變數值 : ")],
		[br.clone()],
		[target_table_el]
	);
}

function fillAllRow(idx, value) {
	var trArray = $("#step2_target_table").find("tr:gt(2)").find("td:eq(" + idx + ")").find(":input");
	
	$.each(trArray, function(key, input) {
		$(input).val(value);
		$(input).trigger("change");
	});
}

function checkDeviceChoose() {
	var selectedObj = $("#stepModal_chooseDevice option:selected");
	
	if (selectedObj.length == 0) {
		alert("請選擇設備");
		$("#stepModal_chooseDevice").addClass("required");
		return false;
		
	} else {
		var deviceId = [];
		var deviceName = [];
		var groupId = [];
		var groupName = [];
		
		$.each(selectedObj, function(key, option) {
			deviceId.push($(option).val());
			deviceName.push($(option).text());
			groupId.push($(option).data("group-id")+"");
			groupName.push($(option).data("group-name"));
		});
		
		/* **************************************************************************
		 * 派送Step 1. >> 紀錄目標設備ID、供裝原因 
		 * **************************************************************************/
		console.log(window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_));
		console.log(window.sessionStorage.getItem(_DELIVERY_GROUP_ID_));
		window.sessionStorage.setItem(_DELIVERY_DEVICE_ID_, JSON.stringify(deviceId));
		window.sessionStorage.setItem(_DELIVERY_DEVICE_NAME_, JSON.stringify(deviceName));
		window.sessionStorage.setItem(_DELIVERY_GROUP_ID_, JSON.stringify(groupId));
		window.sessionStorage.setItem(_DELIVERY_DEVICE_GROUP_NAME_, JSON.stringify(groupName));
		window.sessionStorage.setItem(_DELIVERY_REASON_, $("#stepModal_reason").val());
		
		return true;
	}
}

function checkVariable() {
	var keyArray = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_VAR_KEY_));
	var varInput = $("input[name=input_var]");
	var varSelect = $("select[name=input_var]");
	var success = true;
	
	var validateErrorObj = [];
	var inputVar = [];
	
	if (varInput.length > 0) {
		$.each(varInput, function(key, input) {
			if (input.value.trim().length == 0) {
				success = false
				validateErrorObj.push(input);
				
			} else {
				var varIdx = $(input).data("idx");
				
				if (inputVar[varIdx] === undefined) {
					inputVar[varIdx] = [];
				}
				
				inputVar[varIdx].push(input.value);
			}
		});
	}
	
	if (varSelect.length > 0) {
		$.each(varSelect, function(key, select) {
			if (select.value.trim().length == 0) {
				success = false
				validateErrorObj.push(select);
				
			} else {
				var varIdx = $(select).data("idx");
				
				if (inputVar[varIdx] === undefined) {
					inputVar[varIdx] = [];
				}
				
				inputVar[varIdx].push(select.value);
			}
		});
	}
	
	if (!success) {
		$.each(validateErrorObj, function(key, input) {
			$(input).addClass("required");
		});
		
		alert("請輸入變數值");
		return false;
	}
	
	/* **************************************************************************
	 * 派送Step 2. >> 紀錄輸入的變數值
	 * **************************************************************************/
	window.sessionStorage.setItem(_DELIVERY_VAR_VALUE_, JSON.stringify(inputVar));
	
	return success;
}

function checkDeliveryParameters() {
	//先做初步基本檢核
	var scriptInfoId = window.sessionStorage.getItem(_DELIVERY_SCRIPT_INFO_ID_);
	var scriptCode = window.sessionStorage.getItem(_DELIVERY_SCRIPT_CODE_);
	var deviceId = window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_);
	var varKey = window.sessionStorage.getItem(_DELIVERY_VAR_KEY_);
	var varValue = window.sessionStorage.getItem(_DELIVERY_VAR_VALUE_);

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
	var scriptInfoId = window.sessionStorage.getItem(_DELIVERY_SCRIPT_INFO_ID_);
	var scriptCode = window.sessionStorage.getItem(_DELIVERY_SCRIPT_CODE_);
	var groupId = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_GROUP_ID_));
	var deviceId = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_DEVICE_ID_));
	var varKey = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_VAR_KEY_));
	var varValue = $.parseJSON(window.sessionStorage.getItem(_DELIVERY_VAR_VALUE_));
	var reason = window.sessionStorage.getItem(_DELIVERY_REASON_);
	
	var ps = {
		"scriptInfoId" : scriptInfoId,
		"scriptCode" : scriptCode,
		"groupId" : groupId,
		"deviceId" : deviceId,
		"varKey" : varKey,
		"varValue" : varValue,
		"reason" : reason
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
	
	var scriptContent = scriptContent != null ? scriptContent.replace(/(\r\n|\r|\n)/g, "<br>") : "異常無紀錄";
	
	$('#viewScriptModal_scriptContent').html(scriptContent);
	$('#viewScriptModal').modal('show');
}

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
					
					d.onlySwitchPort = $("#onlySwitchPort").val();
					
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