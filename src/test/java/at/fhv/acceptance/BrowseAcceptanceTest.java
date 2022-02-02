package at.fhv.acceptance;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.init;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.withVariables;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class BrowseAcceptanceTest {

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    ProcessEngine processEngine;

    @Autowired
    RuntimeService runtimeService;

    @BeforeEach
    public void setUp() {
        init(processEngine);
    }

    @Test
    @Deployment
    public void testLoginProcessAndStopAtBrowsingProcess() {
        ProcessInstance process = runtimeService.startProcessInstanceByKey("Process_Book_Lending");
        assertThat(process)
            .isNotNull()
            .isStarted();

        // Login
        complete(task(process), withVariables(
            "username","admin",
            "password", "admin"
        ));
        assertThat(process)
            .hasPassed("Lending_Books_Started")
            .hasPassed("Activity_Enter_Login_Credentials")
            .hasPassed("Activity_Check_Login_Credentials")
            .hasPassed("Gateway_Credentials_Correct")
            .hasNotPassed("Activity_Browse_Books");
    }

    @Test
    @Deployment
    public void testBrowseBooksSubProcess() {
        ProcessInstance process = runtimeService.startProcessInstanceByKey("browse_books");
        assertThat(process)
            .isNotNull()
            .isStarted();

        // Search for Books
        complete(task(process), withVariables("name", "Benjamin Blümchen, Band 1"));
        assertThat(process)
            .hasPassed("Browsing_Books_Started")
            .hasPassed("Activity_Search_Books")
            .hasPassed("Activity_Check_Availability")
            .hasPassed("Gateway_Book_Available")
            .hasNotPassed("Activity_Borrow_Book_Decider");

        // Say yes to borrowing
        complete(task(process), withVariables("borrowing", true));
        assertThat(process)
            .hasPassed("Activity_Borrow_Book_Decider")
            .hasPassed("Gateway_Borrowing")
            .hasPassed("Activity_Update_Cart")
            .hasNotPassed("Activity_Review_Selected");

        // Review books
        complete(task(process), withVariables("browsingDone", true));
        assertThat(process)
            .hasPassed("Activity_Review_Selected")
            .hasPassed("Gateway_Browsing_Done")
            .hasPassed("Browsing_Done")
            .isEnded();
    }

    @Test
    @Deployment
    public void testCompleteProcess() {
        ProcessInstance process = runtimeService.startProcessInstanceByKey("Process_Book_Lending");
        assertThat(process)
            .isNotNull()
            .isStarted();

        // Login
        complete(task(process), withVariables(
            "username","admin",
            "password", "admin"
        ));
        assertThat(process)
            .hasPassed("Lending_Books_Started")
            .hasPassed("Activity_Enter_Login_Credentials")
            .hasPassed("Activity_Check_Login_Credentials")
            .hasPassed("Gateway_Credentials_Correct")
            .hasNotPassed("Activity_Browse_Books");

        // Run SubProcess
        ProcessInstance subProcess = processEngine
            .getRuntimeService()
            .createProcessInstanceQuery()
            .superProcessInstanceId(process.getId())
            .singleResult();

        complete(task(subProcess), withVariables("name", "Benjamin Blümchen, Band 1"));
        assertThat(subProcess)
            .hasPassed("Browsing_Books_Started")
            .hasPassed("Activity_Search_Books")
            .hasPassed("Activity_Check_Availability")
            .hasPassed("Gateway_Book_Available")
            .hasNotPassed("Activity_Borrow_Book_Decider");

        complete(task(subProcess), withVariables("borrowing", true));
        assertThat(subProcess)
            .hasPassed("Activity_Borrow_Book_Decider")
            .hasPassed("Gateway_Borrowing")
            .hasPassed("Activity_Update_Cart")
            .hasNotPassed("Activity_Review_Selected");

        complete(task(subProcess), withVariables("browsingDone", true));
        assertThat(subProcess)
            .hasPassed("Activity_Review_Selected")
            .hasPassed("Gateway_Browsing_Done")
            .hasPassed("Browsing_Done")
            .isEnded();

        // Rest of process
        complete(task(process), withVariables("orderAccepted", true));
        assertThat(process)
            .hasPassed("Activity_Browse_Books")
            .hasPassed("Activity_Count_Books")
            .hasPassed("Activity_Delivery_Cost_Lookup_Table")
            .hasPassed("Activity_Accept_Decline_Order")
            .hasPassed("Gateway_Order_Accept")
            .hasPassed("Activity_Print_Shipping_Label")
            .hasPassed("Activity_Collect_Books")
            .hasPassed("Activity_Package_Books")
            .hasPassed("Activity_Ship_Books")
            .hasPassed("Lending_Books_Ended")
            .isEnded();
    }
}
