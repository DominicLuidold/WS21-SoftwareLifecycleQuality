package at.fhv.unit;

import at.fhv.CredentialsService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class CredentialsServiceTest {

    private CredentialsService _credentialService;

    @BeforeEach
    void setUp() {
        _credentialService = new CredentialsService();
    }

    @Test
    public void testLoginSuccess() {
        // setup
        DelegateExecution delegateExecution = Mockito.mock(DelegateExecution.class);
        ArgumentCaptor<Boolean> credentialsCorrectBoolArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<String> credentialsCorrectNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.when(delegateExecution.getVariable("username")).thenReturn("admin");
        Mockito.when(delegateExecution.getVariable("password")).thenReturn("admin");

        Mockito.doNothing().when(delegateExecution).setVariable(credentialsCorrectNameArgumentCaptor.capture(), credentialsCorrectBoolArgumentCaptor.capture());

        // execute
        _credentialService.execute(delegateExecution);

        // verify
        assertEquals(1, credentialsCorrectNameArgumentCaptor.getAllValues().size());
        assertEquals(1, credentialsCorrectBoolArgumentCaptor.getAllValues().size());

        assertEquals("credentialsCorrect", credentialsCorrectNameArgumentCaptor.getValue());
        assertEquals(true, credentialsCorrectBoolArgumentCaptor.getValue());
    }

    @Test
    public void testInvalidPassword() {
        // setup
        DelegateExecution delegateExecution = Mockito.mock(DelegateExecution.class);
        ArgumentCaptor<Boolean> credentialsCorrectBoolArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<String> credentialsCorrectNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.when(delegateExecution.getVariable("username")).thenReturn("admin");
        Mockito.when(delegateExecution.getVariable("password")).thenReturn("asdf");

        Mockito.doNothing().when(delegateExecution).setVariable(credentialsCorrectNameArgumentCaptor.capture(), credentialsCorrectBoolArgumentCaptor.capture());

        // execute
        _credentialService.execute(delegateExecution);

        // verify
        assertEquals(1, credentialsCorrectNameArgumentCaptor.getAllValues().size());
        assertEquals(1, credentialsCorrectBoolArgumentCaptor.getAllValues().size());

        assertEquals("credentialsCorrect", credentialsCorrectNameArgumentCaptor.getValue());
        assertEquals(false, credentialsCorrectBoolArgumentCaptor.getValue());
    }

    @Test
    public void testInvalidUser() {
        // setup
        DelegateExecution delegateExecution = Mockito.mock(DelegateExecution.class);
        ArgumentCaptor<Boolean> credentialsCorrectBoolArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<String> credentialsCorrectNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.when(delegateExecution.getVariable("username")).thenReturn("asdf");
        Mockito.when(delegateExecution.getVariable("password")).thenReturn("admin");

        Mockito.doNothing().when(delegateExecution).setVariable(credentialsCorrectNameArgumentCaptor.capture(), credentialsCorrectBoolArgumentCaptor.capture());

        // execute
        _credentialService.execute(delegateExecution);

        // verify
        assertEquals(1, credentialsCorrectNameArgumentCaptor.getAllValues().size());
        assertEquals(1, credentialsCorrectBoolArgumentCaptor.getAllValues().size());

        assertEquals("credentialsCorrect", credentialsCorrectNameArgumentCaptor.getValue());
        assertEquals(false, credentialsCorrectBoolArgumentCaptor.getValue());
    }
}