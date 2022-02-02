package at.fhv;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;

public class BookAvailabilityService implements JavaDelegate {
    private final HashMap<String, Integer> books = new HashMap<>();

    public BookAvailabilityService() {
        books.put("Benjamin Blümchen, Band 1", 1);
        books.put("Benjamin Blümchen, Band 2", 1);
        books.put("Benjamin Blümchen, Band 3", 1);
        books.put("Benjamin Blümchen, Band 4", 1);
        books.put("Benjamin Blümchen, Special", 0);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String bookName = (String) delegateExecution.getVariable("name");

        if (doesBookExist(bookName)) {
            if (isBookAvailable(bookName)) {
                delegateExecution.setVariable("available", true);
            }
            else {
                delegateExecution
                        .setVariable("error", "There is not enough stock left of this book! :(");
                delegateExecution.setVariable("available", false);
            }
        }
        else {
            delegateExecution
                    .setVariable("error", "The book with title '" + bookName + "' does not exist.. :(");
            delegateExecution.setVariable("available", false);
        }
    }

    private boolean doesBookExist(String searchedBook) {
        return books.containsKey(searchedBook);
    }

    private boolean isBookAvailable(String searchedBook) {
        return books.get(searchedBook) >= 1;
    }
}
