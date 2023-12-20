package persistence;


import domain.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    List<Student> findAll() throws SQLException;

    Optional<Student> findById(int id) throws SQLException;

    /**
     * Saves the student.
     *
     * @param student professor to save
     * @return student containing at least data as given
     * @throws SQLException if sql statement is incorrect
     * @throws IllegalArgumentException if given student already has an id
     */
    Student save(Student student) throws SQLException;

    void update(Student student) throws SQLException;

    void delete(Student student) throws SQLException;
}
