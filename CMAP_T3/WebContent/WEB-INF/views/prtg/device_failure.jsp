<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
  
   <style>
  	 body {
  		 overflow: hidden;
  	 }
   </style>
   
<!DOCTYPE html>
<section>
	<div class="main">
		<p class="content-title">障礙管理 > 即時監控</p>
		
		<iframe id="prtgFrame" class="scrollbar-macosx" width=100% height=610 frameborder="0" src="${IFRAME_URI }">
			Failed to open PRTG main page.
		</iframe>
		  
		<div id="uriFrame" style="width: 100%; height: 450px;" >&nbsp;</div>
	</div>
</section>
   
<script src="${pageContext.request.contextPath}/resources/js/custom/min/plugin/prtg/cmap.prtg.common.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/custom/min/plugin/prtg/cmap.prtg.deviceFailure.min.js"></script>