package projecyds;

import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class BankLobby {

    List<Teller> tellers = new ArrayList<>(); // holds every teller use array list so we can use any teller at any time
    Queue<Teller> idleTellers = new LinkedList<>();
    /*
     instead of searching every teller to see which is free using a for loop which takes O(n)
      we use a Queue which will take O(1) and when the teller is free we add him to the queue which we call break room and when we need him we just call him using .poll
     */
    Queue<Customer> queue;      // The waiting line for the customers
    Statistics stats = new Statistics();
    int mode;
    boolean showLiveLog;

    public BankLobby(int numTellers, int mode, boolean showLiveLog) {
        this.mode = mode;
        this.showLiveLog = showLiveLog;

        // Initialize the tellers
        for (int i = 1; i <= numTellers; i++) {
            Teller t = new Teller(i); // create new teller
            tellers.add(t); //add to the array list
            idleTellers.add(t); // send always to break room as when created he will be free
        }
        // choose the method of queue we want
        if (mode == 1) {
            queue = new LinkedList<>();    // Mode 1: FIFO Use linked list so it would be O(1)
        } else {
            queue = new PriorityQueue<>(); // Modes 2 & 3: Use priority queue uses the compare to function and it is 0(LogN)
        }
    }

    public void handleArrival(Customer c, double time, Timeline timeline) {
        queue.add(c); //
        String type = (c.priority == 1) ? "[VIP]" : "[REG]";
        log(time, type + " Customer " + c.id + " arrived for " + c.task); // every customer comes and served gets it log

        // Mode 3 (Ageing): Schedule an upgrade using a upgrade event
        if (mode == 3 && c.priority == 2) {
            timeline.addEvent(new Event(time + 30.0, "UPGRADE", c, -1));
        }
        dispatch(time, timeline); // check for free teller to serve this arrival
    }

    public void handleDeparture(int tellerId, Customer c, double time, Timeline timeline) {
        Teller finishedTeller = tellers.get(tellerId - 1); // computer starts counting from 0 for arraylist so we have to do -1 to get the teller we want
        finishedTeller.Busy = false; // set the teller as free now

        idleTellers.add(finishedTeller); // Return to break room as he is free

        log(time, "<<< Customer " + c.id + " finished. Teller " + tellerId + " is available in break room");
        dispatch(time, timeline); // make the lobby look into the break room and the queue
    }

    public void handleUpgrade(Customer c, double time) {
        // Only upgrade them if they are still stuck in line!
        if (queue.contains(c)) {
            queue.remove(c);
            c.priority = 1;
            queue.add(c);
            log(time, "*** Customer " + c.id + " upgraded to VIP ***");
        }
    }

    private void dispatch(double time, Timeline timeline) {
        // Triggers instantly if both lines have items
        while (!queue.isEmpty() && !idleTellers.isEmpty()) {

            Teller freeTeller = idleTellers.poll(); // Instant O(1) retrieval
            Customer c = queue.poll();

            stats.recordWait(time - c.arrivalTime, c.originalPriority, queue.size());
            log(time, ">>> Teller " + freeTeller.id + " grabbed Customer " + c.id);

            // Generate the departure event and hand it back to the Timeline
            Event departureEvent = freeTeller.serve(c, time);
            timeline.addEvent(departureEvent);
        }
    }

    public void printFinalReport(double totalTime) {
        stats.printReport(tellers, totalTime);
    }

    // --- The Clean LocalTime Helper ---
    private String toClockFormat(double rawMinutes) {
        long totalSeconds = Math.round(rawMinutes * 60);
        LocalTime time = LocalTime.of(9, 0); // Bank opens at 9:00 AM
        LocalTime currentTime = time.plusSeconds(totalSeconds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return "[" + currentTime.format(formatter) + "]";
    }

    private void log(double time, String message) {
        if (!showLiveLog) return;
        System.out.println(toClockFormat(time) + " " + message);
    }
}