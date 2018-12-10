package spring.boot.amqp.conf;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 使用默认的配置即可,除非有特殊需求,不要去自定义
 * @author liqq
 *
 */
@Configuration
public class RabbitmqConfiguration {

	@Value("${exchange}")
	private String exchange;
	@Value("${queue}")
	private String queue;

	/**
	 * 如果需要自定义
	 * @return
	 */
//	@Bean
//	@ConfigurationProperties("spring.rabbitmq")
	public ConnectionFactory connectionFactory() {
		return new CachingConnectionFactory();
	}

	/**
	 * 发送消息模版依赖ConnectionFactory
	 * 
	 * @param confirmCallback
	 * @param returnCallback
	 * @return
	 */
	//@Bean
	public RabbitTemplate createRabbitTemplate(MyConfirmCallback confirmCallback, MyReturnCallback returnCallback) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		// 如果只有一个交换机,可以在模版中唯一指定
		rabbitTemplate.setExchange(exchange);
		return rabbitTemplate;
	}

	/**
	 * 接收消息监听容器
	 * 
	 * @return
	 */
	//@Bean
	public SimpleRabbitListenerContainerFactory myMsgContainer(SimpleRabbitListenerContainerFactoryConfigurer configurer) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConcurrentConsumers(2);
		configurer.configure(factory, connectionFactory());
		return factory;
	}

	@Bean
	public TopicExchange createTopicExchange() {
		// durable 交换机重启仍会存在;autoDelete 不使用时删除交换机
		TopicExchange topicExchange = new TopicExchange(exchange, true, true);
		return topicExchange;
	}

	@Bean
	public Queue createQueue() {
		return new Queue(queue, true, false, true);
	}

	@Bean
	public Binding createBinding() {
		// 路由键是通配的key.开头;如key.hello,key.good 都是此队列绑定到交换机的路由键
		// 如果只想把队列绑定到交换机的固定路由键,则把#替换为固定值即可
		return BindingBuilder.bind(createQueue()).to(createTopicExchange()).with("key.#");
	}
}
