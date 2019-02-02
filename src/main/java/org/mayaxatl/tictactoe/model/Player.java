package org.mayaxatl.tictactoe.model;

public enum Player {
  X, O, EMPTY;

  public String toString() {
    if(this == EMPTY) {
      return "";
    }
    return this.name().toLowerCase();
  }
}
