import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.Scanner;
import org.json.simple.parser.JSONParser;
import com.mysql.cj.protocol.Resultset;
import org.json.simple.*;

public class Main {

    /*
    
    I learned how to connect to MySQL from this YouTube video: https://www.youtube.com/watch?v=azsXYnkhBuk

    I learned how to read JSON files from this YouTube video: https://www.youtube.com/watch?v=yLf2-r8w9lQ&t=443s

    Due to issues importing the CSV file into MySQL, I had to make every element of the table a string, so now I have to parse doubles/ints for many statistics

    */
    public static String getJSONFromFile(String filename) {
        String jsonText = "";
        try {
            BufferedReader jsonReader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = jsonReader.readLine()) != null) {
                jsonText += line + "\n";
            }

            jsonReader.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonText;
    }

    //REWORK THIS to add a depth counter, use that to figure out rookies
    public static double getStatFromPastWeeks(String statName, String playerName, int currentWeek) {
        double statistic = 0;

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/advanced_gamelogs", "root", "fantasyfootball");
            Statement statement = con.createStatement();
            ResultSet rs;
            
            //Find BYE week
            int byeWeek2023 = 0;
            ResultSet weeksPlayed2023 = statement.executeQuery
            ("SELECT week FROM advanced_gamelogs.gamelog2023 WHERE (name = '" + playerName + "')");
            for (int i = 1;i < 16;i++) {
                weeksPlayed2023.next();
                if (Integer.parseInt(weeksPlayed2023.getString("week")) != i) {
                    byeWeek2023 = i;
                    break;
                }
            }

            int weeksRemaining = 17;

            for (int i = (currentWeek - 1);i > 0;i--) {
                rs = statement.executeQuery
                ("SELECT " + statName + " FROM advanced_gamelogs.gamelog2024 WHERE (name = '" + playerName + "' && week = " + i + ") && " + statName + " IS NOT NULL");
                if (rs.next()) {
                    if (! rs.getString(statName).equals("")) {
                        statistic += Double.parseDouble(rs.getString(statName));
                    }
                }
                weeksRemaining--;
            }

            //If a player has a BYE in the range of weeks that we grab from, we'd want to extend the query an additional week to make sure we still have 16 data points
            int stoppingPoint = 17 - weeksRemaining;
            if (17 > byeWeek2023 && stoppingPoint <= byeWeek2023) {
                stoppingPoint--;
            }

            for (int i = 17;i > stoppingPoint;i--) {
                rs = statement.executeQuery
                ("SELECT " + statName + " FROM advanced_gamelogs.gamelog2023 WHERE (name = '" + playerName + "' && week = " + i + ") && " + statName + " IS NOT NULL");
                if (rs.next()) {
                    if (! rs.getString(statName).equals("")) {
                        statistic += Double.parseDouble(rs.getString(statName));
                    }
                }
            }
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return statistic/16;
    }
    public static void main(String[] args){
        
        try {
            String teamJSON = getJSONFromFile("Teams.json");
            JSONParser parser = new JSONParser();
            Object object = parser.parse(teamJSON);
            JSONObject mainJSONObject = (JSONObject) object;

            Scanner intScan = new Scanner(System.in);
            Scanner stringScan = new Scanner(System.in);
            
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/advanced_gamelogs", "root", "fantasyfootball");
            System.out.println("Connection established");

            System.out.println("What week of the NFL season is it?");
            int currentWeek = intScan.nextInt();

            System.out.println("Note: If your player's name contains an apostrophe, type the apostrophe twice");
            System.out.println("What is the name of your player?");
            String playerName = stringScan.nextLine();

            Statement statement = con.createStatement();

            String position = "";
            String teamName = "";



            //Grabbing, setting up basic info about the player
            ResultSet basicInfo = statement.executeQuery
            ("SELECT position, team FROM advanced_gamelogs.gamelog2024 WHERE name = '" + playerName + "' LIMIT 1");
            if (basicInfo.next()) {
                position = basicInfo.getString("position");
                teamName = basicInfo.getString("team");
            }

            JSONObject teamLevel = (JSONObject) mainJSONObject.get(teamName);
            JSONArray teamScheduleArray = (JSONArray) teamLevel.get("schedule");
            String currentOpponent = (String) (teamScheduleArray.get(currentWeek-1));
            if (currentOpponent.equals("BYE")) {
                System.out.println("That player is on their bye week!");
                System.exit(0);
            }

            JSONObject opponentLevel = (JSONObject) mainJSONObject.get(currentOpponent);
            int opposingDefensePassRank = (int) (long) opponentLevel.get("DefensivePassRank");
            int opposingDefenseRunRank = (int) (long) opponentLevel.get("DefensiveRunRank");

            double avgFPTs = getStatFromPastWeeks("fantasy_points", playerName, currentWeek);

            ResultSet positionalData;
            double[] positionalAverages;
            
            
            
            //Quarterbacks
            if (position.equals("QB")) {
                int offensivePassRank = (int) (long) teamLevel.get("OffensivePassRank");
                positionalAverages = new double[8];

                positionalAverages[0] = getStatFromPastWeeks("fantasy_points", playerName, currentWeek);
                positionalAverages[1] = getStatFromPastWeeks("rushing_yards", playerName, currentWeek);
                positionalAverages[2] = getStatFromPastWeeks("rushing_touchdowns", playerName, currentWeek);
                positionalAverages[3] = getStatFromPastWeeks("oline_hurries", playerName, currentWeek);
                positionalAverages[4] = getStatFromPastWeeks("sacks_taken", playerName, currentWeek);
                positionalAverages[5] = getStatFromPastWeeks("pass_attempts", playerName, currentWeek);
                positionalAverages[5] = getStatFromPastWeeks("interceptable_passes", playerName, currentWeek);
                positionalAverages[6] = getStatFromPastWeeks("red_zone_completions", playerName, currentWeek);
                positionalAverages[7] = getStatFromPastWeeks("red_zone_carries", playerName, currentWeek);

                Quarterback player = new Quarterback(playerName, currentOpponent, currentWeek, opposingDefensePassRank, offensivePassRank, positionalAverages);
                player.printAll();
            }



            //Running backs
            if (position.equals("RB")) {
                int offensiveRunRank = (int) (long) teamLevel.get("OffensivePassRank");
                positionalAverages = new double[12];

                double receptionsDifference = 
                getStatFromPastWeeks("receptions", playerName, currentWeek) - getStatFromPastWeeks("carries", playerName, currentWeek);
                
                double earlyDownUsage = 
                getStatFromPastWeeks("first_down_targets", playerName, currentWeek) + 
                getStatFromPastWeeks("second_down_targets", playerName, currentWeek) + 
                getStatFromPastWeeks("first_down_carries", playerName, currentWeek) + 
                getStatFromPastWeeks("second_down_carries", playerName, currentWeek);

                positionalAverages[0] = getStatFromPastWeeks("fantasy_points", playerName, currentWeek);
                positionalAverages[1] = receptionsDifference;
                positionalAverages[2] = earlyDownUsage;
                positionalAverages[3] = getStatFromPastWeeks("targets", playerName, currentWeek);
                positionalAverages[4] = getStatFromPastWeeks("total_touches", playerName, currentWeek);
                positionalAverages[5] = getStatFromPastWeeks("snap_share", playerName, currentWeek);
                positionalAverages[6] = getStatFromPastWeeks("red_zone_snaps", playerName, currentWeek);
                positionalAverages[7] = getStatFromPastWeeks("defenders_in_box_run_snaps", playerName, currentWeek);
                positionalAverages[8] = getStatFromPastWeeks("defenders_in_box_red_zone_snaps", playerName, currentWeek);
                positionalAverages[9] = getStatFromPastWeeks("red_zone_carries", playerName, currentWeek);
                positionalAverages[10] = getStatFromPastWeeks("red_zone_targets", playerName, currentWeek);
                positionalAverages[11] = getStatFromPastWeeks("goal_line_carries", playerName, currentWeek);
                 

                RunningBack player = new RunningBack(teamName, currentOpponent, currentWeek, opposingDefenseRunRank, offensiveRunRank, positionalAverages);
                player.printAll();
            }



            //Wide Receivers and Tight Ends
            if (position.equals("WR") || position.equals("TE")) {
                int offensivePassRank = (int) (long) teamLevel.get("OffensivePassRank");
                positionalAverages = new double[10];

                double earlyDownTargets = 
                getStatFromPastWeeks("first_down_targets", playerName, currentWeek);
                getStatFromPastWeeks("second_down_targets", playerName, currentWeek);

                double touchdownTargets = 
                getStatFromPastWeeks("red_zone_targets", playerName, currentWeek) + 
                getStatFromPastWeeks("goal_line_targets", playerName, currentWeek) +
                getStatFromPastWeeks("end_zone_targets", playerName, currentWeek);

                positionalAverages[0] = getStatFromPastWeeks("fantasy_points", playerName, currentWeek);
                positionalAverages[1] = earlyDownTargets;
                positionalAverages[2] = touchdownTargets;
                positionalAverages[3] = getStatFromPastWeeks("targets", playerName, currentWeek);
                positionalAverages[4] = getStatFromPastWeeks("target_share", playerName, currentWeek);
                positionalAverages[5] = getStatFromPastWeeks("air_yards", playerName, currentWeek);
                positionalAverages[6] = getStatFromPastWeeks("slot_snaps", playerName, currentWeek);
                positionalAverages[7] = getStatFromPastWeeks("catchable_targets", playerName, currentWeek);
                positionalAverages[8] = getStatFromPastWeeks("contested_targets", playerName, currentWeek);
                positionalAverages[9] = getStatFromPastWeeks("routes_run", playerName, currentWeek);

                if (position.equals("WR")) {
                    WideReceiver player = new WideReceiver(playerName, currentOpponent, currentWeek, opposingDefensePassRank, offensivePassRank, positionalAverages);
                    player.printAll();
                }
                else {
                    TightEnd player = new TightEnd(playerName, currentOpponent, currentWeek, opposingDefensePassRank, offensivePassRank, positionalAverages);
                    player.printAll();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

