<!doctype html>
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="Expires" content="0">
<meta http-equiv="Pragma" content="No-cache">
<meta http-equiv="Cache-Control" content="no-cache">
<title>BITVC 交易API WEB界面</title>
<link href="https://static.bitvc.com/style/style.min.css"
	rel="stylesheet" media="screen">
</head>
<body>
	<div id="page_head">
		<h1 class="logo l">
			<a href="https://www.bitvc.com"><b>BitVC</b></a>
		</h1>
		<div class="slogan">
			<i class="icon icon_slogan"><em>Vi Veri Veniversum Vivus
					Vici</em></i>
		</div>
		<div id="top_bar">
			<div id="nav" class="l">
				<!--
            -->
				<a href="https://www.bitvc.com/btc_trades/cny_index"><i
					class="icon icon_trade"></i>杠杆交易</a>
				<!--
            -->
				<!--
            -->
				<a href="https://www.bitvc.com/coin_savings/transfer"><i
					class="icon icon_balance_2"></i>余币宝</a>
				<!--
            -->
				<a href="https://www.bitvc.com/deposit/coin" id="nav_cookie_2"><i
					class="icon icon_user"></i>账户</a>
				<!--
            -->
				<a href="#" class="cur" id="nav_cookie_2"><i
					class="icon icon_user"></i>Api测试</a>
			</div>
		</div>
		<div class="masthead"></div>
	</div>
	<div class="section">
		<div class="container_2">
			<!--主内容区域-->
			<div class="test_page m_t_15">
				<div class="alert">
					1、created参数不用手动输入,提交会自动生成<br> 2、下单时若资金密码未开启,trade_password不用输入
				</div>
				<form>
					<fieldset class="fieldset_style m_b_15">
						<legend>BITVC 交易API WEB界面</legend>
						<div class="p_all_15">

							<h4 class="p_b_15 align_c">参数区</h4>
							<div id="condition_div">
								<div class="group">
									<label class="col_1 col_text align_r">请求URL:</label>
									<div class="col_3 col_text" id="url">
										<span class="align_m" id="_url"></span>
									</div>
								</div>
								<div class="group">
									<label class="col_1 col_text align_r" for="access_key">
										<b class="tc_red">*</b> access_key :
									</label>
									<div class="col_3">
										<input type="text" name="" id="access_key"
											placeholder="access_key" class="in_text block" />
									</div>
								</div>
								<div class="group">
									<label class="col_1 col_text align_r" for="secret_key">
										<b class="tc_red">*</b> secret_key :
									</label>
									<div class="col_3">
										<input type="text" name="" id="secret_key"
											placeholder="secret_key" class="in_text block" />
									</div>
								</div>
								<div class="group">
									<label class="col_1 col_text align_r" for="created"> <b
										class="tc_red">*</b> created :
									</label>
									<div class="col_3">
										<input type="text" name="" id="created" class="in_text block"
											placeholder="提交时自动生成" disabled />
									</div>
								</div>
								<div class="group">
									<label class="col_1 col_text align_r" for="method"> <b
										class="tc_red">*</b> method :
									</label>
									<div class="col_3">
										<select name="func" id="method" class="in_select">
											<option value="">--请选择要提交的方法名称--</option>
											<option value="get_account_info">查询资产(get_account_info)</option>
											<option value="get_orders">查询委托单列表(get_orders)</option>
											<option value="order_info">查询委托单详情(order_info)</option>
											<option value="buy">买入限价单(buy)</option>
											<option value="buy_market">买入市价单(buy_market)</option>
											<option value="sell">卖出限价单(sell)</option>
											<option value="sell_market">卖出市价单(sell_market)</option>
											<option value="cancel_order">取消委托(cancel_order)</option>
										</select>
									</div>
								</div>
							</div>

							<div class="group align_c">
								<button id="_submit" onsubmit="return false;"
									class="btn btn_warning">提交</button>
								<button type="reset" class="btn btn_info">重置</button>
							</div>
							<h4 class="p_b_15 align_c">数据区</h4>
							<div class="group" id="returnValue"></div>
						</div>
					</fieldset>
				</form>
			</div>
			<!--主内容区域-->
		</div>
	</div>
	<div id="page_foot">
		<div class="foot_menu">
			<a href="https://www.bitvc.com/about/index">关于我们</a> <i class="seg"></i>
			<a href="https://www.bitvc.com/about/agreement">条款</a> <i class="seg"></i>
			<a href="https://www.bitvc.com/help/index">帮助</a> <i class="seg"></i><a
				href="https://www.bitvc.com/help/api">API文档</a>
		</div>
		<div class="foot_share">
			<a target="_blank" href="https://twitter.com/bitvccom"><i
				class="icon_share icon_twitter"><em>twitter</em></i></a> <a
				target="_blank"
				href="https://www.facebook.com/profile.php?id=100008106019180"><i
				class="icon_share icon_facebook"><em>facebook</em></i></a> <a
				target="_blank" href="http://weibo.com/bitvccom"><i
				class="icon_share icon_weibo"><em>weibo</em></i></a>
		</div>
		<div class="foot_copyright">Copyright@ BitVC.com</div>
	</div>
	<%
	    String path = request.getContextPath();
	%>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/jQuery/jquery.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/jquery.md5.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/js-jsonFmt.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/indexv2.js"></script>
	<script type="text/javascript">
		var path = "<%=path%>";
		$('#_submit').on('click', function(e) {
			e.preventDefault();
			checkAndSubmit(path);
		});
	</script>
</body>
</html>
