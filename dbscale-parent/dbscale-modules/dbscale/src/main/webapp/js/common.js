//pathName-路径名称 webName-项目名 path_root-路径加项目名
function getProjectName() {
    var pathName = window.location.pathname.substring(1);
    var webName = pathName === '' ? '' : pathName.substring(0, pathName.indexOf('/'));
    var path_root = window.location.protocol + '//' + window.location.host + '/' + webName + '/';
    return webName;
}


var COLOR_TASK_COMPLETED = "#19be6b";
var COLOR_TASK_ERROR = "#f8453f";
var COLOR_TASK_RUNNING = "#2d8cf0";
var COLOR_TASK_TIMEOUT = "#696bd8";
var COLOR_TASK_READY = "#80848f";
var COLOR_TASK_UNKNOWN = "#696bd8";

var COLOR_ORDER_APPROVED = "#0eafde";
var COLOR_ORDER_UNAPPROVE = "#ff9900";

var COLOR_ENABLED = COLOR_TASK_COMPLETED;
var COLOR_DISABLED = COLOR_TASK_ERROR;

var COLOR_PASSING = COLOR_TASK_COMPLETED;
var COLOR_WARNING = COLOR_ORDER_UNAPPROVE;
var COLOR_CRITICAL = COLOR_TASK_ERROR;
var COLOR_UNKNOWN = COLOR_TASK_READY;

var COLOR_ORDER_UNAPPROVED = COLOR_TASK_READY;
var COLOR_ORDER_SUCCESS = COLOR_TASK_COMPLETED;
var COLOR_ORDER_EXECUTING = COLOR_TASK_RUNNING;
var COLOR_ORDER_FAILED = COLOR_TASK_ERROR;

var ALLALIGN = null;
var PAGE_SIZE = 20;
var LINEFEED_PAGE_SIZE = 10;
var PAGE_SIZES = [5, 10, 15, 20, 50, 100, 150, 200];
var PAGE_LAYOUTS = ['PrevPage', 'JumpNumber', 'NextPage', 'FullJump', 'Sizes', 'Total'];

var ROWBTN_SHOWNUM = 3;
var ROWBTN_SHOWINDEX = ROWBTN_SHOWNUM - 1;

//创建svg图片 * color-svg的颜色
function getProjectSvg(color) {
    return '<svg style="height:14px;width:14px;margin: -2px 4px -2px 0;"><circle cx="7" cy="7" r="4" stroke-width="2" fill="' + color + '" /><circle cx="7" cy="7" r="7" stroke-width="2" fill="' + color + '" style="fill-opacity:0.4;" /></svg> '
}

/**
 * 按钮数据处理
 * @param btnLists 所有按钮的数据（数组）
 * @param btnPosText 需要判定的pos标识位字符（字符串）
 * @param delBtnCode 不进行处理的按钮code（非必填）（数组）
 * @returns {*} 返回树结构的按钮数据
 */
var btnDataSeparation = function (btnLists, btnPosText, delBtnCode) {
    var btnListTemp = XEUtils.clone(btnLists, true)
    var delBtnArray = []
    if (XEUtils.isArray(delBtnCode)) {
        XEUtils.arrayEach(delBtnCode, code => {
            var temp = XEUtils.remove(btnListTemp, item => item.code === code)[0]
            if (XEUtils.indexOf(temp.pos, btnPosText) > -1) {
                delBtnArray.push(temp)
            }
        })
    }
    var rowBtnListTemp = XEUtils.toTreeArray(btnListTemp, {children: 'childrens', clear: true})
    var btnArray = []
    XEUtils.arrayEach(rowBtnListTemp, value => {
        var btnPos = value.pos
        if (XEUtils.indexOf(btnPos, btnPosText) > -1) {
            btnArray.push(value)
        }
    })
    btnArray = XEUtils.union(btnArray, delBtnArray)
    return XEUtils.toArrayTree(btnArray, {parentKey: 'pid', children: 'childrens'})
}

//操作列初始列宽  * rowBtnList-按钮数据
/**
 * 操作列初始列宽
 * @param rowBtnList 按钮数据
 * @param {array} delBtnCode (选填) 不进行处理的按钮code
 * @returns {string}
 */
var operationWidthInit = function (rowBtnList, delBtnCode) {
    var rowBtnWidth = 0
    XEUtils.arrayEach(rowBtnList, (btnValue, btnIndex) => {
        var flag = true
        if (XEUtils.isArray(delBtnCode))
            XEUtils.arrayEach(delBtnCode, code => {
                if (btnValue.code === code)
                    flag = false
            })
        if (flag) {
            if (btnIndex === 0) {
                rowBtnWidth += 10
            } else {
                rowBtnWidth += 20
            }
            if (btnValue.type === 'navigation') {
                rowBtnWidth += 17
            }
            if (!XEUtils.isEmpty(btnValue.icon)) {
                rowBtnWidth += 16
            } else {
                let entryVal = btnValue.name
                let tempNum = 0
                let entryLen = entryVal.length;
                tempNum += entryLen
                let cnChar = entryVal.match(/[^\x00-\x80]/g);//利用match方法检索出中文字符并返回一个存放中文的数组
                entryLen = XEUtils.isEmpty(cnChar) ? 0 : cnChar.length //算出实际的字符长度
                tempNum += entryLen
                // let tempNum = btnValue.name.length
                rowBtnWidth += Math.ceil(6.5 * tempNum)
            }
        }
    })
    // console.log(rowBtnWidth)
    return rowBtnWidth + 20 + 'px'
}

//创建者括号合并 * name1-括号外面的值 name2-括号里面的值
var ownerNameDispose = function (name1, name2) {
    if (XEUtils.isEmpty(name1) && XEUtils.isEmpty(name2)) {
        return "";
    } else if (!XEUtils.isEmpty(name1) && XEUtils.isEmpty(name2)) {
        return name1;
    } else if (XEUtils.isEmpty(name1) && !XEUtils.isEmpty(name2)) {
        return name2;
    } else {
        return name1 + "(" + name2 + ")";
    }
}

//行按钮的值 * rowBtnValue-按钮数据
var rowBtnValue = function (rowBtnValue) {
    if (XEUtils.isEmpty(rowBtnValue.icon)) {
        return '<span class="vxe-button--content">' + rowBtnValue.name + '</span>'
    } else {
        var imgElement = '<img class="btnSvgColor" src="img/' + rowBtnValue.icon + '" width="16px" height="16px" onload="SVGInject(this)">'
        return '<a title="' + rowBtnValue.name + '">' + imgElement + '</a>'
    }
}
//ip/分配率返回 * name1-当前值 name2-最大值 name3-合并后括号中要显示的字符（不写为空）
var usedCapacityDispose = function (name1, name2, name3) {
    if (!name3) {
        name3 = ""
    }
    if (name1 == null || name2 == null || isNaN(name1) || isNaN(name2)) {
        return "";
    } else if (name1 === 0 || name2 === 0) {
        return "0.0%(" + name1 + "/" + name2 + name3 + ")";
    } else {
        return ((name1 / name2 * 100).toFixed(1) + "% (" + name1 + "/" + name2 + name3 + ")");
    }
}
//校验IP * ip-需要校验的字符串
var isValidIP = function (ip) {
    var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
    return reg.test(ip);
}
//计算两个时间的时间差，返回X天X小时XX分钟XX秒 * createdTime-开始时间 endTime-结束时间
var differenceTime = function (createdTime, endTime) {
    if (!createdTime || !endTime) {
        return ""
    }
    var differenceTime = new Date(endTime).getTime() - new Date(createdTime).getTime()

    if (differenceTime < 0) {
        return ""
    }

    var days = Math.floor(differenceTime / (24 * 3600 * 1000))
    var leave1 = differenceTime % (24 * 3600 * 1000)
    var hours = Math.floor(leave1 / (3600 * 1000))
    var leave2 = leave1 % (3600 * 1000)
    var minutes = Math.floor(leave2 / (60 * 1000))
    var leave3 = leave2 % (60 * 1000)
    var seconds = Math.round(leave3 / 1000)
    //console.log(" 相差 "+days+"天 "+hours+"小时 "+minutes+" 分钟"+seconds+" 秒")

    var returnString = ""
    if (days !== 0) {
        returnString = days + "天" + hours + "小时" + minutes + "分钟" + seconds + "秒"
    } else {
        if (hours !== 0) {
            returnString = hours + "小时" + minutes + "分钟" + seconds + "秒"
        } else {
            if (minutes !== 0) {
                returnString = minutes + "分钟" + seconds + "秒"
            } else {
                if (seconds !== 0) {
                    returnString = seconds + "秒"
                }
            }
        }
    }

    return returnString;
}
//根据数值的大小改变数值的后缀 * sizeData-数值
var sizeSuffixAlter = function (sizeData) {

    size = parseInt(sizeData)
    var sizeValue = ""
    if (size >= 1024 * 1024 * 1024) {
        sizeValue = (size / (1024 * 1024 * 1024)).toFixed(2) + " T"
    } else if (size >= 1024 * 1024) {
        sizeValue = (size / (1024 * 1024)).toFixed(2) + " G"
    } else if (size >= 1024) {
        sizeValue = (size / 1024).toFixed(2) + " M"
    } else {
        sizeValue = size + " K"
    }
    if (sizeData === 0) {
        sizeValue = '0'
    }
    return sizeValue;
}
//数组合并逗号分隔 * array-需要合并的数组 object-提取数组中某个值 value-连接的符号(不填默认，)
var arrayMerger = function (array, object, value) {
    var mergerData = "";
    if (value === null || value === undefined) {
        value = ","
    }
    XEUtils.arrayEach(array, (v, i) => {
        if (i === 0) {
            if (object === null || object === undefined) {
                mergerData += v;
            } else {
                mergerData += v[object];
            }
        } else {
            if (object === null || object === undefined) {
                mergerData += value + v;
            } else {
                mergerData += value + v[object];
            }
        }
    })
    return mergerData;
}
//判断json是否未定义 * jsonData-需要判断的整个json数据 jsonStr-需要判断的json路径
var jsonJudgeNotDefined = function (jsonData, jsonStr) {
    var jsonArray = jsonStr.split(".");
    var forData = jsonData;
    var returnValue = '';
    var returnArray = [];
    var arrayFlag = false;
    var arrayIndex = 0;
    for (var i = 0; i < jsonArray.length; i++) {
        if (forData === null || forData === undefined) {
            break;
        }
        if (i !== jsonArray.length - 1) {
            if (jsonArray[i + 1].split("<>").length === 1) {
                forData = forData[jsonArray[i + 1]]
            } else {
                arrayFlag = true
                arrayIndex = i + 1
                forData = forData[jsonArray[i + 1].split("<>")[0]]
                jsonArray.splice(0, arrayIndex)
                break;
            }
        } else {
            returnValue = forData
        }
    }
    if (arrayFlag) {
        if (forData === null || forData === undefined) {
            return ""
        }
        if (forData.length !== 0) {
            for (var i = 0; i < forData.length; i++) {
                returnArray.push(jsonJudgeNotDefined(forData[i], arrayMerger(jsonArray, null, ".")))
            }
            var flag = false
            for (var i = 0; i < returnArray.length; i++) {
                if (returnArray[i] !== null && returnArray[i] !== "") {
                    flag = true
                }
            }
            if (flag) {
                return returnArray
            } else {
                return ""
            }
        }
    }
    return returnValue
}
//搜索数组 * searchValue-要搜索的值 searchRow-搜索的数据源 searchKeyArray-搜索的数据源的key值（如不传值则为搜索数组）
var searchFun = function (searchValue, searchRow, searchKeyArray) {
    var returnData = [];
    let searchArray;
    let searchOrArray = searchValue.split(' || ');
    let searchAndArray = searchValue.split(' && ');

    if (searchOrArray.length > 1 && searchAndArray.length > 1) {
        return "两种搜索条件不能同时存在！";
    } else if (searchOrArray.length > 1) {
        searchArray = searchOrArray
    } else if (searchAndArray.length > 1) {
        searchArray = searchAndArray
    } else {
        searchArray = searchOrArray
    }

    if (searchArray.length === 1) {
        // searchValue = XEUtils.trim(XEUtils.toString(searchValue[0]))
        if (!XEUtils.isEmpty(searchValue)) {
            if (!XEUtils.isEmpty(searchRow)) {
                for (var rowIndex = 0; rowIndex < searchRow.length; rowIndex++) {
                    var rowItem = searchRow[rowIndex]
                    if (searchKeyArray !== undefined) {
                        for (var keyIndex = 0; keyIndex < searchKeyArray.length; keyIndex++) {
                            var keyItem = searchKeyArray[keyIndex]
                            var rowValue = jsonJudgeNotDefined(rowItem, "rowItem." + keyItem)
                            if (!XEUtils.isArray(rowValue)) {
                                rowValue = XEUtils.toString(rowValue)
                                if (XEUtils.indexOf(rowValue.toLowerCase(), XEUtils.trim(searchValue.toLowerCase())) > -1) {
                                    returnData.push(rowItem)
                                    break;
                                }
                            } else {
                                if (!XEUtils.isEmpty(searchFun(searchValue, rowValue))) {
                                    returnData.push(rowItem)
                                    break;
                                }
                            }
                        }
                    } else if (XEUtils.isArray(rowItem)) {
                        if (!XEUtils.isEmpty(searchFun(searchValue, rowItem))) {
                            returnData.push(rowItem)
                            break;
                        }
                    } else {
                        rowItem = XEUtils.toString(rowItem)
                        if (XEUtils.indexOf(rowItem.toLowerCase(), searchValue.toLowerCase()) > -1) {
                            returnData.push(rowItem)
                            break;
                        }
                    }
                }
                return returnData;
            } else {
                return []
            }
        } else {
            return searchRow;
        }
    } else {
        if (searchOrArray.length > 1) {
            XEUtils.arrayEach(searchArray, (value) => {
                if (!XEUtils.isEmpty(value))
                    returnData = XEUtils.union(returnData, searchFun(value, searchRow, searchKeyArray))
            })
        } else if (searchAndArray.length > 1) {
            returnData = searchRow
            XEUtils.arrayEach(searchArray, (value) => {
                if (!XEUtils.isEmpty(value))
                    returnData = searchFun(value, returnData, searchKeyArray)
            })
        }
        return returnData;
    }
}

//产生任意长度随机字母数字组合 * randomFlag-是否任意长度 min-任意长度最小位[固定位数] max-任意长度最大位
var randomWord = function (randomFlag, min, max) {
    var str = "",
        range = min,
        arr = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'];

    // 随机产生
    if (randomFlag) {
        range = Math.round(Math.random() * (max - min)) + min;
    }
    for (var i = 0; i < range; i++) {
        pos = Math.round(Math.random() * (arr.length - 1));
        str += arr[pos];
    }
    return str;
}

//清除session数据
var clearSession = function () {
    if (typeof (Storage) !== "undefined") {
        sessionStorage = {};
    } else {
        document.cookie = "";
    }
}
//存储session数据 * k-session的key值 v-session的value值
var setSession = function (k, v) {
    if (typeof (Storage) !== "undefined") {
        sessionStorage[k] = v;
    } else {
        if (document.cookie === "") {
            document.cookie = k + "=" + v;
        } else {
            var old_v = getSession(k);
            if (old_v) {
                document.cookie.replace(k + "=" + old_v, k + "=" + v)
            } else {
                document.cookie = document.cookie + " ;" + k + "=" + v;
            }

        }
    }
}

//获取session数据 * k-session的key值
var getSession = function (k) {
    if (typeof (Storage) !== "undefined") {
        return sessionStorage[k];
    } else {
        var cookieArr = document.cookie.split(";")
        for (cookie in cookieArr) {
            if (k === cookie.split("=")[0]) {
                return cookie.split("=")[1]
            }
        }
        return undefined;
    }
}

//获取get传的值 * variable-要获取的key
var getQueryVariable = function (variable) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === variable) {
            if (variable === "menu") {
                return JSON.parse(decodeURIComponent(pair[1], "utf-8"));
            }
            return pair[1];
        }
    }
    return undefined;
}

//获取get传的数组 * variable-要获取的key decodeFlag - 是否需要解码
var getQueryArrayVariable = function (variable, decodeFlag) {
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    let returnArray = []
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split("=");
        if (pair[0] === variable) {
            if (decodeFlag) {
                returnArray.push(JSON.parse(decodeURIComponent(pair[1], "utf-8")))
            } else {
                returnArray.push(pair[1])
            }
        }
    }
    if (returnArray.length !== 0) {
        return returnArray;
    } else {
        return undefined;
    }
}

//操作完成提示 * obj-操作对象 msg-提示信息 type-类型 value-自定义设置
var operationCompletion = function (obj, msg, type, value) {
    if (!XEUtils.isPlainObject(value))
        value = {}
    if (XEUtils.isEmpty(type)) {
        type = "success"
    }
    var showTime = 0
    switch (type) {
        case "success":
            showTime = 1000;
            break;
        case "warning":
            showTime = 3000;
            break;
        case "error":
            showTime = 0;
            obj.$message.closeAll();
            break;
        default:
            console.error("操作提示：类型错误（未定义）")
            return false;
    }
    // obj.$message.closeAll();
    var init = {
        showClose: true,
        duration: showTime,
        message: msg,
        dangerouslyUseHTMLString: true,
        type: type,
        customClass: 'elMessageDiv'
    }
    XEUtils.merge(init, value)
    obj.$message(init)
}

/**
 * 操作确认弹窗
 * @param obj 操作对象(必填)
 * @param titleText 弹窗标题(必填)
 * @param hintText 提示语句(必填)
 * @param value 自定义设置(非必填)
 * @returns {*} 返回操作弹窗
 */
var commonConfirm = function (obj, titleText, hintText, value) {
    if (!XEUtils.isPlainObject(value))
        value = {}
    var init = {
        cancelButtonClass: "btn-custom-cancel",
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        dangerouslyUseHTMLString: true,
        type: 'warning',
        customClass: confirmCustomPrompt(hintText)
    }
    XEUtils.merge(init, value)
    return obj.$confirm(hintText, titleText, init)
}

/**
 * 操作确认弹窗提示自定义类名
 * @param hintText 提示语句(必填)
 */
var confirmCustomPrompt = function (hintText) {
    var customClass = ''
    //拆分提示语句以‘<br>’为分界的段,组成数组
    let hintArray = hintText.split("<br>")
    //拆分提示语句以‘<br>’为分界的段，并以相应段的长度除以420px宽度下每行所能容纳的字符数（中文占两个字符）；+2 指的是提示语句的“ ？”
    XEUtils.each(hintArray, (item, key) => {
        var hintTextLength = 0
        //利用match方法检索出中文字符并返回一个存放中文的数组,并且算出中文字符所占长度，+2 指的是提示语句的句末标点
        let cnChar = item.match(/[^\x00-\x80]/g)
        hintTextLength += XEUtils.isEmpty(cnChar) ? 0 : cnChar.length
        hintTextLength += item.length + 2
        //如果提示语句中存在<b>标签或者<span>标签，则在总长度中减去其所占用的字符长度
        if (item.indexOf('</b>') != -1)
            hintTextLength -= 9
        if (item.indexOf('</span>') != -1)
            hintTextLength -= 54
        //420px宽度下，一行所能显示的最大字符长度为  44， 除以44 意思是指总长度可以放置多少行 44
        hintTextLength = hintTextLength / 44
        //通过得出的行数减去行数的整数部分，如果余数超过0.2就改变弹窗的类名，进而通过类名改变弹窗的宽度
        //parseInt(hintTextLength) !== 0  >>  当行数的整数部分为0时，也就代表着一行就足够
        //(hintTextLength - parseInt(hintTextLength)) != 0  >>  当行数减去行数的整数部分等于0时，也就是说行数刚刚好
        if ((hintTextLength - parseInt(hintTextLength)) < 0.2 &&
            parseInt(hintTextLength) !== 0 &&
            (hintTextLength - parseInt(hintTextLength)) != 0) {
            customClass = 'elConfirmDiv'
        }
    })
    return customClass
}

/**
 * 提示语句
 * @param operationType 操作类型(必填)
 * @param name 操作对象名称(非必填)
 * @param value 自定义设置(非必填)
 * @returns {*} 返回提示语句
 */
var getHintText = function (operationType, name, value) {
    if (!XEUtils.isUndefined(value))
        return value

    var hintText = ''
    if (XEUtils.isString(name)) {
        if (operationType === "编辑") {
            hintText = ' "  <b>' + name + '</b>  " 已<span style="color:green;font-weight:bold">启用</span>，是否确认停用并编辑?'
        } else {
            hintText = '是否确认' + operationType + ' "  <b>' + name + '</b>  " ?'
        }
    } else if (XEUtils.isArray(name)) {
        hintText = '是否确认【' + operationType + '】所选' + name[0] + '?'
    } else {
        hintText = '是否确认' + operationType + '?'
    }
    return hintText
}

var getRiskLevelHtml = function (num, text) {
    var htmlText = "风险等级："
    switch (num) {
        case 0:
            htmlText += "☆☆☆☆☆"
            break;
        case 1:
            htmlText += "★☆☆☆☆"
            break;
        case 2:
            htmlText += "★★☆☆☆"
            break;
        case 3:
            htmlText += "★★★☆☆"
            break;
        case 4:
            htmlText += "★★★★☆"
            break;
        case 5:
            htmlText += "★★★★★"
            break;
    }
    htmlText += "<br>" + text + "<br>"

    return htmlText
}

//列按钮排序
function sortRowSeq(a, b) {
    return a.rowSeq - b.rowSeq
}

//layer弹窗打开 * width-宽度 height-高度 title-标题 url-打开页面地址 value-自定义元素
var layerOpenFun = function (width, height, title, url, value) {
    if (XEUtils.getType(value) !== "object")
        value = {}
    $(':focus').blur();
    var init = {
        type: 2,
        area: [width, height],
        offset: "100px",
        moveOut: true,
        fixed: false,
        maxmin: true,
        title: title,
        content: url,
        zIndex: 1000,
        success: function (layero, index) {
        }
    }
    XEUtils.assign(init, value)
    layer.open(init);
}

var filterNameFlag = ""
var commonRefreshList = function (_this) {
    _this.filterName = filterNameFlag
    _this.btnClick(_this.btnReplaceList.refresh, true, 0)
}

var commonHandleSizeCurrentChange = function (_this, value, code) {
    if (XEUtils.isUndefined(code)) {
        _this.tablePage.pageSize = value.pageSize
        _this.tablePage.currentPage = value.currentPage
        _this.handlePageChange()
    } else {
        _this.tablePage[code].pageSize = value.pageSize
        _this.tablePage[code].currentPage = value.currentPage
        _this.handlePageChange(code)
    }
}

var commonHandlePageChange = function (_this, code) {
    let startNum = (_this.tablePage.currentPage - 1) * _this.tablePage.pageSize;
    let finishNum = startNum + _this.tablePage.pageSize;

    _this.tablePage.totalResult = _this.searchData.length
    if (finishNum > _this.tablePage.totalResult) {
        finishNum = _this.tablePage.totalResult
    }
    if (startNum === finishNum && startNum !== 0) {
        _this.tablePage.currentPage = startNum / _this.tablePage.pageSize
        startNum -= _this.tablePage.pageSize
    }

    _this.tableData = _this.sortData.slice(startNum, finishNum)
    commonGoToAssignPage(_this, code)
}

var commonGoToAssignPage = function (_this, code) {
    let pageSize = XEUtils.isUndefined(code) ? _this.tablePage.pageSize : _this.tablePage[code].pageSize
    let tableDataAll = XEUtils.isUndefined(code) ? _this.tableDataAll : _this.tableDataAll[code]

    if (!XEUtils.isEmpty(_this.refreshSaveData)) {
        let num = 1
        let tempPageSize = pageSize
        let tempJson = _this.refreshSaveData[0]
        let tempArray = _this.refreshSaveData
        _this.refreshSaveData = []
        if (XEUtils.isNumber(tempJson)) {
            num = (tempJson - 1) * tempPageSize
        } else {
            XEUtils.arrayEach(tempArray, (tempItem, tempIndex) => {
                XEUtils.arrayEach(tableDataAll, (item, index) => {
                    let tempNum = 0
                    let chooseNum = 0
                    XEUtils.arrayEach(_this.refreshKey, (itemKey, indexKey) => {
                        if (jsonJudgeNotDefined(item, "item." + itemKey) === jsonJudgeNotDefined(tempJson, "tempJson." + itemKey))
                            tempNum++
                        if (jsonJudgeNotDefined(item, "item." + itemKey) === jsonJudgeNotDefined(tempItem, "tempItem." + itemKey))
                            chooseNum++
                    })
                    if (tempNum === _this.refreshKey.length)
                        num = index
                    if (chooseNum === _this.refreshKey.length)
                        item.isHighlight = true
                })
            })

        }

        let tempCurrentPage = XEUtils.toInteger((num / tempPageSize) + 1)
        let tempPageJson = {
            currentPage: tempCurrentPage,
            pageSize: tempPageSize
        }
        commonHandleSizeCurrentChange(_this, tempPageJson, code)
    }
}

var commonGetSortFun = function (order, sortBy) {
    let ordAlpah = (order === 'asc') ? '>' : '<';
    return new Function('a', 'b', 'return a.' + sortBy + ordAlpah + 'b.' + sortBy + '?1:-1');
}

var commonSortChange = function (_this, column, settings = {}) {
    if (column.order != null) {
        _this.sortData.sort(commonGetSortFun(column.order, column.prop))
    } else {
        _this.sortData = _this.searchData.concat();
    }
    if (!settings.changePage) {
        _this.tablePage.currentPage = 1
    }
    _this.handlePageChange()
}

var commonSearchClick = function (_this, keyArray, settings = {}) {
    var data = _this.tableDataAll.concat();
    let searchArray = data.length === 0 ? [] : searchFun(_this.filterName, data, keyArray);
    if (XEUtils.isArray(searchArray)) {
        filterNameFlag = _this.filterName
        _this.searchData = searchArray
        try {
            _this.$refs.xTable.clearAll()
        } catch (e) {
            console.error(e)
        }
        _this.sortChange({order: null}, settings)
    } else {
        _this.$message.closeAll();
        operationCompletion(_this, searchArray, 'error')
    }
}

var commonTableRowClassName = function (data) {
    if (!XEUtils.isUndefined(data.row.isHighlight)) {
        if (data.row.isHighlight) {
            return 'row-highlightShow'
        } else {
            return 'row-normalShow'
        }
    }
}

var commonCancelHighlight = function (_this, code) {
    let tableDataAll = XEUtils.isUndefined(code) ? _this.tableDataAll : _this.tableDataAll[code]
    XEUtils.arrayEach(tableDataAll, (item, index) => {
        if (!XEUtils.isUndefined(item.isHighlight)) {
            item.isHighlight = false
        }
    })
}

var commonOnresize = function () {

    let btnBoxHeight = 0
    let pageBoxHeight = 0
    let crumbsBoxHeight = 20

    if (document.getElementsByClassName("el-breadcrumb").length !== 0) {
        crumbsBoxHeight = document.getElementsByClassName("el-breadcrumb")[0].offsetHeight + 10
    }

    if (document.getElementsByClassName("btnParentDiv").length !== 0) {
        btnBoxHeight = document.getElementsByClassName("btnParentDiv")[0].offsetHeight
    }
    if (document.getElementsByClassName("vxe-pager").length !== 0) {
        pageBoxHeight = document.getElementsByClassName("vxe-pager")[0].offsetHeight
    }
    //60 ：面包屑高度 + 所有边框线
    return document.documentElement.clientHeight - crumbsBoxHeight - btnBoxHeight - pageBoxHeight + 'px';
}

var commonMinWidth = function (name, obj, refName) {
    let tempJson = {}

    tempJson['title'] = name
    tempJson['min-width'] = resizableMinWidth(name)
    if (obj !== undefined) {
        if (obj.$refs[refName])
            obj.$refs[refName].refreshColumn()
    }
    return tempJson
}

var resizableMinWidth = function (value) {
    let title = value
    if (XEUtils.has(value, 'column.title'))
        title = value.column.title

    let entryVal = title
    let tempNum = 0
    let entryLen = entryVal.length;
    tempNum += entryLen
    let cnChar = entryVal.match(/[^\x00-\x80]/g);//利用match方法检索出中文字符并返回一个存放中文的数组
    entryLen = XEUtils.isEmpty(cnChar) ? 0 : cnChar.length //算出实际的字符长度
    tempNum += entryLen

    return Math.ceil(6.5 * tempNum) + 40 + 'px'
}

var errorMsg = function (errorArray, arrayFlag, dataNameKey, dataMsgKey) {
    let dataName = XEUtils.isEmpty(dataNameKey) ? "errorArray.data.name" : dataNameKey
    let dataMsg = XEUtils.isEmpty(dataMsgKey) ? "errorArray.object.response.data.msg" : dataMsgKey
    let errorName = ''
    let errorMsg = ''
    let str = ''
    if (!arrayFlag && XEUtils.isBoolean(arrayFlag))
        return XEUtils.isFunction(dataMsgKey) ? dataMsgKey(errorArray[0].object) : jsonJudgeNotDefined(errorArray[0], dataMsg)
    XEUtils.arrayEach(errorArray, (item, key) => {
        errorName = XEUtils.isFunction(dataNameKey) ? dataNameKey(item.data) : jsonJudgeNotDefined(item, dataName)
        errorMsg = XEUtils.isFunction(dataMsgKey) ? dataMsgKey(item.object) : jsonJudgeNotDefined(item, dataMsg)
        if (XEUtils.isEmpty(errorName)) {
            errorName = "第" + (key + 1) + "个"
        }
        str += '<div class="msgHintStyle" title="' + errorMsg + '">【<b>' + errorName + '</b>】  ' + errorMsg + '</div>'
    })
    return str
}

var getHtmlMinWidth = function (idName, _this, tableRefNames, tableRefFootAddName) {
    let widthArray = []

    //页面上方按钮宽度计算
    let btnDivs = document.getElementsByClassName('btnParentDiv');
    XEUtils.arrayEach(btnDivs, (btnDivsValue) => {
        let btnsLength = 0
        if (btnDivsValue.children.length === 1 && btnDivsValue.children[0].id === "btnPanel") {
            let btns = btnDivsValue.children[0].children
            XEUtils.arrayEach(btns, (value, index) => {
                // console.log(value.offsetWidth)
                btnsLength += value.offsetWidth
            })
            //增加btnPanel与btnParentDiv之间的宽度
            btnsLength += btnDivsValue.offsetWidth - btnDivsValue.children[0].offsetWidth
        }
        //增加30使盒子不换行（不确定这30从哪里来）
        btnsLength += 30
        //使左右两遍按钮分开50空隙
        btnsLength += 50
        widthArray.push(btnsLength)
    })
    // console.log(XEUtils.max(widthArray))

    //页面表格宽度计算
    XEUtils.arrayEach(tableRefNames, (tableRefName) => {
        let colWidth = 0
        if (XEUtils.isEmpty(tableRefFootAddName))
            tableRefFootAddName = ""
        let tempTableRefData = _this.$refs[tableRefName + tableRefFootAddName]
        let tableRefData = XEUtils.isArray(tempTableRefData) ? tempTableRefData[0] : tempTableRefData
        XEUtils.arrayEach(tableRefData.getColumns(), (col) => {
            if (!XEUtils.isEmpty(col.minWidth)) {
                // console.log(col.minWidth.split('px')[0])
                colWidth += XEUtils.toNumber(col.minWidth)
            } else {
                colWidth += XEUtils.toNumber(col.width)
            }

        })
        //多加30防止没有计算滚动条
        colWidth += 30
        widthArray.push(colWidth)
    })
    // console.log(XEUtils.max(widthArray))

    //页面表格分页假设最大710宽度
    widthArray.push(710)

    //通过DOM修改页面最小宽度
    document.getElementById(idName).style.minWidth = XEUtils.max(widthArray) + "px"
}

var pagePathReturn = function (pageType) {
    switch (pageType) {
        case '所属业务区':
        case '关联业务区':
            return '/app/resource/business_areas'

        case '备份存储':
            return '/app/resource/backup_storage'

        case '所属集群':
        case '关联集群':
        case 'cluster':
            return '/app/resource/clusters'

        case '所属网段':
        case '关联网段':
            return '/app/resource/networks'
        case 'host':
            return '/app/resource/hosts'
    }
}

var colorListReturn = function (params) {
    var colorList = []
    if (params.name === '使用率' && params.percent > 80) {
        return colorList = ['#f8453f', '#dfeee7']
    } else if (params.name === '使用率' && params.percent > 60) {
        return colorList = ['#ff9900', '#dfeee7']
    } else {
        return colorList = ['#19be6b', '#dfeee7']
    }
}

/**
 * 跳转页面方法
 * @param type 类型
 * @param searchName 关键字
 * @param searchKeyType 关键字类型（搜索或高亮）
 */
var pageJumpFun = function (type, searchName = '', searchKeyType = 'highlight') {
    var json = {}
    var menuSkip = {}
    var urlName = ""
    var code = pagePathReturn(type)
    var menuLists = window.top.indexApp.menuLists
    var menuId;
    XEUtils.arrayEach(menuLists, (menuList, menuIndex) => {
        XEUtils.arrayEach(menuList.childrens, (subMenuList, subMenuIndex) => {
            if (subMenuList.code === code) {
                menuSkip = {
                    name: menuList.name,
                    icon: menuList.icon,
                    subMenu: {
                        name: subMenuList.name
                    }
                }
                urlName = subMenuList.name
                menuId = subMenuList.id
            }
        })
    })
    var url = code + "?id=" + menuId + "&menu=" + encodeURIComponent(JSON.stringify(menuSkip), "utf-8") + "&searchKeyWord=" + searchName + "&searchKeyType=" + searchKeyType + "&t=" + (new Date()).getTime()
    json = {
        code: url,
        name: urlName,
    }
    var tab = {
        name: url
    }
    window.top.indexApp.tabsJump(tab)
    window.top.indexApp.menuClick(json, null, false)
}