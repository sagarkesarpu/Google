Tools Used:

1. BDD framework: Cucumber
2. Dependency Build Tool: Maven 3. Test Framework: JUnit
4. API library: Rest Assured
5. UI :Selenium
6. Operating System: Mac OS'

Test:
Hit the https://maps.googleapis.com with my API Key for the origins, destinations, mode provided in the example of the email
Automated maps.google.com for the same origins, destinations, mode used above and picked up the first result
Validated/Asserted the API response to the UI Points to be noted :
Since the value from UI response for minutes was min, I appended “s” to the string in order to compare with the response from API
The value of distance from API response was mi, I appended “les” to the string in order to compare with the response from UI
