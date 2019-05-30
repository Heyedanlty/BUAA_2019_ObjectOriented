import java.util.ArrayList;

import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

public class MyUmlOperation1 {
    private UmlOperation operation;
    private ArrayList<UmlParameter> parameters;
    
    private boolean hasReturnValue;
    private boolean hasParameter;
    
    public MyUmlOperation1() {
        this(null);
    }
    
    public MyUmlOperation1(UmlOperation op) {
        operation = op;
        parameters = new ArrayList<>();
        hasReturnValue = false;
        hasParameter = false;
    }
    
    public void addElement(UmlParameter para) {
        parameters.add(para);
        if (para.getDirection() == Direction.RETURN) {
            hasReturnValue = true;
        }
        if (para.getDirection() == Direction.IN) {
            hasParameter = true;
        }
    }
    
    public UmlOperation getOperation() {
        return operation;
    }
    
    public boolean hasParameter() {
        return hasParameter;
    }
    
    public boolean hasReturn() {
        return hasReturnValue;
    }
}
