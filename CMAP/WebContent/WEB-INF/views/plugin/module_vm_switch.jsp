<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="../../common/taglib.jsp" %>
<section>

	<div id="content" class="container-fluid center">
	  <div class="row col-10">
	  	<span style="font-size: 3em; font-weight: bold;">Power OFF VM name : [</span>
	  	<span style="font-size: 3em; font-weight: bold; color: red;">${VM_NAME }</span>
	  	<span style="font-size: 3em; font-weight: bold;">]</span>
	  </div>
	  <div class="row col-10">
	  	<span style="font-size: 3em; font-weight: bold;">Request from IP_addr : [</span>
	  	<span style="font-size: 3em; font-weight: bold; color: red;">${IP_ADDR }</span>
	  	<span style="font-size: 3em; font-weight: bold;">]</span>
	  </div>
    </div>
  
</section>