package org.mayaxatl.reactorDemo.event;

import org.mayaxatl.reactorDemo.model.Player;

public class JoinEvent extends Event {

  private Player player;
  private String name;

  public JoinEvent(Player player, String name) {
    super("join");
    this.player = player;
    this.name = name;
  }

  public String getPlayer() {
    return player.name();
  }

  public String getName() {
    return name;
  }
}
