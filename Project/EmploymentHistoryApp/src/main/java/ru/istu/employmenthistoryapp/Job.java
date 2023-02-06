package ru.istu.employmenthistoryapp;

public class Job {
    private String date, info, grounds;

    public Job(String date, String info, String grounds) {
        this.date = date;
        this.info = info;
        this.grounds = grounds;
    }

    public String getDate() {
        return date;
    }

    public String getInfo() {
        return info;
    }

    public String getGrounds() {
        return grounds;
    }
}
