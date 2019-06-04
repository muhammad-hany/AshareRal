package com.seagate.ashareral;

public class Person {

    String course,name,title,bio, emailCommittee;


    /**    @apiNote emailCommittee contractor */

    public Person(String emailCommittee, String name, String title, String bio) {
        //dls and officers
        this.emailCommittee = emailCommittee;
        this.name = name;
        this.title = title;
        this.bio = bio;
    }





    public Person(String course, String name, String title, String bio, String email) {
        this.course = course;
        this.name = name;
        this.title = title;
        this.bio = bio;
        this.emailCommittee = email;
    }

    public String getEmailCommittee() {
        return emailCommittee;
    }

    public String getCourse() {
        return course;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getBio() {
        return bio;
    }
}
