package org.mayaxatl.tictactoe.repository;

import org.mayaxatl.tictactoe.event.Event;
import org.mayaxatl.tictactoe.model.Game;
import org.mayaxatl.tictactoe.model.Session;
import org.mayaxatl.tictactoe.model.State;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TicTacToeRepository {

  private final List<Game> games;

  public TicTacToeRepository() {
    games = new ArrayList<>();
  }

  public void move(Session session, int x, int y) {
    Game game = session.getGame();
    if (game != null) {
      game.move(session.getPlaysWith(), x, y);
    }
  }

  public void registerPlayer(Session session, String username) {
    Game game = findGameToJoin();
    session.setUsername(username);
    session.setGame(game);
    game.addPlayer(session);
  }

  public void unregisterPlayer(Session session) {
    if (session.getGame().removePlayer(session.getPlaysWith())) {
      games.remove(session.getGame());
      session.clear();
    }
  }

  public void restart(Session session) {
    if(session.isPlaying()) {
      session.getGame().restart();
    }
  }

  public State getState(Session session) {
    if(session.isPlaying()) {
      return session.getGame().getState(session.getPlaysWith());
    }
    return null;
  }

  public Flux<Event> getEventStream(Session session) {
    if(session.isPlaying()) {
      return session.getGame().getEventStream();
    }
    return null;
  }

  private Game findGameToJoin() {
    for (Game game : games) {
      if (game.canJoin()) {
        return game;
      }
    }
    Game newGame = new Game();
    games.add(newGame);
    return newGame;
  }
}
