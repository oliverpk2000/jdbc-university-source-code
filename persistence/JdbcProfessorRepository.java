package persistence;

import domain.Professor;
import factory.PersonFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record JdbcProfessorRepository(Connection connection) implements ProfessorRepository {

    @Override
    public List<Professor> findAll() throws SQLException {
        var sql =
            """
                SELECT *
                FROM professors
                """;
        try (var statement = connection.prepareStatement(sql)) {
            var professorFactory = new PersonFactory();
            var resultSet = statement.executeQuery();
            var builder = Stream.<Professor>builder();
            return professorFactory
                .buildProfessorStream(resultSet, builder)
                .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    @Override
    public Optional<Professor> findById(int id) throws SQLException {
        var sql =
            """
                SELECT *
                FROM professors
                WHERE professor_id = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            var professorFactory = new PersonFactory();
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            var builder = Stream.<Professor>builder();
            return professorFactory
                .buildProfessorStream(resultSet, builder)
                .findFirst();
        }
    }

    @Override
    public Professor save(Professor professor) throws SQLException {
        if (professor.getId() != null) {
            throw new IllegalArgumentException("already has id");
        }
        var sql =
            """
                INSERT INTO professors (last_name, first_name) VALUES (?, ?)
                """;
        var professorFactory = new PersonFactory();
        var personId = professorFactory.savePersonIntoRepository(connection, sql, professor);
        professor.setId(personId);
        return professor;
    }

    @Override
    public void delete(Professor professor) throws SQLException {
        var sql =
            """
                DELETE FROM professors
                WHERE professor_id = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, professor.getId());
            statement.execute();
        }
    }
}
