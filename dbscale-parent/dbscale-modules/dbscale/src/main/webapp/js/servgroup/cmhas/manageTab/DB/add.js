var addIndex = parent.layer.getFrameIndex(window.name);
var checkName = (rule, value, callback) => {
    var reg = new RegExp("^[0-9]*$");
    if (reg.test(value)) {
        callback(new Error("库名不能为纯数字"));
        return false;
    }
    callback();
};
new Vue({
    el: '#add',
    data: {
        characterSetList: [],
        formData: {
            name: '',
            characterSetId: '',
        },
        formRules: {
            name: [
                {required: true, message: '请输入库名'},
                {pattern: /^[a-zA-Z\d_]+$/, message: '格式不正确（只能输入英文、数字和下划线）', trigger: 'blur'},
                {validator: checkName, trigger: 'blur'}
            ],
            characterSetId: [
                {required: true, message: '请选择字符集'}
            ]
        }
    },
    created: function () {
        this.characterSetListView()
    },
    methods: {
        characterSetListView: function () {
            var _this = this
            sendGet("/" + getProjectName() + "/dicts?dict_type_code=character_set", function (response) {
                _this.characterSetList = response.data.data,
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
                "name": this.formData.name,
                "characterSet": this.formData.characterSetId
            }
            var servGroupId = parent[0].rowId
            var _this = this
            commonConfirm(_this, '新增确认', getHintText('新增')).then(() => {
                sendPost("/" + getProjectName() + "/serv_groups/cmha/" + servGroupId + "/db/schemas", function (response) {
                    layer.closeAll('loading')
                    parent[0].DBListApp.returnList()
                    _this.formClose()
                }, function (error) {
                    operationCompletion(_this, error.response.data.msg, 'error')
                }, jsonData)
            })
        },
        formClose: function () {
            parent.layer.close(addIndex);
        }
    }
})