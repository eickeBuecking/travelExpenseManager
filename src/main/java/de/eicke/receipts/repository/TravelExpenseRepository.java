package de.eicke.receipts.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import de.eicke.receipts.entities.TravelExpense;

public interface TravelExpenseRepository extends MongoRepository<TravelExpense, String>{
	public Optional<TravelExpense> findByTravelId(String travelId);
}
