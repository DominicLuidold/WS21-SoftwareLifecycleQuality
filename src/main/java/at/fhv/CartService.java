package at.fhv;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.ArrayList;
import java.util.List;

public class CartService implements JavaDelegate {

    @Override
    @SuppressWarnings("unchecked")
    public void execute(DelegateExecution delegateExecution) {
        boolean borrowing = (boolean) delegateExecution.getVariable("borrowing");
        String book = (String) delegateExecution.getVariable("name");

        List<String> booksInCart;
        if (delegateExecution.hasVariable("booksInCart")) {
            booksInCart = (ArrayList<String>) delegateExecution.getVariable("booksInCart");
        }
        else {
            booksInCart = new ArrayList<>();
        }

        if (borrowing) {
            booksInCart.add(book);
        }
        delegateExecution.setVariable("booksInCart", booksInCart);

        StringBuilder selectedBooks = new StringBuilder();
        for (String selectedBook : booksInCart) {
            selectedBooks.append("'").append(selectedBook).append("'; ");
        }
        delegateExecution.setVariable("selectedBooks", selectedBooks.toString());
    }
}
