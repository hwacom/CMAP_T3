/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_plugin", "toggleMenu_plugin_items", "cm_ipBlockedRecord");

	$("#btnSearch_record_web").click(function(e) {
		$('#queryFrom').val("WEB");
		findBlockedIpRecordData();
	});
	
	$("#btnSearch_record_mobile").click(function(e) {
		$('#queryFrom').val("MOBILE");
		$('#collapseExample').collapse('hide');
		findBlockedIpRecordData();
	});
});
