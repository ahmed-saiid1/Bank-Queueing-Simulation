package projecyds;

/**
 * Represents a distinct point in time during the simulation.
 * Sorted chronologically by a PriorityQueue to maintain the master timeline.
 */
public class Event implements Comparable<Event> {

    double time;
    String type; // arrival/departure/upgrade
    Customer customer;
    int tellerId; // used only for departures so at arrivals =-1

    public Event(double time, String type, Customer customer, int tellerId) {
        this.time = time;
        this.type = type;
        this.customer = customer;
        this.tellerId = tellerId;
    }

    @Override
    public int compareTo(Event other) {
        /*
        Sorts events by the earliest in time so we make it ascending by writing this then other
        O(1) to do comparison check but O(LogN) to shuffle into the queue
        if both events happen at the same time and compare returns 0  it will pick a event randomly
         */
        return Double.compare(this.time, other.time);
    }
}