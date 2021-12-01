<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<title>DBScale</title>
<%  
	String path = request.getContextPath();  
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
	%>  
	<base href="<%=basePath%>"></base>
<!-- 引入样式 -->
<script src="js/commonCss.js?t=<%=new Date().getTime() %>"></script>
<!-- 引入脚本 -->
<script src="js/commonJs.js?t=<%=new Date().getTime() %>"></script>
<!-- 引入Echarts组件 -->
<script src="js/echarts.min.js"></script>
<style>
.header {
  box-sizing: border-box;
  width: 100%;
  padding: 0 10px; /* 给gutter留padding */
  /* padding-top:60px	 */
}
.el-card__header {
    padding: 18px 20px 0px 20px ;
    border-bottom: 0px solid #ffffff;
    -webkit-box-sizing: border-box;
    box-sizing: border-box;
    font-size:16px
}
.btnParentDiv {
    padding: 15px 10px;
    border-bottom: 0px solid #ffffff;
    overflow: auto;
}
.coverUpDiv {
	height: 100px; 
	width: 100%;
	position:absolute;
	top:130px;
	text-align:center;
	display:inline-block;
}
.el-col-lg-4-8 {
	width: 20%;
}
</style>
</head>

<body>
<div id="List">

	<div class="btnParentDiv">
		 <span style="line-height:34px;color:#999;"><img src="img/homeImg.png" style="height:12px;width:12px"></span>
		 <span style="font-size:12px;color:#999;margin-left:5px">首页</span>
		 <span style="font-size:12px;color:#999;">/</span>
		 <span style="font-size:12px;color:#999;">控制面板</span>
		 <el-button size="small" @click="returnList" icon="el-icon-refresh" style="float:right">刷新</el-button>
	</div>
	
	<div class="header">
		<el-row :gutter="15">
		
			<el-col :span="8">
					<el-card shadow="hover"  style="height:345px">
						<div slot="header" class="clearfix">
						   <span style="font-weight:bold">可用主机统计</span>
						</div>
						<el-row style="background:#fafbfd;height:250px">
							<el-col :span="2">
								<img style="height:26px;position: relative;left: 8px;top: 10px;z-index: 1;" src="img/host.svg" >
							</el-col>
							<el-col :span="14" style="margin-top:30px" >
								<div id="distributablehostShow" style="height: 220px; width: 100%"></div> 
							</el-col>
							<el-col :span="8" style="margin-top:80px" >
								<div style="font-size:14px;margin-top:30px" v-html="statusShow('host','normal')"></div>
								<div style="font-size:14px;margin:12px 0" v-html="statusShow('host','fail')"></div>
							</el-col>
							<el-col :span="22" style="margin-top:30px" v-if="hostIsAble">
								<div class="coverUpDiv" style="right:80px">暂无数据</div>
							</el-col>
						</el-row>
			    	</el-card>
			</el-col>
				<el-col :span="8">
					<el-card shadow="hover" style="height:345px" >
						<div slot="header" class="clearfix">
						   <span style="font-weight:bold">主机单元状态</span>
						</div>
						<el-row style="background:#fafbfd;height:250px">
							<el-col :span="2">
								<img style="height:26px;position: relative;left: 8px;top: 10px;z-index: 1;" src="img/Container.png" >
							</el-col>
							<el-col :span="22" style="margin-top:30px">
								<div id="hoststatusShow" style="height: 220px; width: 100%" ></div> 
							</el-col>
						</el-row>
			    	</el-card>
			</el-col>
			<el-col :span="8">
					<el-card shadow="hover" style="height:345px" >
						<div slot="header" class="clearfix">
						   <span style="font-weight:bold">服务状态</span>
						</div>
						<el-row style="background:#fafbfd;height:250px" >
							<el-col :span="2">
								<img style="height:26px;position: relative;left: 8px;top: 10px;z-index: 1;" src="img/servgroup.svg" >
							</el-col>
							<el-col :span="22" style="margin-top:30px">
								<div  id="service" style="height: 220px; width: 100%"></div>
							</el-col>
							<el-col :span="22" style="margin-top:30px" v-if="serviceIsAble">
								<div class="coverUpDiv">暂无数据</div>
							</el-col>
						</el-row>
			    	</el-card>
			</el-col>
		</el-row>
	    <el-row style="padding-top:15px">
			<el-card shadow="hover" style="height:300px">
				<div slot="header" class="clearfix">
				   <span style="font-weight:bold;color:">使用率统计</span>
				</div>
				<el-row :gutter="10"> 
					<el-col :lg="{span: '4-8'}">
						<el-row style="background:#fafbfd">
							<el-col :span="2">
								<img style="height:26px;position: relative;left: 8px;top: 10px;z-index: 1;" src="img/unit.png" >
							</el-col>
							<el-col :span="7">
								<div id="UnitUsed" style="height: 200px; width: 100%"></div> 
							</el-col>
							<el-col :span="15">
								<br/><br/>
								<span style="font-size:22px;">单元</span><br/>
								<span style="font-size:38px;font-weight:bold">{{Presentation.showUnitUsed}}</span>
								<span style="font-size:24px;">/ {{Presentation.showMaxUnitCnt}}</span>
							</el-col>
						</el-row>
					</el-col>
					<el-col :lg="{span: '4-8'}">
						<el-row style="background:#fafbfd">
							<el-col :span="2">
								<img style="height:26px;position: relative;left: 8px;top: 10px;z-index: 1;" src="img/CPU.png" >
							</el-col>
							<el-col :span="7">
								<div id="CPUUsed" style="height: 200px; width: 100%"></div> 
							</el-col>
							<el-col :span="15">
								<br/><br/>
								<span style="font-size:22px;">CPU</span><br/>
								<span style="font-size:38px;font-weight:bold">{{Presentation.showCPUUsed}}</span>
								<span style="font-size:24px;">/ {{Presentation.showCPUCapacity}}</span>
							</el-col>
						</el-row>
					</el-col>
					<el-col :lg="{span: '4-8'}">
						<el-row style="background:#fafbfd">
							<el-col :span="2">
								<img style="height:26px;position: relative;left: 8px;top: 10px;z-index: 1;" src="img/Memory.png" >
							</el-col>
							<el-col :span="7">
								<div id="memSizeUsed" style="height: 200px; width: 100%"></div> 
							</el-col>
							<el-col :span="15">
								<br/><br/>
								<span style="font-size:22px;">内存</span><br/>
								<span style="font-size:38px;font-weight:bold">{{Presentation.showMemUsed}}</span>
								<span style="font-size:24px;">/ {{Presentation.showMemCapacity}}</span>
								<span style="font-size:22px;">{{Presentation.memTag}}</span>
							</el-col>
						</el-row>
					</el-col>
					<el-col :lg="{span: '4-8'}">
						<el-row style="background:#fafbfd">
							<el-col :span="2" >
								<img style="height:26px;position: relative;left: 8px;top: 10px;z-index: 1;" src="img/Storage.png" >
							</el-col>
							<el-col :span="7" >
								<div id="HSUsed" style="height: 200px; width: 100%"></div> 
							</el-col>
							<el-col :span="15">
								<br/><br/>
								<span style="font-size:22px;">存储</span><br/>
								<span style="font-size:38px;font-weight:bold">{{Presentation.showHSUsed}}</span>
								<span style="font-size:24px;">/ {{Presentation.showHSCapacity}}</span>
								<span style="font-size:22px;">{{Presentation.hsTag}}</span>
							</el-col>
						</el-row>
					</el-col>
					<el-col :lg="{span: '4-8'}">
						<el-row style="background:#fafbfd">
							<el-col :span="2" >
								<img style="height:26px;position: relative;left: 8px;top: 10px;z-index: 1;" src="img/Network.png" >
							</el-col>
							<el-col :span="7">
								<div id="networkUsed" style="height: 200px; width: 99%"></div> 
							</el-col>
							<el-col :span="15">
								<br/><br/>
								<span style="font-size:22px;">网段</span><br/>
								<span style="font-size:38px;font-weight:bold">{{Presentation.showNetUsed}}</span>
								<span style="font-size:24px;">/ {{Presentation.showNetCapacity}}</span>
							</el-col>
						</el-row>
					</el-col>
				</el-row>
	    	</el-card>
		</el-row>
	</div>
</div>
</body>

<script type="text/javascript" src="js/home/list.js?t=<%=new Date().getTime() %>"></script>

</html>