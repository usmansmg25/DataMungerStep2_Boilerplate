package com.stackroute.datamunger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;

import org.junit.BeforeClass;
import org.junit.Test;

import com.stackroute.datamunger.query.parser.AggregateFunction;
import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.QueryParser;
import com.stackroute.datamunger.query.parser.Restriction;

public class DataMungerTest {

	private static QueryParser queryParser;
	private QueryParameter queryParameter;
	private String queryString;

	@BeforeClass
	public static void setup() {
		// setup methods runs, before every test case runs
		// This method is used to initialize the required variables
		queryParser = new QueryParser();

	}

	@AfterClass
	public static void teardown() {
		// teardown method runs, after every test case run
		// This method is to clear the initialized variables
		queryParser = null;

	}

	@Test
	public void testGetFileName() {
		queryString = "select * from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		assertEquals(
				"testGetFileName(): File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());

	}

	@Test
	public void testGetFileNameFailure() {
		queryString = "select * from ipl1.csv";
		queryParameter = queryParser.parseQuery(queryString);
		assertNotEquals(
				"testGetFileNameFailure(): File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());

		queryString = "select winner,team1,team2 from ipl.csv where season > 2014";
		queryParameter = queryParser.parseQuery(queryString);

		assertNotNull(
				"testGetFileNameFailure(): File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				queryParameter.getFileName());

	}

	@Test
	public void testGetFields() {
		queryString = "select city,winner,team1,team2 from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		List<String> expectedFields = new ArrayList<>();
		expectedFields.add("city");
		expectedFields.add("winner");
		expectedFields.add("team1");
		expectedFields.add("team2");

		assertEquals(
				"testGetFields() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				expectedFields, queryParameter.getFields());

	}

	@Test
	public void testGetFieldsFailure() {
		queryString = "select city,winner,team1,team2 from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);

		assertNotNull(
				"testGetFieldsFailure() : Invalid Column / Field values. Please note that the query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				queryParameter.getFields());

	}

	@Test
	public void testGetFieldsAndRestrictions() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<Restriction> restrictionsList = new ArrayList<>();
		Restriction restrictions = new Restriction("season", "2014", ">");
		restrictionsList.add(restrictions);

		assertEquals(
				"testGetFieldsAndRestrictions() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());
		assertEquals(
				"testGetFieldsAndRestrictions() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		assertEquals(
				"testGetFieldsAndRestrictions() :Hint test may be failing because of any 1 of the following reason 1)trim() not used while extracting propertyName or propertyValue or condition to remove extra spaces2)Not considering all relational operators",
				restrictionsList.toString(), queryParameter.getRestrictions().toString());

	}

	@Test
	public void testGetFieldsAndRestrictionsFailure() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and team1 ='Kolkata knight riders'";
		queryParameter = queryParser.parseQuery(queryString);

		List<Restriction> restrictions = queryParameter.getRestrictions();

		assertNotNull(
				"testGetFieldsAndRestrictionsFailure() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

		assertNotNull(
				"testGetFieldsAndRestrictionsFailure() : Invalid Column / Field values. Please note that the query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a List which is to be returned by the method getFields(). Check getFields() method",
				queryParameter.getFields());

	}

	@Test
	public void testGetFieldsAndMultipleRestrictionsAnd() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<String> logicalop = new ArrayList<String>();
		logicalop.add("and");

		List<Restriction> restrictionsList = new ArrayList<>();
		Restriction restriction = new Restriction("season", "2014", ">");
		restrictionsList.add(restriction);
		restriction = new Restriction("city", "Bangalore", "=");
		restrictionsList.add(restriction);

		assertEquals(
				"testGetFieldsAndMultipleRestrictions() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());
		assertEquals(
				"testGetFieldsAndMultipleRestrictions() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		assertEquals(
				"testGetFieldsAndMultipleRestrictionsAnd() :Hint test may be failing because of any 1 of the following reason 1)trim() not used while extracting propertyName or propertyValue or condition to remove extra spaces2)Not considering all relational operators",
				restrictionsList.toString(), queryParameter.getRestrictions().toString());

		assertEquals(
				"testGetFieldsAndMultipleRestrictions() : Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc.",
				logicalop, queryParameter.getLogicalOperators());

	}

	@Test
	public void testGetFieldsAndMultipleRestrictionsFailures() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();

		assertNotNull(
				"testGetFieldsAndMultipleRestrictionsFailures() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

		assertNotNull(
				"testGetFieldsAndMultipleRestrictionsFailures() : Invalid Column / Field values. Please note that the query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a List which is to be returned by the method getFields(). Check getFields() method",
				queryParameter.getFields());

	}

	@Test
	public void testGetFieldsAndMultipleRestrictionsOr() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 or city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<String> logicalop = new ArrayList<String>();
		logicalop.add("or");

		List<Restriction> restrictionsList = new ArrayList<>();

		Restriction restriction = new Restriction("season", "2014", ">");
		restrictionsList.add(restriction);
		restriction = new Restriction("city", "Bangalore", "=");
		restrictionsList.add(restriction);

		assertEquals(
				"testGetFieldsAndMultipleRestrictions2() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());
		assertEquals(
				"testGetFieldsAndMultipleRestrictions2() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		assertEquals(
				"testGetFieldsAndMultipleRestrictionsOr() :Hint test may be failing because of any 1 of the following reason 1)trim() not used while extracting propertyName or propertyValue or condition to remove extra spaces2)Not considering all relational operators",
				restrictionsList.toString(), queryParameter.getRestrictions().toString());
		assertEquals(
				"testGetFieldsAndMultipleRestrictions2() :  Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				logicalop, queryParameter.getLogicalOperators());
	}

	@Test
	public void testGetFieldsAndMultipleRestrictionsOrFailure() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 or city ='Bangalore'";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();

		assertNotNull(
				"testGetFieldsAndMultipleRestrictions2Failure() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

	}

	
	@Test
	public void testGetFieldsAndThreeRestrictions() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore' or city ='Delhi'";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> logicalOperators = new ArrayList<String>();
		logicalOperators.add("and");
		logicalOperators.add("or");

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<Restriction> restrictionsList = new ArrayList<>();

		Restriction restriction = new Restriction("season", "2014", ">");
		restrictionsList.add(restriction);
		restriction = new Restriction("city", "Bangalore", "=");
		restrictionsList.add(restriction);
		restriction = new Restriction("city", "Delhi", "=");
		restrictionsList.add(restriction);

		assertEquals(
				"testGetFieldsAndThreeRestrictions() :  File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());
		assertEquals(
				"testGetFieldsAndThreeRestrictions() :  Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		assertEquals(
				"testGetFieldsAndThreeRestrictions() :Hint test may be failing because of any 1 of the following reason 1)trim() not used while extracting propertyName or propertyValue or condition to remove extra spaces2)Not considering all relational operators",
				restrictionsList.toString(), queryParameter.getRestrictions().toString());

		assertEquals(
				"testGetFieldsAndThreeRestrictions() :  Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				logicalOperators, queryParameter.getLogicalOperators());

	}

	@Test
	public void testGetFieldsAndThreeRestrictionsFailure() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore' or city ='Delhi'";
		queryParameter = queryParser.parseQuery(queryString);

		List<Restriction> restrictions = queryParameter.getRestrictions();
		assertNotNull(
				"testGetFieldsAndThreeRestrictionsFailure() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

	}

	@Test
	public void testGetAggregateFunctions() {
		queryString = "select count(city),avg(win_by_runs),min(season),max(win_by_wickets) from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);

		List<AggregateFunction> aggregateFunctionList = new ArrayList<>();
		AggregateFunction aggregateFunction = new AggregateFunction("city", "count");
		aggregateFunctionList.add(aggregateFunction);
		aggregateFunction = new AggregateFunction("win_by_runs", "avg");
		aggregateFunctionList.add(aggregateFunction);
		aggregateFunction = new AggregateFunction("season", "min");
		aggregateFunctionList.add(aggregateFunction);
		aggregateFunction = new AggregateFunction("win_by_wickets", "max");
		aggregateFunctionList.add(aggregateFunction);

		assertEquals(
				"testGetAggregateFunctions() : Hint: extract the aggregate functions from the query. The presence of the aggregate functions can determined if we have either 'min' or 'max' or 'sum' or 'count' or 'avg' followed by opening braces'(' after 'select' clause in the query. string. in case it is present, then we will have to extract the same. For each aggregate functions, we need to know the following: 1. type of aggregate function(min/max/count/sum/avg), 2. field on which the aggregate function is being applied. Please note that more than one aggregate function can be present in a query",
				aggregateFunctionList.toString(), queryParameter.getAggregateFunctions().toString());
		assertEquals(
				"testGetAggregateFunctions() :  File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());

	}

	@Test
	public void testGetAggregateFunctionsFailure() {
		queryString = "select count(city),avg(win_by_runs),min(season),max(win_by_wickets) from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		List<AggregateFunction> aggregateFunctions = queryParameter.getAggregateFunctions();

		assertNotNull(
				"testGetAggregateFunctionsFailure() : Hint: extract the aggregate functions from the query. The presence of the aggregate functions can determined if we have either 'min' or 'max' or 'sum' or 'count' or 'avg' followed by opening braces'(' after 'select' clause in the query. string. in case it is present, then we will have to extract the same. For each aggregate functions, we need to know the following: 1. type of aggregate function(min/max/count/sum/avg), 2. field on which the aggregate function is being applied. Please note that more than one aggregate function can be present in a query",
				aggregateFunctions);

	}

	@Test
	public void testGetGroupByClause() {
		queryString = "select city,winner,player_match from ipl.csv group by city";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> groupByFields = new ArrayList<String>();
		groupByFields.add("city");

		assertEquals(
				"testGetGroupByClause() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				groupByFields, queryParameter.getGroupByFields());

		List<String> fields = new ArrayList<>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		assertEquals(
				"testGetGroupByClause() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		assertEquals(
				"testGetGroupByClause() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				null, queryParameter.getRestrictions());

		assertEquals(
				"testGetGroupByClause() : Logical Operators should be null. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc",
				null, queryParameter.getLogicalOperators());

	}

	@Test
	public void testGetGroupByClauseFailure() {
		queryString = "select city,avg(win_by_runs) from ipl.csv group by city";
		queryParameter = queryParser.parseQuery(queryString);

		assertNotNull(
				"testGetGroupByClauseFailure() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				queryParameter.getGroupByFields());

	}

	@Test
	public void testGetGroupByOrderByClause() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 "
				+ "and city ='Bangalore' group by winner order by city";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<String> logicalOperators = new ArrayList<String>();
		logicalOperators.add("and");

		List<String> orderByFields = new ArrayList<String>();
		orderByFields.add("city");

		List<String> groupByFields = new ArrayList<String>();
		groupByFields.add("winner");

		List<Restriction> restrictionsList = new ArrayList<>();
		Restriction restriction = new Restriction("season", "2014", ">");
		restrictionsList.add(restriction);
		restriction = new Restriction("city", "Bangalore", "=");
		restrictionsList.add(restriction);

		assertEquals(
				"testGetGroupByOrderByClause() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());
		assertEquals(
				"testGetGroupByOrderByClause() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		assertEquals(
				"testGetGroupByOrderByClause() :Hint test may be failing because of any 1 of the following reason 1)trim() not used while extracting propertyName or propertyValue or condition to remove extra spaces2)Not considering all relational operators",
				restrictionsList.toString(), queryParameter.getRestrictions().toString());
		assertEquals(
				"testGetGroupByOrderByClause() : Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc.",
				logicalOperators, queryParameter.getLogicalOperators());

		assertEquals(
				"testGetGroupByOrderByClause() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				groupByFields, queryParameter.getGroupByFields());

		assertEquals(
				"testGetOrderByClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields, queryParameter.getOrderByFields());

	}

	@Test
	public void testGetGroupByOrderByClauseFailure() {
		queryString = "select city,winner,team1,team2 from ipl.csv where season > 2016 and city ='Bangalore' group by winner order by city";
		queryParameter = queryParser.parseQuery(queryString);

		List<Restriction> restrictions = queryParameter.getRestrictions();
		assertNotNull(
				"testGetGroupByOrderByClauseFailure() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				queryParameter.getGroupByFields());
		assertNotNull(
				"testGetGroupByOrderByClauseFailure() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists.",
				queryParameter.getOrderByFields());
		assertNotNull(
				"testGetGroupByOrderByClauseFailure() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

	}

	@Test
	public void testGetOrderByAndWhereConditionClause() {
		queryString = "select city,winner,player_match from ipl.csv"
				+ " where season > 2014 and city ='Bangalore' order by city";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		List<String> logicalOperators = new ArrayList<String>();
		logicalOperators.add("and");

		List<String> orderByFields = new ArrayList<String>();
		orderByFields.add("city");

		List<Restriction> restrictionList = new ArrayList<>();
		Restriction restriction = new Restriction("season", "2014", ">");
		restrictionList.add(restriction);
		restriction = new Restriction("city", "Bangalore", "=");
		restrictionList.add(restriction);

		assertEquals(
				"testGetOrderByAndWhereConditionClause() : File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());
		assertEquals(
				"testGetOrderByAndWhereConditionClause() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		assertEquals(
				"testGetOrderByAndWhereConditionClause() :Hint test may be failing because of any 1 of the following reason 1)trim() not used while extracting propertyName or propertyValue or condition to remove extra spaces2)Not considering all relational operators",
				restrictionList.toString(), queryParameter.getRestrictions().toString());
		assertEquals(
				"testGetOrderByAndWhereConditionClause() : Retrieval of Logical Operators failed. AND/OR keyword will exist in the query only if where conditions exists and it contains multiple conditions.The extracted logical operators will be stored in a String array which will be returned by the method. Please note that AND/OR can exist as a substring in the conditions as well. For eg: name='Alexander',color='Red' etc.",
				logicalOperators, queryParameter.getLogicalOperators());

		assertEquals(
				"testGetOrderByAndWhereConditionClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields, queryParameter.getOrderByFields());

	}

	@Test
	public void testGetOrderByAndWhereConditionClauseFailure() {
		queryString = "select city,winner,player_match from ipl.csv where season > 2014 and city ='Bangalore' order by city";
		queryParameter = queryParser.parseQuery(queryString);
		List<Restriction> restrictions = queryParameter.getRestrictions();

		assertNotNull(
				"testGetOrderByAndWhereConditionClauseFailure() : Hint: extract the conditions from the query string(if exists). for each condition, we need to capture the following: 1. Name of field, 2. condition, 3. value, please note the query might contain multiple conditions separated by OR/AND operators",
				restrictions);

		assertNotNull(
				"testGetOrderByAndWhereConditionClauseFailure() :Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists.",
				queryParameter.getOrderByFields());

	}

	@Test
	public void testGetOrderByClause() {
		queryString = "select city,winner,player_match from ipl.csv order by city";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> orderByFields = new ArrayList<String>();
		orderByFields.add("city");

		List<String> fields = new ArrayList<String>();
		fields.add("city");
		fields.add("winner");
		fields.add("player_match");

		assertEquals(
				"testGetOrderByClause() : Select fields extractions failed. The query string can have multiple fields separated by comma after the 'select' keyword. The extracted fields is supposed to be stored in a String array which is to be returned by the method getFields(). Check getFields() method",
				fields, queryParameter.getFields());

		assertEquals(
				"testGetOrderByClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields, queryParameter.getOrderByFields());

	}

	@Test
	public void testGetOrderByClauseFailure() {
		queryString = "select city,winner,team1,team2,player_match from ipl.csv order by city";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> orderByFields = queryParameter.getOrderByFields();
		assertNotNull(
				"testGetOrderByClauseFailure() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields);

	}

}