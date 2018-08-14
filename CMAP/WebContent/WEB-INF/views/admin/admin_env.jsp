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
		<div class="container-fluid">
      	  <div class="form-group row">
			<div class="col-lg-2" style="padding-top: 5px;">
    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web">查詢</button>
    	    </div>
    	    <div class="col-lg-4" style="padding-top: 5px;">
    	    	<span id="diffMsg" style="color: red; font-weight: bold; background-color: yellow;"></span>
    	    </div>
      	  </div>
      	</div>

      </div>
      <!-- [END]查詢欄位bar -->
      <!-- [START]操作按鈕bar -->
      <div class="col-12 action-btn-bar">
        <div class="container-fluid">
        	<div id="defaultActionBar" class="row">
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnAdd">新增</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnModify">修改</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnDelete">刪除</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
        			<button type="button" class="btn btn-secondary btn-sm" style="width: 100%" id="btnRefreshAll">refreshAll</button>
		  	    </div>
        	</div>
        	<div id="modifyActionBar" class="row" style="display: none">
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnModifySubmit">送出</button>
		  	    </div>
		  	    <div class="col-lg-2 action-btn-bar-style" align="center">
		  	    	<button type="button" class="btn btn-dark btn-sm" style="width: 100%" id="btnModifyCancel">取消</button>
		  	    </div>
        	</div>
        </div>
      </div>
      <!-- [END]操作按鈕bar -->
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
			  <div class="form-group row">
	    	    <div class="col-sm-12">
			      <button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_mobile">查詢</button>
			    </div>
			  </div>
		  	</div>
		  </div>
	  </div>
	</div>
	<!-- [END]查詢欄位 for 中小型解析度螢幕 -->
	
	<!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div class="col-sm-12 myTableSection" style="display:none;">
		<table id="resutTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <th scope="col" nowrap="nowrap">操作&nbsp;<input type="checkbox" id="checkAll" name="checkAll" /></th>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">備註</th>
		      <th scope="col" nowrap="nowrap">參數名稱</th>
		      <th scope="col" nowrap="nowrap">是否同步?</th>
		      <th scope="col" nowrap="nowrap">參數值(DB)</th>
		      <th scope="col" nowrap="nowrap">參數值(Env)</th>
		      <th scope="col" nowrap="nowrap">CREATE_TIME</th>
		      <th scope="col" nowrap="nowrap">CREATE_BY</th>
		      <th scope="col" nowrap="nowrap">UPDATE_TIME</th>
		      <th scope="col" nowrap="nowrap">UPDATE_BY</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
	
  </div>

</section>

<script>
	$(document).ready(function() {
		$("#btnRefreshAll").click(function() {
			envAction('refreshAll');
		});
		
		$("#btnModify").click(function() {
			changeModifyView();
		});
		
		$("#btnDelete").click(function() {
			envAction('delete');
		});
		
		$("#btnModifySubmit").click(function() {
			envAction('save');
		});
		
		$("#btnModifyCancel").click(function() {
			findData($("#queryFrom").val());
		});
	});
	
	function initActionBar() {
		$("#modifyActionBar").hide();
		$("#defaultActionBar").show();
		$(".dataTable").find("thead").eq(0).find("tr > th").css( 'pointer-events', 'auto' );
		
		$("select[name=resutTable_length]").removeAttr('disabled'); //開放分頁筆數選單
		$("input[type=search]").removeAttr('disabled'); //開放內文搜尋輸入框
	}
	
	function toggleActionBar() {
		$("#modifyActionBar").toggle();
		$("#defaultActionBar").toggle();
	}
	
	function changeModifyView() {
		var checkedItem = $('input[name=chkbox]:checked');
		
		if (checkedItem.length == 0) {
			alert('請先勾選欲修改的項目');
			return;
		}
		
		for (var i=0; i<checkedItem.length; i++) {
			
			var hasInnerText = (document.getElementsByTagName("body")[0].innerText !== undefined) ? true : false;
			
			$('input[name=chkbox]:checked:eq('+i+')').attr('disabled','disabled'); //關閉列表勾選框
			$('input[name=chkbox]:checked:eq('+i+')').parents("tr").children().eq(2).html(
				//切換「備註」欄位為輸入框
				function() {
					var html = '<input type="text" name="modifySettingRemark" value="' + $(this).text() +'" class="form-control form-control-sm" style="min-width: 200px" />';
					return html;
				}
			);
			$('input[name=chkbox]:checked:eq('+i+')').parents("tr").children().eq(3).html(
					//切換「參數名稱」欄位為輸入框
					function() {
						var html = '<input type="text" name="modifySettingName" value="' + $(this).text() +'" class="form-control form-control-sm" style="min-width: 200px" readonly/>';
						return html;
					}
				);
			$('input[name=chkbox]:checked:eq('+i+')').parents("tr").children().eq(5).html(
				//切換「參數值(DB)」欄位為輸入框
				function() {
					var html = '<input type="text" name="modifySettingValue" value="' + $(this).text() +'" class="form-control form-control-sm" style="min-width: 200px" />';
					return html;
				}
			);
		}
		
		toggleActionBar(); //切換action bar按鈕
		$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust(); //重繪表頭寬度
		$(".dataTable").find("thead").eq(0).find("tr > th").removeClass("sorting sorting_asc sorting_desc"); //移除表頭排序箭頭圖示
		$(".dataTable").find("thead").eq(0).find("tr > th").css( 'pointer-events', 'none' ); //移除表頭排序功能
		$("select[name=resutTable_length]").attr('disabled','disabled'); //關閉分頁筆數選單
		$("input[type=search]").attr('disabled','disabled'); //關閉內文搜尋輸入框
		$("li[id^=resutTable_]").addClass('disabled'); //關閉右下角分頁按鈕
	}
	
	function envAction(action) {
		var obj = new Object();
		
		var settingIds = $("input[name='chkbox']:checked").map(function() {
         	return $(this).val();
         }).get();
		
		obj.settingIds = settingIds;
		
		if (action == "save") {
			var modifySettingName = $("input[name='modifySettingName']").map(function() {
							        	return $(this).val();
							        }).get();
			var modifySettingValue = $("input[name='modifySettingValue']").map(function() {
							         	return $(this).val();
							         }).get();
			var modifySettingRemark = $("input[name='modifySettingRemark']").map(function() {
							         	 return $(this).val();
							          }).get();
			
			obj.modifySettingName = modifySettingName;
			obj.modifySettingValue = modifySettingValue;
			obj.modifySettingRemark = modifySettingRemark;
		}
		
		$.ajax({
			url : '${pageContext.request.contextPath}/admin/env/'+action,
			data : JSON.stringify(obj),
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
					findData($("#queryFrom").val());
					
				} else {
					alert('envAction > success > else :: resp.code: '+resp.code);
					alert(resp.message);
				}
			},
			error : function(xhr, ajaxOptions, thrownError) {
				ajaxErrorHandler();
			}
		});
	}
	
	//查詢按鈕動作
	function findData(from) {
		$("#queryFrom").val(from);
		$("input[name=checkAll]").prop("checked", false);
		
		if (from == "MOBILE") {
			$("#collapseExample").collapse("hide");
		}
		
		initActionBar();
		
		if (typeof resutTable !== "undefined") {
			//resutTable.clear().draw(); server-side is enabled.
			resutTable.ajax.reload();
			
  		} else {
  			$(".myTableSection").show();
  			
  			resutTable = $("#resutTable").DataTable(
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
					"url" : "${pageContext.request.contextPath}/admin/env/getEnvConfig.json",
					"type" : "POST",
					"data" : function ( d ) {},
					"dataSrc" : function (json) {
						$("#diffMsg").text(json.msg);
						return json.data;
					},
					"error" : function(xhr, ajaxOptions, thrownError) {
						ajaxErrorHandler();
					}
				},
				"order": [[3 , "asc" ]],
				"drawCallback" : function(settings) {
					$.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust();
					$("div.dataTables_length").parent().removeClass("col-sm-12");
					$("div.dataTables_length").parent().addClass("col-sm-6");
					$("div.dataTables_filter").parent().removeClass("col-sm-12");
					$("div.dataTables_filter").parent().addClass("col-sm-6");
					
					$("div.dataTables_info").parent().removeClass("col-sm-12");
					$("div.dataTables_info").parent().addClass("col-sm-6");
					$("div.dataTables_paginate").parent().removeClass("col-sm-12");
					$("div.dataTables_paginate").parent().addClass("col-sm-6");
					
					bindTrEvent();
				},
				"columns" : [
					{},{},
					{ "data" : "settingRemark" },
					{ "data" : "settingName" },
					{},
					{ "data" : "settingValue" },
					{ "data" : "_settingValue" },
					{ "data" : "createTimeStr" },
					{ "data" : "createBy" },
					{ "data" : "updateTimeStr" },
					{ "data" : "updateBy" },
				],
				"columnDefs" : [
					{
						"targets" : [0],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render" : function(data, type, row) {
									 var html = '<input type="checkbox" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value="'+row.settingId+'">';
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
						"targets" : [4],
						"className" : "center",
						"searchable": true,
						"orderable": true,
						"render": function (data, type, row, meta) {
									var html = row.same;
									if (!row.same) {
										html = '<font color="red">'+row.same+'</font>';
									}
							       	return html;
							   	}
					}
				],
			});
  		}
	}

</script>
