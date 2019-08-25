/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_pm", "toggleMenu_pm_items", "pm_effience");

	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
	
	/*getLoginUri();*/
	closeOpenWindow();
	getPrtgUri(URI_ABNORMAL_TRAFFIC, OPEN_METHOD_IFRAME);
});
