import java.util.ArrayList;

import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

public class MyUmlOperation {
    private UmlOperation umlOperation;
    private ArrayList<UmlParameter> paraList;
    
    private boolean hasReturn;
    private boolean hasParameter;
    
    public MyUmlOperation(UmlOperation op) {
        umlOperation = op;
        paraList = new ArrayList<>();
        hasReturn = false;
        hasParameter = false;
    }
    
    public void addPara(UmlParameter e) {
        paraList.add(e);
        if (((UmlParameter)e).getDirection() == Direction.RETURN) {
            hasReturn = true;
        } else if (((UmlParameter)e).getDirection() == Direction.IN) {
            hasParameter = true;
        }
    }
    
    public UmlOperation getOperation() {
        return umlOperation;
    }
    
    public boolean hasReturn() {
        return hasReturn;
    }
    
    public boolean hasParameter() {
        return hasParameter;
    }
    
}
