var msgData = decodeURIComponent(getQueryVariable("msg"), "utf-8")
var msgListApp = new Vue({
    el: '#msgList',
    data: {
        msg: msgData
    },
    created: function () {
        //console.log(msgData)
    },
    methods: {}
})