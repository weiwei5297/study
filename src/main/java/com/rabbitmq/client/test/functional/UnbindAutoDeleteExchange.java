//  The contents of this file are subject to the Mozilla Public License
//  Version 1.1 (the "License"); you may not use this file except in
//  compliance with the License. You may obtain a copy of the License
//  at http://www.mozilla.org/MPL/
//
//  Software distributed under the License is distributed on an "AS IS"
//  basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See
//  the License for the specific language governing rights and
//  limitations under the License.
//
//  The Original Code is RabbitMQ.
//
//  The Initial Developer of the Original Code is GoPivotal, Inc.
//  Copyright (c) 2007-2013 GoPivotal, Inc.  All rights reserved.
//

package com.rabbitmq.client.test.functional;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.test.BrokerTestCase;

import java.io.IOException;

/**
 * Test that unbinding from an auto-delete exchange causes the exchange to go
 * away
 */
public class UnbindAutoDeleteExchange extends BrokerTestCase {
    public void testUnbind() throws IOException, InterruptedException {
        String exchange = "myexchange";
        channel.exchangeDeclare(exchange, "topic", true, false, null);
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, exchange, "test");
        
        QueueingConsumer  consumer = new QueueingConsumer(channel);
		channel.basicConsume(queue, false, consumer);
		
		QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		String message = new String(delivery.getBody(), "utf-8");
		System.out.println("message: " + message);
		
        channel.queueUnbind(queue, exchange, "test");
        
        delivery = consumer.nextDelivery();
		message = new String(delivery.getBody(), "utf-8");
		System.out.println("message2: " + message);

        try {
            channel.exchangeDeclarePassive(exchange);
            fail("exchange should no longer be there");
        }
        catch (IOException e) {
            checkShutdownSignal(AMQP.NOT_FOUND, e);
        }
    }
}
