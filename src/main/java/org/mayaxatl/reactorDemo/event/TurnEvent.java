package org.mayaxatl.reactorDemo.event;

import org.mayaxatl.reactorDemo.model.Player;

public class TurnEvent extends Event {

  private Player player;

  public TurnEvent(Player player) {
    super("turn");
    this.player = player;
  }

  public String getPlayer() {
    return player.name();
  }
}
