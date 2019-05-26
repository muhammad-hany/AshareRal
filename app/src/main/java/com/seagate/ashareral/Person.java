package com.seagate.ashareral;

public class Person {

    String committeeCourse,name,title,bio;


    /**    @apiNote committeeCourse contractor */

    public Person(String committeeCourse, String name, String title, String bio) {

        this.committeeCourse = committeeCourse;
        this.name = name;
        this.title = title;
        this.bio = bio;
    }


    /**    @apiNote officers contractor */

    public Person(String name, String title, String bio) {
        //officer

        this.name = name;
        this.title = title;
        this.bio = bio;
    }





    public String getCommitteeCourse() {
        return committeeCourse;
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
