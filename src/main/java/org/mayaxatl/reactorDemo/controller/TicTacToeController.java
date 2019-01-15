package org.mayaxatl.reactorDemo.controller;

import org.mayaxatl.reactorDemo.event.Event;
import org.mayaxatl.reactorDemo.model.Session;
import org.mayaxatl.reactorDemo.repository.TicTacToeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

@RestController
public class TicTacToeController {

  private final EmitterProcessor<Event> eventStream;
  private final TicTacToeRepository ticTacToeRepository;
  private final Session session;

  @Autowired
  public TicTacToeController(EmitterProcessor<Event> eventStream, TicTacToeRepository repository, Session session) {
    this.eventStream = eventStream;
    this.ticTacToeRepository = repository;
    this.session = session;
  }

  @GetMapping(value = "/logon", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseBody
  public String logon(@RequestParam("username") String username) {
    if (username != null && !username.isEmpty()) {
      if (ticTacToeRepository.isSpotAvailable()) {
        session.setUsername(username);
        ticTacToeRepository.registerPlayer(session);
        return session.getPlaysWith().name().toLowerCase();
      }
    }
    return "";
  }

  @GetMapping(value = "/play/{x}/{y}", produces = MediaType.TEXT_PLAIN_VALUE)
  public void play(@PathVariable int x, @PathVariable int y) {
    ticTacToeRepository.move(x, y);
  }

  @GetMapping(value = "/restart", produces = MediaType.TEXT_PLAIN_VALUE)
  public void restart() {
    ticTacToeRepository.restart();
  }

  @GetMapping(value = "/sync", produces = MediaType.TEXT_PLAIN_VALUE)
  @ResponseBody
  public String sync() {
    return ticTacToeRepository.getState();
  }

  @RequestMapping(value = "/tictactoe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent> tictactoe() {
    return eventStream.map(event -> ServerSentEvent.builder(event).build());
  }

}
