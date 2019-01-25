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
	
	if ($("#loginFlag").val() == "Y") {
		getLoginUri();
	}
	getPrtgUri(URI_INDEX);
});
