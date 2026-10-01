package projecyds;

import java.util.LinkedList;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Teller implements Comparable<Teller> {
    int id;
    boolean Busy = false;
    double totalworkingtime = 0;
    int customersServed = 0;

    // LinkedList used to store each teller log history takes O(1) as it just add a new node every time
    LinkedList<String> log = new LinkedList<>();

    public Teller(int id) {
        this.id = id;
    }

    private String toClockFormat(double rawMinutes) {
        long totalSeconds = Math.round(rawMinutes * 60);
        LocalTime time = LocalTime.of(9, 0); // Bank opens at 9:00 AM
        LocalTime currentTime = time.plusSeconds(totalSeconds);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        return "[" + currentTime.format(formatter) + "]";
    }

// when customer enters to teller this method is triggered we write it as public event as we want to return an event which is departure
    public Event serve(Customer c, double time) {
        this.Busy = true;
        this.totalworkingtime += c.serviceTime;
        this.customersServed++;
// to write it as like 9:40:55
        log.add(String.format("%s | Served Customer #%d (%s)", toClockFormat(time), c.id, c.task));

// used to find create the departure event and that is by getting the current time and adding it to the service time
        return new Event(time + c.serviceTime, "DEPARTURE", c, this.id);
    }

    /* Binary search tree comparison Here we do it other compared to this because we want it to be descending
     unlike in customer class where it is min heap
      Takes time complexity 0(NlogN) to rank all tellers N is number of teller and logN is time it takes to place one of them in tree
      the function compareTo itself only takes O(1)
     */
    public int compareTo(Teller other) {
        // 1. Primary: Highest amount of customers served
        int compare = Integer.compare(other.customersServed, this.customersServed);

        // 2. Secondary: Most time worked
        if (compare == 0) {
            compare = Double.compare(other.totalworkingtime, this.totalworkingtime);
        }
        /* 3. we have to do this last comparison to avoid any risk
         as if the two comparisons above are equal 0 the tree will treat them as a copy and remove them both
         */
        if (compare == 0) {
            return Integer.compare(this.id, other.id);
        }
        return compare;
    }
}