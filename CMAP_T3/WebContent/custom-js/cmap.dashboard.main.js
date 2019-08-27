/**
 * 
 */
var odd, timer;
var blink_time_interval = 500;
var mapster_layer;

$(document).ready(function() {
	odd = 0;
	timer = null;
	enableMapSter1Layer();
});

function initMapSter() {
	console.log("before init mapster");
	$('#deviceSiteImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4,
        mapKey: 'data-key'
	})
	.mapster('set', true, 'room,prtg');

	//$('area').mapster('set', true);
	//$("#area_prtg").mapster('set', false);
	//blinkMapSter();
	
	console.log("after init mapster");
}

function enableMapSter1Layer() {
	mapster_layer = 1;
	$('#deviceSiteImg').mapster('unbind');
	$('#deviceSiteImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4,
        mapKey: 'data-key'
	})
	.mapster('set', true, 'room');
	
	blinkMapSter();
}

function enableMapSter2Layer() {
	mapster_layer = 2;
	$('#deviceSiteImg').mapster('unbind');
	$('#deviceSiteImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4,
        mapKey: 'data-key'
	})
	.mapster('set', true, 'prtg');
	
	blinkMapSter();
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
	}, blink_time_interval);
}

function showMapSter() {
	if (mapster_layer === 1) {
		$("area[data-key='room']").mapster('select');
	} else if (mapster_layer === 2) {
		$("area[data-key='prtg']").mapster('select');
	}
}

function hideMapSter() {
	if (mapster_layer === 1) {
		$("area[data-key='room']").mapster('deselect');
	} else if (mapster_layer === 2) {
		$("area[data-key='prtg']").mapster('deselect');
	}
}

function jumpTo(site, jumpToLayer) {
	odd = 0;
	clearTimeout(timer);
	//$('area').mapster('deselect');
	var jumpToUrl = "";
	
	switch (site) {
		case "MAIN":
			jumpToUrl = _ctx + '/resources/images/dashboard/left_top.png';
			break;
			
		case "ROOM":
			jumpToUrl = _ctx + '/resources/images/dashboard/left_top_2.png';
			break;
			
		case "PRTG":
			jumpToUrl = _ctx + '/resources/images/dashboard/alarm_2.png';
			break;
	}

	$("#deviceSiteImg").fadeTo(200, 0.3, function() {
		$("#label_deviceSiteImg").hide();
        
        if (site === "PRTG") {
        	$("#deviceSiteImg").hide();
        	$("div[id^=mapster_wrap]").hide();
        	
        } else {
        	$("#prtgIframe").hide();
        	$("#deviceSiteImg").attr('src', jumpToUrl);
        }
        
    }).fadeTo(200, 1, function() {
    	switch (site) {
			case "MAIN":
				$("#deviceSiteImg").show();
	    		enableMapSter1Layer();
				break;
				
			case "ROOM":
				$("#deviceSiteImg").show();
				$("#label_deviceSiteImg").show();
	    		enableMapSter2Layer();
				break;
				
			case "PRTG":
				$("#prtgIframe").show();
				$("#label_deviceSiteImg").show();
				break;
		}
    	
    	if (jumpToLayer === 2) {
			$("#a_deviceSiteImg").attr("onclick", "jumpTo('MAIN', 1)");
			
		} else if (jumpToLayer === 3) {
			$("#a_deviceSiteImg").attr("onclick", "jumpTo('ROOM', 2)");
		}
    });
}