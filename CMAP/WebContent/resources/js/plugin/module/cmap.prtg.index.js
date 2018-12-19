/**
 * 
 */
var _prtgIndexUri = $("meta[name='prtgIndexUri']").attr("content");

$(document).ready(function() {
	initMenuStatus("toggleMenu_prtg", "toggleMenu_prtg_items", "mp_index");
	
	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
	
	getPrtgIndexUri();
	
	//var cookie = '${IFRAME_COOKIE }';
	//console.log("cmap.prtg.index.js >>> " + cookie);
	//document.cookie = cookie;
	
	//$("#indexFrame").attr("src", "https://163.19.163.170:1443/welcome.htm");
});

function getPrtgIndexUri() {
	$.ajax({
		url : _ctx + '/prtg/getPrtgIndexUri',
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
    
  	var obj = $("#indexFrame").get(0).getBoundingClientRect();
  	var x = parseInt(obj.left) + parseInt((window.screenX != undefined ? window.screenX : window.screenLeft));
  	var y = parseInt(obj.top) + parseInt((window.screenY != undefined ? window.screenY : window.screenTop)) + 50;
  	var width = obj.width;
  	var height = obj.height;
  	
  	window.open(
  			_uri, 
  			"_prtg", 
  			"toolbar=no,scrollbars=yes,titlebar=no,status=no,menubar=no,location=no,resizable=yes,top="+y+",left="+x+",width="+width+",height="+height+"\"");
}
/*
var listener = function (e) {
    console.log('Caught ', e.type, ' event from ', e.source.self, ' to ', e.target, ' origin ', e.origin, ' with data ', e.data, '. Full details: ', e);
  };

  function addToFrame(el) {
    var frames = el.querySelectorAll('iframe'), ifrm = 0;
    for (var i = 0; i < frames.length; i++) {
      frames[i].contentWindow.addEventListener("message", listener, true);
      ifrm += addToFrame(frames[i].contentWindow.document);
    }
    return i + ifrm;
  }

  window.addEventListener("message", listener, true);
  var c = addToFrame(document);
  console.log('Recursively added listener to main window and', c, 'frames');
*/
function adjustHeight() {
	var navAndMenuAndFooterHeight = 0;
	if ($('.mobile-menu').css('display') != 'none') {
		navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight() + $('.mobile-menu').outerHeight();
	} else {
		navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight();
	}
	
	var height = $(window).height() - navAndMenuAndFooterHeight - 10;
    $('#indexFrame').css('height', height);
}