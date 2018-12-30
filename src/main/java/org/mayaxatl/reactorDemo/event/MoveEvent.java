package org.mayaxatl.reactorDemo.event;

import org.mayaxatl.reactorDemo.model.Player;

public class MoveEvent extends Event {

  private Integer x;
  private Integer y;
  private Player player;

  public MoveEvent(Player player, int x, int y) {
    super("move");
    this.x = x;
    this.y = y;
    this.player = player;
  }

  public Integer getX() {
    return x;
  }

  public Integer getY() {
    return y;
  }

  public String getPlayer() {
    return player.name();
  }
}
