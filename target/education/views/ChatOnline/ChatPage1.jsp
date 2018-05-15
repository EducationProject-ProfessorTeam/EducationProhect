<%--
  Created by IntelliJ IDEA.
  User: Syure
  Date: 2018/5/11
  Time: 14:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Chat Online</title>
    <script src="./js/jquery-3.2.1.min.js"></script>
    <style type="text/css">
        input#chat {
            width: 410px
        }

        #console-container {
            width: 400px;
        }

        #console {
            border: 1px solid #CCCCCC;
            border-right-color: #999999;
            border-bottom-color: #999999;
            height: 170px;
            overflow-y: scroll;
            padding: 5px;
            width: 100%;
        }

        #console p {
            padding: 0;
            margin: 0;
        }</style>
    <script type="application/javascript">
        var Chat = {};
        Chat.socket = null;
        Chat.connect = (function(host) {
            // 创建websocket
            if ('WebSocket' in window) {
                Chat.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                Chat.socket = new MozWebSocket(host);
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            // websocket成功创建时
            Chat.socket.onopen = function () {
                Console.log('Info: WebSocket connection opened.');
                // 绑定事件
                document.getElementById('chatSend').onclick = function() {
                    Chat.sendMessage();
                };
            };

            // websocket关闭连接时
            Chat.socket.onclose = function () {
                // 解除绑定事件
                document.getElementById('chatSend').click(null);
            };

            // websocket发送消息时
            Chat.socket.onmessage = function (message) {
                Console.log(message.data);
            };
        });

        Chat.initialize = function() {
            // 创建 WebSocket连接
            if (window.location.protocol == 'http:') {
                Chat.connect('ws://' + window.location.host + '/education/chatOnline');
            } else {
                Chat.connect('wss://' + window.location.host + '/education/chatOnline');
            }
        };

        // 发送消息函数
        Chat.sendMessage = (function() {
            var friendId = document.getElementById("hidden-friendId").innerText;
            var messageContain = document.getElementById('chat').value;
            var message = messageContain + "-" +friendId;
            if (friendId == ""){
                alert("为选择发送消息的好友");
                return;
            }else {
                if (message != '') {
                    Chat.socket.send(message);
                    document.getElementById('chat').value = '';
                }else {
                    alert("消息不能为空！")
                }
            }
        });

        var Console = {};
        // 消息展示面板添加消息
        Console.log = (function(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            // while (console.childNodes.length > 25) {
            //     console.removeChild(console.firstChild);
            // }
            console.scrollTop = console.scrollHeight;
        });

        //调用初始化函数
        Chat.initialize();
        document.addEventListener("DOMContentLoaded", function() {
            // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
            var noscripts = document.getElementsByClassName("noscript");
            for (var i = 0; i < noscripts.length; i++) {
                noscripts[i].parentNode.removeChild(noscripts[i]);
            }
        }, false);
    </script>
</head>
<body>
<div class="noscript">
    <h2 style="color: #ff0000">Seems your browser doesn't support JavaScript! WebSocket rely on JavaScript being enabled.</h2>
</div>
<%--文本编辑区--%>
<div><p id="hidden-friendId" ><c:out value="4564661" /></p></div>
<div>
    <p>
        <input type="text" placeholder="type and press enter key to chat" id="chat"/>
        <button id="chatSend" value="Send">Send</button>
    </p>
</div>

<%--消息展示区域--%>
<div id="console-container">
    <div id="console"/>
</div>
</body>
</html>
