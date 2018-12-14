/**
 * 
 */

/**
 * Get inscribed area size
 *
 * @param int oW outer width
 * @param int oH outer height
 * @param int iW inner width
 * @param int iH inner height
 * @param bool R resize if smaller
 */
function getInscribedArea(oW, oH, iW, iH, R){
    if(!R && iW < oW && iH < oH){
        return {
            "h": iH,
            "w": iW
        };
    }
    if((oW / oH) > (iW / iH)){
        return {
            "h": oH,
            "w": Math.round(oH * iW / iH)
        }
    } else {
        return {
            "h": Math.round(oW * iH / iW),
            "w": oW
        };
    }
}

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