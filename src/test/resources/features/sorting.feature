# src/test/resources/features/sorting.feature   (final: Handout 3 + tags from Handout 4)
@SL-104
Feature: Sort the products
  Shoppers can sort the products by name or by price.

  Background:
    Given I am on the login page
    And I have logged in with username "standard_user" and password "secret_sauce"

  Scenario Outline: Sorting by <order>
    When I sort the products by "<order>"
    Then the first product should be "<first>"

    Examples: Sorting by name
      | order         | first                             |
      | Name (A to Z) | Sauce Labs Backpack               |
      | Name (Z to A) | Test.allTheThings() T-Shirt (Red) |

    @smoke
    Examples: Sorting by price
      | order               | first                    |
      | Price (low to high) | Sauce Labs Onesie        |
      | Price (high to low) | Sauce Labs Fleece Jacket |