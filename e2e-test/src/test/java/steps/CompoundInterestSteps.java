package steps;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import support.AngularContainer;
import support.BackendContainer;
import support.DatabaseContainer;
import support.PlaywrightSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class CompoundInterestSteps {

    @Before
    public void setup() {
        BackendContainer.ensureRunning();
        DatabaseContainer.ensureRunning();
        AngularContainer.ensureRunning();
    }

    @After
    public void tearDown() {
        PlaywrightSupport.closePage();
    }

    @When("I open the account menu")
    public void openAccountMenu() {
        PlaywrightSupport.page()
                .getByRole(AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Account menu"))
                .click();
    }

    @Given("the calculator page is open")
    public void openPage() {
        String url = System.getProperty("angular.url");
        PlaywrightSupport.page().navigate(url);
    }

    @When("I enter principal {string}")
    public void enterPrincipal(String value) {
        PlaywrightSupport.page().fill("#originalPrincipalSum", value);
    }

    @When("I enter rate {double}")
    public void enterRate(Double value) {
        PlaywrightSupport.page().fill("#nominalAnnualInterestRate", String.valueOf(value));
    }

    @And("I enter monthtly deposit {int}")
    public void enterMonthtlyDeposit(Integer value) {
        PlaywrightSupport.page().fill("#monthlyDeposit", String.valueOf(value));
    }

    @When("I enter frequency {int}")
    public void enterFrequency(Integer value) {
        PlaywrightSupport.page().fill("#compoundingFrequency", String.valueOf(value));
    }

    @When("I enter years {int}")
    public void enterYears(Integer value) {
        PlaywrightSupport.page().fill("#time", String.valueOf(value));
    }

    @When("I submit the form")
    public void submitForm() {
        PlaywrightSupport.page().getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
    }

    @Then("I should see result {string}")
    public void verifyResult(String expected) {
        String text = PlaywrightSupport.page().textContent("#result");
        assertThat(text).isEqualTo(expected);
    }

}
