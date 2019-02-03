package org.mayaxatl.tictactoe.model;

import org.mayaxatl.tictactoe.event.DrawEvent;
import org.mayaxatl.tictactoe.event.Event;
import org.mayaxatl.tictactoe.event.JoinEvent;
import org.mayaxatl.tictactoe.event.LeaveEvent;
import org.mayaxatl.tictactoe.event.MoveEvent;
import org.mayaxatl.tictactoe.event.RestartEvent;
import org.mayaxatl.tictactoe.event.TurnEvent;
import org.mayaxatl.tictactoe.event.WinEvent;
import reactor.core.publisher.EmitterProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game {

  private final List<Player> availablePlayers;
  private Map<Player, String> players;
  private Board board;
  private int turn;
  private EmitterProcessor<Event> eventStream;

  public Game() {
    players = new HashMap<>();
    board = new Board();
    turn = 0;
    eventStream = EmitterProcessor.create();
    availablePlayers = List.of(Player.X, Player.O);
  }

  public boolean canJoin() {
    return players.size() < 2;
  }

  public void addPlayer(Session session) {
    if(players.size() < availablePlayers.size()) {
      session.setPlaysWith(availablePlayers.get(players.size()));
      players.put(session.getPlaysWith(), session.getUsername());
      eventStream.onNext(new JoinEvent(session.getPlaysWith(), session.getUsername()));
    }
  }

  public boolean removePlayer(Player player) {
    players.remove(player);
    if (players.size() == 0) {
      eventStream.onComplete();
      return true;
    } else {
      eventStream.onNext(new LeaveEvent(player));
      return false;
    }
  }

  public void move(Player forPlayer, int x, int y) {
    var currentPlayer = availablePlayers.get(turn % 2);
    if (forPlayer == currentPlayer) {
      board.play(currentPlayer, x, y);
      eventStream.onNext(new MoveEvent(currentPlayer, x, y));

      if (board.hasWinner().isPresent()) {
        eventStream.onNext(new WinEvent(board.hasWinner().get()));
      } else if (board.isDraw()) {
        eventStream.onNext(new DrawEvent());
      } else {
        turn++;
        eventStream.onNext(new TurnEvent(availablePlayers.get(turn % 2)));
      }
    }
  }

  public void restart() {
    board.reset();
    turn = new Random().nextInt(2);
    eventStream.onNext(new RestartEvent());
    eventStream.onNext(new TurnEvent(availablePlayers.get(turn % 2)));
  }

  public State getState(Player forPlayer) {
    var currentPlayer = availablePlayers.get(turn % 2);
    return new State(players, forPlayer, currentPlayer, board.getBoard());
  }

  public EmitterProcessor<Event> getEventStream() {
    return eventStream;
  }
}
