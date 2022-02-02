package at.fhv;

import org.apache.commons.lang3.NotImplementedException;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class ShippingService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        throw new NotImplementedException();
    }
}
