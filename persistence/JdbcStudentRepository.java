package persistence;

import domain.Student;
import factory.PersonFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record JdbcStudentRepository(Connection connection) implements StudentRepository {

    @Override
    public List<Student> findAll() throws SQLException {
        var sql =
            """
                SELECT *
                FROM students
                """;
        try (var statement = connection.prepareStatement(sql)) {
            var studentFactory = new PersonFactory();
            var resultSet = statement.executeQuery();
            var builder = Stream.<Student>builder();
            return studentFactory
                .buildStudentStream(resultSet, builder)
                .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    @Override
    public Optional<Student> findById(int id) throws SQLException {
        var sql =
            """
                SELECT *
                FROM students
                WHERE student_id = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            var studentFactory = new PersonFactory();
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            var builder = Stream.<Student>builder();
            return studentFactory
                .buildStudentStream(resultSet, builder)
                .findFirst();
        }
    }

    @Override
    public Student save(Student student) throws SQLException {
        if (student.getId() != null) {
            throw new IllegalArgumentException("already has id");
        }
        var sql =
            """
                INSERT INTO students (last_name, first_name) VALUES (?, ?)
                """;
        var studentFactory = new PersonFactory();
        var studentId = studentFactory.savePersonIntoRepository(connection, sql, student);
        student.setId(studentId);
        return student;
    }

    @Override
    public void update(Student student) throws SQLException {
        if (student.getId() == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        var sql = """
            UPDATE students
            SET last_name = ?, first_name = ?
            WHERE student_id = ?;
            """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getLastName());
            statement.setString(2, student.getFirstName());
            statement.setInt(3, student.getId());
            var amountOfUpdates = statement.executeUpdate();
            if(amountOfUpdates < 1){
                throw new IllegalArgumentException("unpersisted dataset");
            }
        }
    }

    @Override
    public void delete(Student student) throws SQLException {
        var sql =
            """
                DELETE FROM students
                WHERE student_id = ?
                """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, student.getId());
            statement.execute();
        }
    }
}
