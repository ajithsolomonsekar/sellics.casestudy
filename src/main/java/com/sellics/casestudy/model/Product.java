package com.sellics.casestudy.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timestamp", "asin", "keyword", "rank" })
public class Product {

	private String timestamp;
	private String asin;
	private String keyword;
	private String rank;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "Product [timestamp=" + timestamp + ", asin=" + asin + ", keyword=" + keyword + ", rank=" + rank + "]";
	}

}
