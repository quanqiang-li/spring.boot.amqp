package spring.boot.amqp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

/**
 * 接收消息
 * 
 * @author liqq
 *
 */
@Component
public class ReceiveMessage {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@RabbitListener(queues = "${queue}")
	//@RabbitListener(queues = "${queue}",containerFactory="myMsgContainer")//如果自定义了监控消息容器
	public void listenerManualAck(Message message, Channel channel) {
		//可以看出并发的消费者
		log.info(Thread.currentThread().getName() + new String(message.getBody()));
		try {
			//设置了spring.rabbitmq.listener.simple.acknowledge-mode=manual
			//即开启了手动确认模式,替代自动确认(失败会压回队列),那么下面这句必须有,否则消费者会阻塞等待确认,造成队列消息堆积
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			Thread.sleep(2000);
		} catch (Exception e) {
			// TODO 如果报错了,那么我们可以进行容错处理,比如转移当前消息进入其它队列
			e.printStackTrace();
		}
	}
}
