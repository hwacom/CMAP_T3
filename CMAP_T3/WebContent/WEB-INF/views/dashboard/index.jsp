<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<section>
	<div class="main">
		<p class="content-title">監控總覽</p>
		<div style="display: flex; flex-wrap: wrap;">
			<div data-sec="prtg" style="margin-left: 0px; width: 820px;">
				<div>
					<a id="a_mapImg" href="#" style="position: absolute;">
						<label id="label_mapImg" for="mapImg" class="mapLabel" style="display: none">回上一層</label>
					</a>
				</div>
				<div style="position: absolute; color: yellow; padding-top: 4px; padding-left: 95px;">
					<span id="bread"></span>
				</div>
				<div id="prtgIframeTitle" style="width: 100%; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
					<span>即時地理位置告警圖</span>
				</div>
				<div id="prtgImgFrame" class="row" style="width: 100%; height: 325px; background-color: #fff;">
					<iframe id="prtgIframe" width="100%" height="580px" frameborder="0" style="display:none;" 
						src="https://msi:1443/public/mapshow.htm?id=2234&mapid=F5DFD747-2802-4A32-862D-2C11EF7B6040">
					</iframe> 
					
					<div id="imgFrame">
						<img id="mapImg" class="img" src="${pageContext.request.contextPath}/resources/images/dashboard/map_layer_1.png" width="820px" height="325px" usemap="#sitemap"/>
					</div>
					
					<div id="buildingFrames" style="display: none;">
						<div id="buildingFrame_4F" style="display: block;position: absolute;left: 363px;width: 882px;height: 185px;top: 103px;border-bottom: 1px gray dashed">
							<iframe id="buildingIframe_4F" width="100%" height="100%" frameborder="0" 
								src="https://cmap.hwacom.dev:1443/public/mapshow.htm?id=2240&mapid=601D64D3-E265-418A-81F7-BB706E7CC653">
							</iframe> 
						</div>
						
						<div id="buildingFrame_3F" style="display: block;position: absolute;left: 363px;width: 882px;height: 185px;top: 292px;border-bottom: 1px gray dashed">
							<iframe id="buildingIframe_3F" width="100%" height="100%" frameborder="0" 
								src="https://cmap.hwacom.dev:1443/public/mapshow.htm?id=2232&mapid=9E54A5E3-0C3D-433D-94A8-1CECAA285C22">
							</iframe> 
						</div>
						
						<div id="buildingFrame_1F" style="display: block;position: absolute;left: 363px;width: 882px;height: 185px;top: 485px;">
							<iframe id="buildingIframe_1F" width="100%" height="100%" frameborder="0" 
								src="https://cmap.hwacom.dev:1443/public/mapshow.htm?id=2241&mapid=724383FB-2E45-4D65-9709-6D1200A54876">
							</iframe> 
						</div>
					</div>
					
					<div id="floorFrames" style="display: none;">
						<div id="floorFrame_3F" style="display: block;position: absolute;left: 67px;width: 1180px;height: 185px;top: 499px;">
							<iframe id="floorIframe_3F" width="100%" height="100%" frameborder="0" 
								src="https://cmap.hwacom.dev:1443/public/mapshow.htm?id=2233&mapid=2F28D931-B5C3-466D-9515-F55DB2EDCBC2">
							</iframe> 
						</div>
					</div>
					
					<map name="sitemap" id="sitemap">
					  <area data-building="building" shape="rect" coords="432, 155, 612, 215" href="#" title="BUILDING" alt="BUILDING" onclick="jumpTo('ALL', 'BUILDING', 2)">
					  <area data-floor="floor" shape="rect" coords="17, 553, 203, 615" href="#" title="FLOOR" alt="FLOOR" onclick="jumpTo('BUILDING', 'FLOOR', 3)">
					  <area data-room="room" shape="rect" coords="530, 93, 564, 123" href="#" title="ROOM" alt="ROOM" onclick="jumpTo('FLOOR', 'ROOM', 4)">
					</map>
		      	</div>
			</div>
	      	<div data-sec="others" style="margin-left: 10px; width: 360px;">
	      		<div style="width: 360px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
	      			<span>整體網路流量</span>
	      		</div>
	      		<div style="height: 150px;">
	      			<!-- 
	      			<iframe width="360px" height="150px" frameborder="0"
						src="http://127.0.0.1:8888/public/mapshow.htm?id=2169&mapid=D04D2281-D51C-4304-A39D-2A7456BD8B1F">
					</iframe>
					 -->
					 <img class="img" src="${pageContext.request.contextPath}/resources/images/dashboard/traffic_1.png" width="360px" height="145px" />
	      		</div>
	      		
	      		<div style="width: 360px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
	      			<span>骨幹網路流量</span>
	      		</div>
	      		<div style="height: 150px;">
	      			<!--  
	      			<iframe width="360px" height="150px" frameborder="0"
						src="http://127.0.0.1:8888/public/mapshow.htm?id=2171&mapid=854CD22D-B4FD-412B-8D45-2EF1F23D4ECF">
					</iframe>
					-->
					<img class="img" src="${pageContext.request.contextPath}/resources/images/dashboard/traffic_2.png" width="360px" height="145px" />
	      		</div>
	      	</div>
	      	
	      	<div data-sec="others" style="margin-left: 0px; width: 590px;">
	      		<div style="width: 590px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
	      			<span>設備狀態</span>
	      		</div>
	      		<div style="height: 215px;">
	      			<!-- 
	      			<iframe width="590px" height="210px" frameborder="0"
						src="http://127.0.0.1:8888/public/mapshow.htm?id=2168&mapid=9B6EBFC6-E58C-4FF6-A790-F4A00F11DBE9">
					</iframe>
					-->
					<img class="img" src="${pageContext.request.contextPath}/resources/images/dashboard/left_bottom.png" width="590px" height="210px" />
	      		</div>
	      	</div>
	      	<div data-sec="others" style="margin-left: 10px; width: 590px;">
	      		<div style="width: 590px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
	      			<span>統一威脅管理平台</span>
	      		</div>
	      		<div style="height: 215px;">
	      			<!-- 
	      			<iframe width="590px" height="210px" frameborder="0"
						src="http://127.0.0.1:8888/public/mapshow.htm?id=2170&mapid=06A02567-961D-4842-8745-ACEB2FBB4F46">
					</iframe>
					 -->
					<img class="img" src="${pageContext.request.contextPath}/resources/images/dashboard/right_bottom.png" width="590px" height="210px" />
	      		</div>
	      	</div>
		</div>
	</div>
</section>

<script src="${pageContext.request.contextPath}/resources/js/custom/min/cmap.dashboard.main.min.js"></script>