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
	getPrtgUri(URI_PERFORMANCE_REPORT, OPEN_METHOD_IFRAME);
});
