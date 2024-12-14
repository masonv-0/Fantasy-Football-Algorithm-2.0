import java.lang.Math;

public class RunningBack {
    
    private String name;
    private String currentOpponent;
    private int currentWeek;
    private int opposingRunDefense;
    private int offensiveRunRank;
    private double averagePoints;
    private double[] positionalAverages;

    RunningBack(String n, String c, int cw, int oppRD, int offRR, double avgF, double[] posAVG) {
        name = n;
        currentOpponent = c;
        currentWeek = cw;
        opposingRunDefense = oppRD;
        offensiveRunRank = offRR;
        averagePoints = avgF;
        positionalAverages = posAVG;
    }

    public void printAll() {
        System.out.println(name);
        System.out.println(currentOpponent);
        System.out.println(currentWeek);
        System.out.println(opposingRunDefense);
        System.out.println(offensiveRunRank);

        for (double index: positionalAverages) {
            System.out.println(index);
        }
    }

    public void calculatePoints() { 
        
        System.out.println();

        //Extract positional data
        double earlyDownUsage = positionalAverages[0];
        double rushingYards = positionalAverages[1];
        double rushingTDs = positionalAverages[2];
        double catchableTargets = positionalAverages[3];
        double receivingYards = positionalAverages[4];
        double receivingTDs = positionalAverages[5];
        double RZOpportunity = positionalAverages[6];
        double goalLineCarries = positionalAverages[7];

        double expectedPoints = 0.0;
        double pointsValue;

        //Rewards RBs for performing well, 14 pts/week is typically top 20, 18 pts/week is top 6
        if (averagePoints >= 14.0) {
            System.out.println("+1 for averaging 14+");

            expectedPoints+=1;
        }

        //Penalizes RBs for performing badly, 12 pts/week is below top 30
        if (averagePoints <= 12.0) {
            System.out.println("-1 for averaging <12");

            expectedPoints-=1;
        }

        //Early down usage
        if (earlyDownUsage > 15.0) {
            System.out.println("+2 for early down usage");

            expectedPoints+=2;
        }
        else {
            System.out.println("-1 for low early down usage");

            expectedPoints-=1;
        }

        //Pure rushing
        pointsValue = Math.round(rushingYards * 0.1 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for rushing yards");
        expectedPoints += pointsValue;

        pointsValue = Math.round(rushingTDs * 6.0 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for rushing TDs");
        expectedPoints += pointsValue;

        //Receiving
        pointsValue = Math.round(catchableTargets * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for receptions");
        expectedPoints += pointsValue;

        pointsValue = Math.round(receivingYards * 0.1 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for receiving yards");
        expectedPoints += pointsValue;

        pointsValue = Math.round(receivingTDs * 6.0 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for receiving TDs");
        expectedPoints += pointsValue;

        //Red zone opportunity
        pointsValue = Math.round(RZOpportunity * 0.25 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for red zone opportunity");
        expectedPoints += pointsValue;

        //Goal line work
        pointsValue = Math.round(goalLineCarries * 2 * 10.0) / 10.0;
        System.out.println("+ " + pointsValue + " for goal line work");
        expectedPoints += pointsValue;

        //Opposing defense
        pointsValue = getRanges(opposingRunDefense);
        System.out.println("- " + pointsValue + " for opposing run defense");
        expectedPoints -= pointsValue;

        //Rushing offense
        pointsValue = getRanges(offensiveRunRank);
        System.out.println("+ " + pointsValue + " for offense's run rank");
        expectedPoints += pointsValue;

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
            return -2.0;
        }

        else {
            return -3.0;
        }
    }

}