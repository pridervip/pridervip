function checkAndSubmit(path) {
	var _func = $("input[name='func']:checked").val();
	var _url = path + $('#_url').html();// _url = String path--
	var _data;
	var created = parseInt(new Date().getTime() / 1000);
	var inputStr = "";
	var sign = "";
	var requiredArr;
	switch (_func) {
	case '1':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"created" : created
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"created" : 1,
			"secretKey" : 1
		};
		break;
	case '2':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"contractType" : $("#contractType").val(),
			"created" : created
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"contractType" : 0,
			"created" : 1,
			"secretKey" : 1
		};
		break;
	case '3':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"contractType" : $("#contractType").val(),
			"created" : created,
			"id" : $("#id").val()
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"contractType" : 1,
			"created" : 1,
			"id" : 1,
			"secretKey" : 1
		};
		break;
	case '4':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"contractType" : $("#contractType").val(),
			"created" : created
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"contractType" : 0,
			"created" : 1,
			"secretKey" : 1
		};
		break;
	case '5':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"price" : $("#price").val(),
			"money" : $("#money").val(),
			"orderType" : $("#orderType").val(),
			"tradeType" : $("#tradeType").val(),
			"coinType" : $("#coinType").val(),
			"contractType" : $("#contractType").val(),
			"leverage" : $("#leverage").val(),
			"tradePassword" : $("#tradePassword").val(),
			"storeId" : $("#storeId").val(),
			"created" : created
		};
		requiredArr = {
			"accessKey" : 1,
			"price" : 1,
			"money" : 1,
			"orderType" : 1,
			"tradeType" : 1,
			"coinType" : 1,
			"contractType" : 1,
			"leverage" : 0,
			"tradePassword":0,
			"storeId" : 0,
			"created" : 1,
			"secretKey" : 1
		};
		break;
	case '6':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"contractType" : $("#contractType").val(),
			"created" : created,
			"id" : $("#id").val()
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"contractType" : 1,
			"created" : 1,
			"id" : 1,
			"secretKey" : 1
		};
		break;
	case '7':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"contractType" : $("#contractType").val(),
			"created" : created,
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"contractType" : 0,
			"created" : 1,
			"secretKey" : 1
		};
		break;
	case '8':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"created" : created,
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"created" : 1,
			"secretKey" : 1
		};
		break;
	case '9':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"created" : created,
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"created" : 1,
			"secretKey" : 1
		};
		break;
	case '10':
		_data = {
			"accessKey" : $("#accessKey").val(),
			"coinType" : $("#coinType").val(),
			"contractType" : $("#contractType").val(),
			"created" : created
		};
		requiredArr = {
			"accessKey" : 1,
			"coinType" : 1,
			"contractType" : 1,
			"created" : 1,
			"secretKey" : 1
		};
		break;
	default:
		$("#returnValue").html(
				"<div class='alert alert_danger'>" + "请先在功能区选择响应的功能!"
						+ "</div>");
		return;
	}
	$("#created").val(created);
	_data["secretKey"] = $("#secretKey").val();
	var keys = [];
	var postData = {};
	for ( var key in _data) {
		if (_data[key] != "") {
			if (requiredArr[key]) {
				keys.push(key);
			}
			if (key == "secretKey") {
				continue;
			}
			postData[key] = _data[key];
		}
	}
	keys = keys.sort();
	for (var index = 0; index < keys.length; index++) {
		inputStr += (keys[index] + "=" + _data[keys[index]] + "&");
	}
	inputStr = inputStr.substring(0, inputStr.length - 1);
	console.log("参与加密sign的字符串:" + inputStr);
	sign = $.md5(inputStr);
	postData["sign"] = sign;
	console.log("_url:" + _url);
	console.log("sign:" + sign);
	console.log("_data:" + JSON.stringify(_data));
	console.log("postData:" + JSON.stringify(postData));
	$.post(_url, postData, function(result) {
		$("#returnValue").html(
				"<pre><code>" + JsonUti.convertToString(result)
						+ "</pre></code>");
	});
}

$("input[name='func']")
		.on(
				'click',
				function() {
					var orderId = "<div name='activeContition' class='group'>"
							+ "<label class='col_1' for='id'>"
							+ "<b class='tc_red'>*</b>id :</label>"
							+ "<div class='col_4'>"
							+ "<input type='text' id='id' class='in_text block' placeholder='id' />"
							+ "</div></div>";

					var coinType = "<div name='activeContition' class='group'>"
							+ "<label class='col_1' for='coinType'>"
							+ "<b class='tc_red'>*</b>coinType :</label>"
							+ "<div class='col_4'>"
							+ "<input type='text' id='coinType' class='in_text block' placeholder='coinType' />"
							+ "</div></div>";

					var price = "<div name='activeContition' class='group'>"
							+ "<label class='col_1' for='price'>"
							+ "<b class='tc_red'>*</b>price :</label>"
							+ "<div class='col_4'>"
							+ "<input type='text' id='price' class='in_text block' placeholder='price' />"
							+ "</div></div>";

					var money = "<div name='activeContition' class='group'>"
							+ "<label class='col_1' for='money'>"
							+ "<b class='tc_red'>*</b>money :</label>"
							+ "<div class='col_4'>"
							+ "<input type='text' id='money' class='in_text block' placeholder='money' />"
							+ "</div></div>";

					var orderType = "<div name='activeContition' class='group'>"
							+ "<label class='col_1' for='orderType'>"
							+ "<b class='tc_red'>*</b>orderType :</label>"
							+ "<div class='col_4'>"
							+ "<input type='text' id='orderType' class='in_text block' placeholder='orderType' />"
							+ "</div></div>";

					var tradeType = "<div name='activeContition' class='group'>"
							+ "<label class='col_1' for='tradeType'>"
							+ "<b class='tc_red'>*</b>tradeType :</label>"
							+ "<div class='col_4'>"
							+ "<input type='text' id='tradeType' class='in_text block' placeholder='tradeType' />"
							+ "</div></div>";

					var contractType = "<div name='activeContition' class='group'>"
							+ "<label class='col_1' for='contractType'>"
							+ "<b class='tc_red'>*</b>contractType :</label>"
							+ "<div class='col_4'>"
							+ "<input type='text' id='contractType' class='in_text block' placeholder='contractType' />"
							+ "</div></div>";

					var leverage = "<div name='activeContition' class='group'>"
							+ "<label class='col_1' for='leverage'>"
							+ "<b class='tc_red'></b>leverage :</label>"
							+ "<div class='col_4'>"
							+ "<input type='text' id='leverage' class='in_text block' placeholder='leverage' />"
							+ "</div></div>";
					var tradePassword = "<div name='activeContition' class='group'>"
						+ "<label class='col_1' for='tradePassword'>"
						+ "<b class='tc_red'></b>tradePassword :</label>"
						+ "<div class='col_4'>"
						+ "<input type='text' id='tradePassword' class='in_text block' placeholder='tradePassword' />"
						+ "</div></div>";
					var storeId = "<div name='activeContition' class='group'>"
						+ "<label class='col_1' for='storeId'>"
						+ "<b class='tc_red'></b>storeId :</label>"
						+ "<div class='col_4'>"
						+ "<input type='text' id='storeId' class='in_text block' placeholder='storeId' />"
						+ "</div></div>";

					var _funId = $(this).val();
					$("div[name='activeContition']").remove();
					$("#order_id").remove();
					$("#returnValue").html();
					switch (_funId) {
					case '1':
						$('#_url').html("/balance");
						$('#condition_div').append(coinType);
						break;
					case '2':
						$('#_url').html("/order/list");
						$('#condition_div').append(coinType + contractType);
						break;
					case '3':
						$('#_url').html("/order");
						$('#condition_div').append(
								orderId + coinType + contractType);
						break;
					case '4':
						$('#_url').html("/holdOrder/list");
						$('#condition_div').append(coinType + contractType);
						break;
					case '5':
						$('#_url').html("/order/save");
						$('#condition_div').append(
								orderType + tradeType + price + money
										+ coinType + contractType + leverage+tradePassword+storeId);
						break;
					case '6':
						$('#_url').html("/order/cancel");
						$('#condition_div').append(
								orderId + coinType + contractType);
						break;
					case '7':
						$('#_url').html("/holdOrder");
						$('#condition_div').append(coinType + contractType);
						break;
					case '8':
						$('#_url').html("/contracts");
						$('#condition_div').append(coinType);
						break;
					case '9':
						$('#_url').html("/balance_history/list");
						$('#condition_div').append(coinType);
						break;
					case '10':
						$('#_url').html("/systemCloseOrder/list");
						$('#condition_div').append(coinType + contractType);
						break;
					}
					

				});