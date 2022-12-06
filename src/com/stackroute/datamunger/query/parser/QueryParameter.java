package com.stackroute.datamunger.query.parser;

import java.util.List;

/* 
 * This class will contain the elements of the parsed Query String such as conditions,
 * logical operators,aggregate functions, file name, fields group by fields, order by
 * fields, Query Type
 * */

public class QueryParameter {

	String queryString;
	String fileName;
	String baseQuery;
	List<Restriction> restrictions;
	List<String> fields;
	List<String> logicalOperators;
	List<AggregateFunction> aggregateFunctions;
	List<String> orderByFields;
	List<String> groupByFields;

	public String getFileName() {
		return fileName;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public void setBaseQuery(String baseQuery) {
		this.baseQuery = baseQuery;
	}

	public void setRestrictions(List<Restriction> restrictions) {
		this.restrictions = restrictions;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public void setLogicalOperators(List<String> logicalOperators) {
		this.logicalOperators = logicalOperators;
	}

	public void setAggregateFunctions(List<AggregateFunction> aggregateFunctions) {
		this.aggregateFunctions = aggregateFunctions;
	}

	public void setOrderByFields(List<String> orderByFields) {
		this.orderByFields = orderByFields;
	}

	public void setGroupByFields(List<String> groupByFields) {
		this.groupByFields = groupByFields;
	}

	public String getBaseQuery() {
		return baseQuery;
	}

	public List<Restriction> getRestrictions() {
		return restrictions;
	}

	public List<String> getLogicalOperators() {
		return logicalOperators;
	}

	public List<String> getFields() {
		return fields;
	}

	public List<AggregateFunction> getAggregateFunctions() {
		return aggregateFunctions;
	}

	public List<String> getGroupByFields() {
		return groupByFields;
	}

	public List<String> getOrderByFields() {
		return orderByFields;
	}
}
