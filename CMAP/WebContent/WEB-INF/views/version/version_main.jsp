<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<!DOCTYPE html>

<section>

  <div id="content" class="container-fluid">
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div class="row search-bar-large">
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
	    	    <div class="col-lg-3 group-field-other">
	    	    	<input type="checkbox" id="" value="" style="vertical-align:middle;" checked="checked">&nbsp;<span class="font-weight-bold">最新版本</span>
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
                    <!-- 
	    	    	<select id="queryGroup2" style="width: 70%">
	    	    		<option value="">=== ALL ===</option>
	    	    	</select>
	    	    	-->
	    	    </div>
	    	    <div class="col-lg-2 group-field-middle">
					<span class="font-weight-bold" style="width: 25%">設備</span>
					<form:select path="queryDevice2" id="queryDevice2" style="width: 70%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${device2List}" />
                    </form:select>
                    <!-- 
	    	    	<select id="queryDevice2" style="width: 70%">
	    	    		<option value="">=== ALL ===</option>
	    	    	</select>
	    	    	-->
				</div>
	    	    <div class="col-lg-4 group-field-right">
	    	    	<span class="font-weight-bold" style="width: 20%">備份日期</span>
	    	    	<input type="date" id="queryExcuteDateBegin2" style="width: 35%">
	    	    	<span class="font-weight-bold center" style="width: 5%">~</span>
	    	    	<input type="date" id="queryExcuteDateEnd2" style="width: 35%">
	    	    </div>
	    	    <div class="col-lg-2" style="padding-top: 5px;">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch" onclick="findData('WEB')">查詢</button>
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
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnCompare" onclick="compareFile()">比對</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnDelete" onclick="deleteData()">刪除</button>
		  	    </div>
        	</div>
        </div>
        <!-- [END]操作按鈕bar -->
      </div>
    </div>
    <!-- [END]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
    
    <!-- 查詢欄位 for 中小型解析度螢幕 -->
    <div class="row search-bar-small-btn">
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
		    	    <div class="col-sm-12">
				      <input id="queryNewChkbox_mobile" type="checkbox" value="" style="vertical-align:middle;" checked="checked"> 最新版本
				    </div>
				  </div>
				  <div class="form-group row">
		    	    <div class="col-sm-12">
				      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch" onclick="findData('MOBILE')">查詢</button>
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
		      <th scope="col" nowrap="nowrap">操作&nbsp;<input type="checkbox" id="checkAll" name="checkAll" onclick="chkAll()" /></th>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">群組</th>
		      <th scope="col" nowrap="nowrap">設備</th>
		      <th scope="col" nowrap="nowrap">系統版本</th>
		      <th scope="col" nowrap="nowrap">版本號</th>
		      <th scope="col" nowrap="nowrap">備份時間</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
  </div>
  
  <input type="hidden" id="queryFrom" name="queryFrom" />
  
</section>

<!-- Modal -->
<div class="modal fade" id="compareModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
      
        <h5 class="modal-title" id="exampleModalLabel"><span id="msgModal_title"></span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      </div>
      <div class="modal-body">
      
        <span id="msgModal_content"></span>
        
      </div>
      <div class="modal-footer">
      
        <button type="button" class="btn btn-secondary" data-dismiss="modal">關閉</button>
        
      </div>
    </div>
  </div>
</div>

<script>
	var resutTable;					//DataTable
	var navAndMenuAndFooterHeight;	//導覽列+Mobile選單+Footer區塊高度
	var deductHeight;				//額外扣除高度 for DataTable資料呈顯區塊高度
	var dataTableHeight;			//DataTable資料呈顯區塊高度

	$(document).ready(function() {
		//$(document).ajaxStart($.blockUI).ajaxStop($.unblockUI);
		
		//計算DataTable區塊高度
		calHeight();
		
		//初始化設備選單
		changeDeviceMenu("queryDevice1", $("#queryGroup1").val());
	    changeDeviceMenu("queryDevice2", $("#queryGroup2").val());
	    
	    //縮放視窗大小時，延遲1秒後重算DataTable區塊高度 & 重繪DataTable
	    $(window).resize(_.debounce(function() {
	    	if (typeof resutTable !== "undefined") {
	    		calHeight();
	    		$('.dataTables_scrollBody').css('height', dataTableHeight);
	    		resutTable.clear().draw();
				resutTable.ajax.reload();
	    	}
	    }, 1000));
	} );
	
	function calHeight() {
		//計算dataTable區塊高度，以window高度扣除navbar、footer和mobile版選單高度
		if ($('.mobile-menu').css('display') != 'none') {
			navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight() + $('.mobile-menu').outerHeight();
		} else {
			navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight();
		}
		
		if ($('.search-bar-large').css('display') != 'none') {
			navAndMenuAndFooterHeight += $('.search-bar-large').outerHeight();
			deductHeight = 150;
		} else {
			deductHeight = 250;
		}
		
		dataTableHeight = (window.innerHeight-navAndMenuAndFooterHeight-deductHeight);
		
		//避免手機裝置橫向狀態下高度縮太小無法閱讀資料，設定最小高度限制為165px
		dataTableHeight = dataTableHeight < 165 ? 165 : dataTableHeight;
	}
	
	//群組選單連動設備選單
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
				alert(xhr.status + "\n" + thrownError);
			}
		});
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
				url : '${pageContext.request.contextPath}/version/viewConfig',
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
			resutTable.clear().draw();
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
						
						} else if ($('#queryFrom').val() == 'MOBILE') {
							d.queryGroup1 = $("#queryGroup1_mobile").val(),
							d.queryGroup2 = $("#queryGroup2_mobile").val(),
							d.queryDevice1 = $("#queryDevice1_mobile").val(),
							d.queryDevice2 = $("#queryDevice2_mobile").val(),
							d.queryDateBegin1 = $("#queryExcuteDateBegin1_mobile").val(),
							d.queryDateEnd1 = $("#queryExcuteDateEnd1_mobile").val(),
							d.queryDateBegin2 = $("#queryExcuteDateBegin2_mobile").val(),
							d.queryDateEnd2 = $("#queryExcuteDateEnd2_mobile").val();
						}
						
						return d;
					}
				},
				"order": [[6 , 'desc' ]],
				/*
				"initComplete": function(settings, json){
	            },
	            */
				"drawCallback" : function(settings) {
					$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust();
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
					{ "data" : "configVersion" },
					{ "data" : "backupTimeStr" }
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
					}
				],
			});
  		}
	}
	
	//勾選項目時調整該列資料<TR>底色
	function changeTrBgColor(obj) {
		var tr = obj.parentNode.parentNode;
		
		if (obj.checked) {
			tr.classList.add('mySelected');
		} else {
			tr.classList.remove('mySelected');
		}
	}

	//全選 or 取消全選
	function chkAll() {
		if ($('#checkAll').is(':checked')) {
			$('input[name=chkbox]').prop('checked', true);
			$('tbody > tr').addClass('mySelected');
			
		} else {
			$('input[name=chkbox]').prop('checked', false);
			$('tbody > tr').removeClass('mySelected');
		}
		
	}
	
</script>
	