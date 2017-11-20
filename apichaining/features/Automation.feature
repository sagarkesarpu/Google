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
Feature: Test google maps get,add, delete requests using API Chaining

 Scenario: get latitude and longitude from google apis get method  
    Given the get apis url is "https://maps.googleapis.com/maps/api/directions/json"
    When get parameters
    |origin                  | destination            |mode			|
	  |San Francisco, CA 94102 | San Francisco, CA 94104|driving	|
		|New York, NY 10037      | New York, NY 10036     |driving	|
    Then the getresponse should be OK

Scenario: post using lat,long,name and types and add a place
    Given the post apis url is "https://maps.googleapis.com/maps/api/place/add/json"
    When post parameters
    |name                  | types      |      
	  |gallery               |["art_gallery"] |
    Then the postresponse should be OK
    
    
Scenario: post place_id to delete a place
    Given delete the place_id from the api add a place response"//maps.googleapis.com/maps/api/place/delete/json"
    When post parameters place_id
    Then the response status should be OK

    
    
    
    
   
  


  