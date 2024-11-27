# Fantasy Football Algorithm 2.0

## Overview 

This is an improvement on my original Fantasy Football Algorithm. The main difference is that now JSON is only used for very general stats like team schedule and team offense/defense rankings.
I bought a Data Analysis membership to [PlayerProfiler]([url](https://www.playerprofiler.com/)) which allowed me to download a CSV file containing boatloads of player data from every week in the past 5 NFL seasons.
Since the file was locked behind a paywall, I can't share it here, so you'll just have to take my word that the SQL queries do what they're supposed to (I promise, they do).

## Algorithm

I don't know what kind of algorithm this is, but it works by establishing a baseline point value for each position, then adding or subtracting projected points based on the relevant statistics for each player.

## Problems/Optimization

Rookies are probably super messed up or straight up impossible to calculate, since the method to get averages for a lot of statistics automatically searches to a depth of 16 prior weeks. I could probably resolve this by adding a list of every rookie and checking whether the inputted player is in that list. Then, I could change the average calculating method to search to a certain depth in response to said player. Once I finish writing the actual algorithm, that's something to take care of.
