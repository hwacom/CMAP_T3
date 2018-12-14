/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_prtg", "toggleMenu_prtg_items", "mp_dashboard");

	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
});


function adjustHeight() {
	var height = $(window).height() - 25;
    $('iframe').css('height', height * 0.9 | 0);
}