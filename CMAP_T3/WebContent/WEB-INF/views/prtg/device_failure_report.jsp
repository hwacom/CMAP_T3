<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
  
   <style>
  	 body {
  		 overflow: hidden;
  	 }
   </style>
   
   <div id="content" class="container-fluid">
       <img src="${pageContext.request.contextPath}/resources/images/working.png" />
   </div>
  
   <!-- 
   <iframe id="prtgFrame" class="scrollbar-macosx" style="width: 98%; height: 650px; padding-left: 10px; padding-top: 10px;" frameborder="0" src="https://172.30.24.68/reports.htm?username=prtgadmin&password=Prtgadmin123">
  	 Failed to open PRTG main page.
   </iframe>
	 -->
	<!-- 
   <div id="uriFrame" style="width: 100%; height: 450px; padding-left: 10px; padding-top: 10px;" >
   		<img class="img" src="${pageContext.request.contextPath}/resources/images/dashboard/report.png" width="98%" height="650px" />
   </div>
    -->
   
<script src="${pageContext.request.contextPath}/resources/js/custom/min/plugin/prtg/cmap.prtg.common.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/custom/min/plugin/prtg/cmap.prtg.deviceFailure.report.min.js"></script>