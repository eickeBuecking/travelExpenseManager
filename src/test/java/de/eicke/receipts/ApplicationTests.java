package de.eicke.receipts;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Date;
import java.util.Optional;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.eicke.receipts.controller.TravelExpenseManager;
import de.eicke.receipts.entities.CostCategory;
import de.eicke.receipts.entities.Receipt;
import de.eicke.receipts.entities.TravelExpense;
import de.eicke.receipts.entities.TravelExpenseStatus;
import de.eicke.receipts.repository.TravelExpenseRepository;



@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ApplicationTests {

	private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	
	
	@Autowired
    private WebApplicationContext context;


	private MockMvc mockMvc;

    @Before
    public void setup() {
    	mockMvc = MockMvcBuilders
                .webAppContextSetup(context)

                // ADD THIS!!
                .apply(springSecurity())
                .build();
    }
	@Autowired
	TravelExpenseManager manager;
	@Autowired
	TravelExpenseRepository repository;
	
	@Autowired
	ObjectMapper objectMapper;

	@After
	public void prepareDatabase() {
		repository.deleteAll();
	}

	@Test
	public void testTravelExpenseCreation() {
	
		TravelExpense expense = createExpense();
		
		String id = manager.registerTravelExpense(expense);
		
		Assert.assertNotNull("ID is nulL!", id);
		
		Assert.assertThat("Wrong number of repository entities!", repository.count(), is((long)1));
		
	}
	
	@Test
	public void testFindByIdAfterInsert() {
		TravelExpense expense = createExpense();
		
		String id = manager.registerTravelExpense(expense);
		TravelExpense result = repository.findOne(id);
		
		Assert.assertNotNull(result);
		
		
		Assert.assertEquals(id, result.getId());
	}
	
	@Test
	public void testFindByTravelIdAfterInsert() {
		TravelExpense expense = createExpense();
		
		String id = manager.registerTravelExpense(expense);
		Optional<TravelExpense> result = repository.findByTravelId(expense.getTravelId());
		
		Assert.assertThat("Travel could not be found by Travel-iD", result.isPresent(), is(true));
		Assert.assertEquals("ID is not the same", id, result.get().getId());
	}
	
	
	private TravelExpense createExpense() {
		TravelExpense expense = new TravelExpense();
		expense.setTravelDate(new Date());
		expense.setStatus(TravelExpenseStatus.CREATED);
		String travelId = "987654321";
		expense.setTravelId(travelId);
		Receipt receipt = new Receipt(new Date(), "Description", 10.99, 2.90, CostCategory.ACCOMMODATION);
		expense.addReceipt(receipt);
		return expense;
	}

	@Test
	public void testCreationViaRest() throws JsonProcessingException, Exception {
		TravelExpense expense = createExpense();
		mockMvc.perform(post("/travelExpenses").with(user("joe"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(expense))).andExpect(status().is2xxSuccessful());
		
	}
	
	@Test
	public void testGetByIdViaRest() throws Exception {
		TravelExpense expense = createExpense();
		manager.registerTravelExpense(expense);
		
		MvcResult result = mockMvc.perform(get("/travelExpenses").with(user("joe"))).andExpect(status().is2xxSuccessful()).andReturn();
		//TODO: Check content.
	}

}
