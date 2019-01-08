package org.mayaxatl.reactorDemo.model;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import static org.mayaxatl.reactorDemo.model.Player.EMPTY;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Session {

  private String username = "";
  private Player playsWith = EMPTY;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Player getPlaysWith() {
    return playsWith;
  }

  public void setPlaysWith(Player playsWith) {
    this.playsWith = playsWith;
  }
}