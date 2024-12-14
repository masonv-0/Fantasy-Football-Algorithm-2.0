import java.lang.Math;

public class WideReceiver{
    
    private String name;
    private String currentOpponent;
    private int currentWeek;
    private int opposingPassDefense;
    private int offensivePassRank;
    private double averagePoints;
    private double[] positionalAverages;

    WideReceiver (String n, String c, int cw, int oppPD, int offPR, double avgF, double[] posAVG) {
        name = n;
        currentOpponent = c;
        currentWeek = cw;
        opposingPassDefense = oppPD;
        offensivePassRank = offPR;
        averagePoints = avgF;
        positionalAverages = posAVG;
    }

    public void printAll() {
        System.out.println(opposingPassDefense);
        System.out.println(offensivePassRank);

        for (double index: positionalAverages) {
            System.out.println(index);
        }
    }

    public void calculatePoints() {

        //Extract positional data
        double earlyDownTargets = positionalAverages[0];
        double touchdownTargets = positionalAverages[1]; 
        double aDOT = positionalAverages[2];
        double catchableTargets = positionalAverages[3];
        double targetShare = positionalAverages[4];
        double slotSnaps = positionalAverages[5];
        double routesRun = positionalAverages[6];

        double expectedPoints = 0.0;
        double pointsValue;

        //Rewards WRs for performing well, 12 pts/week is typically top 20, 14 pts/week is top 10
        if (averagePoints >= 12.0) {
            System.out.println("+1.5 for averaging 12+");

            expectedPoints+=0.75;

            if (averagePoints >= 14.0) {
                System.out.println("+1.5 for averaging 14+");

                expectedPoints+=0.75;
            }
        }

        //Penalizes WRs for performing badly, 9 pts/week is below top 40
        if (averagePoints <= 9.0) {
            System.out.println("-2 for averaging <9");

            expectedPoints-=2;
        }

        //Early down targets
        if (earlyDownTargets >= 2.0) {
            System.out.println("+1 for early down targets");

            expectedPoints+=1;

            if (earlyDownTargets >= 3.0) {
                System.out.println("+1 for early down targets");

                expectedPoints+=1;
            }
        }

        //Touchdown targets
        pointsValue = Math.round(touchdownTargets * 3.0 * 10.0) / 10.0;
        System.out.println("+" + pointsValue + " for touchdown targets");
        expectedPoints += pointsValue;

        //Catchable targets
        pointsValue = Math.round(catchableTargets * 10.0) / 10.0 + Math.round(((aDOT / 10) * catchableTargets) * 10.0) / 10.0;
        System.out.println("+" + pointsValue + " for targets and ADOT");
        expectedPoints += pointsValue;

        //Target share
        if (targetShare >= 19.0) {
            System.out.println("+0.5 for target share");

            expectedPoints+=0.5;
            
            if (targetShare >=25.0) {
                System.out.println("+1 for target share");

                expectedPoints+=1;
            }
        }

        //Slot snaps
        if (slotSnaps >= 20.0) {
            System.out.println("+2 for slot usage");

            expectedPoints+=2;
        }

        //Routes run
        if (routesRun <=20.0) {
            System.out.println("-2 for routes run");

            expectedPoints-=2;
        }

        //Opposing pass defense
        pointsValue = 2 * getRanges(opposingPassDefense);
        System.out.println("- " + pointsValue + " for opposing pass defense");
        expectedPoints -= pointsValue;

        //Passing offense
        pointsValue = 0.5 * getRanges(offensivePassRank);
        System.out.println("+ " + pointsValue + " for offense's pass rank");
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
            return -1.5;
        }

        else {
            return -2.0;
        }
    }
}
