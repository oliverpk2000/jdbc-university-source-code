package factory;

import domain.Person;
import domain.Professor;
import domain.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Stream;

public class PersonFactory {
    public PersonFactory() {

    }

    public Stream<Professor> buildProfessorStream(ResultSet resultSet, Stream.Builder<Professor> builder) throws SQLException {
        while (resultSet.next()) {
            var id = resultSet.getInt("professor_id");
            var lastName = resultSet.getString("last_name");
            var firstName = resultSet.getString("first_name");
            builder.add(new Professor(id, lastName, firstName));
        }
        return builder.build();
    }

    public Stream<Student> buildStudentStream(ResultSet resultSet, Stream.Builder<Student> builder) throws SQLException {
        while (resultSet.next()) {
            var id = resultSet.getInt("student_id");
            var lastName = resultSet.getString("last_name");
            var firstName = resultSet.getString("first_name");
            builder.add(new Student(id, lastName, firstName));
        }
        return builder.build();
    }

    public Integer savePersonIntoRepository(Connection connection, String sql, Person person) throws SQLException {
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, person.getLastName());
            statement.setString(2, person.getFirstName());
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                return generatedKeys.getInt(1);
            else
                throw new SQLException("Saving person failed.");
        }
    }
}
