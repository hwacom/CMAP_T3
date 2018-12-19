<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>

<!DOCTYPE html>

  <style>
  	body {
  		overflow: hidden;
  	}
  </style>
  
  <!-- 
  <iframe id="indexFrame" class="scrollbar-macosx" width=100% height=450px frameborder="0" src="${IFRAME_URI }?jsessionid=${pageContext.session.id}">
  	Failed to open PRTG main page.
  </iframe>
   -->
   <div id="indexFrame" style="width: 100%; height: 450px;" >&nbsp;</div>
              
<script src="${pageContext.request.contextPath}/resources/js/plugin/module/cmap.prtg.index.js"></script>