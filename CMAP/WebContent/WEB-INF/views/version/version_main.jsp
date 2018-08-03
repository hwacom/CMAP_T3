<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<!DOCTYPE html>

<section>

  <div id="content" class="container-fluid">
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div id="search-bar-large" class="row search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
      	<form>
      		<div class="container-fluid">
	      	  <div class="form-group row">
	      	  	<div class="col-lg-1 group-field-other center">
	      	  		<span class="font-weight-bold group-title">第1組</span>
	      	  	</div>
	    	    <div class="col-lg-2 group-field-left">
	    	    	<span class="font-weight-bold" style="width: 25%">群組</span>
	    	    	<form:select path="queryGroup1" id="queryGroup1" style="width: 70%" onchange="changeDeviceMenu('queryDevice1', this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${group1List}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-2 group-field-middle">
					<span class="font-weight-bold" style="width: 25%">設備</span>
					<form:select path="queryDevice1" id="queryDevice1" style="width: 70%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device1List}" />
                    </form:select>
				</div>
	    	    <div class="col-lg-4 group-field-right">
	    	    	<span class="font-weight-bold" style="width: 20%">備份日期</span>
	    	    	<input type="date" id="queryExcuteDateBegin1" style="width: 35%">
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryExcuteDateEnd1" style="width: 35%">
	    	    </div>
	    	    <div class="col-lg-2 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 25%">分類</span>
					<form:select path="queryConfigType" id="queryConfigType" style="width: 70%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${configTypeList}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-1" style="padding-top: 5px;">
	    	    	<input type="checkbox" id="queryNewChkbox" value="" style="vertical-align:middle;" checked="checked">&nbsp;<span class="font-weight-bold">最新版</span>
	    	    </div>
	      	  </div>
	      	  
	      	  <div class="row">
	      	  	<div class="col-lg-1 group-field-other center">
	      	  		<span class="font-weight-bold group-title">第2組</span>
	      	  	</div>
	    	    <div class="col-lg-2 group-field-left">
	    	    	<span class="font-weight-bold" style="width: 25%">群組</span>
	    	    	<form:select path="queryGroup2" id="queryGroup2" style="width: 70%" onchange="changeDeviceMenu('queryDevice2', this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${group2List}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-2 group-field-middle">
					<span class="font-weight-bold" style="width: 25%">設備</span>
					<form:select path="queryDevice2" id="queryDevice2" style="width: 70%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device2List}" />
                    </form:select>
				</div>
	    	    <div class="col-lg-4 group-field-right">
	    	    	<span class="font-weight-bold" style="width: 20%">備份日期</span>
	    	    	<input type="date" id="queryExcuteDateBegin2" style="width: 35%">
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryExcuteDateEnd2" style="width: 35%">
	    	    </div>
	    	    <div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web">查詢</button>
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
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnCompare">比對</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnDelete">刪除</button>
		  	    </div>
        	</div>
        </div>
        <!-- [END]操作按鈕bar -->
      </div>
    </div>
    <!-- [END]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
    
    <!-- 查詢欄位 for 中小型解析度螢幕 -->
    <div id="search-bar-small-btn" class="row search-bar-small-btn">
  	  <button id="mobileMenuBtn" class="btn btn-success col-sm-12" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
	     	查詢條件 ▼
	  </button>
	</div>
	<div class="row search-bar-small">
	  <div class="col-sm-12 collapse" id="collapseExample" style="padding-top: 10px">
		  <div class="card card-body">
		  	<div class="col-12">
		  		<form>
		      	  <div class="form-group row">
		      	  	<div class="col-sm-12">
		      	  		<span class="font-weight-bold group-title">第1組</span>
		      	  	</div>
		      	  </div>
		      	  <div class="form-group row">
		      	  	<label for="group_1" class="col-sm-2 col-form-label">群組</label>
				    <div class="col-sm-10">
				      <form:select path="queryGroup1" id="queryGroup1_mobile" class="form-control form-control-sm" onchange="changeDeviceMenu('queryDevice1_mobile', this.value)">
	                  	<form:option value="" label="=== ALL ===" />
	                    <form:options items="${group1List}" />
	                  </form:select>
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	  	<label for="device_1" class="col-sm-2 col-form-label">設備</label>
		    	    <div class="col-sm-10">
		    	      <form:select path="queryDevice1" id="queryDevice1_mobile" class="form-control form-control-sm">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device1List}" />
                      </form:select>
				    </div>
				  </div>
				  <div class="form-group row">
				  	<label for="bkdate_begin_1" class="col-sm-2 col-form-label">備份日期</label>
				  	<div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateBegin1_mobile">
				    </div>
				    <div class="col-sm-1">~</div>
				    <div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateEnd1_mobile">
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		      	  	<div class="col-sm-12">
		      	  		<span class="font-weight-bold group-title">第2組</span>
		      	  	</div>
		      	  </div>
		      	  <div class="form-group row">
		      	  	<label for="group_2" class="col-sm-2 col-form-label">群組</label>
				    <div class="col-sm-10">
				      <form:select path="queryGroup2" id="queryGroup2_mobile" class="form-control form-control-sm" onchange="changeDeviceMenu('queryDevice2_mobile', this.value)">
	                  	<form:option value="" label="=== ALL ===" />
	                    <form:options items="${group2List}" />
	                  </form:select>
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	  	<label for="device_2" class="col-sm-2 col-form-label">設備</label>
		    	    <div class="col-sm-10">
		    	      <form:select path="queryDevice2" id="queryDevice2_mobile" class="form-control form-control-sm">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device2List}" />
                      </form:select>
				    </div>
				  </div>
				  <div class="form-group row">
				  	<label for="bkdate_begin_2" class="col-sm-2 col-form-label">備份日期</label>
				  	<div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateBegin2_mobile">
				    </div>
				    <div class="col-sm-1">~</div>
				    <div class="col-sm-4">
				      <input type="date" class="form-control form-control-sm" id="queryExcuteDateEnd2_mobile">
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	    <label for="device_2" class="col-sm-2 col-form-label">分類</label>
		    	    <div class="col-sm-10">
		    	      <form:select path="queryConfigType" id="queryConfigType_mobile" class="form-control form-control-sm">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${configTypeList}" />
                      </form:select>
				    </div>
				  </div>
				  <div class="form-group row">
					<div class="col-sm-12">
					  <input id="queryNewChkbox_mobile" type="checkbox" value="" style="vertical-align:middle;" checked="checked"> 最新版本
					</div>
				  </div>
				  <div class="form-group row">
		    	    <div class="col-sm-12">
				      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_mobile">查詢</button>
				    </div>
				  </div>
				</form>
		  	</div>
		  </div>
	  </div>
	</div>
	
	<!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div class="col-sm-12 myTableSection" style="display:none;">
		<table id="resutTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap">操作&nbsp;<input type="checkbox" id="checkAll" name="checkAll" /></th>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">群組名稱</th>
		      <th scope="col" nowrap="nowrap">設備名稱</th>
		      <th scope="col" nowrap="nowrap">設備系統版本</th>
		      <th scope="col" nowrap="nowrap">組態類型</th>
		      <th scope="col" nowrap="nowrap">版本號</th>
		      <th scope="col" nowrap="nowrap">備份時間</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
  </div>
  
</section>

<!-- Modal [Compare] start -->
<div class="modal fade" id="compareModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg" role="document">
    <div class="modal-content">
      <div class="modal-header">
      
        <h5 class="modal-title" id="exampleModalLabel"><span id="msgModal_title">版本比對</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      </div>
      <div class="modal-body">
        <div class="form-group row">
        	<div class="col-1"></div>
        	<label for="viewModal_version" class="col-md-1 col-sm-12 col-form-label">版號:</label>
    		<input type="text" class="form-control form-control-sm col-md-4 col-sm-12" id="viewModal_versionLeft" readonly>
    		<label for="viewModal_version" class="col-md-1 col-sm-12 col-form-label">版號:</label>
    		<input type="text" class="form-control form-control-sm col-md-4 col-sm-12" id="viewModal_versionRight" readonly>
        </div>
        <div class="form-group row">
        	<div class="form-control form-control-sm col-1 conpare-line" id="compareModal_contentLineNum"></div>
        	<div class="form-control form-control-sm col-5 conpare-content nowrap" id="compareModal_contentLeft"></div>
        	<div class="form-control form-control-sm col-5 conpare-content nowrap" id="compareModal_contentRight"></div>
        	<div class="col-1">
        		<span data-feather="chevrons-up" class="feather-compare" id="jumpToTop"></span>
        		<span data-feather="chevron-up" class="feather-compare" id="jumpToPre"></span>
        		<span data-feather="chevron-down" class="feather-compare" id="jumpToNext"></span>
        		<span data-feather="chevrons-down" class="feather-compare" id="jumpToBottom"></span>
        	</div>
        	<div class="col-12 center">
        		<span id="compareModal_summary"></span>
        	</div>
        </div>
      </div>
      <!-- 
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">關閉</button>
      </div>
       -->
    </div>
  </div>
</div>
<!-- Modal [Compare] end -->

<script>
	var currentDiffPos = -1;
	var diffPos = [];
	
	$(document).ready(function() {
		//初始化設備選單
		changeDeviceMenu("queryDevice1", $("#queryGroup1").val());
	    changeDeviceMenu("queryDevice2", $("#queryGroup2").val());
	    
	    //比對按鈕點擊事件
	    $('#btnCompare').click(function (e) {
	    	compareFile();
	    });
	    
	    //刪除按鈕點擊事件
	    $('#btnDelete').click(function (e) {
	    	deleteData();
	    });
	    
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
            
            $(".diffPos").css("background-color", ""); 
            $('.diffPos')[currentDiffPos].style.background = 'yellow';
            
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
            
            $(".diffPos").css("background-color", ""); 
            $('.diffPos')[currentDiffPos].style.background = 'yellow';
            
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
	
	//比對按鈕動作
	function compareFile() {
		var checkedItem = $('input[name=chkbox]:checked');
		
		//比對只能勾選兩個項目
		if (checkedItem.length > 2) {
			alert("版本比對僅能勾選兩筆檔案進行，請重新選擇");
			
		} else {
			var checkedObjArray = new Array();
			var obj;
			for (var i=0; i<checkedItem.length; i++) {
				obj = new Object();
				obj.name = 'versionId';
				obj.value = checkedItem[i].value;
				checkedObjArray.push(obj);
			}
			
			$.ajax({
				url : '${pageContext.request.contextPath}/version/compare',
				data : JSON.stringify(checkedObjArray),
				headers: {
				    'Accept': 'application/json',
				    'Content-Type': 'application/json'
				},
				type : "POST",
				dataType : 'json',
				async: false,
				success : function(resp) {
					if (resp.code == '200') {
						
						$('#compareModal_contentLineNum').html(resp.data.versionLineNum);
						$('#compareModal_contentLeft').html(resp.data.contentLeft);
						$('#compareModal_contentRight').html(resp.data.contentRight);
						$('#viewModal_versionLeft').val(resp.data.versionLeft);
						$('#viewModal_versionRight').val(resp.data.versionRight);
						
						currentDiffPos = 0;
						
						console.log("resp.data.diffPos: "+resp.data.diffPos);
						if (resp.data.diffPos != "") {
							diffPos = resp.data.diffPos.split(",");
							$('#compareModal_summary').html("共 " + diffPos.length + " 個差異");
							
						} else {
							$('#compareModal_summary').html("無差異");
						}
						
						$('#compareModal').modal('show');
						
					} else {
						alert(resp.message);
					}
				},

				error : function(xhr, ajaxOptions, thrownError) {
					alert('error');
					alert(xhr.status + "\n" + thrownError);
				}
			});
		}
	}
	
	//刪除按鈕動作
	function deleteData() {
		var checkedItem = $('input[name=chkbox]:checked');
		
		if (checkedItem.length == 0) {
			alert("請選擇要刪除的項目");
			return;
		}
		
		var val = confirm("確認刪除?");
		
		if (val) {
			var checkedObjArray = new Array();
			var obj;
			for (var i=0; i<checkedItem.length; i++) {
				obj = new Object();
				obj.name = 'versionId';
				obj.value = checkedItem[i].value;
				checkedObjArray.push(obj);
			}
			
			$.ajax({
				url : '${pageContext.request.contextPath}/version/delete',
				data : JSON.stringify(checkedObjArray),
				headers: {
				    'Accept': 'application/json',
				    'Content-Type': 'application/json'
				},
				type : "POST",
				dataType : 'json',
				async: false,
				success : function(resp) {
					if (resp.code == '200') {
						alert(resp.message);
						
						findData($('#queryFrom').val());
					}
				},

				error : function(xhr, ajaxOptions, thrownError) {
					alert('error');
					alert(xhr.status + "\n" + thrownError);
				}
			});
		}
	}
	
	//查詢按鈕動作
	function findData(from) {
		$('#queryFrom').val(from);
		$('input[name=checkAll]').prop('checked', false);
		
		if (from == 'MOBILE') {
			$('#collapseExample').collapse('hide');
		}
		
		if (typeof resutTable !== "undefined") {
			//resutTable.clear().draw(); server-side is enabled.
			resutTable.ajax.reload();
			
  		} else {
  			$(".myTableSection").show();
  			
  			resutTable = $('#resutTable').DataTable(
			{
				"autoWidth" 	: true,
				"paging" 		: true,
				"bFilter" 		: true,
				"ordering" 		: true,
				"info" 			: true,
				"serverSide" 	: true,
				"bLengthChange" : true,
				"pagingType" 	: "full",
				"processing" 	: true,
				"scrollX"		: true,
				"scrollY"		: dataTableHeight,
				"scrollCollapse": true,
				"language" : {
		    		"url" : "${pageContext.request.contextPath}/resources/js/dataTable/i18n/Chinese-traditional.json"
		        },
				"ajax" : {
					"url" : '${pageContext.request.contextPath}/version/getVersionInfoData.json',
					"type" : 'POST',
					"data" : function ( d ) {
						if ($('#queryFrom').val() == 'WEB') {
							d.queryGroup1 = $("#queryGroup1").val(),
							d.queryGroup2 = $("#queryGroup2").val(),
							d.queryDevice1 = $("#queryDevice1").val(),
							d.queryDevice2 = $("#queryDevice2").val(),
							d.queryDateBegin1 = $("#queryExcuteDateBegin1").val(),
							d.queryDateEnd1 = $("#queryExcuteDateEnd1").val(),
							d.queryDateBegin2 = $("#queryExcuteDateBegin2").val(),
							d.queryDateEnd2 = $("#queryExcuteDateEnd2").val();
							d.queryConfigType = $("#queryConfigType").val();
							d.queryNewChkbox = $("#queryNewChkbox").prop("checked") ? true : false;
						
						} else if ($('#queryFrom').val() == 'MOBILE') {
							d.queryGroup1 = $("#queryGroup1_mobile").val(),
							d.queryGroup2 = $("#queryGroup2_mobile").val(),
							d.queryDevice1 = $("#queryDevice1_mobile").val(),
							d.queryDevice2 = $("#queryDevice2_mobile").val(),
							d.queryDateBegin1 = $("#queryExcuteDateBegin1_mobile").val(),
							d.queryDateEnd1 = $("#queryExcuteDateEnd1_mobile").val(),
							d.queryDateBegin2 = $("#queryExcuteDateBegin2_mobile").val(),
							d.queryDateEnd2 = $("#queryExcuteDateEnd2_mobile").val();
							d.queryConfigType = $("#queryConfigType_mobile").val();
							d.queryNewChkbox = $("#queryNewChkbox_mobile").prop("checked") ? true : false;
						}
						
						return d;
					}
				},
				/*"order": [[6 , 'desc' ]],*/
				/*
				"initComplete": function(settings, json){
	            },
	            */
				"drawCallback" : function(settings) {
					//$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust();
					$("div.dataTables_length").parent().removeClass('col-sm-12');
					$("div.dataTables_length").parent().addClass('col-sm-6');
					$("div.dataTables_filter").parent().removeClass('col-sm-12');
					$("div.dataTables_filter").parent().addClass('col-sm-6');
					
					$("div.dataTables_info").parent().removeClass('col-sm-12');
					$("div.dataTables_info").parent().addClass('col-sm-6');
					$("div.dataTables_paginate").parent().removeClass('col-sm-12');
					$("div.dataTables_paginate").parent().addClass('col-sm-6');
				},
				"columns" : [
					{},{},
					{ "data" : "groupName" },
					{ "data" : "deviceName" },
					{ "data" : "systemVersion" },
					{ "data" : "configType" },
					{},
					{}
				],
				"columnDefs" : [
					{
						"targets" : [0],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render" : function(data, type, row) {
									 var html = '<input type="checkbox" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value='+row.versionId+'>';
									 return html;
								 }
					},
					{
						"targets" : [1],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render": function (data, type, row, meta) {
							       	return meta.row + meta.settings._iDisplayStart + 1;
							   	}
					},
					{
						"targets" : [6],
						"className" : "left",
						"searchable": true,
						"orderable": true,
						"render" : function(data, type, row) {
									 var html = '<a href="#" onclick="viewConfig(\''+row.versionId+'\')">'+row.configVersion+'</a>';
									 return html;
								 }
					},
					{
						"targets" : [7],
						"className" : "center",
						"searchable": false,
						"orderable": true,
						"render" : function(data, type, row) {
									 return row.backupTimeStr;
								 }
					}
				],
			});
  		}
	}
</script>
	