<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>

  <style>
  	body {
  		overflow: hidden;
  	}
  </style>
  
  <input type="hidden" id="loginFlag" name="loginFlag" value="${DO_LOGIN }" />
  
  <iframe id="prtgFrame" class="scrollbar-macosx" width=100% height=700 frameborder="0" src="${IFRAME_URI }">
  	Failed to open PRTG main page.
  </iframe>
  
   <div id="uriFrame" style="width: 10px; height: 10px;" style="display: none;">&nbsp;</div>
    
<script src="${pageContext.request.contextPath}/resources/js/plugin/module/cmap.prtg.common.js"></script>          
<script src="${pageContext.request.contextPath}/resources/js/plugin/module/cmap.prtg.index.js"></script>