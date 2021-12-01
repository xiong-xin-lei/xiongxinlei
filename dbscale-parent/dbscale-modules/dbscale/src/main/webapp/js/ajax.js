//获取ajax请求头
function getAjaxHeader() {
    return {
        version: "1.0",
        Authorization: sessionStorage.getItem("token")
    }
}

//默认请求时间
axios.defaults.timeout = 3 * 60 * 1000;

//ajax请求前拦截
axios.interceptors.request.use(function (config) {
    XEUtils.objectEach(getAjaxHeader(), (item, key) => {
        config.headers[key] = item;
    })
    return config;
}, function (error) {
    return Promise.reject(error);
});

// ajax请求后拦截
axios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    return response;
}, function (error) {
    // 对响应错误做点什么

    if (error && error.response) {
        switch (error.response.status) {
            case 400:
                error.message = '请求错误(400)';
                break;
            case 401:
                error.message = '未授权，请重新登录(401)';
                window.top.location.href = "/" + getProjectName() + "/";
                //window.top.location.href="/" + getProjectName() + "/app/login";
                break;
            case 403:
                error.message = '拒绝访问(403)';
                break;
            case 404:
                error.message = '请求出错(404)';
                break;
            case 408:
                error.message = '请求超时(408)';
                break;
            case 500:
                error.message = '服务器错误(500)';
                break;
            case 501:
                error.message = '服务未实现(501)';
                break;
            case 502:
                error.message = '网络错误(502)';
                break;
            case 503:
                error.message = '服务不可用(503)';
                break;
            case 504:
                error.message = '网络超时(504)';
                break;
            case 505:
                error.message = 'HTTP版本不受支持(505)';
                break;
            default:
                error.message = '连接出错' + error.response.status;
        }
    } else {
        let errorType = error.message.split(' ')[0]
        switch (errorType) {
            case "timeout":
                XEUtils.set(error, 'response.data.msg', '请求超时！')
                break;
        }
        error.message = '连接服务器失败!'
    }

    Vue.prototype.$message.closeAll();
    layer.closeAll('loading');
    // console.log(error);

    return Promise.reject(error);
});

//ajax请求封装

function sendGet(url, successFun, falseFun, data) {
    layer.load(1, {
        // shade: [0.4,'#f8f8f8'] //0.1透明度的白色背景
        shade: [0.4, '#999']
    });
    axios.get(url, data).then(response => (
        successFun(response, data)
    )).catch(function (error) {
        falseFun(error, data)
    })
}

function sendPost(url, successFun, falseFun, data) {
    layer.load(1, {
        // shade: [0.4,'#f8f8f8'] //0.1透明度的白色背景
        shade: [0.4, '#999']
    });
    axios.post(url, data).then(response => (
        successFun(response, data)
    )).catch(function (error) {
        falseFun(error, data)
    })
}

function sendPut(url, successFun, falseFun, data) {
    layer.load(1, {
        // shade: [0.4,'#f8f8f8'] //0.1透明度的白色背景
        shade: [0.4, '#999']
    });
    axios.put(url, data).then(response => (
        successFun(response, data)
    )).catch(function (error) {
        falseFun(error, data)
    })
}

function sendDelete(url, successFun, falseFun, data) {
    layer.load(1, {
        // shade: [0.4,'#f8f8f8'] //0.1透明度的白色背景
        shade: [0.4, '#999']
    });
    axios.delete(url, data).then(response => (
        successFun(response, data)
    )).catch(function (error) {
        falseFun(error, data)
    })
}

//无Loading ajax请求封装

function sendGetNoLoading(url, successFun, falseFun, data) {
    axios.get(url, data).then(response => (
        successFun(response, data)
    )).catch(function (error) {
        falseFun(error, data)
    })
}

function sendPutNoLoading(url, successFun, falseFun, data) {
    axios.put(url, data).then(response => (
        successFun(response, data)
    )).catch(function (error) {
        falseFun(error, data)
    })
}

/**
 * 批量ajax操作
 * @param {function(json,function(response),function(error))} fun (必填，方法名) - 批量使用的ajax方法
 *
 * @param {array} data (必填，数组) - 需要批量的数据
 *
 * @param {function(json[],json[])} finishFun (必填，方法) - 所有请求全部完成才会调用
 * @param {json} finishFun.json.data 请求发送的值
 * @param {json} finishFun.json.object 请求返回的值
 *
 * @param {function(json)} [successFun] (选填，方法) - 每次成功都会调用
 * @param {json} successFun.json.data 请求发送的值
 * @param {json} successFun.json.object 请求返回的值
 *
 * @param {function(json)} [falseFun] (选填，方法) - 每次失败都会调用
 * @param {json} falseFun.json.data 请求发送的值
 * @param {json} falseFun.json.object 请求返回的值
 */
function sendAll(fun, data, finishFun, successFun, falseFun) {
    var sendAllSum = data.length
    var sendAllNum = 0
    var successArray = []
    var errorArray = []

    XEUtils.arrayEach(data, (item) => {
        let tempJson = {}
        fun(item, function (response) {
            sendAllNum++;
            tempJson = {
                data: item,
                object: response
            }
            successArray.push(tempJson);
            if (XEUtils.isFunction(successFun))
                successFun(tempJson)
            if (sendAllNum === sendAllSum) {
                finishFun(successArray, errorArray);
            }
        }, function (error) {
            sendAllSum--;
            tempJson = {
                data: item,
                object: error
            }
            errorArray.push(tempJson);
            if (XEUtils.isFunction(falseFun))
                falseFun(tempJson)
            if (sendAllNum === sendAllSum) {// 接口报错
                finishFun(successArray, errorArray);
            } else if (sendAllNum > sendAllSum) {// 代码报错
                layer.closeAll('loading');
                console.error(error);
            } else {
                layer.load(1, {
                    // shade: [0.4,'#f8f8f8'] //0.1透明度的白色背景
                    shade: [0.4, '#999']
                });
            }
        })
    })
}