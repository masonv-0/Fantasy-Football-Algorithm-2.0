## Fantasy Football Algorithm 2.0

This is my new and improved algorithm to predict a player's fantasy performance in a given week of the 2024 NFL season. It uses SQL to extract data from a database containing full gamelogs from 2023 and 2024. Because these gamelogs were locked behind a paywall, I can't share them here. If you're a college or a business representative, you'll just have to take my word that the SQL queries do what they're supposed to (I promise, they do).

I had a good time learning SQL and how to connect it to Java. There are still some things I could improve about the algorithm, such as the way it handles players who were previously injured for an extended time, but I plan to move on from this project for the time being.

I learned how to connect to MySQL from this YouTube video: https://www.youtube.com/watch?v=azsXYnkhBuk

I learned how to read JSON files from this YouTube video: https://www.youtube.com/watch?v=yLf2-r8w9lQ&t=443s

All players are evaluated based on past fantasy performance, but other stats are used in the prediction of different positions, as detailed below.

## QBs

- Rushing performance
- Passing performance
- Hurries and sacks
- Interceptable passes
- Red zone opportunity
- Opposing pass defense
- Offensive pass rank

## RBs

- Rushing performance
- Receiving performance
- Red zone opportunity
- Opposing run defense
- Offensive run rank

## WRs and TEs

- Early down work
- Touchdown targets
- Catchable targets
- Average depth of target
- Target share
- Slot work
- Opposing pass defense
- Offensive pass rank


