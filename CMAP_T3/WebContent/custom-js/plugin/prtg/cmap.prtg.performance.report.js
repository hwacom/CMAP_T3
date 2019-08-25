/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_pm", "toggleMenu_pm_items", "pm_report");

	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
	
	/*getLoginUri();*/
	closeOpenWindow();
	getPrtgUri(URI_DEVICE_FAILURE, OPEN_METHOD_IFRAME);
});
