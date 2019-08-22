/**
 * 
 */
var currentDiffPos = -1;
var diffPos = [];
var diff = $("meta[name='diff']").attr("content");

$(document).ready(function() {
	diffPos = diff.split(",");
	
	if (diffPos != null && diffPos.length > 0) {
		$('#compareModal_summary').html("共 " + diffPos.length + " 個差異");
	} else {
		$('#compareModal_summary').html("無差異");
	}
	
    //[版本比對-Modal] >> 內容三區塊卷軸連動
    var combined = $("div[id$='compareModal_contentLeft']")
    				.add($("div[id$='compareModal_contentRight']"))
    				.add($("div[id$='compareModal_contentLineNum']"));
    
    combined.on("scroll", function () {
        combined.scrollTop($(this).scrollTop());
    });
    
    //[版本比對-Modal] >> 卷軸滾動事件
    $('#compareModal_contentLineNum').scroll(function() {
    	var position = $('#compareModal_contentLineNum').scrollTop();
    	var offsetHeight = $('#compareModal_contentLineNum').prop("offsetHeight");
    	var height = $('#compareModal_contentLineNum').prop("scrollHeight");
    	
    	if (position == 0) {
    		currentDiffPos = -1;
    	} else if ((position+offsetHeight) == height) {
    		currentDiffPos = diffPos.length;
    	}
    });
    
  	//[版本比對-Modal] >> 開啟時將卷軸置頂
    $("#compareModal").on("shown.bs.modal", function () {
    	currentDiffPos = -1;
    	
    	setTimeout(function() {
    		$('#compareModal_contentLineNum').scrollTop(0);
    	}, 300);
    });
    
  	//[版本比對-Modal] >> 卷軸移動至最頂端
    $('#jumpToTop').click(function (e) {
    	$(".diffPos").css("background-color", ""); 
    	
        $('#compareModal_contentLineNum').animate({
            scrollTop: 0
        }, '500');
        
        currentDiffPos = -1;
        
        $('.diffPos').removeClass("compare-current-pos");
        
        changeCompareDesc(false);
    });
    
  	//[版本比對-Modal] >> 卷軸移動至前一個差異行數
    $('#jumpToPre').click(function (e) {
    	if (currentDiffPos <= 0) {
        	currentDiffPos = diffPos.length - 1;
        } else {
        	currentDiffPos -= 1;
        }
    	
        $('#compareModal_contentLineNum').animate({
            scrollTop: $('.diffPos')[currentDiffPos].offsetTop
        }, '500');
        
        
        $('.diffPos').removeClass("compare-current-pos");
        $('.diffPos').eq(currentDiffPos).addClass("compare-current-pos");
        
        changeCompareDesc(true);
    });
    
  	//[版本比對-Modal] >> 卷軸移動至下一個差異行數
    $('#jumpToNext').click(function (e) {
    	if ((currentDiffPos+1) >= diffPos.length) {
        	currentDiffPos = 0;
        } else {
        	currentDiffPos += 1;
        }
    	
        $('#compareModal_contentLineNum').animate({
            scrollTop: $('.diffPos')[currentDiffPos].offsetTop
        }, '500');
        
        $('.diffPos').removeClass("compare-current-pos");
        $('.diffPos').eq(currentDiffPos).addClass("compare-current-pos");
        
        changeCompareDesc(true);
    });
    
  	//[版本比對-Modal] >> 卷軸移動至最底部
    $('#jumpToBottom').click(function (e) {
    	$(".diffPos").css("background-color", ""); 
    	
    	var offsetHeight = $('#compareModal_contentLineNum').prop("offsetHeight");
    	var scrollHeight = $('#compareModal_contentLineNum').prop("scrollHeight");
    	
        $('#compareModal_contentLineNum').animate({
            scrollTop: scrollHeight - offsetHeight
        }, '500');
        
        $('.diffPos').removeClass("compare-current-pos");
        
        changeCompareDesc(false);
    });
} );

function changeCompareDesc(showNow) {
	if (showNow) {
		$('#compareModal_summary').html("共 " + diffPos.length + " 個差異；第 " + (currentDiffPos+1) + " 個差異點");
	} else {
		$('#compareModal_summary').html("共 " + diffPos.length + " 個差異");
	}
}