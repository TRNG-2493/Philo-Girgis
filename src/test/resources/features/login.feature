# src/test/resources/features/login.feature   (final: version 3.2 + tags from Handout 4)
@SL-101
Feature: Log in to Swag Labs
  Shoppers must log in before they can see the products.
  Only known, unlocked users with the shared password get in.

  Background:
    Given I am on the login page

  Rule: Known, unlocked users see the products

    Scenario Outline: <username> logs in
      When I log in with username "<username>" and password "secret_sauce"
      Then I should see the "Products" page

      Examples:
        | username                |
        | standard_user           |
        | problem_user            |
        | performance_glitch_user |
        | visual_user             |

  Rule: Everyone else is refused with a message

    Scenario Outline: Login is refused when <reason>
      When I log in with username "<username>" and password "<password>"
      Then I should see the error "<error>"

      Examples:
        | reason                  | username        | password     | error                                                                     |
        | the user is locked out  | locked_out_user | secret_sauce | Epic sadface: Sorry, this user has been locked out.                       |
        | the password is wrong   | standard_user   | wrong_sauce  | Epic sadface: Username and password do not match any user in this service |
        | the username is missing |                 | secret_sauce | Epic sadface: Username is required                                        |
        | the password is missing | standard_user   |              | Epic sadface: Password is required                                        |

    @wip
    Scenario: Account is locked after three wrong passwords
      When I log in 3 times with username "standard_user" and a wrong password
      Then I should see the error "Epic sadface: Your account is locked"

  @SL-102
  Rule: A logged-in user can log out

    @smoke
    Scenario: Logged-in user logs out
      Given I have logged in with username "standard_user" and password "secret_sauce"
      When I log out
      Then I should be back on the login page