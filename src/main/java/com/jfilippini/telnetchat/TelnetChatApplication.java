package com.jfilippini.telnetchat;

import com.jfilippini.telnetchat.netty.TCPServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class TelnetChatApplication {

	/**
	 * main project entry point
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(TelnetChatApplication.class, args);
	}

	private final TCPServer tcpServer;

	/**
	 * it catches the event when all the context has been initialized,
	 * and starts the TCP server
	 * @return ApplicationListener for the event type
	 */
	@SuppressWarnings({"Convert2Lambda", "java:S1604"})
	@Bean
	public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
		return new ApplicationListener<ApplicationReadyEvent>() {
			@Override
			public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
				log.info("onApplicationEvent {} ", applicationReadyEvent.getSpringApplication());
				tcpServer.start();
			}
		};
	}

}
