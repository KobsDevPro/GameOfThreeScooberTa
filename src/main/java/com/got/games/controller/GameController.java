package com.got.games.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.got.games.model.Player;
import com.got.games.service.GameService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
/**
 * 
 * @author koelbanerjee
 *
 */
@RestController
@Api(tags = {"gameofthree"})
public class GameController {

	@Autowired
	GameService gameService;

	@GetMapping("/start/{isAutoIp}")
	@ApiOperation("Start the game of Three")
	public String startGame(@ApiParam("true (automatic) or false (manual)") @PathVariable boolean isAutoIp,
			 @ApiParam("Number to start in case of manual type") @RequestParam(value = "number") Optional<Integer> numberStart) {
		return gameService.prepareGame(isAutoIp, numberStart.orElse(null));
	}
	
	@PostMapping("/play")
	@ApiOperation("Play the game of Three")
	public  ResponseEntity<String> play(@RequestBody Player player) {
		return gameService.play(player);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation("Health check og the Game")
	@GetMapping("/health")
	public ResponseEntity<String> health() {
		return new ResponseEntity(HttpStatus.OK);
	}

}
