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
	
	/*
	if ($("#loginFlag").val() == "Y") {
		getLoginUri();
	}
	*/
	/*getLoginUri();*/
	
	if ($("#loginFlag").val() == "Y") {
		getPrtgUri(URI_LOGIN, OPEN_METHOD_WINDOW);
		
	} else {
		getPrtgUri(URI_INDEX, OPEN_METHOD_WINDOW);
	}
});
