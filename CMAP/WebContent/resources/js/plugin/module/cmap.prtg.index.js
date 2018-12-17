/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_prtg", "toggleMenu_prtg_items", "mp_index");
	
	adjustHeight();
	
	$(window).resize(_.debounce(function() {
		adjustHeight();
		
	}, 1000));
});

function adjustHeight() {
	var navAndMenuAndFooterHeight = 0;
	if ($('.mobile-menu').css('display') != 'none') {
		navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight() + $('.mobile-menu').outerHeight();
	} else {
		navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight();
	}
	
	var height = $(window).height() - navAndMenuAndFooterHeight - 15;
    $('iframe').css('height', height);
}