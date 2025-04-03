package dk.easv.ticketbar2.be;

public class Events {
    private int EventID;
    private String EventName;
    private String StartDateTime;
    private String EndDateTime;
    private String Location;
    private String Description;
    private String Notes;
    private String LocationGuide;
    private int CoordinatorID;
    private String EventImagePath;


   public Events(int eventID, String eventName, String startDateTime, String endDateTime, String location, String description, String notes, String locationGuide, int coordinatorID, String eventImagePath) {
        EventID = eventID;
        EventName = eventName;
        StartDateTime = startDateTime;
        EndDateTime = endDateTime;
        Location = location;
        Description = description;
        Notes = notes;
        LocationGuide = locationGuide;
        CoordinatorID = coordinatorID;
        EventImagePath = eventImagePath;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getEventID() {
        return EventID;
    }

    public void setEventID(int eventID) {
        EventID = eventID;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        StartDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        EndDateTime = endDateTime;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getLocationGuide() {
        return LocationGuide;
    }

    public void setLocationGuide(String locationGuide) {
        LocationGuide = locationGuide;
    }

    public int getCoordinatorID() {
        return CoordinatorID;
    }

    public void setCoordinatorID(int coordinatorID) {
        CoordinatorID = coordinatorID;
    }

    public String getEventImagePath() {
        return EventImagePath;
    }

    public void setEventImagePath(String eventImagePath) {
        EventImagePath = eventImagePath;
    }
}