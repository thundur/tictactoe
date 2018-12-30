package org.mayaxatl.reactorDemo.repository;

import org.mayaxatl.reactorDemo.event.DrawEvent;
import org.mayaxatl.reactorDemo.event.Event;
import org.mayaxatl.reactorDemo.event.MoveEvent;
import org.mayaxatl.reactorDemo.event.RestartEvent;
import org.mayaxatl.reactorDemo.event.WinEvent;
import org.mayaxatl.reactorDemo.model.Board;
import org.mayaxatl.reactorDemo.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.EmitterProcessor;

import java.util.List;

@Repository
public class TicTacToeRepository {

    private final List<Player> players;
    private int turn = 0;
    private final Board board;
    private final EmitterProcessor<Event> eventStream;

    @Autowired
    public TicTacToeRepository (EmitterProcessor<Event> eventStream) {
        players = List.of(Player.X, Player.O);
        board = new Board();
        this.eventStream = eventStream;
    }

    public void move(int x, int y) {
        Player player = players.get(turn % 2);
        board.play(player, x, y);
        eventStream.onNext(new MoveEvent(player, x, y));

        if (board.hasWinner().isPresent()) {
            eventStream.onNext(new WinEvent(board.hasWinner().get()));
        } else if(board.isDraw()) {
            eventStream.onNext(new DrawEvent());
        } else {
            turn++;
        }
    }

    public String getState() {
        return board.getBoard() + "\n\n" + players.get(turn % 2).name();
    }

    public void restart() {
        board.reset();
        turn = 0;
        eventStream.onNext(new RestartEvent());
    }


}
