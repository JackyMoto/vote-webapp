package com.zcp.vote;

import java.util.Arrays;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;

//import com.zcp.vote.controller.VoteController;
import com.zcp.vote.entity.VoteObject;
import com.zcp.vote.service.VoteService;

//import static org.hamcrest.Matchers.*;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebAppConfiguration 
@ContextConfiguration(locations = {"classpath:applicationContext*.xml"})  
@RunWith(SpringJUnit4ClassRunner. class)  
public class VoteControllerTest {
	
//	@Autowired
//	private WebApplicationContext wac;
	
	@Autowired
	public VoteService service;

	private MockMvc mockMvc;
	
	@Before
	public void setup() throws Exception {
//		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		this.mockMvc = MockMvcBuilders.standaloneSetup(service).build();
	}
	
	@Test
	public void getVoteList() throws Exception {
		
		VoteObject v1 = new VoteObject("lol", "lol", "lol.png", "lol.png");
		VoteObject v2 = new VoteObject("dnf", "dnf", "dnf.png", "dnf.png");
		VoteObject v3 = new VoteObject("cod", "cod", "cod.png", "cod.png");
		
		Mockito.when(service.getVoteList()).thenReturn(Arrays.asList(v1, v2, v3));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/vote/pagelist.do"))
		   .andExpect(MockMvcResultMatchers.status().isOk())
	       .andExpect(MockMvcResultMatchers.view().name("/vote/pagelist"))  
	       .andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/views/vote/pagelist.jsp"))
	       .andExpect(MockMvcResultMatchers.model().attribute("list", Matchers.hasSize(3)))
	       .andDo(MockMvcResultHandlers.print())
	       .andReturn();  
		
		Mockito.verify(service, Mockito.times(1)).getVoteList();
		Mockito.verifyNoMoreInteractions(service);
	}
}
