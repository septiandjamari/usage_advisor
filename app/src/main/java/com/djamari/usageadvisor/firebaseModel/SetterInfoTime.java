package com.djamari.usageadvisor.firebaseModel;

public class SetterInfoTime {
    public String DateCreated, switchBasedTime, switchBasedSchedule;
    public int basedTimeStartHour;
    public int basedTimeStartMinute;
    public int basedTimeFinishHour;
    public int basedTimeFinishMinute;
    public int basedScheduleStartHour;
    public int basedScheduleStartMinute;
    public int basedScheduleFinishHour;
    public int basedScheduleFinishMinute;
    public int radio;
    public int grupBasedTimeLock;
    public int grupBasedScheduleLock;

    public SetterInfoTime(String DateCreated, String switchBasedTime, String switchBasedSchedule,
                          int basedTimeStartHour, int basedTimeStartMinute, int basedTimeFinishHour,
                          int basedTimeFinishMinute, int basedScheduleStartHour, int basedScheduleStartMinute,
                          int basedScheduleFinishHour, int basedScheduleFinishMinute, int radio,
                          int grupBasedTimeLock, int grupBasedScheduleLock) {
        this.DateCreated = DateCreated;
        this.switchBasedTime = switchBasedTime;
        this.switchBasedSchedule = switchBasedSchedule;
        this.basedTimeStartHour = basedTimeStartHour;
        this.basedTimeStartMinute = basedTimeStartMinute;
        this.basedTimeFinishHour = basedTimeFinishHour;
        this.basedTimeFinishMinute = basedTimeFinishMinute;
        this.basedScheduleStartHour = basedScheduleStartHour;
        this.basedScheduleStartMinute = basedScheduleStartMinute;
        this.basedScheduleFinishHour = basedScheduleFinishHour;
        this.basedScheduleFinishMinute = basedScheduleFinishMinute;
        this.radio = radio;
        this.grupBasedTimeLock = grupBasedTimeLock;
        this.grupBasedScheduleLock = grupBasedScheduleLock;
    }
}