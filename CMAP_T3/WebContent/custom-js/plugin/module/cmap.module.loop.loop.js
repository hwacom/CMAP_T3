/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_abnormalAlarm", "toggleMenu_abnormalAlarm_items", "loop_loop");
	
	
});



//查詢按鈕動作
function findData(from) {
	$(".myTableSection").show();
	
	if (typeof resultTable !== "undefined") {
		
	} else {
		resultTable = $('#resultTable').DataTable({
			"autoWidth" 	: true,
			"paging" 		: true,
			"bFilter" 		: true,
			"ordering" 		: true,
			"info" 			: true,
			"serverSide" 	: false,
			"bLengthChange" : true,
			"pagingType" 	: "full",
			"processing" 	: true,
			"scrollX"		: true,
			"scrollY"		: dataTableHeight,
			"scrollCollapse": true,
			"pageLength"	: 100,
			"language" : {
	    		"url" : _ctx + "/resources/js/dataTable/i18n/Chinese-traditional.json"
	        },
		});
	}
}