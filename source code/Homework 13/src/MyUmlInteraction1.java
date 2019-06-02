import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

public class MyUmlInteraction1 implements UmlInteraction {
    private HashMap<String, UmlElement> elementMap;//id2ele
    private HashMap<String, MyUmlClass1> classMap;//id2class
    private HashMap<String, MyUmlInter1> interMap;//id2interface    

    private HashMap<String, Integer> classCount;//name2classCount
    private HashMap<String, Integer> interCount;//name2interCount
    
    private HashMap<String, MyUmlClass1> classNameMap;//name2class
    private HashMap<String, MyUmlInter1> interNameMap;//name2inter
    
    private HashMap<String, UmlAssociation> assoMap;//id2asso
    private HashMap<String, UmlAssociationEnd> endMap;//id2end
    
    public MyUmlInteraction1(UmlElement... umlElements) {
        elementMap = new HashMap<>();
        classMap = new HashMap<>();
        interMap = new HashMap<>();
        classCount = new HashMap<>();
        interCount = new HashMap<>();
        classNameMap = new HashMap<>();
        interNameMap = new HashMap<>();
        assoMap = new HashMap<>();
        endMap = new HashMap<>();
        List<UmlAttribute> atList = new ArrayList<>();
        List<UmlOperation> opList = new ArrayList<>();
        List<UmlParameter> paList = new ArrayList<>();
        List<UmlGeneralization> geList = new ArrayList<>();
        List<UmlInterfaceRealization> irList = new ArrayList<>();
        for (int i = 0; i < umlElements.length; i++) {
            UmlElement e = umlElements[i];
            elementMap.put(e.getId(), e);
            if (e instanceof UmlClass) {
                addElement((UmlClass) e);
            } else if (e instanceof UmlInterface) {
                addElement((UmlInterface) e);
            } else if (e instanceof UmlAssociation) {
                addElement((UmlAssociation) e);
            } else if (e instanceof UmlAssociationEnd) {
                addElement((UmlAssociationEnd) e);
            } else if (e instanceof UmlAttribute) {
                atList.add((UmlAttribute) e);
            } else if (e instanceof UmlOperation) {
                opList.add((UmlOperation) e);
            } else if (e instanceof UmlParameter) {
                paList.add((UmlParameter) e);
            } else if (e instanceof UmlGeneralization) {
                geList.add((UmlGeneralization) e);
            } else if (e instanceof UmlInterfaceRealization) {
                irList.add((UmlInterfaceRealization) e);
            }
        }
        for (UmlAttribute at : atList) {
            addElement(at);
        }
        for (UmlOperation op : opList) {
            addElement(op);
        }
        for (UmlParameter pa : paList) {
            addElement(pa);
        }
        for (UmlGeneralization ge : geList) {
            addElement(ge);
        }
        for (UmlInterfaceRealization ir : irList) {
            addElement(ir);
        }
    }
    
    private void addElement(UmlClass e) {
        String id = e.getId();
        String name = e.getName();
        MyUmlClass1 tmp = new MyUmlClass1(e);
        classMap.put(id, tmp);
        classNameMap.put(name, tmp);
        if (classCount.containsKey(name)) {
            Integer count = classCount.get(name);
            classCount.replace(name, count, count + 1);
        } else {
            classCount.put(name, 1);
        }
    }
    
    private void addElement(UmlInterface e) {
        String id = e.getId();
        String name = e.getName();
        MyUmlInter1 tmp = new MyUmlInter1(e);
        interMap.put(id, tmp);
        interNameMap.put(name, tmp);
        if (interCount.containsKey(name)) {
            Integer count = interCount.get(name);
            interCount.replace(name, count, count + 1);
        } else {
            interCount.put(name, 1);
        }
    }
    
    private void addElement(UmlAssociation e) {
        assoMap.put(e.getId(), e);
    }
    
    private void addElement(UmlAssociationEnd e) {
        endMap.put(e.getId(), e);
    }
    
    private void addElement(UmlAttribute e) {
        String classId = e.getParentId();
        classMap.get(classId).addElement(e);
    }
    
    private void addElement(UmlOperation e) {
        String parentId = e.getParentId();
        if (classMap.containsKey(parentId)) {
            classMap.get(parentId).addElement(e);
        } else if (interMap.containsKey(parentId)) {
            interMap.get(parentId).addElement(e);
        }
    }
    
    private void addElement(UmlParameter e) {
        String opId = e.getParentId();
        String parentId = ((UmlOperation) elementMap.get(opId)).getParentId();
        if (classMap.containsKey(parentId)) {
            classMap.get(parentId).addElement(e);
        } else if (interMap.containsKey(parentId)) {
            interMap.get(parentId).addElement(e);
        }
    }
    
    private void addElement(UmlGeneralization e) {
        String parentId = e.getParentId();
        if (classMap.containsKey(parentId)) {
            classMap.get(parentId).addElement(e);
        } else if (interMap.containsKey(parentId)) {
            interMap.get(parentId).addElement(e);
        }
    }
    
    private void addElement(UmlInterfaceRealization e) {
        String parentId = e.getParentId();
        classMap.get(parentId).addElement(e);
    }
    
    @Override
    public int getClassCount() {
        return classMap.size();
    }
    
    @Override
    public int getClassOperationCount(String className, OperationQueryType type)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return classNameMap.get(className).getClassOperationCount(type);
        }
    }
    
    @Override
    public int getClassAttributeCount(String className, AttributeQueryType type)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return classNameMap.get(className).
                    getClassAttributeCount(type, classMap);
        }
    }
    
    @Override
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return classNameMap.get(className).
                    getClassAssociationCount(classMap, endMap);
        }
    }
    
    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            HashSet<String> idSet = classNameMap.get(className).
                    getClassAssociatedClassList(classMap, assoMap, endMap);
            List<String> list = new ArrayList<>();
            for (String id : idSet) {
                if (classMap.containsKey(id)) {
                    list.add(classMap.get(id).getName());
                }
            }
            return list;
        }
    }
    
    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String opName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return classNameMap.get(className).
                    getClassOperationVisibility(opName);
        }
    }
    
    @Override
    public Visibility getClassAttributeVisibility(
            String className, String atName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return classNameMap.get(className).
                getClassAttributeVisibility(className, atName, classMap);
        }
    }
    
    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return classNameMap.get(className).getTopClass(classMap).getName();
        }
    }
    
    @Override
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            HashSet<String> idSet = classNameMap.get(className).
                    getImplementInterfaceList(classMap, interMap);
            List<String> list = new ArrayList<>();
            for (String id : idSet) {
                list.add(interMap.get(id).getName());
            }
            return list;
        }
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(
            String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return classNameMap.get(className).
                    getInformationNotHidden(classMap);
        }
    }

}
