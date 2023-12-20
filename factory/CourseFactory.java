package factory;


import domain.Course;
import domain.CourseType;
import domain.Student;
import persistence.JdbcProfessorRepository;
import persistence.JdbcStudentRepository;

import java.sql.*;
import java.util.stream.Stream;

public class CourseFactory {
    public CourseFactory() {

    }

    public Stream<Course> buildCourseStream(ResultSet resultSet, Stream.Builder<Course> builder, Connection connection) throws SQLException {
        //only works with join in repo
        while (resultSet.next()) {
            var id = resultSet.getInt("course_id");
            var courseTypeId = resultSet.getString("type_id");
            var professorId = resultSet.getInt("professor_id");
            var description = resultSet.getString("description");
            var beginDate = resultSet.getDate("begin_date");
            var courseTypeDescription = resultSet.getString("course_type_description");

            var professorRepo = new JdbcProfessorRepository(connection);

            var optionalProfessor = professorRepo.findById(professorId);
            if(optionalProfessor.isEmpty()){
                throw new IllegalArgumentException("no professor for course");
            }

            var professor = optionalProfessor.get();
            var courseType = new CourseType(courseTypeId.charAt(0), courseTypeDescription);

            var course = new Course(id, courseType, professor, description, beginDate.toLocalDate());
            builder.add(course);
        }
        return builder.build();
    }

    public Integer saveCourseIntoRepository(Connection connection, String sql, Course course) throws SQLException{
        try (var statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, String.valueOf(course.getType().getId()));
            statement.setInt(2, course.getProfessor().getId());
            statement.setString(3, course.getDescription());
            statement.setDate(4, Date.valueOf(course.getBegin()));
            statement.executeUpdate();
            var generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next())
                return generatedKeys.getInt(1);
            else
                throw new SQLException("Saving course failed.");
        }
    }

    public void saveEnrollment(Connection connection, String sql, Course course, Student student) throws SQLException {
        var studentRepository = new JdbcStudentRepository(connection);
        var optionalStudent = studentRepository.findById(student.getId());
        if(optionalStudent.isEmpty()){
            throw new SQLException("student is not persisted");
        }

        try(var statement = connection.prepareStatement(sql)){
            statement.setInt(1, course.getId());
            statement.setInt(2, student.getId());
            statement.executeUpdate();
        }
    }

    public void deleteEnrollment(Connection connection, String sql, Course course, Student student) throws SQLException {
        try(var statement = connection.prepareStatement(sql)){
            statement.setInt(1, course.getId());
            statement.setInt(2, student.getId());
            var amountOfUpdates = statement.executeUpdate();
            if(amountOfUpdates < 1){
                throw new IllegalArgumentException("student was never enrolled");
            }
        }
    }
}
