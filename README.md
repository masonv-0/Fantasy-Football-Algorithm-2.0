## Fantasy Football Algorithm 2.0

This algorithm is a significant improvement over its predecessor. Instead of manually entering player stats into a JSON file, I can now pull them from a SQL database of gamelogs from every week of the past two seasons. Team stats (passing/rushing offense/defense, schedules) are still in the JSON, but that's something I can live with for the time being. As of writing this, a basic version of the algorithm is working for every position. 

Because I paid for access to the CSV files containing the gamelogs, I haven't included them anywhere in this repository. If you're a college or future employer reading this, you'll just have to take my word that the SQL queries work as intended (which they do, besides rookies, which I'm working on a solution for).

A breakdown of the algorithm by position is given below.

## All players

Past 16 weeks of any stat is taken and averaged by the method getStatFromPastWeeks. It has contingencies built in for BYE weeks, and will go back to the previous season in order to reach the full 16 week requirement. A lot can change in 16 weeks, so I am planning to add a depth checker to account for situations where a player might have improved significantly in the past 4-8 weeks, or if they're a rookie. This should help the algorithm's accuracy with players like Mark Andrews, who started the 2024 season playing like an 85 year old man (thanks, Mark) but has recently stepped back up to his previous production. 

## QBs

- Baseline point value of 15.0, which I plan to eliminate eventually
- Past FPTS performance
- Rush yards
- Rushing TDs
- Hurries allowed by O-Line
- Sacks taken
- Interceptable passes
- Red zone completions
- Red zone carries
- Opposing pass defense
- Passing offense

## RBs

- Baseline point value of 5.0, which I plan to eliminate eventually
- Past FPTs performance
- Whether the player is a receiving back
- Early down usage (1st/2nd down touches)
- Targets
- Total touches
- Snap share
- Red zone opportunity (carries + targets)
- Goal line work
- Opposing run defense
- Rushing offense

## WRs

- Past FPTs performance
- Early down targets (1st/2nd down)
- Touchdown targets (red zone + end zone + goal line)
- Average depth of target + catchable targets (multiplied together to predict receiving yards)
- Catchable targets (added in the case of PPR leagues, like mine)
- Target share
- Slot snaps
- Routes run
- Opposing pass defense
- Passing offense

## TEs

- Past FPTs performance
- Early down targets (1st/2nd down)
- Touchdown targets (red zone + end zone + goal line)
- Average depth of target + catchable targets (multiplied together to predict receiving yards)
- Catchable targets (added in the case of PPR leagues, like mine)
- Target share
- Slot snaps
- Routes run
- Opposing pass defense
- Passing offense

