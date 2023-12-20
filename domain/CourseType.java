package domain;

import validator.Validator;

public final class CourseType implements Validator {
    private final char id;
    private final String description;


    public CourseType(char id, String description) {
        this.id = id;
        this.description = description;
        if(invalidate()){
            throw new IllegalArgumentException("description was blank/null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CourseType that = (CourseType) o;

        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean invalidate() {
        if (description == null) {
            return true;
        }
        return description.isBlank();
    }

    public char getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "CourseType[" +
            "id=" + id + ", " +
            "description=" + description + ']';
    }

}
