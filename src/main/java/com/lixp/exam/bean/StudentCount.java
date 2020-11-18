package com.lixp.exam.bean;

public class StudentCount {
    private String courseString;
    private Integer courseCount;

    public String getCourseString() {
        return courseString;
    }

    public StudentCount setCourseString(String courseString) {
        this.courseString = courseString;
        return this;
    }

    public Integer getCourseCount() {
        return courseCount;
    }

    public StudentCount setCourseCount(Integer courseCount) {
        this.courseCount = courseCount;
        return this;
    }

    public StudentCount(String courseString, Integer courseCount) {
        this.courseString = courseString;
        this.courseCount = courseCount;
    }

    public StudentCount() {
    }

    @Override
    public String toString() {
        return "StudentCount{" +
                "courseString='" + courseString + '\'' +
                ", courseCount=" + courseCount +
                '}';
    }
}
