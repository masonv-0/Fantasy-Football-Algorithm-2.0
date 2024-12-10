import java.lang.Math;

public class Quarterback{
    
    private String name;
    private String currentOpponent;
    private int currentWeek;
    private int opposingPassDefense;
    private int offensivePassRank;
    private double averagePoints;
    private double [] positionalAverages;

    Quarterback (String n, String c, int cw, int oppPD, int offPR, double avgF, double[] posAVG) {
        name = n;
        currentOpponent = c;
        currentWeek = cw;
        opposingPassDefense = oppPD;
        offensivePassRank = offPR;
        averagePoints = avgF;
        positionalAverages = posAVG;
    }

    public void printAll() {
        System.out.println(name);
        System.out.println(currentOpponent);
        System.out.println(currentWeek);
        System.out.println(opposingPassDefense);
        System.out.println(offensivePassRank);

        for (double index: positionalAverages) {
            System.out.println(index);
        }
    }

    public void calculatePoints() {
        
        System.out.println();

        //Extract positional data
        double avgRushYards = positionalAverages[0];
        double avgRushTDs = positionalAverages[1];
        double avgHurries = positionalAverages[2];
        double avgSacksTaken = positionalAverages[3];
        double avgPassAttempts = positionalAverages[4];
        double avgInterceptablePasses = positionalAverages[5];
        double avgRZCompletions = positionalAverages[6];
        double avgRZCarries = positionalAverages[7];


        //Baseline points expected from QBs
        double expectedPoints = 15.0;

        //Rewards QBs for performing well in the past, 20 pts/week is typically top 15
        if (averagePoints >= 20.0) {
            System.out.println("+1.5 for averaging 20+");
            expectedPoints+=1.5;
        }

        //Penalizes QBs for performing badly in the past, 15 pts/week is below top 32
        if (averagePoints <= 15.0) {
            System.out.println("-2 for averaging <15");
            expectedPoints-=2;
        }

        //Rushing
        System.out.println("+ " + Math.round(avgRushYards/10 * 10) / 10.0 + " for average rush yards");
        expectedPoints+=Math.round(avgRushYards/10 * 10) / 10.0;

        System.out.println("+ " + Math.round(avgRushTDs*6 * 10) / 10.0 + " for average rush TDs");
        expectedPoints+=Math.round(avgRushTDs*6 * 10) / 10.0;

        //Hurries and sacks
        System.out.println("- " + Math.round(avgHurries * 0.3 * 10) / 10.0 + " for average hurries");
        expectedPoints-=Math.round(avgHurries * 0.3 * 10) / 10.0;

        System.out.println("- " + Math.round(avgSacksTaken * 10) / 10.0 + " for average sacks taken");
        expectedPoints-=Math.round(avgSacksTaken * 10) / 10.0;

        //Not too few pass attempts, not too many (does this matter?)
        /*
        if (avgPassAttempts < 18.0 || avgPassAttempts > 32.0) {
            expectedPoints-=1.0;
        }
        else {
            expectedPoints+=1.0;
        }
         */

        //Interceptable passes
        System.out.println("- " + Math.round(avgInterceptablePasses * 10) / 10.0 + " for average interceptable passes");
        expectedPoints-=Math.round(avgInterceptablePasses * 10) / 10.0;

        //Red zone completions
        System.out.println("+ " + Math.round(avgRZCompletions * 0.8 * 10) / 10.0 + " for average red zone completions");
        expectedPoints+=Math.round(avgRZCompletions * 0.8 * 10) / 10.0;

        //Red zone carries
        System.out.println("+ " + Math.round(avgRZCarries * 1.5 *10) / 10.0 + " for average red zone carries");
        expectedPoints+=Math.round(avgRZCarries * 1.5 *10) / 10.0;

        //Opposing defense
        System.out.println("- " + getRanges(opposingPassDefense) + " for opposing pass defense");
        expectedPoints-=getRanges(opposingPassDefense);

        //Passing offense
        System.out.println("+ " + getRanges(offensivePassRank) + " for offensive's pass rank");
        expectedPoints+=getRanges(offensivePassRank);
        
        System.out.println();
        System.out.print(name + " is projected ");
        System.out.printf("%.1f", expectedPoints);
        System.out.println(" fantasy points in Week " + currentWeek + " against " + currentOpponent);
        System.out.println();
    }

    public double getRanges(int rank) {
        if (rank < 5) {
            return 1.5;
        }

        else if (rank > 4 && rank < 9) {
            return 1.0;
        }

        else if (rank > 8 && rank < 13) {
            return 0.5;
        }

        else if (rank > 12 && rank < 17) {
            return 0;
        }

        else if (rank > 16 && rank <21) {
            return -0.5;
        }

        else if (rank > 20 && rank < 25) {
            return -1.0;
        }

        else if (rank > 24 && rank < 29) {
            return -1.5;
        }

        else {
            return -2.0;
        }
    }
}
