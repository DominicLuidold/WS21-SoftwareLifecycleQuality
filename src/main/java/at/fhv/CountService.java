package at.fhv;

import java.util.ArrayList;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CountService implements JavaDelegate {

    @Override
    @SuppressWarnings("unchecked") // hihi :D
    public void execute(DelegateExecution delegateExecution) {
        ArrayList<String> booksInCart = (ArrayList<String>) delegateExecution.getVariable("booksInCart");

        delegateExecution.setVariable("bookCount", booksInCart.size());
    }
}
