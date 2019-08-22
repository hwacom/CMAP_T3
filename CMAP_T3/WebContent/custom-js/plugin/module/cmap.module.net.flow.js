/**
 * 
 */

var timer, startTime, timer_start, timer_end;
var waitForNextData = false;
var startNum, pageLength;
var lastScrollYPos = 0;

$(document).ready(function() {
	initMenuStatus("toggleMenu_plugin", "toggleMenu_plugin_items", "cm_netflow");
	
	startNum = 0;
	pageLength = Number($("#pageLength").val());
	
	$("input").val("");
	
	/*
	var inputIp = new Cleave('.input-ip', {
		numericOnly: true,
		delimiter: '.',
	    blocks: [3, 3, 3, 3]
	});
	*/
	
	var inputPort = new Cleave('.input-port', {
		numericOnly: true,
		blocks: [5]
	});
	
	/*
	var inputMac = new Cleave('.input-mac', {
		delimiter: ':',
		blocks: [2, 2, 2, 2, 2, 2],
		uppercase: true
	});
	*/
	
	$("#resultTable").on('xhr.dt', function ( e, settings, json, xhr ) {
		if (json.msg != null) {
			$(".myTableSection").hide();
			alert(json.msg);
		}
	});
	
	var today = new Date();
	var year = today.getFullYear();
	var month = parseInt(today.getMonth()) + 1;
	month = (month < 10) ? ("0".concat(month)) : month;
	var date = today.getDate();
	date = (date < 10) ? ("0".concat(date)) : date;
	
	$("#queryDateBegin").val(year+"-"+month+"-"+date);
	$("#queryTimeBegin").val("00:00");
	$("#queryTimeEnd").val("23:59");
	
});

function countDown(status) {
	if (status == 'START') {
		startTime = parseInt(_timeout) - 1;
		
		timer_start = performance.now();
		timer = setInterval(function () {
			$("#timeoutMsg").val("Timeout倒數 : " + startTime + " 秒");
			startTime--;
			
	    }, 1000);
		
	} else {
		timer_end = performance.now();
		var spent = (parseInt(timer_end) - parseInt(timer_start)) / 1000;
		$("#timeoutMsg").val("查詢花費時間 : " + spent + " 秒");
		
		clearInterval(timer);
	}
}

function bindScrollEvent() {
	$(".dataTables_scrollBody").scroll(function(e) {
		if ($(".dataTables_empty").length == 0) {
			var rowCount = $("#resultTable > tBody > tr").length;
			var scrollTop = $(this).prop("scrollTop");
			var scrollTopMax = $(".dataTables_scrollBody").prop("scrollTopMax");
			
			if (scrollTop > lastScrollYPos) { //移動Y軸時才作動
				lastScrollYPos = scrollTop;

				if (rowCount >= pageLength) { //查詢結果筆數有超過分頁筆數才作動
					//if (scrollTop > (scrollTopMax - (scrollTopMax*0.3))) {
					//捲到最底才查找下一批資料
					if (scrollTop == scrollTopMax) { 
						if (!waitForNextData) {
							waitForNextData = true;
							findNextData();
						}
					}
				}
			}
		}
	});
}

function unbindScrollEvent() {
	$(".dataTables_scrollBody").off("scroll");
}

function addRow(dataList) {
	for (var i=0; i<dataList.length; i++) {
		var data = dataList[i];
		var rowCount = $("#resultTable > tBody > tr").length;
		var cTR = $("#resultTable > tbody > tr:eq(0)").clone();
		$(cTR).find("td:eq(0)").html( ++rowCount );
		$(cTR).find("td:eq(1)").html( data.groupName );
		$(cTR).find("td:eq(2)").html( data.now );
		$(cTR).find("td:eq(3)").html( data.fromDateTime );
		$(cTR).find("td:eq(4)").html( data.toDateTime );
		$(cTR).find("td:eq(5)").html( data.ethernetType );
		$(cTR).find("td:eq(6)").html( data.protocol);
		$(cTR).find("td:eq(7)").html( '<a href="#" onclick="viewIpPort(\''+data.groupId+'\',\''+data.dataId+row.fromDateTime+'\',\''+'\',\''+row.sourceIPInGroup+'\',\'S\')">'+data.sourceIP+'</a>' );
		$(cTR).find("td:eq(8)").html( data.sourcePort );
		$(cTR).find("td:eq(9)").html( data.sourceMAC );
		$(cTR).find("td:eq(10)").html( '<a href="#" onclick="viewIpPort(\''+data.groupId+'\',\''+data.dataId+row.fromDateTime+'\',\''+'\',\''+row.destinationIPInGroup+'\',\'D\')">'+data.destinationIP+'</a>' );
		$(cTR).find("td:eq(11)").html( data.destinationPort );
		$(cTR).find("td:eq(12)").html( data.destinationMAC );
		$(cTR).find("td:eq(13)").html( data.size );
		$(cTR).find("td:eq(14)").html( data.channelID );
		$(cTR).find("td:eq(15)").html( data.toS );
		$(cTR).find("td:eq(16)").html( data.senderIP );
		$(cTR).find("td:eq(17)").html( data.inboundInterface );
		$(cTR).find("td:eq(18)").html( data.outboundInterface );
		$(cTR).find("td:eq(19)").html( data.sourceASI );
		$(cTR).find("td:eq(20)").html( data.destinationASI );
		$(cTR).find("td:eq(21)").html( data.sourceMask );
		$(cTR).find("td:eq(22)").html( data.destinationMask );
		$(cTR).find("td:eq(23)").html( data.nextHop );
		$(cTR).find("td:eq(24)").html( data.sourceVLAN );
		$(cTR).find("td:eq(25)").html( data.destinationVLAN );
		$(cTR).find("td:eq(26)").html( data.flowID );
		$("#resultTable > tbody").append($(cTR));
	}
}

// 查找總流量
function getTotalTraffic() {
	$("#div_TotalFlow").css("display", "contents");
	
	$.ajax({
		url : _ctx + '/plugin/module/netFlow/getTotalTraffic.json',
		data : {
			"queryGroup" : $("#queryGroup").val(),
			"querySourceIp" : $("#query_SourceIp").val(),
			"queryDestinationIp" : $("#query_DestinationIp").val(),
			"querySenderIp" : $("#query_SenderIp").val(),
			"querySourcePort" : $("#query_SourcePort").val(),
			"queryDestinationPort" : $("#query_DestinationPort").val(),
			"queryDateBegin" : $("#queryDateBegin").val(),
			"queryTimeBegin" : $("#queryTimeBegin").val(),
			"queryTimeEnd" : $("#queryTimeEnd").val(),
			"searchValue" : $("#resultTable_filter").find("input").val()
		},
		type : "POST",
		dataType : 'json',
		async: true,
		beforeSend : function(xhr) {
			//showProcessing();
			$("#result_TotalFlow").text("總流量：");
			$("#searchWaiting").show();
		},
		complete : function() {
			//hideProcessing();
			$("#searchWaiting").hide();
		},
		initComplete : function(settings, json) {
        },
		success : function(resp) {
			var traffic = resp.data.TTL_TRAFFIC;
			
			if (traffic === "EMPTY") {
				$("#result_TotalFlow").text("查無符合資料");
			} else {
				$("#result_TotalFlow").text("總流量：" + traffic);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			//ajaxErrorHandler();
			$("#result_TotalFlow").text("查無符合資料");
		}
	});
}

// 取得查詢條件下總筆數
function getTotalFilteredCount() {
	$.ajax({
		url : _ctx + '/plugin/module/netFlow/getTotalFilteredCount.json',
		data : {
			"queryGroup" : $("#queryGroup").val(),
			"querySourceIp" : $("#query_SourceIp").val(),
			"queryDestinationIp" : $("#query_DestinationIp").val(),
			"querySenderIp" : $("#query_SenderIp").val(),
			"querySourcePort" : $("#query_SourcePort").val(),
			"queryDestinationPort" : $("#query_DestinationPort").val(),
			"queryDateBegin" : $("#queryDateBegin").val(),
			"queryTimeBegin" : $("#queryTimeBegin").val(),
			"queryTimeEnd" : $("#queryTimeEnd").val(),
			"searchValue" : $("#resultTable_filter").find("input").val()
		},
		type : "POST",
		dataType : 'json',
		async: true,
		beforeSend : function(xhr) {
			//resultTable_info  顯示第 0 至 0 項結果，共 0 項
			var sNum, eNum;
			if ($(".dataTables_empty").length == 0) { //有查到資料
				sNum = 1;
				eNum = $("#resultTable > tBody > tr").length;
			} else {
				sNum = 0;
				eNum = 0;
			}
			$("#resultTable_info").html(
				'<div class="row" style="padding-left: 15px;">' +
					'<div id="current_count">顯示第 ' + sNum + ' 至 ' + eNum + ' 項結果，共</div>' +
				    '<div id="total_count">' +
				       '<img id="searchWaiting2" class="img_searchWaiting" alt="loading..." src="/resources/images/Processing_4.gif">' +
				    '</div>' +
				    '<div style="">筆</div>' +
				'</div>'
			);
		},
		complete : function() {
			//hideProcessing();
			$("#searchWaiting2").hide();
		},
		initComplete : function(settings, json) {
        },
		success : function(resp) {
			var count = resp.data.FILTERED_COUNT;
			$("#total_count").html('&nbsp;' + count + '&nbsp;');
		},
		error : function(xhr, ajaxOptions, thrownError) {
			//ajaxErrorHandler();
			$("#total_count").html('&nbsp;N/A&nbsp;');
		}
	});
}

// 找下一批資料
function findNextData() {
	var sortIdx = -1;
	var sortStr;
	
	if ($(".dataTable > thead").find(".sorting_asc").length > 0) {
		sortIdx = $(".dataTable > thead").find(".sorting_asc").prop("cellIndex");
	} else if ($(".dataTable > thead").find(".sorting_desc").length > 0) {
		sortIdx = $(".dataTable > thead").find(".sorting_desc").prop("cellIndex");
	}
	
	var sortBy = $(".dataTable > thead > tr > th:eq(" + sortIdx + ")").attr("aria-sort");
	if (sortBy === "descending") {
		sortStr = "desc";
	} else {
		sortStr = "asc";
	}
	
	$.ajax({
		url : _ctx + '/plugin/module/netFlow/getNetFlowData.json',
		data : {
			"queryGroup" : $("#queryGroup").val(),
			"querySourceIp" : $("#query_SourceIp").val(),
			"queryDestinationIp" : $("#query_DestinationIp").val(),
			"querySenderIp" : $("#query_SenderIp").val(),
			"querySourcePort" : $("#query_SourcePort").val(),
			"queryDestinationPort" : $("#query_DestinationPort").val(),
			"queryDateBegin" : $("#queryDateBegin").val(),
			"queryTimeBegin" : $("#queryTimeBegin").val(),
			"queryTimeEnd" : $("#queryTimeEnd").val(),
			"start" : startNum,
			"length" : pageLength,
			"searchValue" : $("#resultTable_filter").find("input").val(),
			"order[0][column]" : sortIdx,
			"order[0][dir]" : sortStr
		},
		type : "POST",
		dataType : 'json',
		async: true,
		beforeSend : function(xhr) {
			countDown('START');
			showProcessing();
		},
		complete : function() {
			countDown('STOP');
			hideProcessing();
			waitForNextData = false;
		},
		initComplete : function(settings, json) {
			if (json.msg != null) {
				$(".myTableSection").hide();
				alert(json.msg);
			}
        },
		success : function(resp) {
			var count = resp.data.length;
			if (count > 0) {
				addRow(resp.data);
				startNum += pageLength;
			}
			
			var sNum, eNum;
			if ($(".dataTables_empty").length == 0) { //有查到資料
				sNum = 1;
				eNum = $("#resultTable > tBody > tr").length;
			} else {
				sNum = 0;
				eNum = 0;
			}
			
			$("#current_count").text('顯示第 ' + sNum + ' 至 ' + eNum + ' 項結果，共');
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#current_count").text('<ERROR>');
			ajaxErrorHandler();
		}
	});
}

function viewIpPort(groupId, dataId, fromDateTime, ipInGroup, type) {
	var obj = new Object();
	obj.groupId = groupId;
	obj.dataId = dataId;
	obj.fromDateTime = fromDateTime;
	obj.type = type;
	
	$.ajax({
		url : _ctx + '/plugin/module/ipMapping/getMappingRecordFromNetFlow.json',
		data : JSON.stringify(obj),
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
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
				$('#viewIpMappingPortModal_groupName').parent().show();
				
				if (ipInGroup == "Y") {
					$('#viewIpMappingPortModal_deviceName').parent().show();
					$('#viewIpMappingPortModal_deviceModel').parent().show();
					$('#viewIpMappingPortModal_portName').parent().show();
					$('#viewIpMappingPortModal_showMsg').parent().show();
					$('#viewIpMappingPortModal_country').parent().hide();
					
				} else {
					$('#viewIpMappingPortModal_deviceName').parent().hide();
					$('#viewIpMappingPortModal_deviceModel').parent().hide();
					$('#viewIpMappingPortModal_portName').parent().hide();
					$('#viewIpMappingPortModal_showMsg').parent().hide();
					$('#viewIpMappingPortModal_country').parent().show();
				}
				$('#viewIpMappingPortModal_ipAddress').parent().show();
				
				$('#viewIpMappingPortModal_groupName').html(resp.data.groupName);
				$('#viewIpMappingPortModal_deviceName').html(resp.data.deviceName);
				$('#viewIpMappingPortModal_deviceModel').html(resp.data.deviceModel);
				$('#viewIpMappingPortModal_ipAddress').html(resp.data.ipAddress);
				$('#viewIpMappingPortModal_portName').html(resp.data.portName);
				$('#viewIpMappingPortModal_showMsg').html(resp.data.showMsg);
				
				var locationHtml = "N/A";
				var location = resp.data.location;
				var countryCode = resp.data.countryCode;
				var countryQueryURL = resp.data.countryQueryURL;
				
				if (location != "" && location != undefined && location != "undefined") {
					locationHtml = "<span class=\"flag-icon flag-icon-" + countryCode + "\"></span>&nbsp;" + location
				} else {
					locationHtml = "<a href=\"" + countryQueryURL + "\" target=\"_blank\">查詢IP來源國家</a>";
				}
				$('#viewIpMappingPortModal_country').html(locationHtml);
				
				$('#viewIpMappingPortModal').modal('show');
				
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
	$('#queryFrom').val(from);
	
	if ($("#queryGroup").val().trim().length == 0) {
		alert(msg_chooseGroup);
		return;
	}
	
	if ($("#queryDateBegin").val().trim().length == 0) {
		alert(msg_chooseDate);
		return;
	}
	
	if (from == 'MOBILE') {
		$('#collapseExample').collapse('hide');
	}
	
	startNum = 0;
	if (typeof resultTable !== "undefined") {
		$(".dataTables_scrollBody").scrollTop(0);
		resultTable.ajax.reload();
		$(".myTableSection").show();
		
	} else {
		$(".myTableSection").show();
		
		resultTable = $('#resultTable').DataTable(
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
			"pageLength"	: pageLength,
			"language" : {
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
	        "createdRow": function( row, data, dataIndex ) {
	        },
			"ajax" : {
				"url" : _ctx + '/plugin/module/netFlow/getNetFlowData.json',
				"type" : 'POST',
				"data" : function ( d ) {
					if ($('#queryFrom').val() == 'WEB') {
						d.queryGroup = $("#queryGroup").val(),
						d.querySourceIp = $("#query_SourceIp").val(),
						d.queryDestinationIp = $("#query_DestinationIp").val(),
						d.querySenderIp = $("#query_SenderIp").val(),
						d.querySourcePort = $("#query_SourcePort").val(),
						d.queryDestinationPort = $("#query_DestinationPort").val(),
						//d.queryMac = $("#queryMac").val(),
						d.queryDateBegin = $("#queryDateBegin").val(),
						//d.queryDateEnd = $("#queryDateEnd").val()
						d.queryTimeBegin = $("#queryTimeBegin").val(),
						d.queryTimeEnd = $("#queryTimeEnd").val()
					
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryGroup = $("#queryGroup_mobile").val(),
						d.querySourceIp = $("#query_SourceIp_mobile").val(),
						d.queryDestinationIp = $("#query_DestinationIp_mobile").val(),
						d.querySenderIp = $("#query_SenderIp_mobile").val(),
						d.querySourcePort = $("#query_SourcePort_mobile").val(),
						d.queryDestinationPort = $("#query_DestinationPort_mobile").val(),
						//d.queryMac = $("#queryMac_mobile").val();
						d.queryDateBegin = $("#queryDateBegin_mobile").val(),
						//d.queryDateEnd = $("#queryDateEnd_mobile").val()
						d.queryTimeBegin = $("#queryTimeBegin").val(),
						d.queryTimeEnd = $("#queryTimeEnd").val()
					}
					d.start = 0; //初始查詢一律從第0筆開始
					d.length = pageLength;
					return d;
				},
				beforeSend : function() {
					countDown('START');
				},
				complete : function() {
					countDown('STOP');
				},
				"error" : function(xhr, ajaxOptions, thrownError) {
					ajaxErrorHandler();
				},
				/*
				"dataSrc" : function(json) {
					if (json.data.length > 0) {
						if (json.otherMsg != null && json.otherMsg != "") {
							$("#div_TotalFlow").css("display", "contents");
							$("#result_TotalFlow").text("總流量：" + json.otherMsg);
						}
						
					} else {
						$("#div_TotalFlow").css("display", "contents");
						$("#result_TotalFlow").text("查無符合資料");
					}
					return json.data;
				},
				*/
				"timeout" : parseInt(_timeout) * 1000 //設定60秒Timeout
			},
			"order": [[3 , 'desc' ]],
			"initComplete": function(settings, json) {
				if (json.msg != null) {
					$(".myTableSection").hide();
					alert(json.msg);
				}
				bindScrollEvent();
            },
			"drawCallback" : function(settings) {
				//$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust();
				/*
				//分頁筆數選單
				$("div.dataTables_length").parent().removeClass('col-sm-12');
				$("div.dataTables_length").parent().addClass('col-sm-6');
				*/
				
				//搜尋
				$("div.dataTables_filter").parent().removeClass('col-sm-12');
				$("div.dataTables_filter").parent().addClass('col-sm-6');
				
				//資料筆數
				$("div.dataTables_info").parent().removeClass('col-sm-12');
				$("div.dataTables_info").parent().addClass('col-sm-6');
				
				/*
				//分頁按鈕
				$("div.dataTables_paginate").parent().removeClass('col-sm-12');
				$("div.dataTables_paginate").parent().addClass('col-sm-6');
				*/
				
				startNum = pageLength; //初始查詢完成後startNum固定為pageLength大小
				lastScrollYPos = $(".dataTables_scrollBody").prop("scrollTop");
				$("#resultTable_filter").find("input").prop("placeholder","(模糊查詢速度較慢)")
				getTotalTraffic();
				getTotalFilteredCount();
				bindTrEvent();
			},
			"columns" : [
				{},
				{ "data" : "groupName" , "orderable" : false },
				{ "data" : "now" , "orderable" : false },
				{ "data" : "fromDateTime" },
				{ "data" : "toDateTime" , "orderable" : false },
				{ "data" : "ethernetType" , "orderable" : false },
				{ "data" : "protocol" , "orderable" : false },
				{},
				{ "data" : "sourcePort" , "orderable" : false },
				{ "data" : "sourceMAC" , "orderable" : false },
				{},
				{ "data" : "destinationPort" , "orderable" : false },
				{ "data" : "destinationMAC" , "orderable" : false },
				{ "data" : "size" , "orderable" : false },
				{ "data" : "channelID" , "orderable" : false },
				{ "data" : "toS" , "orderable" : false },
				{ "data" : "senderIP" , "orderable" : false },
				{ "data" : "inboundInterface" , "orderable" : false },
				{ "data" : "outboundInterface" , "orderable" : false },
				{ "data" : "sourceASI" , "orderable" : false },
				{ "data" : "destinationASI" , "orderable" : false },
				{ "data" : "sourceMask" , "orderable" : false },
				{ "data" : "destinationMask" , "orderable" : false },
				{ "data" : "nextHop" , "orderable" : false },
				{ "data" : "sourceVLAN" , "orderable" : false },
				{ "data" : "destinationVLAN" , "orderable" : false },
				{ "data" : "flowID" , "orderable" : false }
			],
			"columnDefs" : [
				{
					"targets" : [0],
					"className" : "center",
					"searchable": false,
					"orderable": false,
					"render": function (data, type, row, meta) {
						       	return meta.row + meta.settings._iDisplayStart + 1;
						   	}
				},
				{
					"targets" : [7],
					"className" : "left",
					"searchable": true,
					"orderable": true,
					"render" : function(data, type, row) {
									var html = '<a href="#" onclick="viewIpPort(\''+row.groupId+'\',\''+row.dataId+'\',\''+row.fromDateTime+'\',\''+row.sourceIPInGroup+'\',\'S\')">'+row.sourceIP+'</a>';
									return html;
							 }
				},
				{
					"targets" : [10],
					"className" : "left",
					"searchable": true,
					"orderable": true,
					"render" : function(data, type, row) {
									var html = '<a href="#" onclick="viewIpPort(\''+row.groupId+'\',\''+row.dataId+'\',\''+row.fromDateTime+'\',\''+row.destinationIPInGroup+'\',\'D\')">'+row.destinationIP+'</a>';
									return html;
							 }
				}
			],
		});
	}
}