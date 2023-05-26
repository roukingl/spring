function logout() {
    if (confirm("确认登出?")) {
        jQuery.ajax({
            url: 'user/logout',
            type: 'post',
            data: {},
            success: function(result) {
                if (result != null && result.statusCode == 200 && result.data == 1) {
                    location.href = "/login.html";
                } else {
                    alert("登出异常，请重试");
                }
            } 
        });
    }
}

function getUrlValue(key) {
    // ?id=1&name=zhangsan
    var params = location.search;
    if (params.length > 1) {
        // id=1&name=zhangsan
        params = params.substring(1);
        var paramArr = params.split("&");
        for (var i = 0; i < paramArr.length; i++) {
            var kv = paramArr[i].split("=");
            if (kv[0] == key) {
                return kv[1];
            }
        }  
    }
    return "";
}