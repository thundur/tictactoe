package org.mayaxatl.tictactoe.event;

import org.mayaxatl.tictactoe.model.Player;

public class TurnEvent extends Event {

  private Player player;

  public TurnEvent(Player player) {
    super("turn");
    this.player = player;
  }

  public String getPlayer() {
    return player.toString();
  }
}
