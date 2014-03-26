package com.chrisjansen.service;

import com.chrisjansen.entity.MessageTrack;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Chris Jansen
 */
public class MessageTrackBuilder {

    public MessageTrack build(String message){
        MessageTrack messageTrack=new MessageTrack();
        messageTrack.setMessage(message);
        return messageTrack;
    }
}
