package org.mayaxatl.tictactoe.event;

import org.mayaxatl.tictactoe.model.Player;

public class JoinEvent extends Event {

  private Player player;
  private String name;

  public JoinEvent(Player player, String name) {
    super("join");
    this.player = player;
    this.name = name;
  }

  public String getPlayer() {
    return player.toString();
  }

  public String getName() {
    return name;
  }
}
