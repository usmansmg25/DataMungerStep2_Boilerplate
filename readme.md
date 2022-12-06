## Seed code - Boilerplate for step 2 - Database Engine Assignment

### Problem Statement

In our first assignment we extracted different parts of the query and **just displayed**. 

In this assignment we should store the query parameters/parts. We should think where to store these parameters.  

To do this we have to create a separate class with properties and methods.

The class name should be meaningful like **QueryParameter**. In this **QueryParameter** class we need to add properties so that we store various parts of 
the query string which we splitted in our earlier assignment.

## How to identify the properties of this QueryParameter?
It is based on what parts of the query string we displayed/printed in the previous assignment.

    i.e.,
    - queryString
    - file
    - baseQuery
    - restrictions
    - fields
    - QUERY_TYPE
    - where condition / filter
    - logicalOperators
    - aggregateFunctions
    - relational operators
    - order by field
    - group by field
    - aggregate fields

For all these properties we need to identify proper data type.  Let us decide now.
- queryString -> String
- file  -> String
- baseQuery -> String
- restrictions  -> List<Restriction>
- fields -> List<String>
- QUERY_TYPE -> String
- where condition /filter ->  will decide ( it is not String type)
- logicalOperators -> List<String>  ( because we are fetching only logical operators.  There may be more than one logical operators in the given query string)
- aggregateFunctions -> List<AggregateFunction>
- orderByField -> List<String>  (if we need to sort based on only one field.)
- groupByField -> List<String> (if we need to sort based on only one field.)
- aggregate fields -> will decide ( it is not String type)


where condition/ filter part
---------------------------
 In previous assignment-I we have done the following (just printed on the console)
 
    Input String  : **select * from ipl.csv where season > 2014 and city ='Bangalore';**
    Output String : 
                    Condition 1 : 
                        variable : season
                        operator : > 
                        value    : 2014 
                    Condition 2 : 
                        variable : city
                        operator : =
                        value    : 'bangalore'
 
 These values should be captured in the proper data type.  We need to store variable, operator and value. To store/capture these 3 values we need a separate class.
 
 The class name can be Condition OR Restriction OR FilterCondition OR Criteria etc.,  
 
 Let us consider it as Restriction class
 
 In this class we need 3 parameters of String type.
 - propertyName : String
 - propertyValue : String
 - condition : String  ( Assume that all the values are String type.  Whenever required, we can convert into other datatype easily.)
 Also we need to add toString method and parameterized constructor
 
Aggregate Fields
------------------
In previous assignment-I we have done the following (just printed on the console)

User may require the information like who is getting maximum salary or minimum age etc., these are called aggregate functions (minimum, maximum, count, average, sum)

    Input String : **select avg(win_by_wickets),min(win_by_runs) from ipl.csv;** 
    Output String : 
            Aggregate 1
                Aggregate Name  : avg
                Aggregate Field : win_by_wickets

            Aggregate 2
                Aggregate Name  : min
                Aggregate Field : win_by_runs

These values should be captured in the proper data type.  We need to store **aggregate name, aggregate value**.
Totally we need 2 properties.
 
Create a separate class called **AggregateFunction** and add the following fields along with setter/getter methods also add toString method and parameterized constructor

Class : **AggregateFunction**
   
    Properties : 
        field : String
        function : String
        
    
The final QueryParameter class consist of
-----------------------------------------
    - queryString : String
    - file  : String
    - baseQuery -> String
    - fields -> List<String>
    - QUERY_TYPE -> String
    - restrictions : List<Restriction>   ( Array/group of restrictions.  A query may have multiple restrictions/filter conditions)
    - logicalOperators : List<String>
    - aggregateFunctions -> List<AggregateFunction>
    - orderByFields : List<String>
    - groupByFields  :  List<String>

Where we parse the query string build **QueryParameter** object? The parsing logic should not be in **QueryParameter** class. This class should consist of just properties and getter/setter methods only.

The parsing should be done in separate class. The class name can be like **QueryParser**. In this class write a method which takes query string as input and return **QueryParameter** object.

The complete method signature looks like

    public QueryParameter parseQuery(String queryString)
    {

        //parse the query string
        //construct the QueryParameter

        //return Query Parameter object

}


### Expected solution
Return proper query parameter object

### Project structure

The folders and files you see in this repositories, is how it is expected to be in projects, which are submitted for automated evaluation by Hobbes

	Project
	|	
	├── com.stackroute.datamunger.query.parser
	|		└── AggregateFunction.java          // This class is used to store Aggregate Function
	|		└── QueryParameter.java             // This class contains the parameters and accessor/mutator methods of QueryParameter
	|		└── QueryParser.java                // This class will parse the queryString and return an object of QueryParameter class
	|		└── Restriction.java                // This class is for storing Restriction object
	├── com.stackroute.datamunger.test         // all your test cases will be stored in this package
    |       └── DataMungerTest.java  
    		   └── DataMungerTestTask1.java 
    		   └── DataMungerTestTask2.java 
    		   └── DataMungerTestTask3.java 
             // all your test cases are written using JUnit, these test cases can be run by selecting Run As -> JUnit Test 
	|
	├── .classpath			                // This file is generated automatically while creating the project in eclipse
	|
	├── .hobbes   			                // Hobbes specific config options, such as type of evaluation schema, type of tech stack etc., Have saved a default values for convenience
	|
	├── .project			                    // This is automatically generated by eclipse, if this file is removed your eclipse will not recognize this as your eclipse project. 
	|
	├── pom.xml 			                    // This is a default file generated by maven, if this file is removed your project will not get recognised in hobbes.
	|
	└── PROBLEM.md  		                    // This files describes the problem of the assignment/project, you can provide as much as information and clarification you want about the project in this file

> PS: All lint rule files are by default copied during the evaluation process, however if need to be customizing, you should copy from this repo and modify in your project repo


#### To use this as a boilerplate for your new project, you can follow these steps

1. Clone the base boilerplate in the folder **assignment-solution-step2** of your local machine
     
    `git clone https://gitlab-cts.stackroute.in/stack_java_datamunging/DataMungerStep2_Boilerplate.git assignment-solution-step2`

2. Navigate to assignment-solution-step2 folder

    `cd assignment-solution-step2`

3. Remove its remote or original reference

     `git remote rm origin`

4. Create a new repo in gitlab named `assignment-solution-step2` as private repo

5. Add your new repository reference as remote

     `git remote add origin https://gitlab-cts.stackroute.in/{{yourusername}}/assignment-solution-step2.git`

     **Note: {{yourusername}} should be replaced by your username from gitlab**

5. Check the status of your repo 
     
     `git status`

6. Use the following command to update the index using the current content found in the working tree, to prepare the content staged for the next commit.

     `git add .`
 
7. Commit and Push the project to git

     `git commit -a -m "Initial commit | or place your comments according to your need"`

     `git push -u origin master`

8. Check on the git repo online, if the files have been pushed


### Important instructions for Participants
> - We expect you to write the assignment on your own by following through the guidelines, learning plan, and the practice exercises
> - The code must not be plagirized, the mentors will randomly pick the submissions and may ask you to explain the solution
> - The code must be properly indented, code structure maintained as per the boilerplate and properly commented
> - Follow through the problem statement shared with you


## MENTORS TO BEGIN REVIEW YOUR WORK ONLY AFTER ->

- You add the respective Mentor as a Reporter/Master into your Assignment Repository
- You have checked your Assignment on the Automated Evaluation Tool - Hobbes (Check for necessary steps in your Boilerplate - README.md file. ) and got the required score - Check with your mentor about the Score you must achieve before it is accepted for Manual Submission. 
- Intimate your Mentor on Slack and/or Send an Email to learner.support@stackroute.in - with your Git URL 
- Once you done working and is ready for final submission.

### Further Instructions on Release

*** Release 0.1.0 ***

- Right click on the Assignment select Run As -> Java Application to run your Assignment.
- Right click on the Assignment select Run As -> JUnit Test to run your Assignment.