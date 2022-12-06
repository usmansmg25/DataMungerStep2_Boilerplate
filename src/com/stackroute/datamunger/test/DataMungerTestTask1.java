package com.stackroute.datamunger.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.stackroute.datamunger.query.parser.QueryParameter;
import com.stackroute.datamunger.query.parser.QueryParser;

public class DataMungerTestTask1 {

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
	public void testGetBaseQuery() {
		queryString = "select winner,team1,team2 from ipl.csv where season > 2014";
		queryParameter = queryParser.parseQuery(queryString);

		assertEquals(
				"testGetBaseQuery(),Retrieval of Base Query failed. BaseQuery contains from the beginning of the query till the where clause",
				"select winner,team1,team2 from ipl.csv", queryParameter.getBaseQuery());

	}

	@Test
	public void testGetBaseQueryFailure() {
		queryString = "select winner,team1 from ipl.csv order by team1";
		queryParameter = queryParser.parseQuery(queryString);

		assertNotEquals(
				"testGetBaseQueryFailure(),Retrieval of Base Query failed. BaseQuery contains from the beginning of the query till the where clause",
				"select winner,team1 from ipl1.csv", queryParameter.getBaseQuery());

		assertNotNull("testGetBaseQueryFailure() , Retrieval of Base Query returns Null",
				queryParameter.getBaseQuery());
	}

	@Test
	public void testGetFileName() {
		queryString = "select * from ipl.csv";
		queryParameter = queryParser.parseQuery(queryString);
		assertEquals(
				"testGetFileName(): File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				"ipl.csv", queryParameter.getFileName());

		queryString = "select city,winner,team1,team2 from ipl.csv where season > 2014";
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

		queryString = "select city,winner,team1 from ipl.csv";
		assertNotNull(
				"testGetFileNameFailure(): File name extraction failed. Check getFile() method. File name can be found after a space after from clause. Note: CSV file can contain a field that contains from as a part of the column name. For eg: from_date,from_hrs etc",
				queryParameter.getFileName());

	}

	@Test
	public void testGetOrderByClause() {
		queryString = "select city,winner,team1 from ipl.csv order by team1";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> orderByFields = new ArrayList<String>();
		orderByFields.add("team1");

		assertEquals(
				"testGetOrderByClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields, queryParameter.getOrderByFields());

		queryString = "select city,winner,team1 from ipl.csv group by team1 order by winner";

		queryParameter = queryParser.parseQuery(queryString);
		orderByFields.clear();
		orderByFields.add("winner");

		assertEquals(
				"testGetOrderByClause() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields, queryParameter.getOrderByFields());

	}

	@Test
	public void testGetOrderByClauseFailure() {
		queryString = "select city,winner,team1 from ipl.csv order by team1";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> orderByFields = queryParameter.getOrderByFields();

		assertNotNull(
				"testGetOrderByClauseFailure() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				orderByFields);

		queryString = "select city,winner,team1 from ipl.csv order by winner";
		orderByFields.clear();
		orderByFields = queryParameter.getOrderByFields();

		assertNotEquals(
				"testGetOrderByClauseFailure() : Hint: Please note that we will need to extract the field(s) after 'order by' clause in the query, if at all the order by clause exists",
				"team1", orderByFields);
	}

	@Test
	public void testGetGroupByFields() {
		queryString = "select winner,season,team1,team2 from ipl.csv where season > 2014 group by winner";
		queryParameter = queryParser.parseQuery(queryString);

		List<String> groupByFields = new ArrayList<String>();
		groupByFields.add("winner");

		assertEquals(
				"testGetGroupByFields() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible that the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				groupByFields, queryParameter.getGroupByFields());

		queryString = "select winner,season,team1,team2 from ipl.csv where season > 2014 group by season order by team1";
		queryParameter = queryParser.parseQuery(queryString);
		groupByFields.clear();

		groupByFields.add("season");

		assertEquals(
				"testGetGroupByFields() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible that the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				groupByFields, queryParameter.getGroupByFields());

	}

	@Test
	public void testGetGroupByFieldsFailure() {
		queryString = "select winner,season,team2 from ipl.csv where season > 2014 group by winner";
		queryParameter = queryParser.parseQuery(queryString);

		assertNotNull(
				"testGetGroupByFieldsFailure() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				queryParameter.getGroupByFields());

		queryString = "select winner,season,team2 from ipl.csv where season > 2014 group by winner order by team1";
		queryParameter = queryParser.parseQuery(queryString);

		assertNotNull(
				"testGetGroupByFieldsFailure() : Hint: Check getGroupByFields() method. The query string can contain more than one group by fields. it is also possible thant the query string might not contain group by clause at all. The field names, condition values might contain 'group' as a substring. For eg: newsgroup_name",
				queryParameter.getGroupByFields());

	}

}
