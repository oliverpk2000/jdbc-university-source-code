package domain;


import validator.PersonValidator;

public class Professor extends Person {

    private Integer id;
    private final String lastName;
    private final String firstName;

    public Professor(Integer id, String lastName, String firstName) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;

        var personValidator = new PersonValidator(this);
        if (personValidator.invalidate()) {
            throw new IllegalArgumentException("firstName or lastName blank/null");
        }
    }

    public Professor(String lastName, String firstName) {
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

        Professor professor = (Professor) o;

        return getId() != null ? getId().equals(professor.getId()) : professor.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public void setId(int id) {
        this.id = id;
    }
}
