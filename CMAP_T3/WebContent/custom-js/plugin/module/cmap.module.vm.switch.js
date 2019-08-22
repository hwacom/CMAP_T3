/**
 * 
 */
var _ctx = $("meta[name='ctx']").attr("content");
var source = null;
var switchResult;

$(document).ready(function() {
	
	$("#btnCheck").click(function() {
		checkVmStatus();
	});
	
	$("#btnGo").click(function() {
		confirm("請再次確認是否要開始執行切換?<br><font color=\"red\">** 切換過程無法暫停或中止 **</font>", "startSwitch");;
	});
	
	$("#btnClose").click(function() {
		$("#btnGo").attr("disabled", true);
		hideProcessing2();
		stopSSE();
		window.location.replace(_ctx + '/plugin/module/vmswitch/result');
	});
	
	stopSSE();
	
});

function hideProcessing2() {
	$(".mask").hide();
	$(".processing2").hide();
}

function showProcessing2() {
	$(".mask").show();
	$(".processing2").show();
}

function checkVmStatus() {
	var obj = new Object();
	
	$.ajax({
		url : _ctx + '/plugin/module/vmswitch/chkStatus',
		data : JSON.stringify(obj),
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
		},
		type : "POST",
		dataType : 'json',
		async: true,
		beforeSend : function(xhr) {
			showProcessing();
		},
		complete : function() {
			hideProcessing();
		},
		success : function(resp) {
			$("#vmStatusMsg").text(resp.data.VM_STATUS_MSG);
			$("#checkBtn").hide();
			$("#goMsg").show();
			$("#goBtn").show();
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
}

function startSSE() {
	if (!!window.EventSource) {
        source = new EventSource('vmswitch/push'); //为http://localhost:8080/testSpringMVC/push
        s = '';
        
        $('#msg_from_server').show();
        $('#msg_from_server').scrollTop = $('#msg_from_server').scrollHeight;
        
        source.addEventListener('message', function (e) {
        	//console.log("e.data: " + e.data);
        	var data = JSON.parse(e.data);
        	var time = data.time;
        	var step = data.step;
        	var result = data.result;
        	var msg = data.msg;
        	//console.log("step: " + step + " >> result: " + result + " >> msg: " + msg);
        	
        	if (step == '<CLOSE>') {
        		source.close();
        		
        	} else if (step != '<NONE>') {
        		
        		if (step == '<PROCESS_END>') {
        			msg = "======================== [ End ] ========================<br><br>";
        			source.close();
        			
        			$(".processing2").css("background", "none");
        			$("#btnClose").show();
        			
        			alert(switchResult);
        			
        		} else if (step == '<STEP_RESULT>') {
        			switch (result) {
        				case "<ERROR>":
        					msg = " >> <span style=\"color: #ff4747; vertical-align: top;\"><b>" + msg + "</b></span><br>";
        					break;
        					
        				case "<OK>":
        					msg = " >> <span style=\"color: #01ff01; vertical-align: top;\"><b>" + msg + "</b></span><br>";
        					break;
        					
        				case "<WAITING>":
        					msg = " >> <span style=\"color: #ff01ca; vertical-align: top;\"><b>" + msg + "</b></span><br>";
        					break;
        					
        				default:
        					msg = " >> " + msg + "<br>";
        					break;
        			}
        			
        		} else {
            		msg = "[" + time + "] " + step + " >> <span style=\"color: yellow; vertical-align: top;\">" + msg + "</span>";
            		
            		/*
            		s += e.data + "<br/>"
                    $("#msg_from_server").html(s);
                    */
            	}
        		
        		$('#msg_from_server').html(function(i, text) {
        		    return text + msg;
        		});
        		
        		$("#msg_from_server").animate({ scrollTop: $('#msg_from_server').prop("scrollHeight")}, 1000);
        	}
        });

        source.addEventListener('open', function (e) {
            console.log("連線開啟");
        }, false);

        source.addEventListener('error', function (e) {
            if (e.readyState == EventSource.CLOSED) {
                console.log("連線關閉");
                
            } else if (e.readyState != undefined) {
                console.log(e.readyState);
            }
        }, false);
        
    } else {
        console.log("瀏覽器不支援SSE!!");
    }
}

function stopSSE() {
	if (source != null) {
		source.close();
		console.log("關閉SSE");
	}
}

function startSwitch() {
	$('#msg_from_server').html('====================== [ Processing ] ======================<br>');
	var obj = new Object();
	
	$.ajax({
		url : _ctx + '/plugin/module/vmswitch/go',
		data : JSON.stringify(obj),
		headers: {
		    'Accept': 'application/json',
		    'Content-Type': 'application/json'
		},
		type : "POST",
		dataType : 'json',
		async: true,
		beforeSend : function(xhr) {
			showProcessing2();
			startSSE();
		},
		complete : function() {
			//hideProcessing();
		},
		success : function(resp) {
			switchResult = resp.message;
		},
		error : function(xhr, ajaxOptions, thrownError) {
			ajaxErrorHandler();
		}
	});
}

function ajaxErrorHandler() {
	alert('連線逾時，頁面將重新導向');
	location.reload();
}