Feature: Create article draft feature
  As an editor
  I want to create a draft article that is not user-facing
  so I can work on it and eventually publish it on the news portlet.

#  TODO support versioning of articles

  Scenario: Create draft article
    Given the editor "editor" is singed-in
    And the editor has no draft articles

    When I create article
    And enter title of "Draft article"
    And enter description of "Some description"
    And choose mime type of "text/html"
    And enter content of "1st version of content."
    And I submit the article

    Then I can find the article by title
    And the article is of status "DRAFT"
    And has no comments

  Scenario Edit draft article
    Given the draft article titled "Draft article"
    When I change the title to "Draft article refined"
    Then I can find the article by title
    And the article is of status "DRAFT"
    And has no comments

  Scenario: Publish draft article
    Given the draft article titled "Draft article"
    When: I change article status to "PUBLISHED"
    Then: I can find the article on the list of published articles
    And: I can display the rendered article (as a user???)

#  Scenario: Fetch all draft articles of author

#  Scenario: Fetch all published articles of author
