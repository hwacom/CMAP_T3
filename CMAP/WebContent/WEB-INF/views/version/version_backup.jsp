<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<!DOCTYPE html>
<section>

  <div class="container-fluid">
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div class="row search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
      	<form>
      		<div class="container-fluid">
	      	  <div class="form-group row">
	    	    <div class="col-lg-3 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 20%">群組</span>
	    	    	<form:select path="queryGroup1" id="queryGroup1" style="width: 75%" onchange="changeDeviceMenu('queryDevice1', this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${group1List}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-3 group-field-other">
					<span class="font-weight-bold" style="width: 20%">設備</span>
	    	    	<form:select path="queryDevice1" id="queryDevice1" style="width: 75%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device1List}" />
                    </form:select>
				</div>
				<div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch">查詢</button>
	    	    </div>
	      	  </div>
	      	</div>
		</form>
      </div>
      <!-- [END]查詢欄位bar -->
      <!-- [START]操作按鈕bar -->
      <div class="col-12 action-btn-bar">
        <div class="container-fluid">
        	<div class="row">
        		<div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnCompare">備份</button>
		  	    </div>
        	</div>
        </div>
      </div>
      <!-- [END]操作按鈕bar -->
    </div>
    <!-- [END]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
  </div>

</section>

<script>
	var resutTable;

	$(document).ready(function() {
		changeDeviceMenu("queryDevice1", $("#queryGroup1").val());
		
	});
	
	function changeDeviceMenu(deviceMenuObjId, groupId) {
		$( "select[id^='"+deviceMenuObjId+"'] option" ).remove();
		$( "select[id^='"+deviceMenuObjId+"']" ).append("<option value=''>=== ALL ===</option>");
		
		$.ajax({
			url : '${pageContext.request.contextPath}/base/getDeviceMenu',
			data : {
				groupId: groupId
			},
			type : "POST",
			dataType : 'json',
			async: false,
			success : function(resp) {

				if (resp.code == '200') {
					var obj = $.parseJSON(resp.data.device);
					$.each(obj, function(key, value){
						$( "select[id^='"+deviceMenuObjId+"']" ).append("<option value='"+key+"'>"+value+"</option>");
					});
				}
			},

			error : function(xhr, ajaxOptions, thrownError) {
				alert("error");
				alert(xhr.status);
				alert(thrownError);
			}
		});
	}
	
</script>
