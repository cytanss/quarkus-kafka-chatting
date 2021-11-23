package org.cytan.undertowwebsockets.Model;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class MessageDeserializer extends ObjectMapperDeserializer<Message>{
    public MessageDeserializer(){
        super(Message.class);
    }
}