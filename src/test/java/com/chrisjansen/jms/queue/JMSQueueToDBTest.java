package com.chrisjansen.jms.queue;

import com.chrisjansen.entity.MessageTrack;
import com.chrisjansen.repository.MessageTrackRepository;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  This integration:
 *  -----------------
 *  1. Sends a message to a JMS queue.
 *  2. Message is send to a transformer
 *  3. Persists message to a database.
 *
 * @author Chris Jansen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/chrisjansen/jms/queue/jms-adapter-context.xml")
//@ActiveProfiles(profiles = {"DbLocalServer", "MqLocalServer"})
//@ActiveProfiles(profiles = {"DbInMemory", "MqInMemory"})
@ActiveProfiles(profiles = {"DbInMemory", "MqInMemory"})
public class JMSQueueToDBTest implements ApplicationContextAware {

    private final static Logger logger = Logger.getLogger(JMSQueueToDBTest.class.getName());

    private static ApplicationContext applicationContext;

    @Autowired
    private MessageTrackRepository messageTrackRepository;

    @BeforeTransaction
    public void cleanOutExistingData(){
           messageTrackRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback(false)
    public void runBestEfforts1PC()  {

        final MessageChannel stdinToJmsoutChannel = applicationContext.getBean("stdinToJmsoutChannel", MessageChannel.class);

        stdinToJmsoutChannel.send(MessageBuilder.withPayload("Random queue message").build());
    }


    @After
    public void findAllRecordsAfterInsert() throws InterruptedException{
        //sleep for 2 seconds - to let process complete
        logger.debug("Preparing to sleep for 2 seconds...to let the Integration complete!");
        Thread.sleep(2000);

        List<MessageTrack> messageTrackList = messageTrackRepository.findAll();
        Assert.assertEquals(1, messageTrackList.size());
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
