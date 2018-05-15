package com.controller;

import com.domain.User;
import com.utils.GetHttpSessionConfigurator;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ServerEndpoint(value = "/chatOnline", configurator = GetHttpSessionConfigurator.class)
public class ChatOnline {
//    在线成员
    private static final Map<String, ChatOnline> connection = new HashMap<>();
    private String userId;
    private String username;
    private Session session;

//    构造方法
    public ChatOnline() { }

    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
//        获得会话
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user = (User) httpSession.getAttribute("user");
        String userId = user.getUserId();
        this.username = user.getUsername();
        this.userId = userId;
        this.session = session;
//        判断链接是否在链接队列中，存在则不添加
        if (!connection.containsKey(userId)){
            connection.put(userId, this);
            String selfConnectMessage = "** 你已经链接 **";
//            消息方法进行发送
            sendToSomeOne(selfConnectMessage, this.userId);
        }
    }

    @OnClose
    public void onClose(){
//        将链接移除
        connection.remove(this.userId);
    }

    @OnMessage
    public void onMessage(String message){
        String[] messageArray = message.split("-");
//        获得接收方ID
        String revUserId = messageArray[1];
//        判断好友是否在线，不在线则提示好友已经掉线
        if (connection.containsKey(revUserId)) {
//            将消息发方的用户名记录到消息中
            String preparedMessage = String.format("%s: %s", this.username, messageArray[0]);
//            发送消息给双方
            sendToEachOther(preparedMessage, revUserId, this.userId);
        } else {
            ChatOnline revClient = connection.get(revUserId);
            String revDisconnectMessage = String.format("** %s %s **", revUserId, "已经掉线");
//            将掉线消息发送给自己
            sendToSomeOne(revDisconnectMessage, this.userId);
        }
    }

    @OnError
    public void onError(Throwable throwable) throws Throwable{
        System.err.println("Chat Error: Something occur with " + throwable.toString());
        throwable.printStackTrace();
    }

//    发送给指定好友
    public void sendToSomeOne(String msg, String revUserId){
        ChatOnline revClient = connection.get(revUserId);
        try {
            synchronized (revClient){
                revClient.session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
            System.err.println("Chat Error: Failed to send message to " + revClient.username);
            e.printStackTrace();
            try {
                revClient.session.close();
            }catch (IOException e1){
                System.err.println("Chat Error: Failed to close session " + revClient.username);
            }
        }
    }

//    发送消息给双方
    public void sendToEachOther(String msg, String sendUserId, String revUserId){
        ChatOnline sendClient = connection.get(sendUserId);
        ChatOnline revClient = connection.get(revUserId);
        try{
            synchronized (sendClient){
                sendClient.session.getBasicRemote().sendText(msg);
            }
//            在确认发送无误之后，将消息入库
            System.out.println(msg + " from " + sendUserId + " to " + revUserId);
            /*
            * 入库代码
            */
        } catch (IOException e) {
            System.err.println("Chat Error: Failed to send message to " + sendClient.username);
            e.printStackTrace();
            try {
                sendClient.session.close();
            } catch (IOException e1) {
                System.err.println("Chat Error: Failed to close session of " + sendClient.username);
                e1.printStackTrace();
            }
        }
        try {
            synchronized (revClient){
                revClient.session.getBasicRemote().sendText(msg);
            }
        } catch (IOException e) {
            System.err.println("Chat Error: Failed to send message to " + revClient.username);
            e.printStackTrace();
            try {
                sendClient.session.close();
            } catch (IOException e1) {
                System.err.println("Chat Error: Failed to close session of " + revClient.username);
            }
        }
    }
}
