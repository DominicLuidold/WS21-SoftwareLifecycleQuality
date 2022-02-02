package at.fhv.acceptance;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "camunda.bpm.generate-unique-process-engine-name=true",
                "camunda.bpm.generate-unique-process-application-name=true",
                "spring.datasource.generate-unique-name=true",
        }
)
@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfiguration.class)
public class CredentialsServiceStepDefs {
    private static final String activityID = "Process_Book_Lending";

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    ProcessEngine processEngine;

    @Autowired
    RuntimeService runtimeService;
    ProcessInstance instance;

    private String password;
    private String username;

    @Before
    public void setUp() {
        init(this.processEngine);
    }

    @After
    public void tearDown() {
        System.out.println("Stopping a Credentials Service scenario");
    }


    @Given("A login task for the CredentialsService exists and runs")
    public void aLoginTaskForTheCredentialsServiceExistsAndRuns() {
        this.instance = this.runtimeService.startProcessInstanceByKey(activityID);
        assertThat(this.instance).isNotNull();
        assertThat(this.instance).isStarted();
    }

    @When("I enter the username {string}")
    public void iEnterTheUsername(String arg0) {
        this.username = arg0;
    }

    @And("I enter the password {string}")
    public void iEnterThePassword(String arg0) {
        this.password = arg0;
    }

    @Then("I have finished the task")
    public void iHaveFinishedTheTask() {
        complete(task(this.instance), withVariables(
                "username", username,
                "password", password
        ));
        assertThat(this.instance).isWaitingAt("Activity_Browse_Books"); // follow-up task
    }

    @Then("I have to decide if I want to do the task again")
    public void iHaveToDecideIfIWantToDoTheTaskAgain() {
        complete(task(this.instance), withVariables(
                "username", username,
                "password", password
        ));
        assertThat(this.instance).isWaitingAt("Activity_Decide_To_Enter_Credentials_Again");
    }
}
