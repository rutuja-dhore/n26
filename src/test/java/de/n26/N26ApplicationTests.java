package de.n26;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import de.n26.model.Transaction;
import de.n26.util.StatisticsCache;
import net.minidev.json.JSONValue;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class N26ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Before
	@Test
	public void createTransactionAfter60Seconds() throws Exception {
		Instant currentTime = Instant.now();
		long currentTimeInMilis = currentTime.toEpochMilli();

		createTransaction(new Transaction((double) 10, currentTimeInMilis + 5));
		createTransaction(new Transaction((double) 50, currentTimeInMilis + 3));
		createTransaction(new Transaction((double) 30, currentTimeInMilis + 4));
	}

	@Test
	public void getStatistics() throws Exception {
		mockMvc.perform(get("/statistics")).andExpect(status().isOk()).andExpect(jsonPath("$.sum", is(90.0)))
				.andExpect(jsonPath("$.avg", is(30.0))).andExpect(jsonPath("$.max", is(50.0)))
				.andExpect(jsonPath("$.min", is(10.0))).andExpect(jsonPath("$.count", is(3)));
	}

	@Test
	public void createTransactionBefore60Seconds() throws Exception {
		Instant currentTime = Instant.now();
		long currentTimeInMilis = currentTime.toEpochMilli();

		this.mockMvc
				.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
						.content(JSONValue.toJSONString(new Transaction((double) 100, currentTimeInMilis - 70000))))
				.andExpect(status().is(204));
	}

	private void createTransaction(Transaction transaction) throws Exception {
		this.mockMvc.perform(post("/transactions").contentType(MediaType.APPLICATION_JSON)
				.content(JSONValue.toJSONString(transaction))).andExpect(status().is(201));
	}

	@After
	public void clearCache() {
		StatisticsCache.transactionCache.clear();
	}
}