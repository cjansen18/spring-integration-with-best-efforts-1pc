/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.chrisjansen.jms;

import com.chrisjansen.repository.MessageTrackRepository;
import junit.framework.Assert;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * This integration
 *  -----------------
 * 1. Sends a message to outbound JMS gateway
 * 2. Msg retrieved by inbound JMS gateway
 * 3. Message sent to a transformer, where it's char case gets capitalized.
 * 4. Message is the returned on reply channels to #2 and #1 above.
 *
 * @author Chris Jansen
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/com/chrisjansen/jms/jms-gateway-context.xml")
//@ActiveProfiles(profiles = {"DbLocalServer", "MqLocalServer"})
//@ActiveProfiles(profiles = {"DbInMemory", "MqInMemory"})
@ActiveProfiles(profiles = {"DbInMemory", "MqLocalServer"})
public class JMSGatewayTest implements ApplicationContextAware {

    private final static Logger logger = Logger.getLogger(JMSGatewayTest.class.getName());

    private static ApplicationContext applicationContext;

	@Test
	public void testGatewayDemo() throws InterruptedException {

		final MessageChannel stdinToJmsoutChannel = applicationContext.getBean("stdinToJmsoutChannel", MessageChannel.class);

		stdinToJmsoutChannel.send(MessageBuilder.withPayload("activemq jms test").build());

		final QueueChannel queueChannel = applicationContext.getBean("queueChannel", QueueChannel.class);

        Message<String> reply = (Message<String>) queueChannel.receive(60000);
		Assert.assertNotNull(reply);

		String out = reply.getPayload();
		Assert.assertEquals("JMS response: ACTIVEMQ JMS TEST", out);
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
