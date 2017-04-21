package com.api.counter.dto;

import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {
	Set<Entry<String, Long>> counts;

	@JsonProperty("counts")
	public Set<Entry<String, Long>> getCounts() {
		return counts;
	}

	public void setCounts(Set<Entry<String, Long>> set) {
		this.counts = set;
	}

}
