/**
 * 
 */

$(document).ready(function() {
	
	initMapSter();
	 
});

function initMapSter() {
	$('#deviceSiteImg').mapster({
     	fillColor: 'ff0000',
        fillOpacity: 0.4
	});
	 
	$('area').mapster('set',true);
}

function jumpTo(site, jumpToLayer) {
	var jumpToUrl = "";
	
	switch (site) {
		case "MAIN":
			jumpToUrl = _ctx + '/resources/images/dashboard/left_top.png';
			break;
			
		case "E8":
			jumpToUrl = _ctx + '/resources/images/dashboard/right_top.png';
			break;
	}
	
	$("#deviceSiteImg").fadeTo(200, 0.3, function() {
		$("#label_deviceSiteImg").hide();
        $("#deviceSiteImg").attr('src', jumpToUrl);
        
    }).fadeTo(200, 1, function() {
    	initMapSter();
    	
    	if (site !== "MAIN") {
    		$("#label_deviceSiteImg").show();
    		
    		if (jumpToLayer === 2) {
    			$("#a_deviceSiteImg").attr("onclick", "jumpTo('MAIN', 1)");
    		}
    	}
    });
}