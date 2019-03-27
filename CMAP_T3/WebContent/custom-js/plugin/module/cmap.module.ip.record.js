/**
 * 
 */

$(document).ready(function() {
	initMenuStatus("toggleMenu_abnormalAlarm", "toggleMenu_abnormalAlarm_items", "ip_record");
	
	var d = new Date();
	var year = d.getFullYear();
	var month = d.getMonth() + 1 + "";
	month = month.length == 1 ? ("0" + month) : month;
	var date = d.getDate();
	var dateStr = year + "-" + month + "-" + date;
	
	$("#queryDateBegin").val(dateStr);
});



//查詢按鈕動作
function findData(from) {
	$(".myTableSection").show();
	
	if (typeof resutTable !== "undefined") {
		
	} else {
		resutTable = $('#resutTable').DataTable({
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