/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_am", "toggleMenu_am_items", "am_failure");

	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
	
	/*getLoginUri();*/
	closeOpenWindow();
	getPrtgUri(URI_DEVICE_FAILURE, OPEN_METHOD_IFRAME);
});
