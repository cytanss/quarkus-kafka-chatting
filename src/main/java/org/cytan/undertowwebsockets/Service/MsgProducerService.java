package org.cytan.undertowwebsockets.Service;

import org.cytan.undertowwebsockets.Model.Message;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import org.jboss.logging.Logger;

import java.time.LocalDateTime;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MsgProducerService {

    private static final Logger LOGGER = Logger.getLogger(MsgProducerService.class);

    @Channel("send-message")
    Emitter<Message> emitter;

    public void sendMessage(String message, String username){
        Message msg = new Message();
        msg.sender = username;
        msg.content = message;
        msg.timestamp = LocalDateTime.now().toString();

        LOGGER.infof("%s sending message %s to Kafka", username,msg.toString());
        emitter.send(msg);
    }
}
