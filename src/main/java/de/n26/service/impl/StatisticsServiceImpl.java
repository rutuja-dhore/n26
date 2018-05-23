package de.n26.service.impl;

import java.time.Instant;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.n26.model.Statistics;
import de.n26.service.StatisticsService;
import de.n26.util.StatisticsCache;

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Override
	public synchronized Statistics get() {
		long currentTime = Instant.now().toEpochMilli();

		List<Double> last60SecondsCache = StatisticsCache.transactionCache.entrySet().stream()
				.filter(p -> p.getKey().longValue() > currentTime - 60000) 
				.map(p -> p.getValue()).collect(Collectors.toList());

		DoubleSummaryStatistics stats = last60SecondsCache.stream()
				.collect(Collectors.summarizingDouble(Double::doubleValue));

		return new Statistics(stats.getSum(), stats.getAverage(), stats.getMax(), stats.getMin(), stats.getCount());
	}
}
