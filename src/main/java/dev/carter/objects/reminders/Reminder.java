package dev.carter.objects.reminders;

public class Reminder {
    private int reminderID;
    private String reminder;
    
    public Reminder(int reminderID, String reminder){
        this.reminderID = reminderID;
        this.reminder = reminder;
    }
    //Getters
    public int getReminderID() {
        return reminderID;
    }

    public String getReminder() {
        return reminder;
    }

    //Setters
    public void setReminderID(int reminderID) {
        this.reminderID = reminderID;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

}
