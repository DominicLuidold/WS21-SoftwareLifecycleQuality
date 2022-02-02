package at.fhv;

import java.util.ArrayList;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ShippingService implements JavaDelegate {

    @Override
    @SuppressWarnings("unchecked")
    public void execute(DelegateExecution delegateExecution) {
        System.out.println("Shipping to: " + delegateExecution.getVariable("username"));
        System.out.println("Delivery cost: " + delegateExecution.getVariable("deliveryCost") + "€");
        System.out.println("Books in cart: ");

        int i = 1;
        for (String book : (ArrayList<String>) delegateExecution.getVariable("booksInCart")) {
            System.out.println("#" + (i++) + ": " + book);
        }
    }
}
