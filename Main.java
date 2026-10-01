package projecyds;


import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enterprise Queueing Algorithm Evaluator");

        boolean keepRunning = true;

        while (keepRunning) {
            System.out.print("\nEnter number of tellers: ");
            while (!input.hasNextInt()) {
                System.out.print("Invalid! Enter a whole number: ");
                input.next();
            }
            int numTellers = input.nextInt();

            // Ask for hours, calculate in minutes
            System.out.print("Enter simulation time in hours (e.g., 8 or 8.5): ");
            while (!input.hasNextDouble()) {
                System.out.print("Invalid! Enter a number: ");
                input.next();
            }
            double totalHours = input.nextDouble();
            double totalTimeMins = totalHours * 60.0;

            // Ask for the Seed (for controlled experiments)
            System.out.print("Enter a Random Seed (Type 0 for a normal random run): ");
            while (!input.hasNextLong()) {
                System.out.print("Invalid! Enter a number: ");
                input.next();
            }
            long seed = input.nextLong();
            // Ask for live log with strict validation
            System.out.print("Show live event log? (yes/no): ");
            String logAnswer = input.next().trim().toLowerCase();
            // Trap them in a loop until they type a valid variation of yes or no
            while (!logAnswer.equals("y") && !logAnswer.equals("yes") && !logAnswer.equals("n") && !logAnswer.equals("no")) {
                System.out.print("Invalid! Please enter 'yes' or 'no': ");
                logAnswer = input.next().trim().toLowerCase();
            }
            boolean showLog = logAnswer.equals("y") || logAnswer.equals("yes");
            System.out.println("\nSelect Queueing Algorithm:");
            System.out.println("1. Pure FIFO (Utilizes O(1) Queue)");
            System.out.println("2. Strict Priority (Utilizes O(log N) PriorityQueue)");
            System.out.println("3. Ageing Priority (Dynamic Heap Resorting)");
            System.out.print("Choice (1-3): ");

            // 1. First, make sure it's actually a number
            while (!input.hasNextInt()) {
                System.out.print("Invalid! Enter a number (1-3): ");
                input.next();
            }
            int mode = input.nextInt();

            // 2. check if that number is 1, 2, or 3
            while (mode < 1 || mode > 3) {
                System.out.print("Invalid choice! Please select 1, 2, or 3: ");

                // Re-verify it's an integer if they try to type letters here
                while (!input.hasNextInt()) {
                    System.out.print("Invalid! Enter a number: ");
                    input.next();
                }
                mode = input.nextInt();
            }

            System.out.println("\nStarting simulation for " + totalHours + " hours...");

            // THE HANDSHAKE: Connect the Floor Manager and the Timekeeper
            BankLobby lobby = new BankLobby(numTellers, mode, showLog);
            Timeline clock = new Timeline(totalTimeMins, seed, lobby);

            // Start the simulation!
            clock.run();

            while (true) {
                System.out.print("\nRun another simulation? (y/n): ");
                String answer = input.next().trim().toLowerCase();
                if (answer.equals("y") || answer.equals("yes")) break;
                if (answer.equals("n") || answer.equals("no")) {
                    keepRunning = false;
                    System.out.println("Exiting Simulator.");
                    break;
                }
            }
        }
        input.close();
    }
}