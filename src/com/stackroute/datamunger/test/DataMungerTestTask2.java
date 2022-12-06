package com.stackroute.datamunger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stackroute.datamunger.query.parser.AggregateFunction;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.QueryParser;

public class DataMungerTestTask2 {

	private static QueryParser queryParser;
	private QueryParameter queryParameter;
	private String queryString;

	@BeforeClass
	public static void setup() {
		// This methods runs, before running any one of the test case
		// This method is used to initialize the required variables
		queryParser = new QueryParser();

	}

	@AfterClass
	public static void teardown() {
		// This method runs, after running all the test cases
		// This method is used to clear the initialized variables
		queryParser = null;

	}

	@Test
	public void testGetFields() {
		queryString = "select winner,season,team2 from ipl.csv where season > 2014 group by winner";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> fields = new ArrayList<>();
		fields.add("winner");
		fields.add("season");
		fields.add("team2");

		assertEquals(
				"testGetFields() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a List which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		queryString = "select winner,season from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);

		fields.clear();
		fields.add("winner");
		fields.add("season");

		assertEquals(
				"testGetFields() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a List which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

	}

	public void testGetFieldsFailure() {
		queryString = "select winner,season,team1,team2 from ipl.csv where season > 2014";
		queryParameter = queryParser.parseQuery(queryString);

		assertNotNull(
				"testGetFieldsFailure() : Invalid Column / Field values. Please note that the query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a List which is to be returned by the method getFields(). Check getFields() method",
				queryParameter.getFields());

		queryString = "select winner,season,team1,team2 from ipl.csv where season > 2014 order by team2";
		queryParameter = queryParser.parseQuery(queryString);

		assertNotNull(
				"testGetFieldsFailure() : Invalid Column / Field values. Please note that the query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a List which is to be returned by the method getFields(). Check getFields() method",
				queryParameter.getFields());

	}

	@Test
	public void testGetAggregateFunctions() {
		queryString = "select max(city),winner from ipl.csv where season > 2014 and city ='Bangalore' or city ='Delhi' group by winner";
		queryParameter = queryParser.parseQuery(queryString);

		List<AggregateFunction> aggregateFunctionList = new ArrayList<>();

		AggregateFunction aggregateFunction = new AggregateFunction("city", "max");
		aggregateFunctionList.add(aggregateFunction);

		assertEquals(
				"testGetAggregateFunctions() : Hint: extract the aggregate functions from the query. The presence of the aggregate functions can determined if we have either 'min' or 'max' or 'sum' or 'count' or 'avg' followed by opening braces'(' after 'select' clause in the query. string. in case it is present, then we will have to extract the same. For each aggregate functions, we need to know the following: 1. type of aggregate function(min/max/count/sum/avg), 2. field on which the aggregate function is being applied. Please note that more than one aggregate function can be present in a query",
				aggregateFunctionList.toString(), queryParameter.getAggregateFunctions().toString());

		queryString = "select max(city),max(winner),count(team1) from ipl.csv where season > 2014 and city ='Bangalore' or city ='Delhi' group by winner";
		queryParameter = queryParser.parseQuery(queryString);

		aggregateFunctionList.clear();

		aggregateFunction = new AggregateFunction("city", "max");
		aggregateFunctionList.add(aggregateFunction);
		aggregateFunction = new AggregateFunction("winner", "max");
		aggregateFunctionList.add(aggregateFunction);
		aggregateFunction = new AggregateFunction("team1", "count");
		aggregateFunctionList.add(aggregateFunction);

		assertEquals(
				"testGetAggregateFunctions() : Hint: extract the aggregate functions from the query. The presence of the aggregate functions can determined if we have either 'min' or 'max' or 'sum' or 'count' or 'avg' followed by opening braces'(' after 'select' clause in the query. string. in case it is present, then we will have to extract the same. For each aggregate functions, we need to know the following: 1. type of aggregate function(min/max/count/sum/avg), 2. field on which the aggregate function is being applied. Please note that more than one aggregate function can be present in a query",
				aggregateFunctionList.toString(), queryParameter.getAggregateFunctions().toString());

	}

	@Test
	public void testGetAggregateFunctionsFailure() {
		queryString = "select max(city) from ipl.csv where season > 2014 and city ='Bangalore' or city ='Delhi' group by winner order by team1";
		queryParameter = queryParser.parseQuery(queryString);

		List<AggregateFunction> aggregateFunctions = queryParameter.getAggregateFunctions();

		assertNotNull(
				"testGetAggregateFunctionsFailure() : Hint: extract the aggregate functions from the query. The presence of the aggregate functions can determined if we have either 'min' or 'max' or 'sum' or 'count' or 'avg' followed by opening braces'(' after 'select' clause in the query. string. in case it is present, then we will have to extract the same. For each aggregate functions, we need to know the following: 1. type of aggregate function(min/max/count/sum/avg), 2. field on which the aggregate function is being applied. Please note that more than one aggregate function can be present in a query",
				aggregateFunctions.size() > 1);
	}

}
