<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="taglib.jsp" %>
<!DOCTYPE html>

<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>CMAP</title>
	<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
	<meta name="description" content="">
    <meta name="author" content="">
    
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/resources/images/favicon.ico">
    
    <!-- Bootstrap Core CSS -->
    <link href="${pageContext.request.contextPath}/resources/css/bootstrap/bootstrap.min.css" rel="stylesheet">
    <!-- JQuery-UI -->
    <!-- <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.min.css" rel="stylesheet"> -->
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.structure.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/jquery-ui/jquery-ui.theme.min.css" rel="stylesheet">
    <!-- dataTable -->
	<link href="${pageContext.request.contextPath}/resources/DataTables/datatables.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/main.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/css/blog.css" rel="stylesheet">
	
	<!-- Core Javascript -->
	<script src="${pageContext.request.contextPath}/resources/js/jquery/jquery-3.3.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/resources/js/jquery-ui/jquery-ui.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/popper/popper.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/js/bootstrap/bootstrap.min.js"></script>
    <!-- Icons -->
    <script src="${pageContext.request.contextPath}/resources/js/feather-icons/feather.min.js"></script>
	<!-- dataTable -->
	<!-- <script src="${pageContext.request.contextPath}/resources/js/dataTable/jquery.dataTables.min.js"></script> -->
	<script src="${pageContext.request.contextPath}/resources/DataTables/datatables.min.js"></script>
	<!-- Underscore -->
	<script src="${pageContext.request.contextPath}/resources/js/underscore/underscore-min.js"></script>
	<!-- blockUI -->
	<script src="${pageContext.request.contextPath}/resources/js/blockUI/jquery.blockUI.js"></script>
	
</head>

<body>
    <nav class="navbar navbar-dark fixed-top flex-md-nowrap p-0 shadow navbar-bg">
      <a href="#">
      	<img class="img" src="${pageContext.request.contextPath}/resources/images/hwacom.png" width="auto" height="40" />
      	<span class="font-weight-bold title-font" style="color:#1C2269">組態設定管理系統</span>	
      </a>
      <ul class="navbar-nav">
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="${pageContext.request.contextPath}/logout"><span data-feather="log-out"></span></a>
        </li>
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="#" onclick="showProfile()"><span data-feather="user"></span></a>
        </li>
        <li class="nav-item text-nowrap">
          <a class="nav-link" href="#" onclick="toggleMenu()"><span id="menu-icon" data-feather="menu"></span></a>
        </li>
      </ul>
    </nav>

    <div class="container-fluid">
      <div class="row">
        <nav class="web-menu col-md-2 d-none d-md-block sidebar sidebar-bg">
          <div class="sidebar-sticky">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/version/manage">
                  <span data-feather="file-text"></span>
                  	<span>版本管理</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/version/backup">
                  <span data-feather="download"></span>
                  	<span>版本備份</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/version/restore">
                  <span data-feather="upload"></span>
                  	<span>版本還原</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/script">
                  <span data-feather="code"></span>
                  	<span>腳本管理</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/delivery">
                  <span data-feather="cast"></span>
                  	<span>供裝派送</span>
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="${pageContext.request.contextPath}/record">
                  <span data-feather="search"></span>
                  	<span>供裝紀錄</span>
                </a>
              </li>
              <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
              	  <li class="nav-item">
	                <a class="nav-link toggleMenuLink" id="toggleMenu_admin" href="#">
	                  <span data-feather="settings"></span>
	                  	<span>後臺管理&nbsp;<span id="toggleMenu_admin_icon" data-feather="chevron-down"></span></span>
	                </a>
	                <ul aria-expanded="false" id="toggleMenu_admin_items" class="collapse">
	                    <li class="nav-item subMenu-item">
	                    	<a href="${pageContext.request.contextPath}/admin/env/main">
	                    		<span data-feather="command"></span> 系統參數維護
	                    	</a>
	                    </li>
	                    <li class="nav-item subMenu-item">
	                    	<a href="${pageContext.request.contextPath}/admin/script/main">
	                    		<span data-feather="hash"></span> 預設腳本維護
	                    	</a>
	                    </li>
	                    <li class="nav-item subMenu-item">
	                    	<a href="${pageContext.request.contextPath}/admin/job/main">
	                    		<span data-feather="check-square"></span> 排程設定維護
	                    	</a>
	                    </li>
	                    <li class="nav-item subMenu-item">
	                    	<a href="${pageContext.request.contextPath}/admin/log/main">
	                    		<span data-feather="alert-triangle"></span> 系統LOG查詢
	                    	</a>
	                    </li>
	                </ul>
	              </li>
              </sec:authorize>
            </ul>
          </div>
        </nav>

        <!-- ============================================================== -->
        <!-- End Page wrapper  -->
        <!-- ============================================================== -->
        <div class="mobile-menu nav-scroller py-1 mb-2">
	        <nav class="nav d-flex justify-content-between">
	          <div>
	          	<span style="color: white;padding-top:9px;position: fixed;z-index: 999;background-color: #344e6a;width: 12px;height: 2.75rem;margin-top: -3px"><</span>
	          </div>
	          	<span class="p-2"><a href="${pageContext.request.contextPath}/version/manage">版本管理</a></span>
	            <span class="p-2" style="color:white">&nbsp;&nbsp;|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/version/backup">版本備份</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/version/recover">版本還原</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/script">腳本管理</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/delivery">供裝派送</a></span>
	            <span class="p-2" style="color:white">|</span>
	            <span class="p-2"><a class="p-2" href="${pageContext.request.contextPath}/record">供裝紀錄</a></span>
	          <div style="z-index: 9999;margin-right: 11px;margin-top: -3px;position: fixed;left: calc(100% - 15px);">
	          	<span style="color: white;padding-top:9px;background-color: #344e6a;width: 12px;height: 2.75rem;float: right">></span>
	          </div>
	        </nav>
	    </div>
		    
        <main role="main" class="ml-sm-auto col-md-10">
			<decorator:body />
        </main>
        
        <input type="hidden" id="queryFrom" name="queryFrom" />
        
        <!-- Modal [View] start -->
		<div class="modal fade" id="viewModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
		  <div class="modal-dialog modal-lg" role="document">
		    <div class="modal-content">
		      <div class="modal-header">
		        <h5 class="modal-title" id="exampleModalLabel"><span id="msgModal_title">版本內容預覽</span></h5>
		        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
		          <span aria-hidden="true">&times;</span>
		        </button>
		      </div>
		      <div class="modal-body">
		     	<div class="form-group row">
		        	<label for="viewModal_group" class="col-md-2 col-sm-12 col-form-label">群組</label>
		    		<input type="text" class="form-control form-control-sm col-md-9 col-sm-12" id="viewModal_group" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_device" class="col-md-2 col-sm-12 col-form-label">設備</label>
		    		<input type="text" class="form-control form-control-sm col-md-9 col-sm-12" id="viewModal_device" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_version" class="col-md-2 col-sm-12 col-form-label">版本號碼</label>
		    		<input type="text" class="form-control form-control-sm col-md-9 col-sm-12" id="viewModal_version" readonly>
		        </div>
		        <div class="form-group row">
		        	<label for="viewModal_content" class="col-md-2 col-sm-12 col-form-label">版本內容</label>
		        	<!-- <textarea class="form-control col-md-9 col-sm-12" id="viewModal_content" rows="10" readonly></textarea> -->
		        	<div class="form-control form-control-sm col-md-9 col-sm-12" id="viewModal_content" style="height: 300px;overflow: auto;"></div>
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
		<!-- Modal [View] end -->
        
        <footer role="footer" class="ml-sm-auto col-md-10 footer">
        	<span class="copyright">聯絡我們 | Copyright &copy; 2018-2019 HwaCom Systems Inc. All Rights Reserved.</span>	
        </footer>
        
      </div>
    </div>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    
    <script>
    	feather.replace();
      	
    	var resutTable;					//DataTable
    	var navAndMenuAndFooterHeight;	//導覽列+Mobile選單+Footer區塊高度
    	var deductHeight;				//額外扣除高度 for DataTable資料呈顯區塊高度
    	var dataTableHeight;			//DataTable資料呈顯區塊高度
    	var isWEB = true;
    	
    	$(document).ready(function() {
    		// get current URL path and assign 'active' class
			var pathname = window.location.pathname;
    		
    		if (pathname == '/CMAP/' || pathname == '/CMAP/index') {
    			$('.nav > .nav-item > a[href="/CMAP/version/manage"]').addClass('active');
    		} else {
    			$('.nav > .nav-item > a[href="'+pathname+'"]').addClass('active');
    		}
    		
    		if ($('.mobile-menu').is(':visible')) {
    			isWEB = false;
    		}
    		
    		$('.toggleMenuLink').click(function() {
    			var itemId = '#'+this.id+'_items';
    			var iconId = '#'+this.id+' > span > svg.feather-';
    			
    			$(itemId).toggleClass('collapse');
    			
    			if ($(itemId).hasClass('collapse')) {
    				iconId = iconId+'chevron-up';
    				$(iconId).replaceWith(feather.icons['chevron-down'].toSvg());
    			} else {
    				iconId = iconId+'chevron-down';
    				$(iconId).replaceWith(feather.icons['chevron-up'].toSvg());
    			}
    		});
    		
    		//計算DataTable區塊高度
    		calHeight();
    		
    		//縮放視窗大小時，延遲1秒後重算DataTable區塊高度 & 重繪DataTable
    	    $(window).resize(_.debounce(function() {
    	    	if (typeof resutTable !== "undefined") {
    	    		calHeight();
    	    		$('.dataTables_scrollBody').css('max-height', dataTableHeight);
    	    		resutTable.clear().draw();
    				resutTable.ajax.reload();
    				
    				if (typeof $("#checkAll") !== "undefined") {
    					$('input[name=checkAll]').prop('checked', false);
    				}
    				
    				if (typeof initActionBar === 'function') {
    					initActionBar();
    				}
    	    	}
    	    	if (typeof resutTable_errorLog !== "undefined") {
    	    		calHeight();
    	    		$('.dataTables_scrollBody').css('max-height', dataTableHeight);
    	    		resutTable_errorLog.clear().draw();
    	    		resutTable_errorLog.ajax.reload();
    				
    				if (typeof $("#checkAll") !== "undefined") {
    					$('input[name=checkAll]').prop('checked', false);
    				}
    				
    				if (typeof initActionBar === 'function') {
    					initActionBar();
    				}
    	    	}
    	    	if (typeof resutTable_jobLog !== "undefined") {
    	    		calHeight();
    	    		$('.dataTables_scrollBody').css('max-height', dataTableHeight);
    	    		resutTable_jobLog.clear().draw();
    	    		resutTable_jobLog.ajax.reload();
    				
    				if (typeof $("#checkAll") !== "undefined") {
    					$('input[name=checkAll]').prop('checked', false);
    				}
    				
    				if (typeof initActionBar === 'function') {
    					initActionBar();
    				}
    	    	}
    	    }, 1000));
    		
    	  	//全選 or 取消全選
    	    $('#checkAll').click(function (e) {
    	    	if ($(this).is(':checked')) {
    				$('input[name=chkbox]').prop('checked', true);
    				$('tbody > tr').addClass('mySelected');
    				
    			} else {
    				$('input[name=chkbox]').prop('checked', false);
    				$('tbody > tr').removeClass('mySelected');
    			}
    	    });
    	  
    	  	//查詢按鈕(Web)點擊事件
    	    $('#btnSearch_web').click(function (e) {
    	    	findData('WEB');
    	    });
    	  	
    	  	//查詢按鈕(Mobile)點擊事件
    	    $('#btnSearch_mobile').click(function (e) {
    	    	findData('MOBILE');
    	    });
    	  	
    	    //[組態檔內容-Modal] >> 開啟時將卷軸置頂
            $("#viewModal").on("shown.bs.modal", function () {
            	setTimeout(function() {
            		$('#viewModal_content').scrollTop(0);
            	}, 30);
            });
    	    
            $('[data-toggle="tooltip"]').tooltip({
            	'placement': 'top',
            	'html': true
            });
            
            $(".dataTable thead td").click(function() {
            	bindTrEvent();
            });
		});
    	
    	function bindTrEvent() {
    		$('.dataTable tbody tr').click(function(event) {
    			console.log('.dataTable tbody tr click');
                if (event.target.tagName !== 'A' && event.target.type !== 'checkbox') {
                  $(':checkbox', this).trigger('click');
                }
            });
    	}
    	
    	function uncheckAll() {
    		$('input[name=checkAll]').prop('checked', false);
    		$('input[name=chkbox]').prop('checked', false);
			$('tbody > tr').removeClass('mySelected');
			
    	}
    	
    	function initModal() {
    		$("input[name^=input]").val('');
    		$("textarea[name^=input]").val('');
    		$("select").prop("selectedIndex", 0);
    		$(".modal").find(':disabled').attr('disabled', false);
    		$("div[id^=sec_]").hide();
    	}
    	
    	//隱藏 or 顯示功能選單
    	function toggleMenu() {
    		if (isWEB) {
   				$('.web-menu').toggleClass('d-md-block');
       			$('main').toggleClass('col-md-10').toggleClass('col-md-12');
       			$('footer').toggleClass('col-md-10').toggleClass('col-md-12');
    			
    		} else {
    			$('.mobile-menu').toggleClass('d-none');
    			$('#search-bar-small-btn').toggleClass('search-bar-small-btn');
    		}
    		
    		changeMenuIconColor();
    		
    		//計算DataTable區塊高度
    		calHeight();
    		$(window).trigger('resize');
    	}
    	
    	//改變當前功能選單項目style
    	function changeMenuIconColor() {
    		$("#menu-icon").toggleClass('icon-hightlight');
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
    	
    	//計算DataTable可呈顯的區塊高度
    	function calHeight() {
    		//計算dataTable區塊高度，以window高度扣除navbar、footer和mobile版選單高度
    		if ($('.mobile-menu').css('display') != 'none') {
    			navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight() + $('.mobile-menu').outerHeight();
    		} else {
    			navAndMenuAndFooterHeight = $('.navbar').outerHeight() + $(".footer").outerHeight();
    		}
    		
    		if ($('.search-bar-large').css('display') != 'none') {
    			navAndMenuAndFooterHeight += $('.search-bar-large').outerHeight();
    			deductHeight = 160;
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
    				ajaxErrorHandler();
    			}
    		});
    	}
    	
    	//查看組態檔內容
    	function viewConfig(viewConfig) {
    		var obj = new Object();
    		obj.name = 'versionId';
    		obj.value = viewConfig;
    		
    		$.ajax({
    			url : '${pageContext.request.contextPath}/version/view',
    			data : JSON.stringify(obj),
    			headers: {
    			    'Accept': 'application/json',
    			    'Content-Type': 'application/json'
    			},
    			type : "POST",
    			async: false,
    			success : function(resp) {
    				if (resp.code == '200') {
    					$('#viewModal_group').val(resp.data.group);
    					$('#viewModal_device').val(resp.data.device);
    					$('#viewModal_version').val(resp.data.version);
    					$('#viewModal_content').html(resp.data.content);
    					
    					$('#viewModal').modal('show');
    					
    				} else {
    					alert(resp.message);
    				}
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
    	
    	$.fn.serializeObject = function()
    	{
    	    var o = {};
    	    var a = this.serializeArray();
    	    $.each(a, function() {
    	        if (o[this.name] !== undefined) {
    	            if (!o[this.name].push) {
    	                o[this.name] = [o[this.name]];
    	            }
    	            o[this.name].push(this.value || '');
    	        } else {
    	            o[this.name] = this.value || '';
    	        }
    	    });
    	    return o;
    	};
    	
    	//切換欄位顯示部分或全部內容
    	function changeShowRemarks(obj){						//obj是td
    	   var content = $(obj).attr("content");
    	   if (content != null && content != '') {
    	      if ($(obj).attr("isDetail") == 'true') {			//若當前是顯示全部，則切換成顯示部分
    	         $(obj).attr('isDetail', false);
    	         $(obj).html(getPartialRemarksHtml(content));
    	         $(obj).switchClass("cursor_zoom_out", "cursor_zoom_in");
    	      } else {											//若當前是顯示部分，則切換成顯示全部
    	         $(obj).attr('isDetail', true);
    	         $(obj).html(getTotalRemarksHtml(content));
    	         $(obj).switchClass("cursor_zoom_in", "cursor_zoom_out");
    	      }
    	   }
    	   
    	   $.fn.dataTable.tables( { visible: true, api: true } ).columns.adjust(); //調整dataTable欄位寬度
    	}
    	
    	//只顯示部分內容
    	function getPartialRemarksHtml(remarks) {
    		return remarks.substr(0,remarkShowLength) + '&nbsp;&nbsp;<a href="javascript:void(0);" >...(顯示)</a>';
    	}

    	//顯示全部內容
    	function getTotalRemarksHtml(remarks) {
    		return remarks + '&nbsp;&nbsp;<a href="javascript:void(0);" >(隱藏)</a>';
    	}

    </script>

</body>
</html>