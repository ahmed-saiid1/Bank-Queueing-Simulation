package projecyds;

import java.util.PriorityQueue;
import java.util.Random;

public class Timeline {

    public PriorityQueue<Event> events = new PriorityQueue<>();
    public double totalTime; // bank closing time
    public Random random;
    public BankLobby lobby;

    public Timeline(double totalTime, long seed, BankLobby lobby) {
        this.totalTime = totalTime;
        this.lobby = lobby;

        if (seed == 0) {
            this.random = new Random();     // Normal random run
        } else {
            this.random = new Random(seed); // Locked random seed so we can compare our 3 diffrent queues
        }
    }

    // Allows the Lobby to schedule future events
    public void addEvent(Event e) {
        events.add(e);
    }

    public void run() {
        generateArrivals();

        System.out.println("\n--- Simulation Log ---");

        // The Engine Loop: Keeps running until the bank is completely empty
        while (!events.isEmpty()) {
            Event e = events.poll();

            // Stop letting new people in after closing time
            if (e.time > totalTime && e.type.equals("ARRIVAL"))
                continue;

            // Hand the events over to the Lobby manager using String checks
            switch (e.type) {
                case "ARRIVAL" -> {
                    lobby.handleArrival(e.customer, e.time, this);
                    break;
                }
                case "DEPARTURE" -> {
                    lobby.handleDeparture(e.tellerId, e.customer, e.time, this);
                    break;
                }
                case "UPGRADE" -> {
                    lobby.handleUpgrade(e.customer, e.time);
                    break;
                }
            }
        }

        // When time is up, tell the lobby to print the final grades!
        lobby.printFinalReport(totalTime);
    }
// takes O(NLogN) as it loops N times for number of customers and LogN to sort the queue
    private void generateArrivals() {
        double time = 0; // represents 9:00
        int id = 1;
        String[] allTasks = Customer.ALL_TASKS;   // Grab the array of Strings from Customer
        // Math formula to generate random arrival intervals
        while ((time += -2.0 * Math.log(random.nextDouble())) < totalTime)
            /* this is a Poisson Process it is the best formula i found to calculate random time it creats an Exponential Distribution
             random.nextDouble() asks for a java number between 0.000 and 0.999
              then it is put in a log and multipled by -2 and then add it with the current time
             */
        {
            int priority = (random.nextInt(100) < 20) ? 1 : 2;
            // pick random number from 0 to 100 if under 20 the customer is VIP so it is a 20% chance it is vip
            String task = allTasks[random.nextInt(allTasks.length)];
            // Pick a random string from the array tasks in customer
            events.add(new Event(time, "ARRIVAL", new Customer(id++, priority, time, task), -1));
        }
    }
}