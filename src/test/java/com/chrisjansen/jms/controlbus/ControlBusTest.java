package com.chrisjansen.jms.controlbus;

/**
 * @author: Christopher M. Jansen
 * @since: 3/28/14 12:25 PM
 */

import com.chrisjansen.entity.MessageTrack;
import com.chrisjansen.repository.MessageTrackRepository;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.message.GenericMessage;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.log4j.Logger;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/chrisjansen/jms/controlbus/control-bus-jms-context.xml")
@ActiveProfiles(profiles = {"DbInMemory", "MqLocalServer"})
public class ControlBusTest implements ApplicationContextAware {

    private static Logger logger = Logger.getLogger(ControlBusTest.class);
    private static ApplicationContext applicationContext;

    private int globalMessageCount=0;
    private int messageCountPerRun=4;

    @Autowired
    private MessageTrackRepository messageTrackRepository;

    @Before
    public void cleanOutExistingData(){
        logger.info("Cleaning all messages from messageRepository!");
        messageTrackRepository.deleteAll();
    }

    @Test
    public void controlBusTest(){
        List<MessageTrack> messageTrackList =null;


         logger.info("Load message: run #1!");
         loadMessages();

        //1. validate that we have messages stored..
        messageTrackList= messageTrackRepository.findAll();
        Assert.assertEquals(messageCountPerRun, messageTrackList.size());

        //2. kill the input channel using control bus
        MessageChannel controlChannel = applicationContext.getBean("controlChannel", MessageChannel.class);
        controlChannel.send(new GenericMessage<String>("@jmsIn.stop()"));

        //3. send in more messages
        logger.info("Load message: run #2!");
        loadMessages();

        //4. validate messages havent been processed (ie - count in dtabase should be the same)
        //TODO: Find out how to verify current count in queue!
        messageTrackList= messageTrackRepository.findAll();
        Assert.assertEquals(messageCountPerRun, messageTrackList.size());

        //5. start channel back up
        controlChannel.send(new GenericMessage<String>("@jmsIn.start()"));
        /** Observation -
         *  At this point, S.I. will **not** process the queued items previously inserted from loadMessages() run #2.
         *  From this point on, only introducing **new** messages to the queue will cause the processing of loadMessages() run #2.
         */

        //..this is message load #3
        logger.info("Load message: run #3!");
        loadMessages();

        //6. verify database record count has increased
        messageTrackList= messageTrackRepository.findAll();
        Assert.assertEquals((messageCountPerRun*3), messageTrackList.size());
    }

    private void loadMessages()  {
        final MessageChannel stdinToJmsoutChannel = applicationContext.getBean("stdinToJmsoutChannel", MessageChannel.class);

        for (int messageCount=0;messageCount<messageCountPerRun;messageCount++){
            globalMessageCount++;
            stdinToJmsoutChannel.send(MessageBuilder.withPayload("Random queue message Number:+"+globalMessageCount).build());
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            logger.error("Exception during processing sleep",e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext appContext)
            throws BeansException {
        applicationContext = appContext;

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
