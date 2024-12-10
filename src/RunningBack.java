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
        double receptionsDifference = positionalAverages[0];
        double earlyDownUsage = positionalAverages[1];
        double targets = positionalAverages[2];
        double totalTouches = positionalAverages[3];
        double snapShare = positionalAverages[4];
        double RZOpportunity = positionalAverages[5];
        double goalLineCarries = positionalAverages[6];


        
        //Baseline points
        double expectedPoints = 5.0;

        //Rewards RBs for performing well, 14 pts/week is typically top 20, 18 pts/week is top 6
        if (averagePoints >= 14.0) {
            System.out.println("+3 for averaging 14+");
            expectedPoints+=3;
            if (averagePoints >= 18.0) {
                System.out.println("+1.5 for averaging 18+");
                expectedPoints+=1.5;
            }
        }

        //Penalizes RBs for performing badly, 12 pts/week is below top 30
        if (averagePoints <= 12.0) {
            System.out.println("-1 for averaging <12");
            expectedPoints-=1;
        }

        //Rewards receiving backs
        if (receptionsDifference > -7.0) {
            System.out.println("+3 for receiving back");
            expectedPoints+=3;
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

        //Targets
        System.out.println("+ " + Math.round(targets*0.8 * 10.0) / 10.0 + " for targets");
        expectedPoints+=Math.round(targets*0.8 * 10.0) / 10.0;

        //Total touches
        System.out.println("+ " + Math.round(totalTouches/10 * 10.0) / 10.0 + " for total touches");
        expectedPoints+=Math.round(totalTouches/10 * 10.0) / 10.0;

        //Snap share, high-usage backs are typically 50% or more
        if (snapShare >= 60.0) {
            System.out.println("+2.5 for snap share");
            expectedPoints+=2.5;
        }
        else {
            System.out.println("-1.5 for low snap share");
            expectedPoints-=1.5;
        }

        //Red zone opportunity
        System.out.println("+ " + Math.round(RZOpportunity/4 * 10.0) / 10.0 + " for red zone opportunity");
        expectedPoints+=Math.round(RZOpportunity/4 * 10.0) / 10.0;

        //Goal line work
        System.out.println("+ " + Math.round(goalLineCarries*2 * 10.0) / 10.0 + " for goal line work");
        expectedPoints+=Math.round(goalLineCarries*2 * 10.0) / 10.0;

        //Opposing defense
        System.out.println("- " + getRanges(offensiveRunRank) + " for opposing run defense");
        expectedPoints-=getRanges(opposingRunDefense);

        //Rushing offense
        System.out.println("+ " + getRanges(offensiveRunRank) + " for offense's run rank");
        expectedPoints+=getRanges(offensiveRunRank);

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
            return -2.5;
        }

        else {
            return -5.0;
        }
    }

}