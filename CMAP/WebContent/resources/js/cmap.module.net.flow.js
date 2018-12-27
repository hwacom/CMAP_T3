/**
 * 
 */

var timer, startTime, timer_start, timer_end;

$(document).ready(function() {
	initMenuStatus("toggleMenu_plugin", "toggleMenu_plugin_items", "cm_netflow");
	
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
	
	$("#resutTable").on('xhr.dt', function ( e, settings, json, xhr ) {
		if (json.msg != null) {
			$(".myTableSection").hide();
			alert(json.msg);
		}
	});
	
	var today = new Date();
	const year = today.getFullYear();
	const month = parseInt(today.getMonth()) + 1;
	const date = today.getDate();
	
	$("#queryDateBegin").val(year+"-"+month+"-"+date);
	
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

//查詢按鈕動作
function findData(from) {
	$('#queryFrom').val(from);
	
	if ($("#queryDateBegin").val().trim().length == 0) {
		alert("請選擇日期");
		return;
	}
	
	if (from == 'MOBILE') {
		$('#collapseExample').collapse('hide');
	}
	
	if (typeof resutTable !== "undefined") {
		resutTable.ajax.reload();
		$(".myTableSection").show();
		
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
						d.queryDateBegin = $("#queryDateBegin").val()
						//d.queryDateEnd = $("#queryDateEnd").val()
					
					} else if ($('#queryFrom').val() == 'MOBILE') {
						d.queryGroup = $("#queryGroup_mobile").val(),
						d.querySourceIp = $("#query_SourceIp_mobile").val(),
						d.queryDestinationIp = $("#query_DestinationIp_mobile").val(),
						d.querySenderIp = $("#query_SenderIp_mobile").val(),
						d.querySourcePort = $("#query_SourcePort_mobile").val(),
						d.queryDestinationPort = $("#query_DestinationPort_mobile").val(),
						//d.queryMac = $("#queryMac_mobile").val();
						d.queryDateBegin = $("#queryDateBegin_mobile").val()
						//d.queryDateEnd = $("#queryDateEnd_mobile").val()
					}
					
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
				"timeout" : parseInt(_timeout) * 1000 //設定60秒Timeout
			},
			/*"order": [[6 , 'desc' ]],*/
			"initComplete": function(settings, json) {
				if (json.msg != null) {
					$(".myTableSection").hide();
					alert(json.msg);
				}
            },
			"drawCallback" : function(settings, json) {
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
			},
			"columns" : [
				{},
				{ "data" : "groupName" },
				{ "data" : "now" },
				{ "data" : "fromDateTime" },
				{ "data" : "toDateTime" },
				{ "data" : "ethernetType" },
				{ "data" : "protocol" },
				{ "data" : "sourceIP" },
				{ "data" : "sourcePort" },
				{ "data" : "sourceMAC" },
				{ "data" : "destinationIP" },
				{ "data" : "destinationPort" },
				{ "data" : "destinationMAC" },
				{ "data" : "size" },
				{ "data" : "channelID" },
				{ "data" : "toS" },
				{ "data" : "senderIP" },
				{ "data" : "inboundInterface" },
				{ "data" : "outboundInterface" },
				{ "data" : "sourceASI" },
				{ "data" : "destinationASI" },
				{ "data" : "sourceMask" },
				{ "data" : "destinationMask" },
				{ "data" : "nextHop" },
				{ "data" : "sourceVLAN" },
				{ "data" : "destinationVLAN" },
				{ "data" : "flowID" }
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
				}
			],
		});
	}
}