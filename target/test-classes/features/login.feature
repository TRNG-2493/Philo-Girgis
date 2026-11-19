Feature: Log in to Swag Labs
  Shoppers must log in before they can see the products.
  Only known, unlocked users with the shared password get in.

  Scenario Outline: <username> logs in
    Given I am on the login page
    When I log in with username "<username>" and password "secret_sauce"
    Then I should see the "Products" page

    Examples:
      | username                |
      | standard_user           |
      | problem_user            |
      | performance_glitch_user |
      | visual_user             |

  Scenario Outline: Login is refused when <reason>
    Given I am on the login page
    When I log in with username "<username>" and password "<password>"
    Then I should see the error "<error>"

    Examples:
      | reason                  | username        | password     | error                                                                     |
      | the user is locked out  | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out.                       |
      | the password is wrong   | standard_user   | wrong_sauce  | Epic sadface: Username and password do not match any user in this service |
      | the username is missing |                 | secret_sauce | Epic sadface: Username is required                                        |
      | the password is missing | standard_user   |              | Epic sadface: Password is required                                        |

  Scenario: Logged-in user logs out
    Given I am on the login page
    And I have logged in with username "standard_user" and password "secret_sauce"
    When I log out
    Then I should be back on the login page