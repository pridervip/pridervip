function checkAndSubmit(path) {
    $("#returnValue").html("");
    var _func = $("#method").val();
    var _url = path + "/apiv2";// _url = String path--
    var _data;
    var created = parseInt(new Date().getTime() / 1000);
    var inputStr = "";
    var requiredArr;
    var sign;
    switch (_func) {
        case 'get_account_info':
            _data = {
                "access_key": $("#access_key").val(),
                "created": created,
                "method": "get_account_info"
            };
            requiredArr = {
                "access_key": 1,
                "created": 1,
                "method": 1,
                "secret_key": 1
            };
            break;
        case 'get_orders':
            _data = {
                "access_key": $("#access_key").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "method": "get_orders"
            };
            requiredArr = {
                "access_key": 1,
                "coin_type": 1,
                "created": 1,
                "method": 1,
                "secret_key": 1
            };
            break;
        case 'order_info':
            _data = {
                "access_key": $("#access_key").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "id": $("#id").val(),
                "method": "order_info"
            };
            requiredArr = {
                "access_key": 1,
                "coin_type": 1,
                "created": 1,
                "id": 1,
                "method": 1,
                "secret_key": 1
            };
            break;
        case 'buy':
            _data = {
                "access_key": $("#access_key").val(),
                "amount": $("#amount").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "method": "buy",
                "price": $("#price").val(),
                "trade_password": $("#trade_password").val()
            };
            requiredArr = {
                "access_key": 1,
                "amount": 1,
                "coin_type": 1,
                "created": 1,
                "method": 1,
                "price": 1,
                "secret_key": 1,
                "trade_password": 0
            };
            break;
        case 'buy_market':
            _data = {
                "access_key": $("#access_key").val(),
                "amount": $("#amount").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "method": "buy_market",
                "trade_password": $("#trade_password").val()
            };
            requiredArr = {
                "access_key": 1,
                "amount": 1,
                "coin_type": 1,
                "created": 1,
                "method": 1,
                "secret_key": 1,
                "trade_password": 0
            };
            break;
        case 'sell':
            _data = {
                "access_key": $("#access_key").val(),
                "amount": $("#amount").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "method": "sell",
                "price": $("#price").val(),
                "trade_password": $("#trade_password").val()
            };
            requiredArr = {
                "access_key": 1,
                "amount": 1,
                "coin_type": 1,
                "created": 1,
                "method": 1,
                "price": 1,
                "secret_key": 1,
                "trade_password": 0
            };
            break;
        case 'sell_market':
            _data = {
                "access_key": $("#access_key").val(),
                "amount": $("#amount").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "method": "sell_market",
                "trade_password": $("#trade_password").val()
            };
            requiredArr = {
                "access_key": 1,
                "amount": 1,
                "coin_type": 1,
                "created": 1,
                "method": 1,
                "secret_key": 1,
                "trade_password": 0
            };
            break;
        case 'cancel_order':
            _data = {
                "access_key": $("#access_key").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "id": $("#id").val(),
                "method": "cancel_order"
            };
            requiredArr = {
                "access_key": 1,
                "coin_type": 1,
                "created": 1,
                "id": 1,
                "method": 1,
                "secret_key": 1
            };
            break;
        default:
            $("#returnValue").html(
                    "<div class='alert alert_danger'>" + "请选择方法!" + "</div>");
            return;
    }
    $("#created").val(created);
    _data["secret_key"] = $("#secret_key").val();
    var keys = [];
    var postData = {};
    for (var key in _data) {
        if (_data[key] != "") {
            if (requiredArr[key]) {
                keys.push(key);
            }
            if (key == "secret_key" || key == "id") {
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

    $.ajax({
        type: 'POST',
        headers: {
            "Accept-Language": "zh-CN"
        },
        url: _url,
        data: postData,
        success: function (result) {
            $("#returnValue").html(
                    "<pre><code>" + JsonUti.convertToString(result)
                    + "</pre></code>");
        }
    });
}

$("#method").change(function () {
    var orderId = "<div name='activeContition' class='group'>"
        + "<label class='col_1 col_text align_r' for='id'>"
        + "<b class='tc_red'>*</b>id :</label>"
        + "<div class='col_3'>"
        + "<input type='text' id='id' class='in_text block' placeholder='id' />"
        + "</div></div>";

    var coinType = "<div name='activeContition' class='group'>"
        + "<label class='col_1 col_text align_r' for='coin_type'>"
        + "<b class='tc_red'>*</b>coin_type :</label>"
        + "<div class='col_3'>"
        + "<input type='text' id='coin_type' class='in_text block' placeholder='coin_type' />"
        + "</div></div>";

    var amount = "<div name='activeContition' class='group'>"
        + "<label class='col_1 col_text align_r' for='amount'>"
        + "<b class='tc_red'>*</b>amount :</label>"
        + "<div class='col_3'>"
        + "<input type='text' id='amount' class='in_text block' placeholder='amount' />"
        + "</div></div>";

    var price = "<div name='activeContition' class='group'>"
        + "<label class='col_1 col_text align_r' for='price'>"
        + "<b class='tc_red'>*</b>price :</label>"
        + "<div class='col_3'>"
        + "<input type='text' id='price' class='in_text block' placeholder='price' />"
        + "</div></div>";

    var trade_password = "<div name='activeContition' class='group'>"
        + "<label class='col_1 col_text align_r' for='trade_password'>"
        + "<b class='tc_red'>*</b>trade_password :</label>"
        + "<div class='col_3'>"
        + "<input type='text' id='trade_password' class='in_text block' placeholder='trade_password' />"
        + "</div></div>";

    var _funId = $(this).val();
    $("div[name='activeContition']").remove();
    $("#order_id").remove();
    switch (_funId) {
        case 'get_account_info':
            $('#_url').html("/apiv2");
            break;
        case 'get_orders':
            $('#_url').html("/apiv2");
            $('#condition_div').append(coinType);
            break;
        case 'order_info':
            $('#_url').html("/apiv2");
            $('#condition_div').append(orderId + coinType);
            break;
        case 'buy':
            $('#_url').html("/apiv2");
            $('#condition_div').append(
                    coinType + price + amount + trade_password);
            break;
        case 'buy_market':
            $('#_url').html("/apiv2");
            $('#condition_div').append(
                    coinType + amount + trade_password);
            break;
        case 'sell':
            $('#_url').html("/apiv2");
            $('#condition_div').append(
                    coinType + price + amount + trade_password);
            break;
        case 'sell_market':
            $('#_url').html("/apiv2");
            $('#condition_div').append(
                    coinType + amount + trade_password);
            break;
        case 'cancel_order':
            $('#_url').html("/apiv2");
            $('#condition_div').append(orderId + coinType);
            break;
    }
});