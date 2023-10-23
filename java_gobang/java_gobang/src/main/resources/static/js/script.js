// 存储游戏信息的对象，由玩家都进入房间准备就绪后返回的响应赋值
let gameInfo = {
    roomId: null, // 该玩家游戏房间id
    thisUserId: null, // 该玩家自己id
    thatUserId: null, // 对手玩家id
    isWhite: true, // 是否为自己先手，true就是自己先手，false就不是自己先手
}

// websocket 连接
var webSocketURL = "ws://" + location.host + "/game";
var webSocket = new WebSocket(webSocketURL);

// 连接成功执行的方法
webSocket.onopen = function () {
    console.log("游戏连接成功");
}

// 连接发生错误执行的方法
webSocket.onerror = function () {
    console.log("连接发生错误");
}

// 连接关闭执行的方法
webSocket.onclose = function () {
    console.log("连接关闭");
}

// 浏览器窗口关闭来主动关闭WebSocket连接
window.onbeforeunload = function () {
    webSocket.close();
}

// WebSocket连接收到响应后执行的方法，event为收到的响应，需要将负载转换成js对象
webSocket.onmessage = function (event) {
    var result = JSON.parse(event.data);
    // 连接失败
    if (!result.ok) {
        console.log("连接失败，请重试！原因：" + result.reason);
        return;
    }
    // 判断响应特征
    if (result.message === "gameReady") {
        // 初始化信息
        gameInfo.roomId = result.roomId;
        gameInfo.thisUserId = result.thisUserId;
        gameInfo.thatUserId = result.thatUserId;
        // 如果返回的先手玩家id是自己，就赋值true，反之赋值false
        gameInfo.isWhite = (result.firstUserId === result.thisUserId);
        // 初始化棋盘
        initGame();
        // 显示提示栏信息
        setScreenText(gameInfo.isWhite);
    } else if (result.message === "repeatConnection") {
        // 说明玩家多开，加载到登录页面
        alert(result.reason);
        location.replace("/login.html");
    }

}


// 设定界面显示相关操作，来修改显示栏提示
function setScreenText(me) {
    let screen = document.querySelector('#screen');
    if (me) {
        screen.innerHTML = "轮到你落子了!";
    } else {
        screen.innerHTML = "轮到对方落子了!";
    }
}

// 初始化一局游戏
function initGame() {
    // 是我下还是对方下. 根据服务器分配的先后手情况决定
    let me = gameInfo.isWhite;
    // 游戏是否结束
    let over = false;
    let chessBoard = [];
    //初始化chessBord数组(表示棋盘的数组)，如果为0表示当前位置可以落子，为1表示当前位置已经有棋子了
    for (let i = 0; i < 15; i++) {
        chessBoard[i] = [];
        for (let j = 0; j < 15; j++) {
            chessBoard[i][j] = 0;
        }
    }
    // 使用chess标签来绘制棋盘
    let chess = document.querySelector('#chess');
    let context = chess.getContext('2d');
    context.strokeStyle = "#090909";
    // 背景图片
    let logo = new Image();
    logo.src = "img/logo.png";
    logo.onload = function () {
        context.drawImage(logo, 0, 0, 450, 450);
        initChessBoard();
    }

    // 绘制棋盘网格
    function initChessBoard() {
        for (let i = 0; i < 15; i++) {
            context.moveTo(15 + i * 30, 15);
            context.lineTo(15 + i * 30, 430);
            context.stroke();
            context.moveTo(15, 15 + i * 30);
            context.lineTo(435, 15 + i * 30);
            context.stroke();
        }
    }

    // 绘制一个棋子
    function oneStep(i, j, isWhite) {
        context.beginPath();
        context.arc(15 + i * 30, 15 + j * 30, 13, 0, 2 * Math.PI);
        context.closePath();
        var gradient = context.createRadialGradient(15 + i * 30 + 2, 15 + j * 30 - 2, 13, 15 + i * 30 + 2, 15 + j * 30 - 2, 0);
        if (!isWhite) {
            gradient.addColorStop(0, "#0A0A0A");
            gradient.addColorStop(1, "#636766");
        } else {
            gradient.addColorStop(0, "#D1D1D1");
            gradient.addColorStop(1, "#F9F9F9");
        }
        context.fillStyle = gradient;
        context.fill();
    }

    // 在画布上点击执行的方法，需要发送落子请求
    chess.onclick = function (e) {
        if (over) {
            return;
        }
        if (!me) {
            return;
        }
        let x = e.offsetX;
        let y = e.offsetY;
        // 注意, 横坐标是列, 纵坐标是行
        let col = Math.floor(x / 30);
        let row = Math.floor(y / 30);
        if (chessBoard[row][col] === 0) {
            // 发送坐标给服务器, 服务器要返回结果
            send(row, col);
        }
    }

    // 发送落子请求
    function send(row, col) {
        var request = {
            message: "putChess",
            userId: gameInfo.thisUserId,
            row: row,
            col: col
        };

        webSocket.send(JSON.stringify(request));
    }
    // 上面的onmessage方法是玩家准备好时需要初始化页面执行的方法，现在需要修改该方法来处理落子响应
    webSocket.onmessage = function (e) {
        // 处理成js对象
        var result = JSON.parse(e.data);

        if (result.message === "No Message") {
            // 落子请求失败，提醒重试
            alert("落子失败，请重试!");
            return;
        }
        if (result.message !== "putChess") {
            console.log("响应类型错误");
            return;
        }
        // 如果落子响应显示落子玩家是自己就绘制自己颜色棋子，不是就绘制对手颜色棋子
        if (result.userId === gameInfo.thisUserId) {
            // 自己下子绘制棋子
            oneStep(result.col, result.row, gameInfo.isWhite);
        } else if (result.userId === gameInfo.thatUserId) {
            // 对面下子绘制棋子
            oneStep(result.col, result.row, !gameInfo.isWhite);
        } else {
            // 发生错误
            console.log("发生错误，绘制棋子错误");
            return;
        }
        // 在前端数组上标记该地方已经落子
        chessBoard[result.row][result.col] = 1;
        // 交换双方落子，并修改提示框信息
        me = !me;
        setScreenText(me);

        // 判定游戏是否结束
        let screenDiv = document.querySelector('#screen');
        if (result.winUserId !== 0) {
            // 如果胜利玩家id是自己就提示栏显示你赢了，反之显示你输了
            if (result.winUserId === gameInfo.thisUserId) {
                screenDiv.innerHTML = '你赢了!';
            } else if (result.winUserId === gameInfo.thatUserId) {
                screenDiv.innerHTML = '你输了!';
            } else {
                alert("winner 字段错误! " + result.winUserId);
            }

            // 增加一个按钮, 让玩家点击之后, 再回到游戏大厅~
            let backBtn = document.createElement('button');
            backBtn.innerHTML = '回到大厅';
            backBtn.onclick = function () {
                location.replace('/game_hall.html');
            }
            let fatherDiv = document.querySelector('.container>div');
            fatherDiv.appendChild(backBtn);
        }
    }
}