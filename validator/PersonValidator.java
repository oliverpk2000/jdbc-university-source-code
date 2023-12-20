package validator;

import domain.Person;

public class PersonValidator implements Validator {
    private final Person person;

    public PersonValidator(Person person) {
        this.person = person;
    }

    @Override
    public boolean invalidate() {
        if (person.getLastName().isBlank()) {
            return true;
        }
        return person.getFirstName().isBlank();
    }
}
