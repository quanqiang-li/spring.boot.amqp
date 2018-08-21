package spring.boot.amqp.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.stereotype.Component;
/**
 * ReturnCallback接口用于实现消息发送到RabbitMQ交换器，但无相应队列与交换器绑定时的回调。
 * 会把消息转发到这里,代理中不保存,因为没有队列绑定,没地方存
 * @author liqq
 *
 */
@Component
public class MyReturnCallback implements ReturnCallback {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		log.info("message:{}", new String(message.getBody()));
		log.info("replyCode:{}", replyCode);
		log.info("replyText:{}", replyText);
		log.info("exchange:{}", exchange);
		log.info("routingKey:{}", routingKey);
	}
}
