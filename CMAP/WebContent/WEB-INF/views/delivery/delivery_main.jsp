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
	    	    	<span class="font-weight-bold" style="width: 30%">查詢角度</span>
	    	    	<select id="searchBySelect" style="width: 50%" onchange="changeSearchBy()">
	    	    		<option value="device" selected="selected">設備</option>
	    	    		<option value="script">腳本</option>
	    	    	</select>
	    	    </div>
	    	    <div id="searchBy_script" class="col-lg-3 group-field-full" style="display: none;">
					<span class="font-weight-bold" style="width: 30%">腳本類別</span>
	    	    	<select id="device_1" style="width: 65%">
	    	    		<option value="">=== ALL ===</option>
	    	    	</select>
				</div>
				<div id="searchBy_device_1" class="col-lg-3 group-field-left" style="display: inline;">
					<span class="font-weight-bold" style="width: 20%">群組</span>
					<form:select path="group" id="group" style="width: 75%" onchange="changeDeviceMenu(this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${groupList}" />
                    </form:select>
				</div>
				<div id="searchBy_device_2" class="col-lg-3 group-field-right" style="display: inline;">
					<span class="font-weight-bold" style="width: 20%">設備</span>
	    	    	<form:select path="device" id="device" style="width: 75%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${deviceList}" />
                    </form:select>
				</div>
				<div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch">派送</button>
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
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnCompare">還原</button>
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
	$(document).ready(function() {
		changeSearchBy();	
	});
	
	function changeSearchBy() {
		var selectVal = $('#searchBySelect').val();
		if (selectVal == "script") {
			$('#searchBy_script').show();
			$('#searchBy_device_1').hide();
			$('#searchBy_device_2').hide();
		} else {
			$('#searchBy_script').hide();
			$('#searchBy_device_1').show();
			$('#searchBy_device_2').show();
		}
	}
	
	function changeDeviceMenu(groupId) {
		$("#device option").remove();
		$("#device").append("<option value=''>=== ALL ===</option>");
		
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
						$("#device").append("<option value='"+key+"'>"+value+"</option>");
					});
					
				} else {
					alert(resp);
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
