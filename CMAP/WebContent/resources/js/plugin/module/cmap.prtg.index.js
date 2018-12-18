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

var listener = function (e) {
    console.log('Caught ', e.type, ' event from ', e.source.self, ' to ', e.target, ' origin ', e.origin, ' with data ', e.data, '. Full details: ', e);
  };

  function addToFrame(el) {
    var frames = el.querySelectorAll('iframe'), ifrm = 0;
    for (var i = 0; i < frames.length; i++) {
      frames[i].contentWindow.addEventListener("message", listener, true);
      ifrm += addToFrame(frames[i].contentWindow.document);
    }
    return i + ifrm;
  }

  window.addEventListener("message", listener, true);
  var c = addToFrame(document);
  console.log('Recursively added listener to main window and', c, 'frames');

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