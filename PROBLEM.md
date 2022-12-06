### Problem Statement

In our first assignment we extracted different parts of the query and just displayed.
In this assignment we should store the query parameters/parts. We should think where to store these parameters.
To do this we have to create a separate class with properties and methods.
The class name should be meaningful like QueryParameter. In this QueryParameter class we need to add properties so that we store various parts of the query string which we splitted in our earlier assignment.

## Our task 1 involves four steps, given below:

 1)Extract only base part(before `where` word) of the query from the given query   string and store it in a String

		Input String : select * from ipl.csv where season > 2014 and city = 'Bangalore'
		Output String : select * from ipl.csv  
 
 2)Extract only file name from the query string and store it in String
	
		Input String : select * from ipl.csv where season > 2014 and city = 'Bangalore'
		Output String : ipl.csv
		
3)Extract the order by field from the given string and store it in a List of type String.

         Input String : select * from ipl.csv where season > 2016 and city=      						'Bangalore' order by win_by_runs
	     Output String : win_by_runs	
	     
4)Extract the group by field from the given string and store it in a List of type String. 

         Input String : select team1, city from ipl.csv where season > 2016 and 	                     city='Bangalore' group by team1
		 Output String : team1	  
		    
	 
After all the logic are written for these methods run DataMungerTestTask1.java file. 
----------------------------------------------------------------------   

## Our task 2 involves two steps, given below: 

1)Extract the selected fields/information from the given query and store it in a List of type String.

     Input String : select city,winner,player_match from ipl.csv where season > 2014 
                    and city = 'Bangalore'
	Output String :	city
            			winner
            			player_match
            			
2)Extract the aggregrate functions and fields and store it in a List of type Aggregrate.

        	Input String : select avg(win_by_wickets),min(win_by_runs) from ipl.csv 
	     Output String : 
		                 avg(win_by_wickets)
                          min(win_by_runs)
                          
                          
  	 After all the logic are written for these methods run DataMungerTestTask2.java file. 
----------------------------------------------------------------------           			
## Our task 3 involves two steps, given below:    

1)Extract multiple conditions, from the given query String and store it in List of type Restriction.
	    
	    Input String : select * from ipl.csv where season > 2014 and city = 'Bangalore'
		Output String : season > 2014 
                         city ='bangalore'     
                         
2)Extract the logical operators in sequence from the given query string and   store it in List of type  String.
  Note: Logical operators are "and, or, not"
  
         Input String : select season,winner,player_match from ipl.csv where 	                season >2014 and city ='Bangalore' or date > '31-12-2014'
	    Output String : 
		               and
		               or  
		           
After all the logic are written for these methods run DataMungerTestTask3.java file. 
----------------------------------------------------------------------  	
At the end of all the methods Implementation done run DataMungertest.java file.  	   	
-------------------------------------------------------------------------------		
            			
            			
            			
            			
            			
            			
            			
            			
            			
            			
            			
            			
            			
            			