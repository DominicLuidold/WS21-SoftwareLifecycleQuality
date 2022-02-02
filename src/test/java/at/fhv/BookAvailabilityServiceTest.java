package at.fhv;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class BookAvailabilityServiceTest {

    private BookAvailabilityService _bookAvailabilityService;

    @BeforeEach
    void setUp() {
        _bookAvailabilityService = new BookAvailabilityService();
    }

    @Test
    public void testBookExistsAndIsAvailable() {
        // setup
        DelegateExecution delegateExecution = Mockito.mock(DelegateExecution.class);
        ArgumentCaptor<Boolean> availableBookArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<String> variableNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.when(delegateExecution.getVariable("name")).thenReturn("Benjamin Blümchen, Band 1");

        Mockito.doNothing().when(delegateExecution).setVariable(variableNameArgumentCaptor.capture(), availableBookArgumentCaptor.capture());

        // execute
        _bookAvailabilityService.execute(delegateExecution);

        // verify
        assertEquals(1, variableNameArgumentCaptor.getAllValues().size());
        assertEquals(1, availableBookArgumentCaptor.getAllValues().size());

        assertEquals("available", variableNameArgumentCaptor.getValue());
        assertEquals(true, availableBookArgumentCaptor.getValue());
    }

    @Test
    public void testBookExistsButNotAvailable() {
        // setup
        DelegateExecution delegateExecution = Mockito.mock(DelegateExecution.class);
        ArgumentCaptor<Boolean> variableValueArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<String> variableNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.when(delegateExecution.getVariable("name")).thenReturn("Benjamin Blümchen, Special");

        Mockito.doNothing().when(delegateExecution).setVariable(variableNameArgumentCaptor.capture(), variableValueArgumentCaptor.capture());

        // execute
        _bookAvailabilityService.execute(delegateExecution);

        // verify
        assertEquals(2, variableNameArgumentCaptor.getAllValues().size());
        assertEquals(2, variableValueArgumentCaptor.getAllValues().size());

        assertEquals("error", variableNameArgumentCaptor.getAllValues().get(0));
        assertEquals("available", variableNameArgumentCaptor.getAllValues().get(1));
        assertEquals(false, variableValueArgumentCaptor.getAllValues().get(1));
        assertEquals("There is not enough stock left of this book! :(", variableValueArgumentCaptor.getAllValues().get(0));
    }

    @Test
    public void testBookNotExistsNotAvailable() {
        // setup
        DelegateExecution delegateExecution = Mockito.mock(DelegateExecution.class);
        ArgumentCaptor<Boolean> variableValueArgumentCaptor = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<String> variableNameArgumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.when(delegateExecution.getVariable("name")).thenReturn("Benjamin Blümchen, Band X");

        Mockito.doNothing().when(delegateExecution).setVariable(variableNameArgumentCaptor.capture(), variableValueArgumentCaptor.capture());

        // execute
        _bookAvailabilityService.execute(delegateExecution);

        // verify
        assertEquals(2, variableNameArgumentCaptor.getAllValues().size());
        assertEquals(2, variableValueArgumentCaptor.getAllValues().size());

        assertEquals("error", variableNameArgumentCaptor.getAllValues().get(0));
        assertEquals("available", variableNameArgumentCaptor.getAllValues().get(1));
        assertEquals(false, variableValueArgumentCaptor.getAllValues().get(1));
        assertEquals("The book with title 'Benjamin Blümchen, Band X' does not exist.. :(", variableValueArgumentCaptor.getAllValues().get(0));
    }
}