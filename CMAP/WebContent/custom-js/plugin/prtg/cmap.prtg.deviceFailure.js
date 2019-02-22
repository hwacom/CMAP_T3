/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_abnormalAlarm", "toggleMenu_abnormalAlarm_items", "device_failure");

	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
	
	getLoginUri();
	getPrtgUri(URI_DEVICE_FAILURE);
});
