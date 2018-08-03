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
	    	    	<form:select path="queryGroup1" id="queryGroup" style="width: 75%" onchange="changeDeviceMenu('queryDevice', this.value)">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${group1List}" />
                    </form:select>
	    	    </div>
	    	    <div class="col-lg-3 group-field-other">
					<span class="font-weight-bold" style="width: 20%">設備</span>
	    	    	<form:select path="queryDevice1" id="queryDevice" style="width: 75%">
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
		  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnBackup">備份</button>
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
				      <form:select path="queryGroup1" id="queryGroup_mobile" class="form-control form-control-sm" onchange="changeDeviceMenu('queryDevice_mobile', this.value)">
	                  	<form:option value="" label="=== ALL ===" />
	                    <form:options items="${group1List}" />
	                  </form:select>
				    </div>
		    	  </div>
		    	  <div class="form-group row">
		    	  	<label for="device_1" class="col-sm-2 col-form-label">設備</label>
		    	    <div class="col-sm-10">
		    	      <form:select path="queryDevice1" id="queryDevice_mobile" class="form-control form-control-sm">
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
		      <th scope="col" nowrap="nowrap">操作&nbsp;<input type="checkbox" id="checkAll" name="checkAll" /></th>
		      <th scope="col" nowrap="nowrap">序</th>
		      <th scope="col" nowrap="nowrap">群組名稱</th>
		      <th scope="col" nowrap="nowrap">設備名稱</th>
		      <th scope="col" nowrap="nowrap">設備系統版本</th>
		      <th scope="col" nowrap="nowrap">最新備份版本號</th>
		      <th scope="col" nowrap="nowrap">備份類型</th>
		      <th scope="col" nowrap="nowrap">最新備份時間</th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
	
  </div>

</section>

<!-- Modal [Backup_dialog] start -->
<div class="modal fade" id="backupDialogModal" tabindex="-1" role="dialog" aria-labelledby="backupDialogModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
      
        <h5 class="modal-title" id="backupDialogModalLabel"><span id="msgModal_title">操作確認</span></h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
        
      </div>
      <div class="modal-body">
        <form role="form" id="formEdit" name="formEdit">
            <div class="box-body">
            	<div class="form-group">
                  <label for="queryConfigType" class="control-label">請選擇要備份的組態檔類型:<span class="pull-right" style="color: red;">＊ </span></label>
                  <form:select path="queryConfigType" id="queryConfigType">
                      <form:option value="" label="兩者都要" />
                      <form:options items="${configTypeList}" />
                  </form:select>
                </div>                              
            </div>
            
            <div class="modal-footer">
        		<button type="button" class="btn btn-default" id="btnClose" data-dismiss="modal">關閉</button>
        		<button type="button" class="btn btn-success" id="btnConfirm">確認</button>
			</div>
        </form>
      </div>
    </div>
  </div>
</div>
<!-- Modal [Backup_dialog] end -->

<script>

	$(document).ready(function() {
		changeDeviceMenu("queryDevice", $("#queryGroup").val());
	    
	    //備份按鈕點擊事件
	    $('#btnBackup').click(function (e) {
	    	if (chkChecked()) {
	    		$("#backupDialogModal").modal();
	    		
	    	} else {
	    		alert('請勾選要備份的設備');
	    	}
	    });
	    
	    $('#btnConfirm').click(function (e) {
	    	doBackup();
	    });
	});
	
	function doBackup() {
		var checkedItem = $('input[name=chkbox]:checked');
		var configType = $('#queryConfigType').val();

		var checkedObjArray = new Array();
		var obj = new Object();
		obj.configType = configType;
		checkedObjArray.push(obj);
		
		for (var i=0; i<checkedItem.length; i++) {
			obj = new Object();
			obj.deviceListId = checkedItem[i].value;
			checkedObjArray.push(obj);
		}
		
		$.ajax({
			url : '${pageContext.request.contextPath}/version/backup/execute',
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
					
					setTimeout(function(){
						$("#backupDialogModal").modal('hide');
						
					}, 500);
				}
			},

			error : function(xhr, ajaxOptions, thrownError) {
				alert('error');
				alert(xhr.status + "\n" + thrownError);
				$("#backupDialogModal").modal('hide');
			}
		});
	}
	
	
	function chkChecked() {
		var hasChecked = false;
		
		$('input[type=checkbox][name=chkbox]').each(function () {
   	        if (this.checked) {
   	        	hasChecked = true;
   	         	return false;
   	        }
		});
		
		return hasChecked;
		//$("#btnBackup").prop( "disabled", disabled );
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
					"url" : '${pageContext.request.contextPath}/version/getDeviceListData.json',
					"type" : 'POST',
					"data" : function ( d ) {
						if ($('#queryFrom').val() == 'WEB') {
							d.queryGroup = $("#queryGroup").val(),
							d.queryDevice = $("#queryDevice").val()
						
						} else if ($('#queryFrom').val() == 'MOBILE') {
							d.queryGroup = $("#queryGroup_mobile").val(),
							d.queryDevice = $("#queryDevice_mobile").val()
						}
						
						return d;
					}
				},
				/*"order": [[2 , 'asc' ]],*/
				/*
				"initComplete": function(settings, json) {
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
					{},
					{ "data" : "configType" },
					{}
				],
				"columnDefs" : [
					{
						"targets" : [0],
						"className" : "center",
						"searchable": false,
						"orderable": false,
						"render" : function(data, type, row) {
									 var html = '<input type="checkbox" id="chkbox" name="chkbox" onclick="changeTrBgColor(this)" value='+row.deviceListId+'>';
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
						"targets" : [5],
						"className" : "left",
						"searchable": true,
						"orderable": true,
						"render" : function(data, type, row) {
									 var html = "";
									 if (row.versionId != null) {
										 html = '<a href="#" onclick="viewConfig(\''+row.versionId+'\')">'+row.configVersion+'</a>';
									 }
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
