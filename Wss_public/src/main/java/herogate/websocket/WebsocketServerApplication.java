package herogate.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WebsocketServerApplication extends SpringBootServletInitializer // tomcat 에서 실행하기 위해서 상속받아야됨
{
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WebsocketServerApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(WebsocketServerApplication.class, args);
	}

}
