import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

public class MyUmlInterface1 {
    private UmlInterface umlInterface;
    private ArrayList<MyUmlOperation1> operations;
    private HashMap<String, MyUmlOperation1> operationMap;    
    private ArrayList<UmlGeneralization> generalizations;
    
    private int nonReturnOp = -1;
    private int returnOp = -1;
    private int nonParamOp = -1;
    private int paramOp = -1;
    
    public MyUmlInterface1() {
        this(null);
    }
    
    public MyUmlInterface1(UmlInterface inter) {
        umlInterface = inter;        
        operations = new ArrayList<>();
        operationMap = new HashMap<>();        
        generalizations = new ArrayList<>();
    }
    
    public void addElement(UmlElement e) {
        if (e instanceof UmlOperation) {
            MyUmlOperation1 tmpOp = new MyUmlOperation1((UmlOperation)e);
            operations.add(tmpOp);
            operationMap.put(e.getId(), tmpOp);
        } else if (e instanceof UmlGeneralization) {
            generalizations.add((UmlGeneralization) e);
        } else if (e instanceof UmlParameter) {
            String parentId = e.getParentId();
            operationMap.get(parentId).addElement((UmlParameter) e);
        }
        return;
    }

    public int getClassOperationCount(OperationQueryType queryType) {
        if (queryType == OperationQueryType.ALL) {
            return operations.size();
        } else if (queryType == OperationQueryType.NON_RETURN) {
            if (nonReturnOp != -1) {
                //do nothing
            } else {
                int count = 0;
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).hasReturn()) {
                        count++;
                    }
                }
                returnOp = count;
                nonReturnOp = operations.size() - count;
            }
            return nonReturnOp;
        } else if (queryType == OperationQueryType.RETURN) {
            if (returnOp != -1) {
                //do nothing
            } else {
                int count = 0;
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).hasReturn()) {
                        count++;
                    }
                }
                returnOp = count;
                nonReturnOp = operations.size() - count;
            }
            return returnOp;
        } else if (queryType == OperationQueryType.PARAM) {
            if (paramOp != -1) {
                //do nothing
            } else {
                int count = 0;
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).hasParameter()) {
                        count++;
                    }
                }
                paramOp = count;
                nonParamOp = operations.size() - count;
            }
            return paramOp;
        } else if (queryType == OperationQueryType.NON_PARAM) {
            if (nonParamOp != -1) {
                //do nothing
            } else {
                int count = 0;
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).hasParameter()) {
                        count++;
                    }
                }
                paramOp = count;
                nonParamOp = operations.size() - count;
            }
            return nonParamOp;
        }
        return 0;
    }

    public boolean hasSuperClass(HashMap<String, UmlElement> elementMap) {
        if (generalizations.size() == 0) {
            return false;
        } else if (elementMap.get(
                generalizations.get(0).getTarget()).getElementType() == 
                ElementType.UML_CLASS) {
            return true;
        } else {
            return false;
        }
       
    }
    
    protected ArrayList<UmlGeneralization> getGeneralizations() {
        return generalizations;
    }
    
    public int getClassAssociationCount() {
        return 0;
    }
    
    public HashSet<String> getClassAssociatedClassList(
            HashMap<String, UmlElement> elementMap,
            ArrayList<UmlAssociation> assoList) {
        //TODO interface associatedClassList
        umlInterface.getId();
        return null;
    }
    
    public Map<Visibility, Integer> getClassOperationVisibility(
            String operationName) {
        HashMap<Visibility, Integer> vsMap = new HashMap<>();
        int publicC = 0;
        int packageC = 0;
        int protectedC = 0;
        int privateC = 0;        
        for (MyUmlOperation1 op : operations) {
            if (op.getOperation().getName().equals(operationName)) {
                switch (op.getOperation().getVisibility()) {
                    case PUBLIC:
                        publicC++;
                        break;
                    case PACKAGE:
                        packageC++;
                        break;
                    case PROTECTED:
                        protectedC++;
                        break;
                    case PRIVATE:
                        privateC++;
                        break;
                    default:
                        break;
                }
                
            }
        }
        vsMap.put(Visibility.PUBLIC, publicC);
        vsMap.put(Visibility.PACKAGE, packageC);
        vsMap.put(Visibility.PROTECTED, protectedC);
        vsMap.put(Visibility.PRIVATE, privateC);
        return vsMap;
    }
    
    public HashSet<String> getImplementInterfaceList(
            HashMap<String, UmlElement> elementMap,
            HashMap<String, MyUmlInterface1> myInterfaceMap,
            HashMap<String, MyUmlClass1> myClassMap) {
        HashSet<String> idSet = new HashSet<>();
        for (UmlGeneralization ge : generalizations) {
            String interfaceId = ge.getTarget();
            idSet.add(interfaceId);
            String superName = elementMap.get(interfaceId).getName();
            MyUmlInterface1 superInterface = myInterfaceMap.get(superName);
            idSet.addAll(superInterface.getImplementInterfaceList(
                    elementMap, myInterfaceMap, myClassMap));
        }
        return idSet;
    }
    
}
