package org.mayaxatl.reactorDemo;

import org.mayaxatl.reactorDemo.event.Event;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.EmitterProcessor;

@SpringBootApplication
public class TicTacToeServer {

  public static void main(String[] args) {
    SpringApplication.run(TicTacToeServer.class, args);
  }

  @Bean
  public EmitterProcessor<Event> eventProcessor() {
    return EmitterProcessor.create();
  }
}
