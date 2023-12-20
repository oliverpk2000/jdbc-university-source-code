package domain;


import validator.PersonValidator;

public class Student extends Person {

    private Integer id;
    private String lastName;
    private String firstName;

    public Student(Integer id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;

        var personValidator = new PersonValidator(this);
        if (personValidator.invalidate()) {
            throw new IllegalArgumentException("firstName or lastName blank/null");
        }
    }

    public Student(String lastName, String firstName) {
        this(null, lastName, firstName);
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return getId() != null ? getId().equals(student.getId()) : student.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(int id) {
        this.id = id;
    }
}
