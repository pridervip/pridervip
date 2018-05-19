function checkAndSubmit(path) {
    var _func = $("input[name='func']:checked").val();
    var _url = path;// _url = String path--
    var _data;
    var created = parseInt(new Date().getTime() / 1000);
    var inputStr = "";
    var requiredArr;
    var sign;
    switch (_func) {
        case '1':
            _url += "/api/accountInfo/get";
            _data = {
                "access_key": $("#access_key").val(),
                "created": created
            };
            requiredArr = {
                "access_key": 1,
                "created": 1,
                "secret_key": 1
            };
            break;
        case '2':
            _url += "/api/order/list";
            _data = {
                "access_key": $("#access_key").val(),
                "coin_type": $("#coin_type").val(),
                "created": created
            };
            requiredArr = {
                "access_key": 1,
                "coin_type": 1,
                "created": 1,
                "secret_key": 1
            };
            break;
        case '3':
            _url += "/api/order/" + $("#order_id").val();
            _data = {
                "access_key": $("#access_key").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "id": $("#order_id").val()
            };
            requiredArr = {
                "access_key": 1,
                "coin_type": 1,
                "created": 1,
                "id": 1,
                "secret_key": 1
            };
            break;
        case '4':
            _url += "/api/order/buy";
            _data = {
                "access_key": $("#access_key").val(),
                "amount": $("#amount").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "price": $("#price").val(),
                "trade_password": $("#trade_password").val()
            };
            requiredArr = {
                "access_key": 1,
                "amount": 1,
                "coin_type": 1,
                "created": 1,
                "price": 1,
                "secret_key": 1,
                "trade_password": 0
            };
            break;
        case '5':
            _url += "/api/order/buy_market";
            _data = {
                "access_key": $("#access_key").val(),
                "amount": $("#amount").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "trade_password": $("#trade_password").val()
            };
            requiredArr = {
                "access_key": 1,
                "amount": 1,
                "coin_type": 1,
                "created": 1,
                "secret_key": 1,
                "trade_password": 0
            };
            break;
        case '6':
            _url += "/api/order/sell";
            _data = {
                "access_key": $("#access_key").val(),
                "amount": $("#amount").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "price": $("#price").val(),
                "trade_password": $("#trade_password").val()
            };
            requiredArr = {
                "access_key": 1,
                "amount": 1,
                "coin_type": 1,
                "created": 1,
                "price": 1,
                "secret_key": 1,
                "trade_password": 0
            };
            break;
        case '7':
            _url += "/api/order/sell_market";
            _data = {
                "access_key": $("#access_key").val(),
                "amount": $("#amount").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "trade_password": $("#trade_password").val()
            };
            requiredArr = {
                "access_key": 1,
                "amount": 1,
                "coin_type": 1,
                "created": 1,
                "secret_key": 1,
                "trade_password": 0
            };
            break;
        case '8':
            _url += "/api/order/cancel/" + $("#order_id").val();
            _data = {
                "access_key": $("#access_key").val(),
                "coin_type": $("#coin_type").val(),
                "created": created,
                "id": $("#order_id").val()
            };
            requiredArr = {
                "access_key": 1,
                "coin_type": 1,
                "created": 1,
                "id": 1,
                "secret_key": 1
            };
            break;
        default:
            $("#returnValue").html(
                    "<div class='alert alert_danger'>" + "请先在功能区选择响应的功能!"
                    + "</div>");
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
            if (key == "secret_key") {
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
    sign = $.md5(inputStr);
    postData["sign"] = sign;
    console.log("参与加密sign的字符串:" + inputStr);
    console.log("_url:" + _url);
//    console.log("sign:" + sign);
//    console.log("_data:" + JSON.stringify(_data));
    console.log("postData:" + JSON.stringify(postData));

//    $.ajax({
//        type: "POST",
//        url: _url,
//        data: postData,
//        dataType: "json",
//        success: function (data) {
//            $("#returnValue").html("<pre><code>" + JsonUti.convertToString(data) + "</pre></code>");
//            // Play with returned data in JSON format
//        },
//        error: function (msg) {
//            $("#returnValue").html("<pre><code>" + msg + "</pre></code>");
//        }
//    });
    $.post(_url, postData, function (result) {
        $("#returnValue").html("<pre><code>" + JsonUti.convertToString(result) + "</pre></code>");
    });
}

$("input[name='func']").on('click', function () {
    var orderId = "<input id='order_id' type='text' class='in_text mini' placeholder='订单ID' />";
    var coinType = "<div name='activeContition' class='group'>"
        + "<label class='col_1' for='coin_type'>"
        + "<b class='tc_red'>*</b>coin_type :</label>"
        + "<div class='col_4'>"
        + "<input type='text' id='coin_type' class='in_text block' placeholder='coin_type' />"
        + "</div></div>";
    var contractType = "<div name='activeContition' class='group'>"
        + "<label class='col_1' for='contract_type'>"
        + "<b class='tc_red'>*</b>contract_type :</label>"
        + "<div class='col_4'>"
        + "<input type='text' id='contract_type' class='in_text block' placeholder='contract_type' />"
        + "</div></div>";

    var amount = "<div name='activeContition' class='group'>"
        + "<label class='col_1' for='amount'>"
        + "<b class='tc_red'>*</b>amount :</label>"
        + "<div class='col_4'>"
        + "<input type='text' id='amount' class='in_text block' placeholder='amount' />"
        + "</div></div>";

    var price = "<div name='activeContition' class='group'>"
        + "<label class='col_1' for='price'>"
        + "<b class='tc_red'>*</b>price :</label>"
        + "<div class='col_4'>"
        + "<input type='text' id='price' class='in_text block' placeholder='price' />"
        + "</div></div>";

    var trade_password = "<div name='activeContition' class='group'>"
        + "<label class='col_1' for='trade_password'>"
        + "<b class='tc_red'>*</b>trade_password :</label>"
        + "<div class='col_4'>"
        + "<input type='text' id='trade_password' class='in_text block' placeholder='trade_password' />"
        + "</div></div>";

    var _funId = $(this).val();
    $("div[name='activeContition']").remove();
    $("#order_id").remove();
    switch (_funId) {
        case '1':
            $('#_url').html("/api/accountInfo/get");
            break;
        case '2':
            $('#_url').html("/api/order/list");
            $('#condition_div').append(coinType);
            break;
        case '3':
            $('#_url').html("/api/order/");
            $('#url').append(orderId);
            $('#condition_div').append(coinType);
            break;
        case '4':
            $('#_url').html("/api/order/buy");
            $('#condition_div').append(
                    coinType + price + amount + trade_password);
            break;
        case '5':
            $('#_url').html("/api/order/buy_market");
            $('#condition_div').append(
                    coinType + amount + trade_password);
            break;
        case '6':
            $('#_url').html("/api/order/sell");
            $('#condition_div').append(
                    coinType + price + amount + trade_password);
            break;
        case '7':
            $('#_url').html("/api/order/sell_market");
            $('#condition_div').append(
                    coinType + amount + trade_password);
            break;
        case '8':
            $('#_url').html("/api/order/cancel/");
            $('#url').append(orderId);
            $('#condition_div').append(coinType);
            break;
       
    }
});