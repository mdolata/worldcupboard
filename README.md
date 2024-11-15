# worldcupboard
This is a simple library to collect and return data about sport event.

# Key requirements
- keep it simple
- in-memory store
- focus on quality

# API contract:
 - create a game
   - receives
     - home team 
     - away team
   - returns
     - game id?
 - finish a game
   - receives
     - game id?
   - returns
     - result of the operation (boolean/enum)
     - final score
 - add event
   - receives
     - game id?
     - home team score
     - away team score
   - returns
     - result of the operation (boolean/enum)
 - get summary
   - returns
     - game id?
     - list of games
     - order by total score then by creation time

# Possible errors by operation
## Create a game
 - a game already exists
   - library returns false as result and gameId of the exists game

## Finish a game
 - a game already finished
 - a game not exists

## Update score
 - a game not exists

# Assumptions
1. I assume that fire `Finish a game` operation will remove the game from the system. The game won't show on `get summary` operation
2. For returning errors I see two options: vavr.Either or Java's exceptions. Because it's a library I'd prefer to use vanilla Java because library's clients will be forced to use external and additional library
   1. Another option is to use simple result structures
3. The matches are short live objects and updates are simply events - I consider to implement CQRS pattern 

4. I'm not adding multi thread support having in mind the solution should be simple
5. I assume events can be sent in the wrong order, it should not be a problem for summary queries
6. I decided to use UUID as game ids because it's a standard. Simpler version could be a simple string concatenation of team's names.
7. I intentionally don't put logs, as it's supposed to be a simple library
