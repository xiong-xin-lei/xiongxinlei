openTerminal();
var initNum = 0
var initSum = 1

/*var timeOutIndex
var timeOutDay = 0
var timeOutHour = 0
var timeOutMinute = 0
var timeOutSecond = 3

let timeout = (timeOutDay * 24 * 60 * 60 * 1000) + (timeOutHour * 60 * 60 * 1000) + (timeOutMinute * 60 * 1000) + (timeOutSecond * 1000)*/

/*if (XEUtils.isInteger(timeOutIndex))
    clearTimeout(timeOutIndex)*/
if (closeTime !== 0)
    timeOutIndex = setTimeout(function () {
        var thisUrl = XEUtils.locat().path
        window.top.indexApp.tabsEdit(thisUrl)
    }, closeTime)

//动态行数：可见高度/xterm-rows高度
function openTerminal() {
    var client = new WSSHClient();

    const width = window.innerWidth - 15;
    const height = window.innerHeight;
    const cols = parseInt(width / 8.5);
    const rows = parseInt(height / 21);

    var term = new Terminal({
        cols: cols,
        rows: rows,
        cursorBlink: true, // 光标闪烁
        cursorStyle: "block", // 光标样式  null | 'block' | 'underline' | 'bar'
        scrollback: 800, //回滚
        tabStopWidth: 10, //制表宽度
        screenKeys: true
    });

    /*term.on('data', function (keyValue, event) {
        client.sendData(keyValue, cols, rows)
    });*/
    term.open(document.getElementById('terminal'), true);
    //在页面上显示连接中...
    term.writeln('连接中...');
    //执行连接操作
    client.connect({
        onError: function (error) {
            //连接失败回调
            term.writeln('Error: ' + error);
        },
        onErrorData: function (error) {
            //数据连接失败回调
            term.writeln(error);
        },
        onConnect: function () {
            //连接成功回调
            //client.sendInitData(options);
            //term.writeln("连接成功");
            client.sendInitData({"Op": "bind", "SessionID": sessionId});
            client.sendInitData({"Op": "resize", "Cols": cols, "Rows": rows});
            term.clear()
        },
        onClose: function (evt) {
            //连接关闭回调
            term.write("连接失败或已关闭");
            console.log("连接失败或已关闭")
        },
        onData: function (data) {
            //收到数据时回调
            if (XEUtils.isArray(data)) {
                dataJson = XEUtils.toStringJSON(XEUtils.unescape(data[0]))
                if (dataJson.Op === "stdout") {
                    term.write(dataJson.Data);
                    if (initNum !== initSum) {
                        client.sendData('unitMGR client log_follow\r', 150, 31);
                        initNum++
                    }
                }
            }
        }
    });
}