package org.mayaxatl.tictactoe.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class State {

  private final Map<Player, String> players;
  private final Player me;
  private final Player turn;
  private final String[][] board;

  public State(Map<Player, String> players, Player me, Player turn, String[][] board) {
    this.players = players;
    this.me = me;
    this.turn = turn;
    this.board = board;
  }

  @JsonProperty
  public Map<String, String> getPlayers() {
    Map<String, String> result = new HashMap<>();
    players.forEach((key, value) -> result.put(key.toString(), value));
    return result;
  }

  @JsonProperty
  public String getMe() {
    return me.toString();
  }

  @JsonProperty
  public String getTurn() {
    return turn.toString();
  }

  @JsonProperty
  public String[][] getBoard() {
    return board;
  }

}
