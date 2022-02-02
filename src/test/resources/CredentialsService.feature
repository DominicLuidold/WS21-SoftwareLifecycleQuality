Feature: CredentialsService
  Background:
    Given A login task for the CredentialsService exists and runs

  Scenario: Correct username and password
    When I enter the username "admin"
    And I enter the password "admin"
    Then I have finished the task

  Scenario: Unknown username
    When I enter the username "abc"
    And I enter the password "abc"
    Then I have to decide if I want to do the task again

  Scenario: Correct username but incorrect password
    When I enter the username "admin"
    And I enter the password "abc"
    Then I have to decide if I want to do the task again