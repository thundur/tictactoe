package org.mayaxatl.tictactoe.model.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mayaxatl.tictactoe.model.Player;

import java.util.Map;

public class AdminGame {
  private final Map<Player, String> players;
  private final boolean active;
  private final int id;

  public AdminGame(Map<Player, String> players, boolean active, int id) {
    this.players = players;
    this.active = active;
    this.id = id;
  }

  @JsonProperty
  public Map<Player, String> getPlayers() {
    return players;
  }

  @JsonProperty
  public boolean isActive() {
    return active;
  }

  @JsonProperty
  public int getId() {
    return id;
  }
}
