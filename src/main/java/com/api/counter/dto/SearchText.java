package com.api.counter.dto;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchText {
	
	ArrayList<String> searchText;

	@JsonProperty("searchText")
	public ArrayList<String> getSearchText() {
		return searchText;
	}

	public void setSearchText(ArrayList<String> searchText) {
		this.searchText = searchText;
	}
	
}
