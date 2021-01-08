Feature: booking facade scenarios

  Scenario Outline: Create user
    Given Provide "<id>" and "<username>"
    When Need to create user
    Then Check saved user
    Examples:
      | id | username |
      | 3  | user_3   |

  Scenario Outline: Create event
    Given Provide event "<id>" and event "<name>"
    When Need to create event
    Then Check saved event
    Examples:
      | id | name    |
      | 3  | event_3 |

  Scenario Outline: Create ticket for event
    Given Provide ticket "<id>" and event with id "<event_id>" and name "<event_name>"
    When Need to create ticket
    Then Check saved ticket
    Examples:
      | id | event_id | event_name |
      | 5  | 1        | event1    |

  Scenario Outline: Book ticket
    Given Provide user "<user_id>" and ticket "<ticket_id>"
    When Need to book ticket
    Then Book the ticket "<ticket_id>" successfully
    Examples:
      | user_id | ticket_id |
      | 1       | 1         |

  Scenario Outline: Cancel the booking of ticket
    Given Provide user "<user_id>" and booked ticket "<ticket_id>" for provided user
    When Need to cancel the booking
    Then Cancel the booking of ticket "<ticket_id>" successfully
    Examples:
      | user_id | ticket_id |
      | 2       | 2         |