package nl.hkstwk.calculationmodule.builder;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class Person {
    private final String firstName;
    private final String lastName;
    private final List<String> middleNames;
    private final String gender;
    private final String email;

    Person(Builder<?> builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.middleNames = builder.middleNames;
        this.gender = builder.gender;
        this.email = builder.email;
    }

    public static FirstNameStep<?> builder() {
        return new Builder<>();
    }

    // --- Step interfaces ---
    public interface FirstNameStep<T> {
        LastNameStep<T> firstName(String firstName);
    }

    public interface LastNameStep<T> {
        OptionalFieldsStep<T> lastName(String lastName);
    }

    public interface OptionalFieldsStep<T> {
        OptionalFieldsStep<T> middleNames(List<String> middleNames);
        OptionalFieldsStep<T> gender(String gender);
        OptionalFieldsStep<T> email(String email);
        Person build();
    }

    // --- Builder implementation ---
    public static class Builder<T extends Builder<T>>
            implements FirstNameStep<T>, LastNameStep<T>, OptionalFieldsStep<T> {

        private String firstName;
        private String lastName;
        private List<String> middleNames;
        private String gender;
        private String email;

        @Override
        public LastNameStep<T> firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        @Override
        public OptionalFieldsStep<T> lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        @Override
        public OptionalFieldsStep<T> middleNames(List<String> middleNames) {
            this.middleNames = middleNames;
            return this;
        }

        @Override
        public OptionalFieldsStep<T> gender(String gender) {
            this.gender = gender;
            return this;
        }

        @Override
        public OptionalFieldsStep<T> email(String email) {
            this.email = email;
            return this;
        }

        @Override
        public Person build() {
            return new Person(this);
        }

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }
    }
}
