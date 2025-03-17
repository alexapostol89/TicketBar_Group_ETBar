package dk.easv.ticketbar2.be;

public class Events {
    private int Event_id;
    private String Event_name;
    private String Event_date;
    private String Location;
    private int Created_by;

    public Events(int event_id, String event_name, String event_date, String location, int created_by) {
        Event_id = event_id;
        Event_name = event_name;
        Event_date = event_date;
        Location = location;
        Created_by = created_by;
    }

    public int getEvent_id() {
        return Event_id;
    }

    public void setEvent_id(int event_id) {
        Event_id = event_id;
    }

    public String getEvent_name() {
        return Event_name;
    }

    public void setEvent_name(String event_name) {
        Event_name = event_name;
    }

    public String getEvent_date() {
        return Event_date;
    }

    public void setEvent_date(String event_date) {
        Event_date = event_date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getCreated_by() {
        return Created_by;
    }

    public void setCreated_by(int created_by) {
        Created_by = created_by;
    }
}
