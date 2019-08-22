/**
 * 
 */
var _ctx = $("meta[name='ctx']").attr("content");
	
$(document).ready(function() {
	$("#btn_getPasshash").click(function() {
		getPasshash();
	});
});

function getPasshash() {
	var obj = new Object();
	obj.groupId = $("#input_SourceId").val();
	obj.role =  $("#input_Role").val().split(',');
	obj.account = $("#input_Account").val();
	
	$.ajax({
		url : _ctx + '/prtg/getPasshash',
		data : JSON.stringify(obj),
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
		},
		type : "POST",
		dataType : 'json',
		async: true,
		beforeSend : function() {
			showProcessing();
		},
		complete : function() {
			hideProcessing();
		},
		success : function(resp) {
			$("#div_Result").html(JSON.stringify(resp, undefined, 2));
		},
		error : function(xhr, ajaxOptions, thrownError) {
			$("#div_Result").html(thrownError);
		}
	});
}
