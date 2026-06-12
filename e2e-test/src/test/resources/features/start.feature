Feature: Compound interest calculation
  As a user of the interest calculator
  I want to enter principal, rate and years
  So that I can see the calculated compound interest

  Scenario: Calculate compound interest with valid inputs
    Given the calculator page is open
    When I enter principal "1000"
    And I enter rate 0.04
    And I enter frequency 1
    And I enter monthtly deposit 0
    And I enter years 1
    And I submit the form
    Then I should see the final amount "1,040.00"
