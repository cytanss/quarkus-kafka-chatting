package org.cytan.undertowwebsockets.Service;

import javax.enterprise.context.ApplicationScoped;

import org.cytan.undertowwebsockets.Model.Message;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MsgConsumerService {
    
    private static final Logger LOGGER = Logger.getLogger(MsgConsumerService.class);

    @Incoming("receive-message")
    @Outgoing("message-stream")
    public Message receiveMessage(Message message){
        
        LOGGER.infof("Receiving message %s to Kafka",message.toString());
        return message;
    }
}
