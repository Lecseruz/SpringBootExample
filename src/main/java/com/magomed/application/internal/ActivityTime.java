package com.magomed.application.internal;

public class ActivityTime {
    private int activity;
    private long timestamp;

    public ActivityTime(int activity, long timestamp) {
        this.activity = activity;
        this.timestamp = timestamp;
    }

    public int getActivity() {
        return activity;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
