package org.mayaxatl.tictactoe.event;

import org.mayaxatl.tictactoe.model.Player;

public class MoveEvent extends Event {

  private final Integer x;
  private final Integer y;
  private final Player player;

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
    return player.toString();
  }
}
