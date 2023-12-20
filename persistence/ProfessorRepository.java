package persistence;


import domain.Professor;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProfessorRepository {

    List<Professor> findAll() throws SQLException;

    Optional<Professor> findById(int id) throws SQLException;

    /**
     * Saves the professor.
     * @param professor professor to save
     * @return professor containing at least data as given
     * @throws SQLException if sql statement is incorrect
     * @throws IllegalArgumentException if given professor already has an id
     */
    Professor save(Professor professor) throws SQLException;

    void delete(Professor professor) throws SQLException;
}
