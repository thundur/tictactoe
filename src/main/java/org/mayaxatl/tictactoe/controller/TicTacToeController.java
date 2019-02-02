package org.mayaxatl.tictactoe.controller;

import org.mayaxatl.tictactoe.event.Event;
import org.mayaxatl.tictactoe.model.Session;
import org.mayaxatl.tictactoe.model.State;
import org.mayaxatl.tictactoe.repository.TicTacToeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.Map;

@RestController
public class TicTacToeController {

  private final EmitterProcessor<Event> eventStream;
  private final TicTacToeRepository ticTacToeRepository;
  private final Session session;

  @Autowired
  public TicTacToeController(EmitterProcessor<Event> eventStream, TicTacToeRepository repository, Session session) {
    this.eventStream = eventStream;
    ticTacToeRepository = repository;
    this.session = session;
  }

  @GetMapping(value = "/logon", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public Map<String, String> logon(@RequestParam("username") String username) {
    if (username != null && !username.isEmpty()) {
      if (ticTacToeRepository.isSpotAvailable()) {
        session.setUsername(username);
        ticTacToeRepository.registerPlayer(session);
        return Map.of("symbol", session.getPlaysWith().toString(), "username", username);
      }
    }
    return Collections.emptyMap();
  }

  @GetMapping(value = "/logoff", produces = MediaType.TEXT_PLAIN_VALUE)
  public void logoff() {
    ticTacToeRepository.unregisterPlayer(session);
    session.clear();
  }

  @GetMapping(value = "/play/{x}/{y}", produces = MediaType.TEXT_PLAIN_VALUE)
  public void play(@PathVariable int x, @PathVariable int y) {
    if (session.isPlaying()) {
      ticTacToeRepository.move(x, y);
    }
  }

  @GetMapping(value = "/restart", produces = MediaType.TEXT_PLAIN_VALUE)
  public void restart() {
    if (session.isPlaying()) {
      ticTacToeRepository.restart();
    }
  }

  @GetMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public State sync() {
    return ticTacToeRepository.getState();
  }

  @GetMapping(value = "/tictactoe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<ServerSentEvent> tictactoe() {
    return eventStream.map(event -> ServerSentEvent.builder(event).build());
  }

}
