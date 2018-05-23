package de.n26.service.impl;

import org.springframework.stereotype.Service;

import de.n26.model.Transaction;
import de.n26.service.TransactionService;
import de.n26.util.StatisticsCache;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Override
	public synchronized void create(Transaction transactionModel) {
		StatisticsCache.transactionCache.put(transactionModel.getTimestamp(), transactionModel.getAmount());
	}
}
