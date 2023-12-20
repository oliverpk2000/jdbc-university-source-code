package persistence;

import domain.*;
import factory.CourseFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record JdbcCourseRepository(Connection connection) implements CourseRepository {

    @Override
    public List<Course> findAll() throws SQLException {
        var sql = """
            SELECT c.course_id, c.type_id, c.professor_id, c.description, c.begin_date, ct.description AS course_type_description
            FROM courses c
            JOIN course_types ct ON c.type_id = ct.type_id
            """;
        try (var statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            var builder = Stream.<Course>builder();
            var courseFactory = new CourseFactory();
            return courseFactory
                .buildCourseStream(resultSet, builder, connection)
                .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    @Override
    public List<Course> findAllByProfessor(Professor professor) throws SQLException {
        var sql = """
            SELECT c.course_id, c.type_id, c.professor_id, c.description, c.begin_date, ct.description AS course_type_description
            FROM courses c
            JOIN course_types ct ON c.type_id = ct.type_id
            WHERE c.professor_id = ?
            """;
        return getCourses(connection, sql, professor);
    }

    public List<Course> getCourses(Connection connection, String sql, Person person) throws SQLException {
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, person.getId());
            var resultSet = statement.executeQuery();
            var builder = Stream.<Course>builder();
            var courseFactory = new CourseFactory();
            return courseFactory
                .buildCourseStream(resultSet, builder, connection)
                .collect(Collectors.toCollection(ArrayList::new));
        }
    }

    @Override
    public Optional<Course> findById(int id) throws SQLException {
        var sql = """
            SELECT c.course_id, c.type_id, c.professor_id, c.description, c.begin_date, ct.description AS course_type_description
            FROM courses c
            JOIN course_types ct ON c.type_id = ct.type_id
            WHERE c.course_id = ?
            """;
        try (var statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            var resultSet = statement.executeQuery();
            var builder = Stream.<Course>builder();
            var courseFactory = new CourseFactory();
            return courseFactory
                .buildCourseStream(resultSet, builder, connection)
                .findFirst();
        }
    }

    @Override
    public Course save(Course course) throws SQLException {
        if (course.getId() != null) {
            throw new IllegalArgumentException("already has id");
        }
        if (course.getBegin().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("course started in the past");
        }
        var sql =
            """
                INSERT INTO courses (type_id, professor_id, description, begin_date)
                VALUES (?, ?, ?, ?)
                """;
        var courseFactory = new CourseFactory();
        var courseId = courseFactory.saveCourseIntoRepository(connection, sql, course);
        course.setId(courseId);
        return course;
    }

    @Override
    public List<Course> findAllByStudent(Student student) throws SQLException {
        var sql = """
            SELECT c.course_id, c.type_id, c.professor_id, c.description, c.begin_date, ct.description AS course_type_description
            FROM courses c
            JOIN course_types ct ON c.type_id = ct.type_id
            WHERE c.course_id IN
                (SELECT course_id
                 FROM courses_students
                 WHERE student_id = ?)
            """;
        return getCourses(connection, sql, student);
    }

    @Override
    public void enrollInCourse(Student student, Course course) throws SQLException {
        var sql = """
            INSERT INTO courses_students (course_id, student_id)
            VALUES (?,?)
            """;
        var courseFactory = new CourseFactory();
        courseFactory.saveEnrollment(connection, sql, course, student);
    }

    @Override
    public void unenrollFromCourse(Student student, Course course) throws SQLException {
        var sql = """
            DELETE FROM courses_students
            WHERE course_id = ? AND student_id = ?
            """;
        var courseFactory = new CourseFactory();
        courseFactory.deleteEnrollment(connection, sql, course, student);

    }
}
