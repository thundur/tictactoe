package org.mayaxatl.tictactoe.event;

import org.mayaxatl.tictactoe.model.Player;

public class WinEvent extends Event {

  private Player winner;

  public WinEvent(Player winner) {
    super("win");
    this.winner = winner;
  }

  public String getWinner() {
    return winner.toString();
  }
}
