/**
 * 
 */
const URI_INDEX = "getPrtgIndexUri";
const URI_DASHBOARD = "getPrtgDashboardUri";
const URI_NET_FLOW_SUMMARY = "getPrtgNetFlowSummaryUri";

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
	var wdw = window.open("", "_prtg");
    wdw.close();
    
  	var obj = $("#uriFrame").get(0).getBoundingClientRect();
  	var x = parseInt(obj.left) + parseInt((window.screenX != undefined ? window.screenX : window.screenLeft));
  	var y = parseInt(obj.top) + parseInt((window.screenY != undefined ? window.screenY : window.screenTop)) + 50;
  	var width = obj.width;
  	var height = obj.height;
  	
  	openWindow = window.open(
  			_uri, 
  			"_prtg", 
  			"toolbar=no,scrollbars=yes,titlebar=no,status=no,menubar=no,location=no,resizable=yes,top="+y+",left="+x+",width="+width+",height="+height+"\"");

  	openWindow.focus();
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