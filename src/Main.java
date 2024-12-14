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

    public static double getStatFromPastWeeks(String statName, String playerName, int currentWeek, int depth) {
        double statistic = 0;

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/advanced_gamelogs", "root", "fantasyfootball");
            Statement statement = con.createStatement();
            ResultSet rs;

            int weeksRemaining = depth;

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

            int stoppingPoint = depth - weeksRemaining;

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

            double avgFPTs = getStatFromPastWeeks("fantasy_points", playerName, currentWeek, 16);

            double[] positionalAverages;
            
            
            
            //Quarterbacks
            if (position.equals("QB")) {
                int offensivePassRank = (int) (long) teamLevel.get("OffensivePassRank");
                positionalAverages = new double[10];

                positionalAverages[0] = getStatFromPastWeeks("rushing_yards", playerName, currentWeek, 12);
                positionalAverages[1] = getStatFromPastWeeks("rushing_touchdowns", playerName, currentWeek, 12);
                positionalAverages[2] = getStatFromPastWeeks("passing_yards", playerName, currentWeek, 12);
                positionalAverages[3] = getStatFromPastWeeks("passing_touchdowns", playerName, currentWeek, 12);
                positionalAverages[4] = getStatFromPastWeeks("oline_hurries", playerName, currentWeek, 12);
                positionalAverages[5] = getStatFromPastWeeks("sacks_taken", playerName, currentWeek, 12);
                positionalAverages[6] = getStatFromPastWeeks("interceptable_passes", playerName, currentWeek, 12);
                positionalAverages[7] = getStatFromPastWeeks("red_zone_completions", playerName, currentWeek, 12);
                positionalAverages[8] = getStatFromPastWeeks("red_zone_carries", playerName, currentWeek, 12);

                Quarterback player = new Quarterback(playerName, currentOpponent, currentWeek, opposingDefensePassRank, offensivePassRank, avgFPTs, positionalAverages);
                player.calculatePoints();
            }



            //Running backs
            else if (position.equals("RB")) {
                int offensiveRunRank = (int) (long) teamLevel.get("OffensivePassRank");
                positionalAverages = new double[8];
                
                double earlyDownUsage = 
                getStatFromPastWeeks("first_down_targets", playerName, currentWeek, 8) + 
                getStatFromPastWeeks("second_down_targets", playerName, currentWeek, 8) + 
                getStatFromPastWeeks("first_down_carries", playerName, currentWeek, 8) + 
                getStatFromPastWeeks("second_down_carries", playerName, currentWeek, 8);

                double redZoneOpportunity = 
                getStatFromPastWeeks("red_zone_carries", playerName, currentWeek, 8) +
                getStatFromPastWeeks("red_zone_targets", playerName, currentWeek, 8);

                positionalAverages[0] = earlyDownUsage;
                positionalAverages[1] = getStatFromPastWeeks("rushing_yards", playerName, currentWeek, 8);
                positionalAverages[2] = getStatFromPastWeeks("rushing_touchdowns", playerName, currentWeek, 8);
                positionalAverages[3] = getStatFromPastWeeks("catchable_targets", playerName, currentWeek, 8);
                positionalAverages[4] = getStatFromPastWeeks("receiving_yards", playerName, currentWeek, 8);
                positionalAverages[5] = getStatFromPastWeeks("receiving_touchdowns", playerName, currentWeek, 8);
                positionalAverages[6] = redZoneOpportunity;
                positionalAverages[7] = getStatFromPastWeeks("goal_line_carries", playerName, currentWeek, 8);
                 

                RunningBack player = new RunningBack(playerName, currentOpponent, currentWeek, opposingDefenseRunRank, offensiveRunRank, avgFPTs, positionalAverages);
                player.calculatePoints();
            }



            //Wide Receivers and Tight Ends
            else {
                int offensivePassRank = (int) (long) teamLevel.get("OffensivePassRank");
                positionalAverages = new double[7];

                double targets = getStatFromPastWeeks("targets", playerName, currentWeek, 12);
                double catchableTargets = getStatFromPastWeeks("catchable_targets", playerName, currentWeek, 12);

                double earlyDownTargets = 
                getStatFromPastWeeks("first_down_targets", playerName, currentWeek, 12);
                getStatFromPastWeeks("second_down_targets", playerName, currentWeek, 12);

                double touchdownTargets = 
                getStatFromPastWeeks("red_zone_targets", playerName, currentWeek, 12) + 
                getStatFromPastWeeks("goal_line_targets", playerName, currentWeek, 12) +
                getStatFromPastWeeks("end_zone_targets", playerName, currentWeek, 12);

                double aDOT = 
                getStatFromPastWeeks("air_yards", playerName, currentWeek, 12) / targets;

                positionalAverages[0] = earlyDownTargets;
                positionalAverages[1] = touchdownTargets;
                positionalAverages[2] = aDOT;
                positionalAverages[3] = catchableTargets;
                positionalAverages[4] = getStatFromPastWeeks("target_share", playerName, currentWeek, 12);
                positionalAverages[5] = getStatFromPastWeeks("slot_snaps", playerName, currentWeek, 12);
                positionalAverages[6] = getStatFromPastWeeks("routes_run", playerName, currentWeek, 8);

                if (position.equals("WR")) {
                    WideReceiver player = new WideReceiver(playerName, currentOpponent, currentWeek, opposingDefensePassRank, offensivePassRank, avgFPTs, positionalAverages);
                    player.calculatePoints();
                }
                else {
                    TightEnd player = new TightEnd(playerName, currentOpponent, currentWeek, opposingDefensePassRank, offensivePassRank, avgFPTs, positionalAverages);
                    player.calculatePoints();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

