package org.mayaxatl.reactorDemo.event;

import org.mayaxatl.reactorDemo.model.Player;

public class WinEvent extends Event {

  private Player winner;

  public WinEvent(Player winner) {
    super("win");
    this.winner = winner;
  }

  public String getWinner() {
    return winner.name();
  }
}
