package org.mayaxatl.tictactoe.model.admin;

import java.util.ArrayList;
import java.util.List;

public class AdminGames {

  private final List<AdminGame> games;

  public AdminGames() {
    games = new ArrayList<>();
  }

  public void addGame(AdminGame game) {
    games.add(game);
  }

  public List<AdminGame> getGames() {
    return List.copyOf(games);
  }
}
