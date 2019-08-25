/**
 * 
 */
var URI_LOGIN = "getLoginUri";
var URI_INDEX = "getPrtgIndexUri";
var URI_DASHBOARD = "getPrtgDashboardUri";
var URI_TOPOGRAPHY = "getPrtgTopographyUri";
var URI_ALARM_SUMMARY = "getPrtgAlarmSummaryUri";
var URI_NET_FLOW_SUMMARY = "getPrtgNetFlowSummaryUri";
var URI_NET_FLOW_OUTPUT = "getPrtgNetFlowOutputUri";
var URI_NET_FLOW_OUTPUT_CORE = "getPrtgNetFlowOutputCoreUri";
var URI_DEVICE_FAILURE = "getPrtgDeviceFailureUri";
var URI_DEVICE_FAILURE_REPORT = "getPrtgDeviceFailureReportUri";
var URI_PERFORMANCE = "getPrtgPerformanceUri";
var URI_PERFORMANCE_REPORT = "getPrtgPerformanceReportUri";
var URI_ABNORMAL_TRAFFIC = "getPrtgAbnormalTrafficUri";
var URI_EMAIL_UPDATE = "getEmailUpdateUri";

var OPEN_METHOD_IFRAME = "IFRAME";
var OPEN_METHOD_WINDOW = "WINDOW";

var openWindow;
var prtgLoginWindow;

function getPrtgUri(uriMethod, openMethod) {
	$.ajax({
		url : _ctx + '/prtg/' + uriMethod,
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
		},
		type : "POST",
		dataType : 'json',
		async: false,
		success : function(resp) {
			if (resp.code == '200') {
				var uri = resp.data.uri;
				
				if (openMethod == OPEN_METHOD_IFRAME) {
					$("#prtgFrame").attr('src', uri);
					
				} else if (openMethod == OPEN_METHOD_WINDOW) {
					openPrtgWindow(uri);
				}
				
			} else {
				alert(resp.message);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("取得PRTG資訊失敗，請重新操作或連繫系統管理員");
		}
	});
}

function getLoginUri() {
	$.ajax({
		url : _ctx + '/prtg/getLoginUri',
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
		},
		type : "POST",
		dataType : 'json',
		async: false,
		success : function(resp) {
			if (resp.code == '200') {
				openPrtgLoginSmallWindow(resp.data.uri);
				
			} else {
				alert(resp.message);
			}
		},
		error : function(xhr, ajaxOptions, thrownError) {
			alert("取得PRTG資訊失敗，請重新操作或連繫系統管理員");
		}
	});
}

function openPrtgWindow(_uri) {
	//var wdw = window.open("", "_prtg");
    //wdw.close();
  	var obj = $("#uriFrame").get(0).getBoundingClientRect();
  	//var x = parseInt(obj.left) + parseInt((window.screenX != undefined ? window.screenX : window.screenLeft)) + 30;
  	//var y = parseInt(obj.top) + parseInt((window.screenY != undefined ? window.screenY : window.screenTop)) + 100;
  	var menu = $("#uriFrame").get(0).getBoundingClientRect();
  	
  	var x = parseInt(menu.width) + 60;
  	var y = parseInt(menu.top) + 100;
  	
  	var width = obj.width;
  	var height = obj.height - 50;

  	openWindow = window.open(
  			_uri, 
  			"_prtg", 
  			"toolbar=no,scrollbars=yes,titlebar=no,status=no,menubar=no,location=no,resizable=yes,top="+y+",left="+x+",width="+width+",height="+height+"\"");

  	openWindow.focus();
	//prtgLoginWindow.blur();
	
	/*
	setTimeout(function() {
		closePrtgLoginSmallWindow()
	}, 6000);
	*/
}

function closeOpenWindow() {
	if (openWindow != null) {
		openWindow.close();
	}
}

function openPrtgLoginSmallWindow(_uri) {
	prtgLoginWindow = window.open(
  			_uri, 
  			"_prtgLogin", 
  			"toolbar=no,scrollbars=yes,titlebar=no,status=no,menubar=no,location=no,resizable=yes,top="+10000+",left="+10000+",width="+10+",height="+10+"\"");

  	//openWindow.focus();
	prtgLoginWindow.blur();
	
	/*
	setTimeout(function() {
		closePrtgWindow()
	}, 6000);
	*/
}

function closePrtgLoginSmallWindow() {
	prtgLoginWindow.close();
}

function adjustHeight() {
	var navAndMenuAndFooterHeight = 0;
	if ($('.mobile-menu').css('display') != 'none') {
		navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight() + $('.mobile-menu').outerHeight();
	} else {
		navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight();
	}
	
	var height = $(window).height() - navAndMenuAndFooterHeight - 10;
    $('#uriFrame').css('height', height);
}