package com.got.games.service;

import java.net.URLEncoder;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.got.games.model.Player;

@Service
public class GameService {
	
private final RestTemplate restTemplate;
	
//	@Value("${random.max.number}")
	private Integer limitRangeNumber=100000;
	
	@Value("${second.player.address}")
	private String uriSecondPlayer;
	
	@Value("${play.game.address}")
	private String uriPlayGame;
	
	Logger logger = LoggerFactory.getLogger(GameService.class);
	
	@Autowired
	ConfigurableApplicationContext context;
	
	public GameService(RestTemplateBuilder restTemplateBuilder) {
		restTemplate = restTemplateBuilder.build();
	}

	public String prepareGame(boolean automaticoInput, Integer numberStart) {
		if (getSecondPlayer(true)) {
			Player player = new Player();
			if (automaticoInput) {
				player.setNumber(getRandomNumber());
			} else {
				player.setNumber(numberStart);
			}
			
			JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
			jmsTemplate.convertAndSend("start", player);
			
			logger.info("Game started!");
			return "Game started!";
		} else {
			
			return "Second Player inactive!";
		}
		
	}
	
	@JmsListener(destination = "start")
	public void startGame(Player player) {
		logger.info("Sending number: " + player.getNumber());
		restTemplate.postForObject(uriPlayGame, player, Player.class);
	}
	
	public boolean getSecondPlayer(boolean logInfo)  {
		if (logInfo) {
			logger.info("Getting second player...");
		}
		try {
			restTemplate.getForObject(uriSecondPlayer, String.class);
		} catch (Exception ex) {
			logger.error("The game has been ended because of no active player");
			return false;
		}
		if (logInfo) {
			logger.info("Second player is active!");
		}
		
		return true;
	}
	
	private int getRandomNumber() {
		
		Random r = new Random();
		return r.nextInt(limitRangeNumber);
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<String> play(Player player) {
		
		if (validatePlayer(player)) {
			int currentNumber = player.getNumber();
			
			logger.info("Received number: " + currentNumber);
		
			if (currentNumber <= 0) {
				logger.error("Error, something went wrong.");
			} else {
				//Main logic to calculate the number and setting the value in player
				String next = Long.toString(Math.round(((double) currentNumber) / 3));
				player.setNumber(Integer.parseInt(next));
				
				if (player.getNumber() == 1) {
					logger.info("Sending number: " + player.getNumber());
					logger.info("You are the winner!!");
				} else {
					
					logger.info("Sending number: " + player.getNumber());
					
					if (getSecondPlayer(false)) {
						restTemplate.postForObject(uriPlayGame, player, Player.class);
					} else {
						logger.error("The game has been ended because second player is inactive!!");
						return new ResponseEntity(HttpStatus.NOT_FOUND);
					}
					
				}
				
			}
		
		
		} else {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
			
		return new ResponseEntity(HttpStatus.OK);
		
	}
	
	private boolean validatePlayer(Player player) {
		if (player == null || player.getNumber() == null) {
			logger.error("The game has been ended because player information is unavailable or wrong!!");
			return false;
		} 
		return true;
	}
}
