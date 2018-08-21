package spring.boot.amqp.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.stereotype.Component;

/**
 * ConfirmCallback接口用于实现消息发送到RabbitMQ交换机后接收ack回调
 * 只要正确发送到交换机ack就会得到true,其他false
 * @author liqq
 *
 */
@Component
public class MyConfirmCallback implements ConfirmCallback {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		//TODO 对于ack为false的,可以记录CorrelationData.id,后续处理
		log.info("CorrelationData.id:{}", correlationData.getId());
		log.info("ack:{}", ack);
		log.info("cause:{}", cause);
	}
}
