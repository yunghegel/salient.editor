package events;

public interface Event {

    /**
     * The type of event.
     */
    EventType getType();

    /**
     * Calls this method if the event has triggered.
     */
    void action();

}