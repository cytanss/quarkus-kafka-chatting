package org.cytan.undertowwebsockets;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.cytan.undertowwebsockets.Model.Message;
import org.cytan.undertowwebsockets.Service.MsgProducerService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.common.util.URIDecoder;

@ServerEndpoint("/chat/{username}")
@ApplicationScoped
public class SupersonicChatSocket {

    private static final Logger LOG = Logger.getLogger(SupersonicChatSocket.class);

    @Inject
    private MsgProducerService msgProducerService;

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) {
        username = URIDecoder.decodeURIComponent(username);
        LOG.info(">>>>> "+username+" has joined");
        sessions.put(username, session);
        broadcast("User " + username + " joined");
    }

    @OnClose
    public void onClose(Session session, @PathParam("username") String username) {
        username = URIDecoder.decodeURIComponent(username);
        sessions.remove(username);
        LOG.info("User " + username + " left");
        broadcast("User " + username + " left");
    }

    @OnError
    public void onError(Session session, @PathParam("username") String username, Throwable throwable) {
        username = URIDecoder.decodeURIComponent(username);
        sessions.remove(username);
        LOG.error("onError", throwable);
        broadcast("User " + username + " left on error: " + throwable);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("username") String username) {
        username = URIDecoder.decodeURIComponent(username);
        if (message.equalsIgnoreCase("_ready_")) {
            broadcast("User: " + username + " joined");
        } else if (message.equalsIgnoreCase("_allUsers_")) {
            LOG.debug(getOnlineUsers());
            broadcast("_allUsers_"+getOnlineUsers());
        } else {
            msgProducerService.sendMessage(message, username);
            //broadcast(">> " + username + ": " + message);
        }
    }

    private void broadcast(String message) {
        sessions.values().forEach(s -> {
            s.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }

    @Incoming("message-stream")
    public void readMessage(Message message){
        LOG.infof("Broadcasting Message: %s",message.toString());
        broadcast(message.getConvertedTime()+" >> " + message.sender + ": " + message.content);
    }

    private String getOnlineUsers(){
        //String allUsers = "";
        return sessions.keySet().toString();
        //return allUsers;
    }
}