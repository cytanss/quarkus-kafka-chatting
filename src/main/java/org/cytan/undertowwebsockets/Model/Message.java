package org.cytan.undertowwebsockets.Model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.jboss.logging.Logger;

public class Message {

    private static final Logger LOGGER = Logger.getLogger(Message.class);

    public String sender;
    public String content;
    public String timestamp;

    @Override
    public String toString(){
        return "Sender: "+sender+", content: "+content+", timestamp:"+timestamp;
    }

    public String getConvertedTime(){

        LOGGER.info("ZoneId.systemDefault(): " + ZoneId.systemDefault());

        LocalDateTime localDate = LocalDateTime.parse(this.timestamp);
        // convert LocalDateTime to ZonedDateTime, with default system zone id
        ZonedDateTime zonedDateTime = localDate.atZone(ZoneId.systemDefault());
        ZonedDateTime klDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Kuala_Lumpur"));
        
        //System.out.println(klDateTime);
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        return klDateTime.format(dtf);

    }
}