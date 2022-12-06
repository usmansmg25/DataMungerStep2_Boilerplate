package com.stackroute.datamunger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.QueryParser;
import com.stackroute.datamunger.query.parser.Restriction;

public class DataMungerTestTask3 {

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
	public void testGetRestrictions() {
		queryString = "select winner,season,team1,team2 from ipl.csv " + "where season > 2014";

		queryParameter = queryParser.parseQuery(queryString);

		List<Restriction> restrictionList = new ArrayList<>();

		Restriction restriction = new Restriction("season", "2014", ">");

		restrictionList.add(restriction);
		
		

		assertEquals(
				"testGetRestrictions() :Hint test may be failing because of any 1 of the following reason 1)trim() not used while extracting propertyName or propertyValue or condition to remove extra spaces2)Not considering all relational operators",
				restrictionList.toString(), queryParameter.getRestrictions().toString());

		restrictionList.clear();
		
		queryString = "select winner,season,team1,team2 from ipl.csv " + "where season = 2014 and city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);

		restriction = new Restriction("season", "2014", "=");
		restrictionList.add(restriction);
		restriction = new Restriction("city", "Bangalore", "=");
		restrictionList.add(restriction);

		assertEquals(
				"testGetRestrictions() :Hint test may be failing because of any 1 of the following reason 1)trim() not used while extracting propertyName or propertyValue or condition to remove extra spaces2)Not considering all relational operators",
				restrictionList.toString(), queryParameter.getRestrictions().toString());

	}

	@Test
	public void testGetRestrictionsFailure() {
		queryString = "select winner,season,team1,team2 from ipl.csv where " + "season = 2014 and city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);

		List<Restriction> restriction = queryParameter.getRestrictions();

		assertNotNull(
				"testGetFieldsAndMultipleRestrictionsFailures() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restriction.size() > 0);

		queryString = "select winner,season,team1,team2 from ipl.csv where season = 2014 and city ='Bangalore' or city ='Delhi'";
		queryParameter = queryParser.parseQuery(queryString);

		restriction.clear();
		assertNotNull(
				"testGetFieldsAndMultipleRestrictionsFailures() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restriction.size() > 0);

	}

	@Test
	public void testGetLogicalOperators() {
		queryString = "select city,winner,team from ipl.csv where season > 2014 and city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> logicalOperators = new ArrayList<>();
		logicalOperators.add("and");

		assertEquals(
				"testGetLogicalOperators() :  Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				logicalOperators, queryParameter.getLogicalOperators());

		queryString = "select city,winner,team from ipl.csv where season > 2014 and city ='Bangalore' and team1 ='Kolkata knight riders'";
		queryParameter = queryParser.parseQuery(queryString);
		logicalOperators.clear();
		logicalOperators.add("and");
		logicalOperators.add("and");

		assertEquals(
				"testGetLogicalOperators() :  Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				logicalOperators, queryParameter.getLogicalOperators());

	}

	@Test
	public void testGetLogicalOperatorsFailures() {
		queryString = "select city,winner,team from ipl.csv where season > 2014 and city ='Bangalore' or city ='Delhi'";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> logicalOperators = queryParameter.getLogicalOperators();

		assertNotNull(
				"testGetLogicalOperatorsFailures() :  Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				logicalOperators);

		queryString = "select city,winner,team from ipl.csv where season > 2014";
		queryParameter = queryParser.parseQuery(queryString);
		logicalOperators.clear();
		logicalOperators = queryParameter.getLogicalOperators();

		assertNotNull(
				"testGetLogicalOperatorsFailures() :  Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				logicalOperators);

	}

}
