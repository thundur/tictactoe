package org.mayaxatl.tictactoe.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class Board {

  private final List<List<Player>> board;
  private final int gridSize = 3;

  public Board() {
    board = new ArrayList<>();
    for (var i = 0; i < gridSize; i++) {
      var row = new ArrayList<Player>();
      for (var j = 0; j < gridSize; j++) {
        row.add(Player.EMPTY);
      }
      board.add(row);
    }
  }

  public void clear() {
    for (var i = 0; i < gridSize; i++) {
      for (var j = 0; j < gridSize; j++) {
        board.get(i).set(j, Player.EMPTY);
      }
    }
  }

  public void play(Player player, int x, int y) {
    board.get(x).set(y, player);
  }

  public Optional<Player> getWinner() {
    Optional<Player> winner = checkDiagonals();
    if (winner.isPresent()) {
      return winner;
    }

    winner = checkColumns();
    if (winner.isPresent()) {
      return winner;
    }
    return checkRows();
  }

  public boolean isDraw() {
    return isFull() && getWinner().isEmpty();
  }

  private boolean isFull() {
    for (var i = 0; i < gridSize; i++) {
      for (var j = 0; j < gridSize; j++) {
        if (board.get(i).get(j) == Player.EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  private Optional<Player> checkRows() {
    return board.stream().filter(this::allEqual).filter(row -> row.get(0) != Player.EMPTY).flatMap(Collection::stream).findFirst();
  }

  private Optional<Player> checkColumns() {
    List<Player> column;
    for (var col = 0; col < gridSize; col++) {
      column = List.of(board.get(0).get(col), board.get(1).get(col), board.get(2).get(col));
      Player first = column.get(0);
      if (first != Player.EMPTY && allEqual(column)) {
        return Optional.of(first);
      }
    }
    return Optional.empty();
  }

  private Optional<Player> checkDiagonals() {
    List<Player> diagonal = new ArrayList<>();
    for (var i = 0; i < gridSize; i++) {
      diagonal.add(board.get(i).get(i));
    }
    Player first = diagonal.get(0);
    if (first != Player.EMPTY && allEqual(diagonal)) {
      return Optional.of(first);
    }

    diagonal.clear();
    for (var i = 0; i < gridSize; i++) {
      diagonal.add(board.get(i).get((gridSize - 1) - i));
    }
    first = diagonal.get(0);
    if (first != Player.EMPTY && allEqual(diagonal)) {
      return Optional.of(first);
    }
    return Optional.empty();
  }

  private boolean allEqual(List<Player> row) {
    var base = row.get(0);
    return row.stream().noneMatch(player -> player != base);
  }

  public String[][] getBoard() {
    String[][] result = new String[3][3];
    for (var i = 0; i < gridSize; i++) {
      for (var j = 0; j < gridSize; j++) {
        result[i][j] = board.get(i).get(j).toString();
      }
    }
    return result;
  }

}
