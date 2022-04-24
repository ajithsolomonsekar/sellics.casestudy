package com.sellics.casestudy.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class TimeSeriesResponse extends BaseReponse {

	private String docType;
	private String version;
	private TimeSeriesResponseHeader header;
	private List<TimeSeriesResponseData> data;

	public TimeSeriesResponse(String docType, String version) {
		super();
		this.docType = docType;
		this.version = version;
	}

	public TimeSeriesResponse() {
		super();
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public TimeSeriesResponseHeader getHeader() {
		return header;
	}

	public void setHeader(TimeSeriesResponseHeader header) {
		this.header = header;
	}

	public List<TimeSeriesResponseData> getData() {
		return data;
	}

	public void setData(List<TimeSeriesResponseData> data) {
		this.data = data;
	}

}
