package spring.boot.amqp;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import spring.boot.amqp.service.SendMessage;

@SpringBootApplication
public class Application {

	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		SendMessage bean = context.getBean(SendMessage.class);
		for (int i = 0; i < 10; i++) {
			bean.send("key.hello","hello 中国",UUID.randomUUID().toString());
		}
		try {
			Thread.sleep(1000*100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		context.close();
	}
}
