package at.fhv;

import java.util.HashMap;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class BookAvailabilityService implements JavaDelegate {
    private final HashMap<String, Integer> books = new HashMap<>();

    public BookAvailabilityService() {
        books.put("Benjamin Blümchen, Band 1", 1);
        books.put("Benjamin Blümchen, Band 2", 3);
        books.put("Benjamin Blümchen, Band 3", 2);
        books.put("Benjamin Blümchen, Band 4", 5);
        books.put("Benjamin Blümchen, Special", 0);
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String bookName = (String) delegateExecution.getVariable("name");

        boolean isAvailable = isBookAvailable(bookName);
        delegateExecution.setVariable("available", isAvailable);
    }

    private boolean isBookAvailable(String searchedBook) {
        if (!books.containsKey(searchedBook)) {
            return false;
        }

        return books.get(searchedBook) >= 1;
    }
}
