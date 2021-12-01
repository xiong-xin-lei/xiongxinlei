var addIndex = parent.layer.getFrameIndex(window.name);
var checkNum = (rule, value, callback) => {
    const r = /^\+?[1-9][0-9]*$/;
    value = XEUtils.toString(value)
    var valueSplit = value.split(".")
    if (!r.test(value)) {
        if (valueSplit.length <= 2) {
            if (valueSplit[0].length !== 1)
                if (valueSplit[0][0] === "0") {
                    callback(new Error('格式不正确(开头数字不能为0)'));
                    return false;
                }
            if (valueSplit.length === 2)
                if (valueSplit[1].length !== 1) {
                    callback(new Error('精确到小数点后一位'));
                    return false;
                }
        } else {
            callback(new Error('只能有一位小数点'));
            return false;
        }
    }
    value = XEUtils.toNumber(value)
    if (value === 0) {
        callback(new Error('不能为0'));
        return false;
    }
    if (value >= 1000) {
        callback(new Error('最大值为999.9'));
        return false;
    }
    callback();
};
var checkMemNum = (rule, value, callback) => {
    value = XEUtils.toNumber(value)
    if (add.formData.type === "mysql") {
        if (value < 2) {
            callback(new Error('最小值为2'));
            return false;
        }
    } else {
        if (value < 1) {
            callback(new Error('最小值为1'));
            return false;
        }
    }
    callback();
};
var add = new Vue({
    el: '#add',
    data: {
        typeList: [],
        formData: {
            type: '',
            cpuCnt: '',
            memSize: [],
            enabled: true,
        },
        formRules: {
            type: [
                {required: true, message: '请选择类型'}
            ],
            cpuCnt: [
                {required: true, message: '请输入CPU数量'},
                {validator: checkNum, trigger: ['change', 'blur']}
            ],
            memSize: [
                {required: true, message: '请输入内存容量'},
                {validator: checkNum, trigger: ['change', 'blur']},
                {validator: checkMemNum, trigger: ['change', 'blur']}
            ],
            enabled: [
                {required: true, message: '请选择状态'}
            ]
        }
    },
    created: function () {
        this.typeListView();
    },
    methods: {
        typeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/def_servs?enabled=true", function (response) {
                _this.typeList = response.data.data
                layer.closeAll('loading')
            }, function (error) {
                operationCompletion(_this, error.response.data.msg, 'error')
            }, null)
        },
        formSubmit: function (formName) {//addModal
            var verify = false;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    return true;
                } else {
                    verify = true;
                    //console.log('error submit!!');
                    return false;
                }
            });
            if (verify) {
                return false;
            }

            var jsonData = {
                "type": this.formData.type,
                "cpuCnt": this.formData.cpuCnt,
                "memSize": this.formData.memSize,
                "enabled": this.formData.enabled
            }
            var _this = this
            //console.log("新增",jsonData)
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/scales", function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent.listApp, "操作成功！")
                    parent.listApp.returnList()
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            }).catch(() => {
            });
        },
        formClose: function () {
            parent.layer.close(addIndex);
        }
    }
})