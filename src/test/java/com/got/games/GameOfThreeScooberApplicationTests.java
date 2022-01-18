package com.got.games;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.got.games.controller.GameController;
import com.got.games.service.GameService;
@RunWith(SpringRunner.class)
@SpringBootTest
class GameOfThreeScooberApplicationTests {

private MockMvc mockMvc;
	
	@Autowired
	private GameController gameController;
	
	@Autowired
	private GameService gameService;
	
	
	
	@Before
    public void setUp() throws Exception {
      mockMvc = MockMvcBuilders.standaloneSetup(gameController)
                .build();
        
	}
	

	@Test
	public void testtStartGameUp() {
		 if (gameService.getSecondPlayer(false)) {
			 try {
					mockMvc.perform(MockMvcRequestBuilders.get("/start/true"))
					        .andExpect(MockMvcResultMatchers.content().string("The game has been started!"))
					        .andExpect(MockMvcResultMatchers.status().isOk());
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
	}
	
	@Test
	public void testStartGameDown() {
		if (!gameService.getSecondPlayer(false)) {
			 try {
					mockMvc.perform(MockMvcRequestBuilders.get("/start/true"))
					        .andExpect(MockMvcResultMatchers.content().string("There is no second player active!")) 
					        .andExpect(MockMvcResultMatchers.status().isOk());
				} catch (Exception e) {
					e.printStackTrace();
				}
		 }
	}
	
	@Test
	public void testPlayGameWithInvalidPlayer() {
		
		 try {
			 //Some invalid Json
			 String json = "{\n" +
		                "  \"title\": \"Greetings\",\n" +
		                "  \"value\": \"Hello World\"\n" +
		                "}";
				mockMvc.perform(MockMvcRequestBuilders.post("/play").contentType(MediaType.APPLICATION_JSON).content(json))
				        .andExpect(MockMvcResultMatchers.status().isBadRequest());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@Test
	public void testPlayGameWithSecondPlayerDown() {
		
		 try {
			 if (!gameService.getSecondPlayer(false)) {
				 
				 String json = "{\n" +
						 "  \"number\": 9\n" +
						 "}";
				 mockMvc.perform(MockMvcRequestBuilders.post("/play").contentType(MediaType.APPLICATION_JSON).content(json))
				 .andExpect(MockMvcResultMatchers.status().isNotFound());
			 }
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
	@Test
	public void testPlayGameWinnerPlayer() {
		
		 try {
			 String json = "{\n" +
		                "  \"number\": 3\n" +
		                "}";
				mockMvc.perform(MockMvcRequestBuilders.post("/play").contentType(MediaType.APPLICATION_JSON).content(json))
				        .andExpect(MockMvcResultMatchers.status().isOk());
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@Test
	public void testHealth() {
		 try {
				mockMvc.perform(MockMvcRequestBuilders.get("/health"))
				        .andExpect(MockMvcResultMatchers.status().isOk()) ;
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
