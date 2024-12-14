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
        double passingYards = positionalAverages[2];
        double passingTDs = positionalAverages[3];
        double avgHurries = positionalAverages[4];
        double avgSacksTaken = positionalAverages[5];
        double avgInterceptablePasses = positionalAverages[6];
        double avgRZCompletions = positionalAverages[7];
        double avgRZCarries = positionalAverages[8];

        double expectedPoints = 0.0;
        double pointsValue;

        //Rewards QBs for performing well in the past, 20 pts/week is typically top 15
        if (averagePoints >= 20.0) {
            System.out.println("+1.0 for averaging 20+");
            
            expectedPoints+=1.0;

            if (averagePoints >= 22.0) {
                System.out.println("+1.0 for averaging 22+");
                
                expectedPoints+=1.0;
            }
        }

        //Penalizes QBs for performing badly in the past, 15 pts/week is below top 32
        if (averagePoints <= 15.0) {
            System.out.println("-2 for averaging <15");
            
            expectedPoints-=2;
        }

        //Rushing
        pointsValue = Math.round(avgRushYards * 0.1 * 10.0) / 10.0;
        System.out.println
        ("+ " + pointsValue + " for rush yards");
        expectedPoints += pointsValue;

        pointsValue = Math.round(avgRushTDs * 6.0 * 10) / 10.0;
        System.out.println("+ " + pointsValue + " for rush TDs");
        expectedPoints += pointsValue;

        //Passing
        pointsValue = Math.round(passingYards * 0.04 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for passing yards");
        expectedPoints += pointsValue;

        pointsValue = Math.round(passingTDs * 4.0 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for passing TDs");
        expectedPoints += pointsValue;

        //Hurries and sacks
        pointsValue = Math.round(avgHurries * 0.3 * 10.0) / 10.0;
        System.out.println("- " + pointsValue + " for hurries");
        expectedPoints -= pointsValue;

        pointsValue = Math.round(avgSacksTaken * 10.0) / 10.0;
        System.out.println("- " + pointsValue + " for sacks taken");
        expectedPoints -= pointsValue;

        //Interceptable passes
        pointsValue = Math.round(avgInterceptablePasses * 10.0) / 10.0;
        System.out.println("- " + pointsValue + " for interceptable passes");
        expectedPoints -= pointsValue;

        //Red zone completions
        pointsValue = Math.round(avgRZCompletions * 0.8 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for red zone completions");
        expectedPoints += pointsValue;

        //Red zone carries
        pointsValue = Math.round(avgRZCarries * 1.5 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for red zone carries");
        expectedPoints += pointsValue;

        //Opposing defense
        pointsValue = 2 * getRanges(opposingPassDefense);
        System.out.println("- " + pointsValue + " for opposing pass defense");
        expectedPoints -= pointsValue;

        //Passing offense
        System.out.println("+ " + getRanges(offensivePassRank) + " for offense's pass rank");
        expectedPoints+=getRanges(offensivePassRank);
        
        System.out.println();
        System.out.print(name + " is projected ");
        System.out.printf("%.1f", expectedPoints);
        System.out.println(" fantasy points in Week " + currentWeek + " against " + currentOpponent);
        System.out.println();
    }

    public double getRanges(int rank) {
        if (rank < 5) {
            return 2.0;
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
