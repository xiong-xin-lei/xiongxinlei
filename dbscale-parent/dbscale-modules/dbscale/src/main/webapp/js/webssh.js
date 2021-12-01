function WSSHClient() {
};

WSSHClient.prototype._generateEndpoint = function () {
    /*if (window.location.protocol == 'https:') {
        var protocol = 'wss://';
    } else {
        var protocol = 'ws://';
    }
    var endpoint = protocol + sshUrl;*/
    let protocol = "ws://"
    if (XEUtils.trim(scheme.toLowerCase()) === "https")
        protocol = "wss://"
    return protocol + sshUrl;
};

WSSHClient.prototype.connect = function (options) {
    var endpoint = this._generateEndpoint();

    if (window.WebSocket) {
        //如果支持websocket
        this._connection = new WebSocket(endpoint);
    } else {
        //否则报错
        options.onError('WebSocket Not Supported');
        return;
    }

    this._connection.onopen = function () {
        options.onConnect();
    };

    this._connection.onmessage = function (evt) {
        var data = XEUtils.toStringJSON(evt.data.toString().slice(1));
        //data = base64.decode(data);
        if (evt.data.toString().slice(0, 1) === "c") {
            if (data[1] !== "Process exited")
                options.onErrorData(data[1]);
        } else {
            options.onData(data);
        }
    };


    this._connection.onclose = function (evt) {
        options.onClose();
    };
};

WSSHClient.prototype.send = function (data) {
    this._connection.send(JSON.stringify(data));
};

WSSHClient.prototype.sendData = function (keyValue, col, row) {
    //连接参数
    var data = [JSON.stringify({"Op": "stdin", "Data": keyValue, "Cols": col, "Rows": row})]
    this._connection.send(JSON.stringify(data));
}

WSSHClient.prototype.sendInitData = function (data) {
    //连接参数
    data = [JSON.stringify(data)]
    this._connection.send(JSON.stringify(data));
}

WSSHClient.prototype.sendClientData = function (data) {
    //发送指令
    this._connection.send(JSON.stringify({"operate": "command", "command": data}))
}

WSSHClient.prototype.close = function (data) {
    this._connection.close();
};

var client = new WSSHClient();
