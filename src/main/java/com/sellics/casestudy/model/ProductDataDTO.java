package com.sellics.casestudy.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "timestamp", "asin", "keyword", "rank" })
public class ProductDataDTO {

	private Long timestamp;
	private String asin;
	private String keyword;
	private Integer rank;

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
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

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "Product [timestamp=" + timestamp + ", asin=" + asin + ", keyword=" + keyword + ", rank=" + rank + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(asin, keyword, rank, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductDataDTO other = (ProductDataDTO) obj;
		return Objects.equals(asin, other.asin) && Objects.equals(keyword, other.keyword) && rank == other.rank
				&& Objects.equals(timestamp, other.timestamp);
	}

}
