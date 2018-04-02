package de.eicke.receipts.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.eicke.receipts.entities.TravelExpense;
import de.eicke.receipts.repository.TravelExpenseRepository;

@Component
public class TravelExpenseManager {

	@Autowired
	private TravelExpenseRepository repository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Optional<TravelExpense> getTravelExpenseById(String id) {
		 TravelExpense exp = repository.findOne(id);
		 Optional<TravelExpense> expense = Optional.of(exp);
		if (!expense.isPresent()) {
			logger.info("TravelExpenses with ID {0} not found!", id); 
		}
		return expense;
	}
	
	public List<TravelExpense> getAllTravelExpenses() {
		return repository.findAll();
	}
	
	public String registerTravelExpense(TravelExpense expense) {
		if (expense == null) {
			throw new RuntimeException("Expense must not be null!");
		}
		TravelExpense result = repository.insert(expense);
		return result.getId();
	}
	
	public Optional<TravelExpense> findTravelExpenseByTravelId(String travelId) {
		Optional<TravelExpense> expense = repository.findByTravelId(travelId);
		if (!expense.isPresent()) {
			logger.info("TravelExpenses with Travel-ID {} not found!", travelId); 
		}
		return expense;
	} 
}
