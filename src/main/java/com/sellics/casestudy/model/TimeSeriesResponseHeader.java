package com.sellics.casestudy.model;

import java.util.List;

public class TimeSeriesResponseHeader {

	private String startTime;
	private String endTime;
	private int recordCount;
	private List<TimeSeriesResponseHeaderColumn> columns;

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}

	public List<TimeSeriesResponseHeaderColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<TimeSeriesResponseHeaderColumn> columns) {
		this.columns = columns;
	}

}
