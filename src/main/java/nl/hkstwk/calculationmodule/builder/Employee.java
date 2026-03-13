package nl.hkstwk.calculationmodule.builder;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class Employee extends Person {

    private final String employeeId;

    private Employee(Builder builder) {
        super(builder);
        this.employeeId = builder.employeeId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public interface EmployeeOptionalFieldsStep<T>
            extends Person.OptionalFieldsStep<T> {

        EmployeeOptionalFieldsStep<T> employeeId(String employeeId);

        @Override
        Employee build();
    }

    public static class Builder
            extends Person.Builder<Builder>
            implements EmployeeOptionalFieldsStep<Builder> {

        private String employeeId;

        @Override
        public EmployeeOptionalFieldsStep<Builder> lastName(String lastName) {
            super.lastName(lastName);
            return this;
        }

        @Override
        public EmployeeOptionalFieldsStep<Builder> employeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        @Override
        public Employee build() {
            return new Employee(this);
        }
    }
}
