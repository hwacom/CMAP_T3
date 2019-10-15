/**
 * 
 */
const MAP_ALL = "ALL";
const MAP_BUILDING = "BUILDING";
const MAP_FLOOR = "FLOOR";
const MAP_ROOM = "ROOM";

var odd, timer;
var blink_time_interval = 500;
var mapster_layer;
var current_layer, next_layer;

$(document).ready(function() {
	odd = 0;
	timer = null;
	enableMapSter4Layer1();
	
	current_layer = MAP_ALL;
	next_layer = MAP_ALL;
});

function initMapSter() {
	console.log("before init mapster");
	$('#mapImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4,
        mapKey: 'data-key'
	})
	.mapster('set', true, 'building,floor,room');

	//$('area').mapster('set', true);
	//$("#area_prtg").mapster('set', false);
	//blinkMapSter();
	
	console.log("after init mapster");
}

/*
 * mapster_layer = 1 :: 機場 > [建築物]
 */
function enableMapSter4Layer1() {
	console.log("----- enableMapSter4Layer1 -----");
	mapster_layer = 1;
	$('#mapImg').mapster('unbind');
	$('#mapImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4,
        mapKey: 'data-key'
	})
	.mapster('set', true, 'building');
	
	blinkMapSter();
}

/*
 * mapster_layer = 2 :: 機場 > 建築物 > [樓層]
 */
function enableMapSter4Layer2() {
	console.log("----- enableMapSter4Layer2 -----");
	mapster_layer = 2;
	$('#mapImg').mapster('unbind');
	$('#mapImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4,
        mapKey: 'data-key'
	})
	.mapster('set', true, 'floor');
	
	blinkMapSter();
}

/*
 * mapster_layer = 3 :: 機場 > 建築物 > 樓層 > [房間]
 */
function enableMapSter4Layer3() {
	console.log("----- enableMapSter4Layer3 -----");
	mapster_layer = 3;
	$('#mapImg').mapster('unbind');
	$('#mapImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4,
        mapKey: 'data-key'
	})
	.mapster('set', true, 'room');
	
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

/*
 * mapster_layer = 1 :: 機場 > [建築物]
 * mapster_layer = 2 :: 機場 > 建築物 > [樓層]
 * mapster_layer = 3 :: 機場 > 建築物 > 樓層 > [房間]
 * mapster_layer = 4 :: 機場 > 建築物 > 樓層 > 房間 > [設備]
 */
function showMapSter() {
	if (mapster_layer === 1) {
		$("area[data-key='building']").mapster('select');
		
	} else if (mapster_layer === 2) {
		$("area[data-key='floor']").mapster('select');
		
	} else if (mapster_layer === 3) {
		$("area[data-key='room']").mapster('select');
	}
}

function hideMapSter() {
	if (mapster_layer === 1) {
		$("area[data-key='building']").mapster('deselect');
		
	} else if (mapster_layer === 2) {
		$("area[data-key='floor']").mapster('deselect');
		
	} else if (mapster_layer === 3) {
		$("area[data-key='room']").mapster('deselect');
	}
}

/*
 * 點擊告警位置時，控制 fadeOut 圖層邏輯
 */
function fadeOutControl(jumpToSite) {
	$("#label_mapImg").hide();
    
    if (jumpToSite === MAP_ROOM) {
    	$("#mapImg").hide();
    	$("div[id^=mapster_wrap]").hide();
    	
    } else {
    	$("#prtgIframe").hide();
    	$("#mapImg").attr('src', jumpToUrl);
    }
}

function changeView2Layer1() {
	jumpToUrl = _ctx + '/resources/images/dashboard/left_top.png';
	$("#mapImg").show();
}

function changeView2Layer2() {
	$("#a_mapImg").attr("onclick", "jumpTo('ALL', 1)");
}

function changeView2Layer3() {
	if (current_layer === MAP_ALL) {
		$("#prtgIframeTitle").animate({
			"width" : "1180px"
		}, {
			duration: 1000,
		    complete: function() {
		    	jumpToUrl = _ctx + '/resources/images/dashboard/left_top_2.png';
		    	
		    	$("#mapImg").show();
				$("#label_mapImg").show();
	    		enableMapSter4Layer2();
		    }
		});
	}
	
	$("#a_mapImg").attr("onclick", "jumpTo('FLOOR', 2)");
}

function changeView2Layer4() {
	$("#prtgIframe").show();
	$("#label_mapImg").show();
}

/*
 * 點擊告警位置時，控制 fadeIn 圖層邏輯
 */
function fadeInControl(jumpToSite, jumpToLayer) {
	switch (jumpToSite) {
		case MAP_ALL:
			changeView2Layer1();
			enableMapSter4Layer1();
			break;
			
		case MAP_BUILDING:
			changeView2Layer2();
			break;
			
		case MAP_FLOOR:
			changeView2Layer3();
			break;
			
		case MAP_ROOM:
			changeView2Layer4();
			break;
	}
}

function jumpTo(jumpToSite, jumpToLayer) {
	odd = 0;
	clearTimeout(timer);
	//$('area').mapster('deselect');
	var jumpToUrl = "";
	
	switch (jumpToSite) {
		default:
			next_layer = jumpToSite;
		
		/*
		// 機場
		case MAP_ALL:
			jumpToUrl = _ctx + '/resources/images/dashboard/left_top.png';
			break;
			
		// 機場 > 建築物
		case MAP_BUILDING:
			jumpToUrl = _ctx + '/resources/images/dashboard/left_top_2.png';
			break;
		
		// 機場 > 建築物 > 樓層
		case MAP_FLOOR:
			jumpToUrl = _ctx + '/resources/images/dashboard/left_top_2.png';
			break;
			
		// 機場 > 建築物 > 樓層 > 房間
		case MAP_ROOM:
			jumpToUrl = _ctx + '/resources/images/dashboard/alarm_2.png';
			break;
		*/
	}

	$("#mapImg").fadeTo(200, 0.3, function() {
		fadeOutControl(jumpToSite);
        
    }).fadeTo(200, 1, function() {
    	fadeInControl(jumpToSite, jumpToLayer);
    });
}