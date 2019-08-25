/**
 * 
 */
var odd, timer;

$(document).ready(function() {
	initMenuStatus("toggleMenu_am", "toggleMenu_am_items", "am_failure");
	
	odd = 0;
	timer = null;
	initMapSter();
	 
});

function initMapSter() {
	$('#deviceSiteImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4
	});

	$('area').mapster('set', true);
	//blinkMapSter();
}

function blinkMapSter() {
	var step = odd % 2;
	
	if (step == 0) {
		showMapSter();
		
	} else {
		hideMapSter()
	}
	
	odd++;
	
	timer = setTimeout(function() { 
		blinkMapSter();
	}, 1000);
}

function showMapSter() {
	$('area').mapster('select');
}

function hideMapSter() {
	$('area').mapster('deselect');
}

function jumpTo(site, jumpToLayer) {
	clearTimeout(timer);
	//$('area').mapster('deselect');
	var jumpToUrl = "";
	
	switch (site) {
		case "MAIN":
			jumpToUrl = _ctx + '/resources/images/dashboard/left_top.png';
			break;
			
		case "E8":
			jumpToUrl = _ctx + '/resources/images/dashboard/alarm_2.png';
			break;
	}

	$("#deviceSiteImg").fadeTo(200, 0.3, function() {
		$("#label_deviceSiteImg").hide();
        $("#deviceSiteImg").attr('src', jumpToUrl);
        
    }).fadeTo(200, 1, function() {
    	if (site !== "MAIN") {
    		$("#label_deviceSiteImg").show();
    		
    		if (jumpToLayer === 2) {
    			$("#a_deviceSiteImg").attr("onclick", "jumpTo('MAIN', 1)");
    		}
    	} else {
    		initMapSter();
    	}
    });
}