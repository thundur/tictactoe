package org.mayaxatl.tictactoe.event;

public abstract class Event {

  private final String type;

  protected Event(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}
