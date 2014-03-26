package com.chrisjansen.jms;

import com.chrisjansen.entity.MessageTrack;
import com.chrisjansen.repository.MessageTrackRepository;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Chris Jansen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/chrisjansen/jms/jms-adapter-context.xml")
@ActiveProfiles(profiles = "H2Server")
@TransactionConfiguration(defaultRollback = false)
@Transactional
public class JMSQueueToDBTest implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Autowired
    private MessageTrackRepository messageTrackRepository;

    @Test
    public void runBestEfforts1PC(){

        final MessageChannel stdinToJmsoutChannel = applicationContext.getBean("stdinToJmsoutChannel", MessageChannel.class);

        stdinToJmsoutChannel.send(MessageBuilder.withPayload("Random queue message").build());
    }

    @After
    public void findAllRecords(){
        List<MessageTrack> messageTrackList = messageTrackRepository.findAll();
        Assert.assertNotNull(messageTrackList);
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
