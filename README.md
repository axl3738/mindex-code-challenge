# Mindex-code-challenge

# How to run
1. cd into `mindex-java-code-challenge`
2. run `gradlew bootRun`

# How to run tests
1. cd into `mindex-java-code-challenge`
2. run `gradlew test`

# Features Added
- A new endpoint to return all employees currently in the database for easy access.
- A new type `ReportingStructure` that uses recursion to cycle through all employees' direct reports and return the total number of reports for a specified employee. This
  comes with a new endpoint `/report/{id}` where the id is the employee's id.
- A new type `Compensation` that consists of an employee, salary and effectiveDate field. This comes with two new endpoints: `/compensation` and `/compensation/{id}` where the
  id is the employee's id.
- New tests to test the requests associated with the `ReportingStucture` and `Compensation` types. 
