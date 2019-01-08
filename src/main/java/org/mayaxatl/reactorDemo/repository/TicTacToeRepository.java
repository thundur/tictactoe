package org.mayaxatl.reactorDemo.repository;

import org.mayaxatl.reactorDemo.event.DrawEvent;
import org.mayaxatl.reactorDemo.event.Event;
import org.mayaxatl.reactorDemo.event.JoinEvent;
import org.mayaxatl.reactorDemo.event.MoveEvent;
import org.mayaxatl.reactorDemo.event.RestartEvent;
import org.mayaxatl.reactorDemo.event.TurnEvent;
import org.mayaxatl.reactorDemo.event.WinEvent;
import org.mayaxatl.reactorDemo.model.Board;
import org.mayaxatl.reactorDemo.model.Player;
import org.mayaxatl.reactorDemo.model.Session;
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

    @Autowired
    public TicTacToeRepository (EmitterProcessor<Event> eventStream) {
        availablePlayers = List.of(Player.X, Player.O);
        players = new HashMap<>();
        board = new Board();
        this.eventStream = eventStream;
    }

    public void move(int x, int y) {
        Player player = availablePlayers.get(turn % 2);
        board.play(player, x, y);
        eventStream.onNext(new MoveEvent(player, x, y));

        if (board.hasWinner().isPresent()) {
            eventStream.onNext(new WinEvent(board.hasWinner().get()));
        } else if(board.isDraw()) {
            eventStream.onNext(new DrawEvent());
        } else {
            turn++;
            eventStream.onNext(new TurnEvent(availablePlayers.get(turn % 2)));
        }
    }

    public String getState() {
        var currentPlayer = availablePlayers.get(turn % 2);
        return board.getBoard() + currentPlayer.name();
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
        if(players.size() < 2) {
          session.setPlaysWith(availablePlayers.get(players.size()));
          players.put(session.getPlaysWith(), session.getUsername());
          eventStream.onNext(new JoinEvent(session.getPlaysWith(), session.getUsername()));
        }
    }

}
