/**
 * 
 */
var URI_INDEX = "getPrtgIndexUri";
var URI_DASHBOARD = "getPrtgDashboardUri";
var URI_NET_FLOW_SUMMARY = "getPrtgNetFlowSummaryUri";

var prtgLoginWindow;

function getPrtgUri(method) {
	$.ajax({
		url : _ctx + '/prtg/' + method,
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
		},
		type : "POST",
		dataType : 'json',
		async: false,
		success : function(resp) {
			if (resp.code == '200') {
				$("#prtgFrame").attr('src', resp.data.uri);
				
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
				openPrtgWindow(resp.data.uri);
				
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
	/*
	var wdw = window.open("", "_prtg");
    wdw.close();
    
  	var obj = $("#uriFrame").get(0).getBoundingClientRect();
  	var x = parseInt(obj.left) + parseInt((window.screenX != undefined ? window.screenX : window.screenLeft)) + 30;
  	var y = parseInt(obj.top) + parseInt((window.screenY != undefined ? window.screenY : window.screenTop)) + 100;
  	var width = obj.width;
  	var height = obj.height - 50;
  	*/
  	
	prtgLoginWindow = window.open(
  			_uri, 
  			"_prtg", 
  			"toolbar=no,scrollbars=yes,titlebar=no,status=no,menubar=no,location=no,resizable=yes,top="+10000+",left="+10000+",width="+10+",height="+10+"\"");

  	//openWindow.focus();
	prtgLoginWindow.blur();
	
	setTimeout(function() {
		closePrtgWindow()
	}, 3000);
}

function closePrtgWindow() {
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