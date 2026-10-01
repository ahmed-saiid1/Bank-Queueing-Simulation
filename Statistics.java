package projecyds;

import java.util.*;

/**
 * Handles data aggregation and final report generation.
 * Leverages a TreeSet (Binary Search Tree) for automatic performance ranking.
 */
public class Statistics {
    double totalWait = 0, maxWait = 0;
    int served = 0, maxQueue = 0;
    double vipWait = 0, regWait = 0;
    int vipServed = 0, regServed = 0;

    public void recordWait(double wait, int originalPriority, int queueSize) {
        totalWait += wait;
        served++;
        if (wait > maxWait) maxWait = wait;
        if (queueSize > maxQueue) maxQueue = queueSize;
        if (originalPriority == 1) { vipWait += wait; vipServed++; }
        else { regWait += wait; regServed++; }
    }

    public void printReport(List<Teller> tellers, double totalTime) {
        System.out.println("\n--- FINAL SIMULATION REPORT ---");
        System.out.printf("Overall Avg Wait: %.2f mins (Max: %.2f mins)\n", (totalWait / served), maxWait);
        if (vipServed > 0) System.out.printf(" -> VIP Avg Wait: %.2f mins\n", (vipWait / vipServed));
        if (regServed > 0) System.out.printf(" -> REG Avg Wait: %.2f mins\n", (regWait / regServed));
        System.out.println("Max Line Size   : " + maxQueue + " customers");
        System.out.println("\n[ Teller Leaderboard (TreeSet BST) ]");
        // Automatically ranks objects in O(N log N) time based on Teller.compareTo()
        TreeSet<Teller> rankedTellers = new TreeSet<>(tellers);
        int rank = 1;
        for (Teller t : rankedTellers) {
            System.out.printf("#%d - Teller %d : %d served (%.1f%% active)\n",
                    rank++, t.id, t.customersServed, (t.totalworkingtime /totalTime)*100);
        }
        System.out.println("\n[ Each Teller First and Last Log ]");
        for (Teller t : tellers) {
            if (!t.log.isEmpty()) {
                System.out.println("Teller " + t.id + " Shift:");
                System.out.println("  First Task -> " + t.log.getFirst());
                System.out.println("  Last Task  -> " + t.log.getLast());
            } else {
                System.out.println("Teller " + t.id + " Shift: No customers served.");
            }
        }

    }
}