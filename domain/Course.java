package domain;

import validator.Validator;

import java.time.LocalDate;

public class Course implements Validator {

    private Integer id;
    private final CourseType type;
    private final Professor professor;
    private final String description;
    private final LocalDate begin;

    public Course(Integer id, CourseType type, Professor professor, String description, LocalDate begin) {
        this.id = id;
        this.type = type;
        this.professor = professor;
        this.description = description;
        this.begin = begin;

        if(invalidate()){
            throw new IllegalArgumentException("course arguments could not be validated");
        }
    }

    public Course(CourseType type, Professor professor, String description, LocalDate begin) {
        this(null, type, professor, description, begin);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CourseType getType() {
        return type;
    }

    public Professor getProfessor() {
        return professor;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getBegin() {
        return begin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        return getId() != null ? getId().equals(course.getId()) : course.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    @Override
    public boolean invalidate() {
        if (type == null) {
            return true;
        }
        if (professor == null) {
            return true;
        }
        if (description == null) {
            return true;
        }
        if(description.isBlank()){
            return true;
        }
        return begin == null;
    }
}
