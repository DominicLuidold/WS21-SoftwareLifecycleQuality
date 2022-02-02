package at.fhv;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import java.util.HashMap;

public final class CredentialsService implements JavaDelegate {
    private final HashMap<String, String> credentials = new HashMap<>();

    public CredentialsService() {
        // Here could be your safe implementation with a database call :)
        credentials.put("admin", "admin");
        credentials.put("michi", "michi");
        credentials.put("lukas", "lukas");
        credentials.put("dominic", "dominic");
    }

    @Override
    public void execute(DelegateExecution delegateExecution) {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");

        boolean credentialsCorrect = areCredentialsCorrect(username, password);
        delegateExecution.setVariable("credentialsCorrect", credentialsCorrect);
    }

    private boolean areCredentialsCorrect(String username, String providedPassword) {
        if (!credentials.containsKey(username)) {
            return false;
        }

        String password = credentials.get(username);

        return password.equals(providedPassword);
    }
}
