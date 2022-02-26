package org.mayaxatl.tictactoe.event;

import org.mayaxatl.tictactoe.model.Player;

public class LeaveEvent extends Event {

  private final Player player;

  public LeaveEvent(Player player) {
    super("leave");
    this.player = player;
  }

  public String getPlayer() {
    return player.toString();
  }
}
