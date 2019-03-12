/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_prtg", "toggleMenu_prtg_items", "mp_dashboard");

	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
	
	/*getLoginUri();*/
	closeOpenWindow();
	getPrtgUri(URI_DASHBOARD, OPEN_METHOD_IFRAME);
});
