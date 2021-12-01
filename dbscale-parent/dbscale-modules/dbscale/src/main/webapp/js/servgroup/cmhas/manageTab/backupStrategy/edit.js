var editIndex = parent.layer.getFrameIndex(window.name);
var strategyId = getQueryVariable("strategy_id")
var backupStrategyEditApp = new Vue({
    el: '#backupStrategyEdit',
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
            fileRetentionNum: 1,
            description: ''
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
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        backupStorageTypeListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=backup_storage_type", function (response) {
                _this.backupStorageTypeList = response.data.data
                _this.endFun()
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
        },
        endFun: function () {
            this.initialization++;
            if (this.initialization === this.endNum)
                this.editCreated()
        },
        editCreated: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/backup_strategies/" + strategyId, function (response) {
                var data = response.data.data;
                _this.formData.type = data.type.code
                _this.formData.backupStorageType = data.backupStorageType.code

                var cronExpressionArray = jsonJudgeNotDefined(data, "data.cronExpression").split(' ')
                _this.formData.time = cronExpressionArray[0] + " " + cronExpressionArray[1]
                if (cronExpressionArray[2] !== "*") {
                    _this.formData.cronExpression = 'month'
                    _this.formData.month = cronExpressionArray[2]
                } else if (cronExpressionArray[4] !== "*") {
                    _this.formData.cronExpression = 'week'
                    _this.formData.week = cronExpressionArray[4]
                } else {
                    _this.formData.cronExpression = 'day'
                }

                _this.formData.fileRetentionNum = data.fileRetentionNum
                _this.formData.description = data.description
                layer.closeAll('loading')
            }, function (error) {
            	operationCompletion( _this, error.response.data.msg, "error")
            }, null)
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
                backupStorageType: backupStorageType,
                type: type,
                tables: [],
                cronExpression: cronExpression,
                fileRetentionNum: fileRetentionNum,
                description: description
            }
            var _this = this
        	commonConfirm(_this, '编辑确认', getHintText('编辑')).then(() => {
                sendPut("/" + getProjectName() + "/backup_strategies/" + strategyId, function (response) {
                    layer.closeAll('loading')
                    operationCompletion(parent[0].backupStrategyListApp, "操作成功！")
                    parent[0].backupStrategyListApp.refreshSaveData.push(parent[0].backupStrategyListApp.selectedData)
                    parent[0].backupStrategyListApp.returnList()
                    _this.formClose()
                }, function (error) {
                	operationCompletion( _this, error.response.data.msg, "error")
                }, jsonData)
            })
        },
        formClose: function () {
            if(parent[0].backupStrategyListApp.updateStatus)
            	parent[0].backupStrategyListApp.returnList()
            parent.layer.close(editIndex);
        }
    }
})