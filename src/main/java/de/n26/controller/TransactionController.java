package de.n26.controller;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.n26.model.Transaction;
import de.n26.service.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping
	public synchronized ResponseEntity<Object> create(@RequestBody Transaction transactionModel) {
		Instant instant = Instant.now();
		long timeNow = instant.toEpochMilli();

		if (transactionModel.getTimestamp() > (timeNow - 60000)) {
			transactionService.create(transactionModel);
			return ResponseEntity.status(201).build();
		} else {
			return ResponseEntity.status(204).build();
		}
	}

}
