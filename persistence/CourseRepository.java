package persistence;

import domain.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CourseRepository {

    List<Course> findAll() throws SQLException;

    List<Course> findAllByProfessor(Professor professor) throws SQLException;

    Optional<Course> findById(int id) throws SQLException;

    /**
     * Saves the course.
     * @param course course to save
     * @return course containing at least data as given
     * @throws SQLException if sql statement is incorrect
     * @throws IllegalArgumentException if begin in the past or if given course already has an id
     */
    Course save(Course course) throws SQLException;

    List<Course> findAllByStudent(Student student) throws SQLException;

    void enrollInCourse(Student student, Course course) throws SQLException;

    void unenrollFromCourse(Student student, Course course) throws SQLException;
}
