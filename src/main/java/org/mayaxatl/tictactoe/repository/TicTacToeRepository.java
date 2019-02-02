package org.mayaxatl.tictactoe.repository;

import org.mayaxatl.tictactoe.event.DrawEvent;
import org.mayaxatl.tictactoe.event.Event;
import org.mayaxatl.tictactoe.event.JoinEvent;
import org.mayaxatl.tictactoe.event.LeaveEvent;
import org.mayaxatl.tictactoe.event.MoveEvent;
import org.mayaxatl.tictactoe.event.RestartEvent;
import org.mayaxatl.tictactoe.event.TurnEvent;
import org.mayaxatl.tictactoe.event.WinEvent;
import org.mayaxatl.tictactoe.model.Board;
import org.mayaxatl.tictactoe.model.Player;
import org.mayaxatl.tictactoe.model.Session;
import org.mayaxatl.tictactoe.model.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.EmitterProcessor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TicTacToeRepository {

  private final List<Player> availablePlayers;
  private final Map<Player, String> players;
  private int turn = 0;
  private final Board board;
  private final EmitterProcessor<Event> eventStream;
  private final Session session;

  @Autowired
  public TicTacToeRepository(EmitterProcessor<Event> eventStream, Session session) {
    availablePlayers = List.of(Player.X, Player.O);
    players = new HashMap<>();
    board = new Board();
    this.eventStream = eventStream;
    this.session = session;
  }

  public void move(int x, int y) {
    var currentPlayer = availablePlayers.get(turn % 2);
    if (session.getPlaysWith() == currentPlayer) {
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

  public State getState() {
    var currentPlayer = availablePlayers.get(turn % 2);
    return new State(players, session.getPlaysWith(), currentPlayer, board.getBoard());
  }

  public boolean isSpotAvailable() {
    return players.size() < 2;
  }

  public void restart() {
    board.reset();
    turn = 0;
    eventStream.onNext(new RestartEvent());
  }

  public void registerPlayer(Session session) {
    if (players.size() < 2) {
      session.setPlaysWith(availablePlayers.get(players.size()));
      players.put(session.getPlaysWith(), session.getUsername());
      eventStream.onNext(new JoinEvent(session.getPlaysWith(), session.getUsername()));
    }
  }

  public void unregisterPlayer(Session session) {
    players.remove(session.getPlaysWith());
    eventStream.onNext(new LeaveEvent(session.getPlaysWith()));
  }

}
