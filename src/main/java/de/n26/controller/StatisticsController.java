package de.n26.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.n26.model.Statistics;
import de.n26.service.StatisticsService;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

	@Autowired
	private StatisticsService statisticsService;

	@GetMapping
	public synchronized Statistics get() {
		return statisticsService.get();
	}
}
