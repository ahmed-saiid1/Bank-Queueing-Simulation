package projecyds;

/**
 * Represents a bank customer.
 * Implements Comparable to allow the PriorityQueue (Min-Heap) to sort customers
 * based on priority level (VIP vs Regular) and chronological arrival time.
 */
public class Customer implements Comparable<Customer> {

    // use array so we can make it easier in banklobby
    public static final String[] ALL_TASKS = {"DEPOSIT", "WITHDRAWAL", "NEW_ACCOUNT", "LOAN"};

    int id, priority; // priority 1=Vip 2=regular
    int originalPriority; //used so in mode 3 we can calculate vip average time correctly
    double arrivalTime, serviceTime;
    String task;

    public Customer(int id, int priority, double arrivalTime, String task) {
        this.id = id;
        this.priority = priority;
        this.originalPriority = priority;
        this.arrivalTime = arrivalTime;
        this.task = task;

        if (task.equals("DEPOSIT")) {
            this.serviceTime = 3.0;
        } else if (task.equals("WITHDRAWAL")) {
            this.serviceTime = 5.0;
        } else if (task.equals("NEW_ACCOUNT")) {
            this.serviceTime = 15.0;
        } else if (task.equals("LOAN")) {
            this.serviceTime = 30.0;
        } else {
            this.serviceTime = 0.0; // Fallback
        }
    }
    @Override
    /*inserting a customer taked O(logN) time where N is number of people standing in the line
     beacuse it shuffels the new customer through the tree but the method itself is 0(1)
     */
    public int compareTo(Customer newcustomer) {
        // 1st Priority: VIPs (1) beat Regulars (2) if smaller return -1 and if equal return 0 and greater than return 1
        if (this.priority != newcustomer.priority) {
            return Integer.compare(this.priority, newcustomer.priority);
        }
        // 2nd Priority: Whoever arrived first wins ties
        return Double.compare(this.arrivalTime, newcustomer.arrivalTime);
    }
}