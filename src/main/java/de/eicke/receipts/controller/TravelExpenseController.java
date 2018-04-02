package de.eicke.receipts.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.eicke.exceptions.ErrorMessage;
import de.eicke.exceptions.TravelManagerException;
import de.eicke.receipts.entities.TravelExpense;

@RestController
public class TravelExpenseController {
	@Autowired
	TravelExpenseManager manager;
	
	private final static String path = "/travelExpenses";
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(path=path, method=RequestMethod.POST)
	public ResponseEntity<Void> register(@Valid @RequestBody final TravelExpense expense) {
		logger.info("TravelExpenseRegistration for Travel {}", expense.getTravelId());
		
		String id = manager.registerTravelExpense(expense);
		
		final URI location = ServletUriComponentsBuilder
	            .fromCurrentServletMapping().path(path + "/{id}").build()
	            .expand(id).toUri();

	      final HttpHeaders headers = new HttpHeaders();
	      headers.setLocation(location);

	      final ResponseEntity<Void> entity = new ResponseEntity<Void>(headers,
	            HttpStatus.CREATED);
	      
	      return entity;
	}
	
	@RequestMapping(path=path, method=RequestMethod.GET)
	public ResponseEntity<TravelExpense[]> getTravelExpenses() {
		logger.info("Fetch all TravelExpenses!");
		List<TravelExpense> expenses = manager.getAllTravelExpenses();
		TravelExpense[] resultExpenses = new TravelExpense[expenses.size()];
		resultExpenses = expenses.toArray(resultExpenses);
		final ResponseEntity<TravelExpense[]> result = new ResponseEntity<TravelExpense[]>(resultExpenses, HttpStatus.OK); 
		return result;
	}
	
	@RequestMapping(path=path + "/{someId}", method=RequestMethod.GET)
	public ResponseEntity<TravelExpense> getTravelExpenseById(@PathVariable(value="someId") final String expenseId) {
		Optional<TravelExpense> expense = manager.getTravelExpenseById(expenseId);
		if (!expense.isPresent()) {
			ResponseEntity<TravelExpense> result = new ResponseEntity<TravelExpense>(HttpStatus.NOT_FOUND);
			return result;
		} else {
			ResponseEntity<TravelExpense> result = new ResponseEntity<TravelExpense>(expense.get(), HttpStatus.OK);
			return result;
		}
	}
	
	@RequestMapping(path=path, method=RequestMethod.GET, params="travelId")
	public ResponseEntity<TravelExpense> getTravelExpensesByTravelId(@RequestParam(value="travelId") final String travelId) {
		Optional<TravelExpense> expense = manager.findTravelExpenseByTravelId(travelId);
		if (!expense.isPresent()) {
			ResponseEntity<TravelExpense> result = new ResponseEntity<TravelExpense>(HttpStatus.NOT_FOUND);
			return result;
		} else {
			ResponseEntity<TravelExpense> result = new ResponseEntity<TravelExpense>(expense.get(), HttpStatus.OK);
			return result;
		}
	}

	
	@ExceptionHandler(TravelManagerException.class)
	public ResponseEntity<ErrorMessage> exceptionHandler(Exception ex) {
		ErrorMessage error = new ErrorMessage();
		error.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
		error.setMessage(ex.getMessage());
		return new ResponseEntity<ErrorMessage>(error, HttpStatus.BAD_REQUEST);
	}
}
