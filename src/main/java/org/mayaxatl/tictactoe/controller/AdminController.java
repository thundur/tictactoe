package org.mayaxatl.tictactoe.controller;

import org.mayaxatl.tictactoe.model.admin.AdminGames;
import org.mayaxatl.tictactoe.repository.TicTacToeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {
  private final TicTacToeRepository ticTacToeRepository;

  @Autowired
  public AdminController(TicTacToeRepository repository) {
    ticTacToeRepository = repository;
  }

  @GetMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public AdminGames getGames() {
    return ticTacToeRepository.getGames();
  }
}
