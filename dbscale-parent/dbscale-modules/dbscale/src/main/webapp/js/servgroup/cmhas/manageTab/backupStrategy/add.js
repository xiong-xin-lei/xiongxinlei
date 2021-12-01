var addIndex = parent.layer.getFrameIndex(window.name);
var servGroupId = getQueryVariable("serv_group_id")
var validateFileRetentionNum = (rule, value, callback) => {
    switch (value) {
        case '':
        case null:
        case 0:
        case undefined:
            switch (backupStrategyAddApp.formData.cronExpression) {
                case 'day':
                    callback(new Error('请输入文件保留天数'));
                    break;
                case 'week':
                    callback(new Error('请输入文件保留周数'));
                    break;
                case 'month':
                    callback(new Error('请输入文件保留月数'));
                    break;
                default:
                    callback(new Error('请输入文件保留时间'));
                    break;
            }
            break;
        default:
            callback();
            break;
    }
};
var backupStrategyAddApp = new Vue({
    el: '#backupStrategyAdd',
    data: {
        initialization: 0,
        endNum: 2,
        backupTypeList: [],
        backupStorageTypeList: [],
        cronExpressionList: [
            {
                code: 'day',
                name: '按天'
            }, {
                code: 'week',
                name: '按周'
            }, {
                code: 'month',
                name: '按月'
            }
        ],
        timeList: function (start, end) {
            var array = []
            for (var i = start; i <= end; i++) {
                array.push(
                    {
                        code: i,
                        name: i
                    }
                )
            }
            return array
        },
        showItem: function (type) {
            switch (this.formData.cronExpression) {
                case 'week':
                    switch (type) {
                        case 'week':
                            return true
                        default:
                            return false
                    }
                case 'month':
                    switch (type) {
                        case 'month':
                            return true
                        default:
                            return false
                    }
                default:
                    return false
            }
        },
        formData: {
            backupStorageType: '',
            type: '',
            cronExpression: 'day',
            month: null,
            week: null,
            time: null,
            fileRetentionNum: 3,
            description: ''
        },
        formRules: {
            backupStorageType: [
                {required: true, message: '请选择备份存储类型'}
            ],
            type: [
                {required: true, message: '请选择备份类型'}
            ],
            cronExpression: [
                {required: true, message: '请选择备份周期'}
            ],
            month: [
                {required: true, message: '请选择日期'}
            ],
            week: [
                {required: true, message: '请选择周'}
            ],
            time: [
                {required: true, message: '请选择时间'}
            ],
            fileRetentionNum: [
                {required: true, validator: validateFileRetentionNum, trigger: ['blur', 'change']}
            ]
        },
        fileRetentionName: function () {
            switch (this.formData.cronExpression) {
                case 'day':
                    return "文件保留天数"
                case 'week':
                    return "文件保留周数"
                case 'month':
                    return "文件保留月数"
                default:
                    return "文件保留时间"
            }
        }
    },
    created: function () {
        this.backupTypeListView()
        this.backupStorageTypeListView()
    },
    methods: {
        backupTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_type", function (response) {
                _this.backupTypeList = response.data.data
                _this.formData.type = _this.backupTypeList[0].code
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        backupStorageTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_storage_type", function (response) {
                _this.backupStorageTypeList = response.data.data
                _this.formData.backupStorageType = _this.backupStorageTypeList[0].code
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        endFun: function () {
            this.initialization++;
            if (this.initialization === this.endNum)
                layer.closeAll('loading')
        },
        cronExpressionChange: function (formName) {
            this.$refs[formName].clearValidate();
        },
        formSubmit: function (formName) {//backupStrategyForm
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
            var backupStorageType = this.formData.backupStorageType
            var type = this.formData.type
            var cronExpressionType = this.formData.cronExpression
            var month = this.formData.month
            var week = this.formData.week
            var time = this.formData.time
            var cronExpression = ""
            switch (cronExpressionType) {
                case 'day':
                    cronExpression = time + " * * *"
                    break;
                case 'week':
                    cronExpression = time + " * * " + week
                    break;
                case 'month':
                    cronExpression = time + " " + month + " * *"
                    break;
            }
            var fileRetentionNum = this.formData.fileRetentionNum
            var description = this.formData.description
            var jsonData = {
                servGroupId: servGroupId,
                backupStorageType: backupStorageType,
                type: type,
                tables: [],
                cronExpression: cronExpression,
                fileRetentionNum: fileRetentionNum,
                enabled: true,
                description: description
            }
            var _this = this
        	 commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/backup_strategies", function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent[0].backupStrategyListApp, "操作成功！")
                    parent[0].backupStrategyListApp.btnClick(parent[0].backupStrategyListApp.btnReplaceList.refresh, true, 0)
                    _this.formClose()
                }, function (error) {
                	operationCompletion( _this, error.response.data.msg, "error")
                }, jsonData)
            })
        },
        formClose: function () {
            parent.layer.close(addIndex);
        }
    }
})