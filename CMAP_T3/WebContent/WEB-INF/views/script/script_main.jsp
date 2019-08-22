<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<section>

  <div id="content" class="main">
  	<p class="content-title"><spring:message code="func.script.manage" /></p>
  	
    <!-- [START]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
	<div id="search-bar-large" class="section search-bar-large">
	  <!-- [START]查詢欄位bar -->
      <div class="col-12 search-bar">
      	<form>
      		<div class="container-fluid">
	      	  <div class="form-group row">
	    	    <div class="col-3 group-field-other">
	    	    	<span class="font-weight-bold" style="width: 30%"><spring:message code="script" /><spring:message code="type" /></span>
	    	    	<form:select path="scriptType" id="queryScriptType" style="width: 65%">
                        <form:option value="" label="=== ALL ===" />
                        <form:options items="${scriptTypeList}" />
                    </form:select>
	    	    </div>
				<div class="col-1">
	    	    	<button type="button" class="btn btn-primary btn-sm" style="width: 100%" id="btnSearch_web"><spring:message code="inquiry" /></button>
	    	    </div>
	      	  </div>
	      	</div>
		</form>
      </div>
      <!-- [END]查詢欄位bar -->
      
      <c:if test="${enableModify}">
      	  <!-- [START]操作按鈕bar -->
	      <div class="col-12 action-btn-bar">
	        <div class="container-fluid">
	        	<div class="row">
	        		<div class="col-1 action-btn-bar-style center">
			  	    	<button type="button" class="btn btn-success btn-sm" style="width: 100%" id="btnAdd"><spring:message code="script.add" /></button>
			  	    </div>
			  	    <div class="col-1 action-btn-bar-style center">
			  	    	<button type="button" class="btn btn-info btn-sm" style="width: 100%" id="btnModify_script"><spring:message code="script.modify" /></button>
			  	    </div>
			  	    <div class="col-1 action-btn-bar-style center">
			  	    	<button type="button" class="btn btn-danger btn-sm" style="width: 100%" id="btnDelete"><spring:message code="script.delete" /></button>
			  	    </div>
			  	    <div class="center" style="width: 3%">
			  	    	<span style="font-size: 1.5rem">|</span>
			  	    </div>
			  	    <div class="col-1 action-btn-bar-style center">
			  	    	<button type="button" class="btn btn-secondary btn-sm" style="width: 100%" id="btnModify_var"><spring:message code="variable.modify" /></button>
			  	    </div>
			  	    <div class="col-1 action-btn-bar-style center">
			  	    	<button type="button" class="btn btn-secondary btn-sm" style="width: 100%" id="btnModify_type"><spring:message code="script.type.modify" /></button>
			  	    </div>
	        	</div>
	        </div>
	      </div>
	      <!-- [END]操作按鈕bar -->
      </c:if>
    </div>
    <!-- [END]查詢欄位&操作按鈕 for 大型解析度螢幕 -->
    
    <!-- 查詢結果TABLE區塊 -->
	<div class="row">
	  <div class="col-12 myTableSection" style="display:none;">
		<table id="resultTable" class="dataTable myTable table-striped table-hover table-sm table-responsive-sm nowrap" style="width:100%;">
		  <thead class="center">
		    <tr>
		      <c:if test="${enableModify}">
		      	<!-- 有開放可以修改時才顯示操作欄位 -->
		      	<th scope="col" nowrap="nowrap"><spring:message code="action" /></th>
		      </c:if>
		      <c:if test="${not enableModify}">
		      	<th scope="col" nowrap="nowrap"></th>
		      </c:if>
		      <th scope="col" nowrap="nowrap"><spring:message code="seq" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="script.name" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="type" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="system.version" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="execute.script.content" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="execute.script.remark" /></th>
		      <!-- 
		      <th scope="col" nowrap="nowrap"><spring:message code="check.script.content" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="check.script.remark" /></th>
		       -->
		      <th scope="col" nowrap="nowrap"><spring:message code="create.time" /></th>
		      <th scope="col" nowrap="nowrap"><spring:message code="last.update.time" /></th>
		    </tr>
		  </thead>
		</table>
	  </div>
	</div>
	
  </div>

</section>

<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.script.main.min.js"></script>