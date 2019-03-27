/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_prtg", "toggleMenu_prtg_items", "mp_netFlowSummary");

	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
	
	/*getLoginUri();*/
	closeOpenWindow();
	getPrtgUri(URI_NET_FLOW_SUMMARY, OPEN_METHOD_IFRAME);
});
