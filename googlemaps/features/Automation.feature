#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@tag
Feature: Test google maps api



 Scenario: test distance and time between origin and destination   
    Given the apis are up and running for "https://maps.googleapis.com/maps/api/directions/json"
    When flow parameters
    |origin                  | destination            |mode			|
	  |San Francisco, CA 94102 | San Francisco, CA 94104|driving	|
		|New York, NY 10037      | New York, NY 10036     |driving	|
    Then the status should be OK

  Scenario Outline: automating google maps UI
  
  	Given open chrome and  launch google maps "https://www.google.com/maps"
  	Then verify that I entered destination as <destination>
  When verify that I entered origin as <origin>
  	
  	Examples: This is test data
    |origin                  | destination            |
	  |"San Francisco, CA 94102" | "San Francisco, CA 94104"|
		|"New York, NY 10037"   | "New York, NY 10036"   |
    
    
    
    
   
  


  