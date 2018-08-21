package spring.boot.amqp.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import spring.boot.amqp.conf.MyConfirmCallback;
import spring.boot.amqp.conf.MyReturnCallback;

@Service
public class SendMessage {
	
	private RabbitTemplate rabbitTemplate;
	
	/**
	 * 在发送消息服务中,对自动配置的RabbitTemplate增加回调设置
	 * @param rabbitTemplate
	 */
	@Autowired
	public SendMessage(RabbitTemplate rabbitTemplate,@Value("${exchange}") String exchange) {
		this.rabbitTemplate = rabbitTemplate;
		rabbitTemplate.setConfirmCallback(new MyConfirmCallback());
		rabbitTemplate.setReturnCallback(new MyReturnCallback());
		// 如果只有一个交换机,可以在模版中唯一指定,这里不能用成员变量的@Value值,因为还没有初始化,改为构造参数
		rabbitTemplate.setExchange(exchange);
				
	}
	
	/**
	 * 发送消息
	 * @param routingKey
	 * @param message
	 * @param dataId
	 */
	public void send(String routingKey,String message,String dataId){
		CorrelationData data = new CorrelationData(dataId);
		
		rabbitTemplate.convertAndSend(rabbitTemplate.getExchange(),routingKey,message,data);
	}
}
