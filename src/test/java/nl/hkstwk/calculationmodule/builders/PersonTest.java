package nl.hkstwk.calculationmodule.builders;

import nl.hkstwk.calculationmodule.builder.Employee;
import nl.hkstwk.calculationmodule.builder.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PersonTest {

    @Test
    void ShouldHaveAllFieldsPopulated() {
        Person person = Person.builder().firstName("Harm").lastName("Kolvoort").gender("man").middleNames(List.of("van", "der")).email("mail@mailserver.com").build();

        assertThat(person.getFirstName()).isEqualToIgnoringCase("harm");
        assertThat(person.getLastName()).isEqualToIgnoringCase("kolvoort");
        assertThat(person.getMiddleNames()).isEqualTo(List.of("van", "der"));
        assertThat(person.getGender()).isEqualToIgnoringCase("man");
        assertThat(person.getEmail()).contains("mailserver");
    }

    @Test
    void shouldHaveOnlyFirstAndLastName() {
        Person person = Person.builder().firstName("firstName").lastName("lastName").build();

        assertThat(person.getFirstName()).isEqualToIgnoringCase("firstname");
        assertThat(person.getLastName()).isEqualToIgnoringCase("lastname");
        assertThat(person.getMiddleNames()).isNull();
        assertThat(person.getGender()).isNull();
        assertThat(person.getEmail()).isNull();
    }

    @Test
    void shouldHaveFirstAndLastNamePlusOptionalEmail() {
        Person person = Person.builder().firstName("fName").lastName("lName").email("mail@mail.nl").build();

        assertThat(person.getFirstName()).isEqualToIgnoringCase("fname");
        assertThat(person.getLastName()).isEqualToIgnoringCase("lname");
        assertThat(person.getMiddleNames()).isNull();
        assertThat(person.getGender()).isNull();
        assertThat(person.getEmail()).contains("mail@");
    }

    @Test
    void employeeTest() {
        Employee employee = (Employee) Employee.builder()
                .firstName("joepie")
                .lastName("poepie")
                .middleNames(List.of("de"))
                .gender("male")
                .email("emp@mail.nl").build();
    }
}