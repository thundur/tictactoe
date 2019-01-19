package org.mayaxatl.reactorDemo.event;

import org.mayaxatl.reactorDemo.model.Player;

public class LeaveEvent extends Event {

  private Player player;

  public LeaveEvent(Player player) {
    super("leave");
    this.player = player;
  }

  public String getPlayer() {
    return player.name();
  }
}
