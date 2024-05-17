package com.moonlight.practice;

import java.util.ArrayList;
import java.util.List;

public enum RepoTemp {
	INSTANCE;

	public List<String> allHolidays() {
		List<String> holidays = new ArrayList<>();
		holidays.add("02-04-2024");
		holidays.add("05-04-2024");
		holidays.add("06-04-2024");
		return holidays;
	}
}
