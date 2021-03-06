Spring Integration - using Best Efforts 1PC transaction control
=======================

Sample application demonstrating [Best Efforts 1PC transaction control](http://www.javaworld.com/article/2077963/open-source-tools/distributed-transactions-in-spring--with-and-without-xa.html) project.

JUnits created
=============
* '''JMSGateWayTest''' - demonstrates both inbound and outbound JMS gateway functionality.  This currently works successfully.
* '''JMSQueueToDBTest''' - message sent to a queue, transformed, and persisted to H2 database.
* '''JMSQueueToDBWithErrorTest''' - message sent to a queue, an exception will be thrown during database persistence, causing the message to be rolled back to the JMS queue.
** This test will be used to demonstrate Best Efforts 1PC transaction control.

Technologies used
=======
* Spring Integration
* Spring Data - used for CRUD operations
* H2 database - in memory and server versions, controlled by Spring Profile
* ActiveMQ queue - in memory and server versions, controlled by Spring Profile

To Do's
=======
