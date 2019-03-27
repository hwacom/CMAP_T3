<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<section>
	<div class="main">
		<p class="content-title">監控總覽</p>
		<div style="display: flex; flex-wrap: wrap;">
			<div style="margin-left: 0px; width: 820px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
				<span>即時地理位置告警圖</span>
			</div>
			<div style="margin-left: 0px; width: 820px;">
	      		<iframe width="820px" height="335px" frameborder="0"
					src="http://msi:8081/public/mapshow.htm?id=2167&mapid=5495CCB1-9026-409D-BD3F-186FCB053FD3">
				</iframe>
	      	</div>
	      	
	      	<div style="margin-left: 10px; width: 360px; margin-top: -30px;">
	      		<div style="width: 360px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
	      			<span>整體網路流量</span>
	      		</div>
	      		<div style="height: 150px;">
	      			<iframe width="360px" height="150px" frameborder="0"
						src="http://msi:8081/public/mapshow.htm?id=2169&mapid=D04D2281-D51C-4304-A39D-2A7456BD8B1F">
					</iframe>
	      		</div>
	      		
	      		<div style="padding-top: 5px; width: 360px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
	      			<span>骨幹網路流量</span>
	      		</div>
	      		<div style="height: 150px;">
	      			<iframe width="360px" height="150px" frameborder="0"
						src="http://MSI:8081/public/mapshow.htm?id=2171&mapid=854CD22D-B4FD-412B-8D45-2EF1F23D4ECF">
					</iframe>
	      		</div>
	      	</div>
	      	
	      	<div style="margin-left: 0px; width: 590px;">
	      		<div style="width: 590px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
	      			<span>設備狀態</span>
	      		</div>
	      		<div style="height: 215px;">
	      			<iframe width="590px" height="210px" frameborder="0"
						src="http://MSI:8081/public/mapshow.htm?id=2168&mapid=9B6EBFC6-E58C-4FF6-A790-F4A00F11DBE9">
					</iframe>
	      		</div>
	      	</div>
	      	<div style="margin-left: 10px; width: 590px;">
	      		<div style="width: 590px; background-color: #232832; color:#fff; line-height:30px; font-size: 20px; text-align: center;">
	      			<span>統一威脅管理平台</span>
	      		</div>
	      		<div style="height: 215px;">
	      			<iframe width="590px" height="210px" frameborder="0"
						src="http://MSI:8081/public/mapshow.htm?id=2170&mapid=06A02567-961D-4842-8745-ACEB2FBB4F46">
					</iframe>
	      		</div>
	      	</div>
		</div>
	</div>
</section>
