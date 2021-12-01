<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
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
	<link rel="stylesheet" href="css/index.css?t=<%=new Date().getTime() %>">
</head>

<body>
	<div id="index" v-cloak>
		<el-container>

			<el-header style="background-color: #21232c;color: #ffffff;font-size:14px;padding: 0;">
				<el-row>
					<el-col :span="7">
						<el-tooltip placement="right">
							<div slot="content">
								<span><span class="tooltipText">Web版本：</span>{{ version.web }}</span><br/>
								<span><span class="tooltipText">CM版本：</span>{{ version.cm }}</span>
							</div>
							<img src="img/new_Logo.png" style="height: 60px;">
						</el-tooltip>
					</el-col>
					<el-col :span="8">
						<div style="text-align:center">
							<span style="line-height: 60px;">严禁存储、传输、处理国家秘密信息</span>
						</div>
					</el-col>
					<el-col :span="9">
						<div style="display:inline-block;margin-right:20px;float:right">
							<el-dropdown style="cursor: pointer;">
									<span class="el-dropdown-link"
										  style="line-height: 60px;font-size:14px;color:white;padding: 13px 0px;">
										<i class="el-icon-user-solid" style="padding:5px"></i>{{User}}</span>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item class="el-dropdown-menu-item">
										<vxe-button type="text" @click="LoginOut()">&nbsp;注&nbsp;&nbsp;销&nbsp;
										</vxe-button>
									</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
						</div>
						<div style="display:inline-block;margin-right:20px;float:right">
							<el-dropdown style="cursor: pointer;">
									<span class="el-dropdown-link" style="line-height: 60px;font-size:14px;color:white;padding: 13px 0px;">
										<i class="el-icon-s-home" style="padding:5px" @click="siteDetail"></i>{{siteName}}
									</span>
								<el-dropdown-menu slot="dropdown">
									<el-dropdown-item class="el-dropdown-menu-item" v-for="siteData in siteList">
										<vxe-button type="text" @click="siteRefresh(siteData)">{{siteData.name}}
										</vxe-button>
									</el-dropdown-item>
								</el-dropdown-menu>
							</el-dropdown>
						</div>
					</el-col>
				</el-row>
			</el-header>

			<el-container>
				<el-aside :width="menuSwitch?'64px':'220px'" style="background-color:#21232c;position: relative;">
					<el-menu unique-opened="true" :default-active="defaultActive" background-color="#21232c" text-color="#8a8ea5" :collapse-transition="false"
							 :collapse="menuSwitch" :default-openeds="createdOpen(true)" style="border-right: none;line-height: 0;" @open="handleOpen">
						<el-submenu :index="menuList.code" v-for="menuList in menuLists">
							<template slot="title">
								<img class="svgColor" :src="['img/'+menuList.icon]" width="16px" height="16px"
									 onload="SVGInject(this)">
								<span style="padding-top: 1px;">{{menuList.name}}</span>
							</template>
							<el-menu-item-group>
								<el-menu-item :index="suMenuList.code" :url="suMenuList.code"
											  v-for="suMenuList in menuList.childrens" @click="menuClick(suMenuList,menuList,true)"
												v-if="isShow(suMenuList)">
									<img class="suSvgColor" :src="['img/'+suMenuList.icon]" width="16px"
										 height="16px" onload="SVGInject(this)">
									<span style="padding-top: 1px;">{{suMenuList.name}}</span>
								</el-menu-item>
							</el-menu-item-group>
						</el-submenu>
					</el-menu>
					<el-button class="menu-btn" @click="menuSwitch=!menuSwitch" icon="el-icon-arrow-left" v-if="!menuSwitch"></el-button>
					<el-button class="menu-btn" @click="menuSwitch=!menuSwitch" icon="el-icon-arrow-right" v-else></el-button>
				</el-aside>
				<el-container>
					<el-main>
						<el-tabs v-model="tabsValue" @tab-remove="tabsEdit" @tab-click="tabsSwitch">
							<el-tab-pane>
								<span slot="label">首页</span>
								<iframe width="100%" :height="iframe_heighth" :src="homeUrl" id="homePage"></iframe>
							</el-tab-pane>
							<template v-for="tabData in tabDatas">
								<el-tab-pane closable :label="tabData.name" :name="tabData.url" v-if="tabData.show" >
									<iframe width="100%" :height="iframe_heighth" :src="tabData.url" :id="tabData.url"></iframe>
								</el-tab-pane>
							</template>
						</el-tabs>
					</el-main>
				</el-container>

			</el-container>
			<el-footer style="background-color: #21232c;color: #ffffff;height: 30px;font-size: 12px">
				<div class="prompt">
					<span style="line-height: 30px;">@bsgchina</span>
				</div>
			</el-footer>
		</el-container>
	</div>
</body>

<script type="text/javascript">
	<% Object menuList = request.getAttribute("menuList"); %>
	var menuList = <%= menuList %>
</script>

<script type="text/javascript" src="js/index.js?t=<%=new Date().getTime() %>"></script>

</html>