package org.mayaxatl.tictactoe.model;

import org.mayaxatl.tictactoe.event.DrawEvent;
import org.mayaxatl.tictactoe.event.Event;
import org.mayaxatl.tictactoe.event.JoinEvent;
import org.mayaxatl.tictactoe.event.LeaveEvent;
import org.mayaxatl.tictactoe.event.MoveEvent;
import org.mayaxatl.tictactoe.event.RestartEvent;
import org.mayaxatl.tictactoe.event.TurnEvent;
import org.mayaxatl.tictactoe.event.WinEvent;
import reactor.core.publisher.Sinks;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

  private final List<Player> availablePlayers;
  private final Map<Player, String> players;
  private final Board board;
  private int turn;
  private final Sinks.Many<Event> eventStream;

  private Instant lastActivity;

  public Game() {
    players = new HashMap<>(2);
    board = new Board();
    turn = 0;
    eventStream = Sinks.many().multicast().onBackpressureBuffer();
    availablePlayers = List.of(Player.X, Player.O);
    lastActivity = Instant.now();
  }

  public boolean canJoin() {
    return players.size() < 2;
  }

  public void addPlayer(Session session) {
    if (players.size() < availablePlayers.size()) {
      session.setPlaysWith(availablePlayers.get(players.size()));
      players.put(session.getPlaysWith(), session.getUsername());
      lastActivity = Instant.now();
      eventStream.tryEmitNext(new JoinEvent(session.getPlaysWith(), session.getUsername()));
    }
  }

  public boolean removePlayer(Player player) {
    players.remove(player);
    if (players.size() == 0) {
      eventStream.tryEmitComplete();
      return true;
    } else {
      lastActivity = Instant.now();
      eventStream.tryEmitNext(new LeaveEvent(player));
      return false;
    }
  }

  public void move(Player forPlayer, int x, int y) {
    var currentPlayer = availablePlayers.get(turn % 2);
    if (forPlayer == currentPlayer) {
      board.play(currentPlayer, x, y);
      eventStream.tryEmitNext(new MoveEvent(currentPlayer, x, y));
      lastActivity = Instant.now();

      if (board.getWinner().isPresent()) {
        eventStream.tryEmitNext(new WinEvent(board.getWinner().get()));
      } else if (board.isDraw()) {
        eventStream.tryEmitNext(new DrawEvent());
      } else {
        turn++;
        eventStream.tryEmitNext(new TurnEvent(availablePlayers.get(turn % 2)));
      }
    }
  }

  public void restart() {
    board.clear();
    turn = new Random().nextInt(2);
    eventStream.tryEmitNext(new RestartEvent());
    eventStream.tryEmitNext(new TurnEvent(availablePlayers.get(turn % 2)));
    lastActivity = Instant.now();
  }

  public State getState(Player forPlayer) {
    var currentPlayer = availablePlayers.get(turn % 2);
    lastActivity = Instant.now();
    return new State(players, forPlayer, currentPlayer, board.getBoard());
  }

  public Sinks.Many<Event> getEventStream() {
    return eventStream;
  }

  public boolean isInactive() {
    return lastActivity.plusSeconds(600).isBefore(Instant.now());
  }

  public Map<Player, String> getPlayers() {
    return Map.copyOf(players);
  }
}
