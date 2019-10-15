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
	
	current_layer = MAP_ALL;
	next_layer = MAP_ALL;
	
	$("area[data-building]").hide();
	$("area[data-floor]").hide();
	$("area[data-room]").hide();
	
	enableMapSter4Layer1();
	//jumpTo("ALL", "ALL", 1);
});

function initMapSter() {
	/*
	console.log("before init mapster");
	$('#mapImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4,
        mapKey: 'data-key'
	})
	.mapster('set', true, 'building,floor,room');
	*/
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
        mapKey: 'data-building'
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
        mapKey: 'data-floor'
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
        mapKey: 'data-room'
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
		$("area[data-building='building']").mapster('select');
		
	} else if (mapster_layer === 2) {
		$("area[data-floor='floor']").mapster('select');
		
	} else if (mapster_layer === 3) {
		$("area[data-room='room']").mapster('select');
	}
}

function hideMapSter() {
	if (mapster_layer === 1) {
		$("area[data-building='building']").mapster('deselect');
		
	} else if (mapster_layer === 2) {
		$("area[data-floor='floor']").mapster('deselect');
		
	} else if (mapster_layer === 3) {
		$("area[data-room='room']").mapster('deselect');
	}
}

/*
 * 點擊告警位置時，控制 fadeOut 圖層邏輯
 */
function fadeOutControl(jumpToSite) {
	$("#label_mapImg").hide();
	$("#buildingFrames").hide();
	$("#floorFrames").hide();
    
    if (jumpToSite === MAP_ROOM) {
    	$("#mapImg").hide();
    	$("div[id^=mapster_wrap]").hide();
    	
    } else {
    	$("#prtgIframe").hide();
    	$("div[id^=mapster_wrap]").hide();
    	$("#mapImg").attr("src", "");
    }
}

// 機場[ALL]
function changeView2Layer1() {
	if (current_layer === MAP_ALL) {
		jumpToUrl = _ctx + '/resources/images/dashboard/map_layer_1.png';
		$("#mapImg").show();
		enableMapSter4Layer1();
		
	} else if (current_layer === MAP_BUILDING) {
		$("#mapster_wrap_0").hide();
		$("#buildingFrames").hide();
		$("#floorFrames").hide();
		
		$("#prtgImgFrame").animate({
			"height" : "325px"
		}, {
			duration: "normal",
			complete: function() {
				$("[data-sec=prtg]").animate({
					"width" : "820px"
				}, {
					duration: "normal",
				    complete: function() {
				    	jumpToUrl = _ctx + '/resources/images/dashboard/map_layer_1.png';
				    	
				    	$("#mapImg").fadeIn("normal", function() {
				    		$(this).attr({
					    		"src" : jumpToUrl,
					    		"width" : "820px",
					    		"height" : "325px",
					    		"display" : "inline"
					    	});
				    		
				    		enableMapSter4Layer1();
				    		$("#label_mapImg").hide();
				    		$("#bread").text("");
				    		
				    		//$("[data-sec=others]").show();
				    		$("[data-sec=others]").fadeIn("normal");
				    	});
				    }
				});
			}
		});
	}
}

// 建築物[BUILDING]
function changeView2Layer2() {
	var bread_text = "機場 > 第三航廈";
	
	if ((current_layer === MAP_ALL) || (current_layer === MAP_FLOOR)) {
		$("[data-sec=others]").hide();
		$("#mapster_wrap_0").hide();
		$("#floorFrames").hide();
		
		jumpToUrl = _ctx + '/resources/images/dashboard/map_layer_2.jpg';
		
		if (current_layer === MAP_ALL) {
			$("[data-sec=prtg]").animate({
				"width" : "1180px"
			}, {
				duration: "normal",
				complete: function() {
					$("#prtgImgFrame").animate({
						"height" : "580px"
					}, {
						duration: "normal",
					    complete: function() {
					    	$("#mapImg").fadeIn("normal", function() {
					    		$(this).attr({
						    		"src" : jumpToUrl,
						    		"width" : "auto",
						    		"height" : "580px",
						    		"display" : "inline"
						    	});
					    		
					    		enableMapSter4Layer2();
					    		$("#label_mapImg").show();
					    		$("#bread").text(bread_text);
					    	});
					    	
					    	$("#buildingFrames").fadeIn("normal");
					    }
					});
				}
			});
			
		} else if (current_layer === MAP_FLOOR) {
	    	$("#mapImg").fadeIn("normal", function() {
	    		$(this).attr({
		    		"src" : jumpToUrl,
		    		"width" : "auto",
		    		"height" : "580px",
		    		"display" : "inline"
		    	});
	    		
	    		enableMapSter4Layer2();
	    		$("#label_mapImg").show();
	    		$("#bread").text(bread_text);
	    		
	    		$("#buildingFrames").fadeIn("normal");
	    	});
		}
		
		$("#a_mapImg").attr("onclick", "jumpTo('BUILDING', 'ALL', 1)");
	}
}

// 樓層[FLOOR]
function changeView2Layer3() {
	var bread_text = "機場 > 第三航廈 > 3F(入境層)";
	
	if ((current_layer === MAP_BUILDING) || (current_layer === MAP_ROOM)) {
		$("[data-sec=others]").hide();
		$("#mapster_wrap_0").hide();
		$("#buildingFrames").hide();
		
		if (current_layer === MAP_ROOM) {
			$("#prtgIframe").hide();
		}
		
		jumpToUrl = _ctx + '/resources/images/dashboard/map_layer_3.png';
    	
    	$("#mapImg").fadeIn("normal", function() {
    		$(this).attr({
	    		"src" : jumpToUrl,
	    		"width" : "1180px",
	    		"height" : "395px", //428px
	    		"display" : "inline"
	    	});
    		
    		enableMapSter4Layer3();
    		$("#label_mapImg").show();
    		$("#bread").text(bread_text);
    		
    		$("#floorFrames").fadeIn("normal");
    	});
    	
    	$("#a_mapImg").attr("onclick", "jumpTo('FLOOR', 'BUILDING', 2)");
	}
}

// 房間[ROOM]
function changeView2Layer4() {
	var bread_text = "機場 > 第三航廈 > 3F(入境層) > 網路網路";
		
	if (current_layer === MAP_FLOOR) {
		$("[data-sec=others]").hide();
		$("#mapster_wrap_0").hide();
		$("#buildingFrames").hide();
		$("#floorFrames").hide();
    	
    	$("#prtgIframe").fadeIn("normal", function() {
    		$(this).attr({
	    		"width" : "100%",
	    		"height" : "100%", //428px
	    		"display" : "inline"
	    	});
    		
    		$("#label_mapImg").show();
    		$("#bread").text(bread_text);
    	});
    	
    	$("#a_mapImg").attr("onclick", "jumpTo('ROOM', 'FLOOR', 2)");
	}
}

/*
 * 點擊告警位置時，控制 fadeIn 圖層邏輯
 */
function fadeInControl(jumpToSite, jumpToLayer) {
	switch (jumpToSite) {
		case MAP_ALL:
			changeView2Layer1();
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

function jumpTo(currentSite, jumpToSite, jumpToLayer) {
	odd = 0;
	clearTimeout(timer);
	var jumpToUrl = "";
	
	switch (jumpToSite) {
		default:
			current_layer = currentSite;
			next_layer = jumpToSite;
		
		/*
		// 機場
		case MAP_ALL:
			jumpToUrl = _ctx + '/resources/images/dashboard/.png';
			break;
			
		// 機場 > 建築物
		case MAP_BUILDING:
			jumpToUrl = _ctx + '/resources/images/dashboard/map_layer_3.png';
			break;
		
		// 機場 > 建築物 > 樓層
		case MAP_FLOOR:
			jumpToUrl = _ctx + '/resources/images/dashboard/map_layer_3.png';
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