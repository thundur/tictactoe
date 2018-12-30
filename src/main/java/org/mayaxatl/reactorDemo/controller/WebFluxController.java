package org.mayaxatl.reactorDemo.controller;

import org.mayaxatl.reactorDemo.event.Event;
import org.mayaxatl.reactorDemo.repository.TicTacToeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import javax.servlet.http.HttpSession;

@RestController
public class WebFluxController {

  private final EmitterProcessor<Event> eventStream;
  private final TicTacToeRepository ticTacToeRepository;

  @Autowired
  public WebFluxController(EmitterProcessor<Event> eventStream, TicTacToeRepository repository) {
    this.eventStream = eventStream;
    this.ticTacToeRepository = repository;
  }

  @GetMapping("/logon")
  public String logon(HttpSession session) {
    return "";
  }

  @GetMapping("/play/{x}/{y}")
  public void play(@PathVariable int x, @PathVariable int y, HttpSession session) {
    ticTacToeRepository.move(x, y);
  }

  @GetMapping("/restart")
  public void restart() {
    ticTacToeRepository.restart();
  }

  @GetMapping("/sync")
  @ResponseBody
  public String sync() {
    return ticTacToeRepository.getState();
  }

  @RequestMapping(value = "/tictactoe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent> tictactoe() {
    return eventStream.map(event -> ServerSentEvent.builder(event).build());
  }

}
