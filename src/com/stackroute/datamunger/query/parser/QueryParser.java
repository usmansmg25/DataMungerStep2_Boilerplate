
package com.stackroute.datamunger.query.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*There are total 4 DataMungerTest file:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 4 methods
 * a)getBaseQuery()  b)getFileName()  c)getOrderByClause()  d)getGroupByFields()
 * 
 * Once you implement the above 4 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 2 methods
 * a)getFields() b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getRestrictions()  b)getLogicalOperators()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class QueryParser {

	private QueryParameter queryParameter = new QueryParameter();
	private String[] queriesStr =null;

	/*
	 * This method will parse the queryString and will return the object of
	 * QueryParameter class
	 */
	public QueryParameter parseQuery(String queryString) {
		queryParameter.setQueryString(queryString);
		queryParameter.setFileName(getFileName(queryString));
		queryParameter.setBaseQuery(getBaseQuery(queryString));
		queryParameter.setGroupByFields(getGroupByFields(queryString));
		queryParameter.setOrderByFields(getOrderByFields(queryString));
		queryParameter.setFields(getFields(queryString));
		queryParameter.setAggregateFunctions(getAggregateFunctions(queryString));
		queryParameter.setRestrictions(getRestrictions(queryString));
		queryParameter.setLogicalOperators(getLogicalOperators(queryString));
		
		return queryParameter;
	}
	
	
	private String[] getSplitStrings(String queryString) {
		if (queriesStr == null || queriesStr.length > 0) {
			queriesStr = queryString.toLowerCase().split(" ");
		}
		return queriesStr;
	}	
	
	
	/*
	 * Extract the name of the file from the query. File name can be found after the
	 * "from" clause.
	 */
	public String getFileName(String queryString) {
		Pattern pattern = Pattern.compile(
				"from\\s+(?:\\w+\\.)*(\\w+)($|\\s+[WHERE,JOIN,START\\s+WITH,ORDER\\s+BY,GROUP\\s+BY])",
				Pattern.CASE_INSENSITIVE);
		Matcher match = pattern.matcher(queryString);
		while (match.find()) {
			queryString = match.group(0);
		}
		String[] fileName = queryString.split(" ");

		return fileName[1];
	}

	/*
	 * 
	 * Extract the baseQuery from the query.This method is used to extract the
	 * baseQuery from the query string. BaseQuery contains from the beginning of the
	 * query till the where clause
	 */
	public String getBaseQuery(String queryString) {
		String fileName= getFileName(queryString);
		return queryString.substring(0, queryString.indexOf(fileName)) +fileName;
	}

	/*
	 * extract the order by fields from the query string. Please note that we will
	 * need to extract the field(s) after "order by" clause in the query, if at all
	 * the order by clause exists. For eg: select city,winner,team1,team2 from
	 * data/ipl.csv order by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one order by fields.
	 */
	public List<String> getOrderByFields(String queryString) {
		int index =queryString.indexOf(" order by ");
		if(index==-1) return null;
		String orderByClause= queryString.substring(index+10).trim();
		return Arrays.asList(orderByClause.split(","));
	}

	/*
	 * Extract the group by fields from the query string. Please note that we will
	 * need to extract the field(s) after "group by" clause in the query, if at all
	 * the group by clause exists. For eg: select city,max(win_by_runs) from
	 * data/ipl.csv group by city from the query mentioned above, we need to extract
	 * "city". Please note that we can have more than one group by fields.
	 */
	public List<String> getGroupByFields(String queryString) {
		int index = queryString.indexOf(" group by ");
		if (index == -1)
			return null;
		String[] strs = queryString.substring(index + 10).trim().split(" ");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			if ("order".equals(strs[i])) {
				break;
			}
			sb.append(strs[i].trim()+"#");
		}
		return Arrays.asList(sb.toString().split("#"));
	}


	/*
	 * Extract the selected fields from the query string. Please note that we will
	 * need to extract the field(s) after "select" clause followed by a space from
	 * the query string. For eg: select city,win_by_runs from data/ipl.csv from the
	 * query mentioned above, we need to extract "city" and "win_by_runs". Please
	 * note that we might have a field containing name "from_date" or "from_hrs".
	 * Hence, consider this while parsing.
	 */
	public List<String> getFields(String queryString) {
		StringBuilder sb = new StringBuilder();
		String[] queriesStr = getSplitStrings(queryString);
		for (int i = 0; i < queriesStr.length; i++) {
			if ("from".equals(queriesStr[i])) {
				break;
			}
			if (!"select".equals(queriesStr[i])) {
				sb.append(queriesStr[i]);
			}
		}
		return Arrays.asList(sb.toString().split(",\\s*"));
	}
	

	/*
	 * Extract the conditions from the query string(if exists). for each condition,
	 * we need to capture the following: 1. Name of field 2. condition 3. value
	 * 
	 * For eg: select city,winner,team1,team2,player_of_match from data/ipl.csv
	 * where season >= 2008 or toss_decision != bat
	 * 
	 * here, for the first condition, "season>=2008" we need to capture: 1. Name of
	 * field: season 2. condition: >= 3. value: 2008
	 * 
	 * the query might contain multiple conditions separated by OR/AND operators.
	 * Please consider this while parsing the conditions.
	 * 
	 */	
	
	public List<Restriction> getRestrictions(String queryString) {
		List<Restriction> restrictions=new ArrayList<>();
		int conditionIndex=queryString.indexOf(" where ");
		if(conditionIndex==-1) {
			return null;
		}
		String conditionPartQuery=queryString.substring(conditionIndex+7);
		String[] queriesPart = conditionPartQuery.split(" ");		
		for (int i = 0; i < queriesPart.length; i++) {			
			if (queriesPart[i].equals("and") || queriesPart[i].equals("or")) {
				
			}else if(queriesPart[i].trim().length()>0){			
				if ("group".equals(queriesPart[i].trim()) || "order".equals(queriesPart[i])) {
					break;
				}
				int quoteIndex=queriesPart[i+1].trim().indexOf("'");
				if(quoteIndex != -1) {
					//find the whole word ex: 'Kolkata_knight_riders' and remove the '
					restrictions.add(new Restriction(queriesPart[i].trim(), queriesPart[i+1].trim().substring(quoteIndex).replace("'", ""), queriesPart[i+1].substring(0, quoteIndex)));
					i=i+1;
				}else {
				restrictions.add(new Restriction(queriesPart[i].trim(), queriesPart[i+2].trim(), queriesPart[i+1].trim()));
				i=i+2;
				}
			}
		}
		return restrictions;
	}
	
	

	/*
	 * Extract the logical operators(AND/OR) from the query, if at all it is
	 * present. For eg: select city,winner,team1,team2,player_of_match from
	 * data/ipl.csv where season >= 2008 or toss_decision != bat and city =
	 * bangalore
	 * 
	 * The query mentioned above in the example should return a List of Strings
	 * containing [or,and]
	 */
	public List<String> getLogicalOperators(String queryString) {
		List<String> logicalOperators=new ArrayList<>();
		int conditionIndex=queryString.indexOf(" where ");
		if(conditionIndex==-1) {
			return null;
		}
		String conditionPartQuery=queryString.substring(conditionIndex+7).replaceAll("'", " ");
		String[] queriesPart = conditionPartQuery.split(" ");
		for (int i = 0; i < queriesPart.length; i++) {
			if (queriesPart[i].equals("and") || queriesPart[i].equals("or")) {
				logicalOperators.add(queriesPart[i]);
			}
		}
		return logicalOperators;
	}

	/*
	 * Extract the aggregate functions from the query. The presence of the aggregate
	 * functions can determined if we have either "min" or "max" or "sum" or "count"
	 * or "avg" followed by opening braces"(" after "select" clause in the query
	 * string. in case it is present, then we will have to extract the same. For
	 * each aggregate functions, we need to know the following: 1. type of aggregate
	 * function(min/max/count/sum/avg) 2. field on which the aggregate function is
	 * being applied.
	 * 
	 * Please note that more than one aggregate function can be present in a query.
	 * 
	 * 
	 */
	public List<AggregateFunction> getAggregateFunctions(String queryString) {
		List<AggregateFunction> aggregateFunctions=new ArrayList<>();
		String[] queriesStr = getSplitStrings(queryString);
		for (int i = 0; i < queriesStr.length; i++) {
			if ("from".equals(queriesStr[i])) {
				break;
			}
			if (!"select".equals(queriesStr[i])) {
				String[] fields = queriesStr[i].split(",");
				for (int j = 0; j < fields.length; j++) {
					String field=fields[j].trim();
					if (field.startsWith("sum(") || field.startsWith("count(")
							|| field.startsWith("min(") || field.startsWith("max(")
							|| field.startsWith("avg")) {
						aggregateFunctions.add(
								new AggregateFunction(
										field.substring(field.indexOf("(")+1, field.indexOf(")")),
										field.substring(0, field.indexOf("("))
								));
					}
				}
			}

		}
		if (aggregateFunctions.isEmpty())
			return null;
		return aggregateFunctions;
	}

}
