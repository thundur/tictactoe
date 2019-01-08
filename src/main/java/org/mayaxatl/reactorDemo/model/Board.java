package org.mayaxatl.reactorDemo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Board {

  private List<List<Player>> board;
  private final int gridSize = 3;

  public Board() {
    board = new ArrayList<>();
    for (var i = 0; i < gridSize; i++) {
      var row = new ArrayList<Player>();
      for(var j = 0; j < gridSize; j++) {
        row.add(Player.EMPTY);
      }
      board.add(row);
    }
  }

  public void reset() {
    for (var i = 0; i < gridSize; i++) {
      for (var j = 0; j < gridSize; j++) {
        board.get(i).set(j, Player.EMPTY);
      }
    }
  }

  public void play(Player player, int x, int y) {
    board.get(x).set(y, player);
  }

  public Optional<Player> hasWinner() {
    Optional<Player> winner = checkDiagonals();
    if(winner.isPresent()) {
      return winner;
    }

   winner = checkColumns();
    if(winner.isPresent()) {
      return winner;
    }
    return checkRows();
  }

  public boolean isDraw() {
    return isFull() && hasWinner().isPresent();
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
    for(List<Player> row : board) {
      Player first = row.get(0);
      if(first != Player.EMPTY && allEqual(row)) {
        return Optional.of(first);
      }
    }
    return Optional.empty();
  }

  private Optional<Player> checkColumns() {
    List<Player> column;
    for(var col = 0; col < gridSize; col++) {
      column = List.of(board.get(0).get(col), board.get(1).get(col), board.get(2).get(col));
      Player first = column.get(0);
      if(first != Player.EMPTY && allEqual(column)) {
        return Optional.of(first);
      }
    }
    return Optional.empty();
  }

  private Optional<Player> checkDiagonals() {
    List<Player> diagonal = new ArrayList<>();
    for(var i = 0; i < gridSize; i++) {
      diagonal.add(board.get(i).get(i));
    }
    Player first = diagonal.get(0);
    if(first != Player.EMPTY && allEqual(diagonal)) {
      return Optional.of(first);
    }

    diagonal.clear();
    for(var i = 0; i < gridSize; i++) {
      diagonal.add(board.get(i).get((gridSize - 1) - i));
    }
    first = diagonal.get(0);
    if(first != Player.EMPTY && allEqual(diagonal)) {
      return Optional.of(first);
    }
    return Optional.empty();
  }

  private boolean allEqual(List<Player> row) {
    var base = row.get(0);
    for(var player : row) {
      if(player != base) {
        return false;
      }
    }
    return true;
  }

  public String getBoard() {
    StringBuilder result = new StringBuilder();
    for (var i = 0; i < gridSize; i++) {
      for (var j = 0; j < gridSize; j++) {
        result.append(board.get(i).get(j).name());
        result.append(" ");
      }
      result.append("\n");
    }
    return result.toString();
  }

}
