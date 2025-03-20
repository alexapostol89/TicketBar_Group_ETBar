package dk.easv.ticketbar2.be;

public class Events {
    private int Event_id;
    private int Created_by;
    private String Event_name;
    private String Event_date;
    private String End_date;
    private String Location;
    private String Event_description;
    private String Event_notes;
    private String Event_location_guide;
    private String Event_image_path;


    public Events(int event_id, int created_by, String event_name, String event_date, String end_date, String location, String event_description, String event_notes, String event_location_guide, String event_image_path) {
        Event_id = event_id;
        Created_by = created_by;
        Event_name = event_name;
        Event_date = event_date;
        End_date = end_date;
        Location = location;
        Event_description = event_description;
        Event_notes = event_notes;
        Event_location_guide = event_location_guide;
        Event_image_path = event_image_path;
    }

    public int getEvent_id() {
        return Event_id;
    }

    public void setEvent_id(int event_id) {
        Event_id = event_id;
    }

    public int getCreated_by() {
        return Created_by;
    }

    public void setCreated_by(int created_by) {
        Created_by = created_by;
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

    public String getEnd_date() {
        return End_date;
    }

    public void setEnd_date(String end_date) {
        End_date = end_date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getEvent_description() {
        return Event_description;
    }

    public void setEvent_description(String event_description) {
        Event_description = event_description;
    }

    public String getEvent_notes() {
        return Event_notes;
    }

    public void setEvent_notes(String event_notes) {
        Event_notes = event_notes;
    }

    public String getEvent_location_guide() {
        return Event_location_guide;
    }

    public void setEvent_location_guide(String event_location_guide) {
        Event_location_guide = event_location_guide;
    }

    public String getEvent_image_path() {
        return Event_image_path;
    }

    public void setEvent_image_path(String event_image_path) {
        Event_image_path = event_image_path;
    }
}
