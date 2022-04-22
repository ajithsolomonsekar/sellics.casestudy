package com.sellics.casestudy.model;

import java.util.SortedSet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class TimeSeriesResponse extends BaseReponse {

//	@JsonProperty("JsonTs")
//	private String jsonTs = "irregular";
//	@JsonProperty("BasePeriod")
//	private String basePeriod;
//	@JsonProperty("Anchor")
//	private String anchor;
//	@JsonProperty("SubPeriods")
//	private String subPeriods;

	@JsonProperty("ASIN")
	private String asin;
	@JsonProperty("Keyword")
	private String keyword;

	@JsonProperty("Observations")
	private SortedSet<String[]> observations;

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

	public SortedSet<String[]> getObservations() {
		return observations;
	}

	public void setObservations(SortedSet<String[]> observations) {
		this.observations = observations;
	}

}
