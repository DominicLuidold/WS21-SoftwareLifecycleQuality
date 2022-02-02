package at.fhv;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;

public class CountService implements JavaDelegate {

    @Override
    @SuppressWarnings("unchecked") // hihi :D
    public void execute(DelegateExecution delegateExecution) {
        ArrayList<String> booksInCart = (ArrayList<String>) delegateExecution.getVariable("booksInCart");

        delegateExecution.setVariable("bookCount", booksInCart.size());
    }
}
