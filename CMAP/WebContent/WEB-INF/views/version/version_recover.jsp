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
		  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnCompare">還原</button>
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
		  		<form>
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
		      <th scope="col" nowrap="nowrap">操作</th>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">群組</th>
		      <th scope="col" nowrap="nowrap">設備</th>
		      <th scope="col" nowrap="nowrap">系統版本</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
	
  </div>

</section>


<script>
	var resutTable;

	$(document).ready(function() {
		changeDeviceMenu("queryDevice1", $("#queryGroup1").val());
		
		//查詢按鈕(Web)點擊事件
	    $('#btnSearch_web').click(function (e) {
	    	findData('WEB');
	    });
	  	
	  	//查詢按鈕(Mobile)點擊事件
	    $('#btnSearch_mobile').click(function (e) {
	    	findData('MOBILE');
	    });
	});
	
	function resetTrBgColor() {
		$('tbody > tr').removeClass('mySelected');
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
						d.maxCountByDevice = true;
						
						if ($('#queryFrom').val() == 'WEB') {
							d.queryGroup1 = $("#queryGroup1").val(),
							d.queryDevice1 = $("#queryDevice1").val()
						
						} else if ($('#queryFrom').val() == 'MOBILE') {
							d.queryGroup1 = $("#queryGroup1_mobile").val(),
							d.queryDevice1 = $("#queryDevice1_mobile").val()
						}
						
						return d;
					}
				},
				"order": [[2 , 'asc' ]],
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
					{ "data" : "systemVersion" }
				],
				"columnDefs" : [
					{
						"targets" : [0],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render" : function(data, type, row) {
									 var html = '<input type="radio" id="radiobox" name="radiobox" onclick="resetTrBgColor();changeTrBgColor(this)" value='+row.versionId+'>';
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
	
</script>
