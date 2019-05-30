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
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlParameter;

public class MyUmlInteraction1 implements UmlInteraction {
    private HashMap<String, MyUmlClass1> myClassMap;//name2MyClass
    private HashMap<String, String> classIdMap;//id2ClassName
    private HashMap<String, Integer> classCount;//ClassName2count
      
    private HashMap<String, MyUmlInterface1> myInterfaceMap;//name2MyInterface
    private HashMap<String, String> interfaceIdMap;//id2InterfaceName
    private HashMap<String, Integer> interfaceCount;//InterfaceName2count
    
    private HashMap<String, UmlElement> elementMap;//id2Element
    
    private ArrayList<UmlAssociation> assoList;//id2Asso
    
    public MyUmlInteraction1(UmlElement... elements) {
        myClassMap = new HashMap<>();
        classIdMap = new HashMap<>();
        classCount = new HashMap<>();
        myInterfaceMap = new HashMap<>();
        interfaceIdMap = new HashMap<>();
        interfaceCount = new HashMap<>();
        elementMap = new HashMap<>();
        assoList = new ArrayList<>();
        for (int i = 0; i < elements.length; i++) {
            UmlElement tmp = elements[i];
            elementMap.put(tmp.getId(), tmp);
            addElement(tmp);
        }
    }
    
    private void addElement(UmlElement e) {
        if (e instanceof UmlClass) {            
            addClass((UmlClass) e);
        } else if (e instanceof UmlInterface) {            
            addInterface((UmlInterface) e);
        } else if (e instanceof UmlParameter) {            
            addParameter((UmlParameter) e);
        } else if (e instanceof UmlAssociation) {
            addAssociation((UmlAssociation) e);
        } else if (e instanceof UmlAssociationEnd) {
            addAssociationEnd((UmlAssociationEnd) e);
        } else {            
            addOthers(e);
        }
        return;
    }

    private void addClass(UmlClass e) {
        String name = e.getName();
        myClassMap.put(name, new MyUmlClass1(e));
        classIdMap.put(e.getId(), name);            
        if (classCount.containsKey(name)) {
            Integer count = classCount.get(name);
            classCount.replace(name, count, count + 1);
        } else {
            classCount.put(name, 1);
        }
    }

    private void addInterface(UmlInterface e) {
        String name = e.getName();
        myInterfaceMap.put(name, new MyUmlInterface1(e));
        interfaceIdMap.put(e.getId(), name);
        if (interfaceCount.containsKey(name)) {
            Integer count = classCount.get(name);
            classCount.replace(name, count, count + 1);
        } else {
            classCount.put(name, 1);
        }
    }
    
    private void addParameter(UmlParameter e) {
        String parentId = elementMap.get(e.getParentId()).getParentId();
        if (classIdMap.containsKey(parentId)) {
            String clName = classIdMap.get(parentId);
            myClassMap.get(clName).addElement(e);
        } else {
            String itName = interfaceIdMap.get(parentId);
            myInterfaceMap.get(itName).addElement(e);
        }
    }
    
    private void addAssociation(UmlAssociation e) {
        assoList.add(e);
    }
    
    private void addAssociationEnd(UmlAssociationEnd e) {
    }
    
    private void addOthers(UmlElement e) {
        String parentId = e.getParentId();
        if (classIdMap.containsKey(parentId)) {
            String clName = classIdMap.get(parentId);     
            MyUmlClass1 cl = myClassMap.get(clName);
            cl.addElement(e);
        } else {
            String itName = interfaceIdMap.get(parentId);
            MyUmlInterface1 it = myInterfaceMap.get(itName);
            it.addElement(e);
        }
    }
    
    @Override
    public int getClassCount() {
        return classIdMap.size();
    }

    @Override
    public int getClassOperationCount(
            String className, OperationQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) != 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return myClassMap.get(className).getClassOperationCount(queryType);
        }
    }
    
    @Override
    public int getClassAttributeCount(
            String className, AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) != 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return myClassMap.get(className).getClassAttributeCount(
                    queryType, myClassMap, classIdMap, elementMap);
        }
    }
    
    @Override
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) != 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return myClassMap.get(className).getClassAssociationCount(
                    myClassMap, classIdMap, elementMap, assoList);
        }
    }
    
    @Override
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) != 1) {
            throw new ClassDuplicatedException(className);
        } else {
            HashSet<String> idSet =  myClassMap.get(className).
                getClassAssociatedClassList(
                        myClassMap, classIdMap, elementMap, assoList);
            List<String> list = new ArrayList<>();
            for (String id : idSet) {
                UmlElement e = elementMap.get(id);
                if (e.getElementType() == ElementType.UML_CLASS) {
                    list.add(e.getName());
                }
            }
            return list;
        }
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) != 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return myClassMap.get(className).
                    getClassOperationVisibility(operationName);
        }        
    }

    @Override
    public Visibility getClassAttributeVisibility(
            String className, String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) > 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return myClassMap.get(className).
                getClassAttributeVisibility(className, attributeName,
                        myClassMap, classIdMap, elementMap);
        }
    }
    
    @Override
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) != 1) {
            throw new ClassDuplicatedException(className);
        } else {
            return myClassMap.get(className).
                    getTopClass(myClassMap, classIdMap, elementMap).getName();
        }
    }
    
    @Override
    public List<String> getImplementInterfaceList(String className) 
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classCount.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (classCount.get(className) != 1) {
            throw new ClassDuplicatedException(className);
        } else {
            HashSet<String> idSet = 
                    myClassMap.get(className).getImplementInterfaceList(
                            elementMap, myInterfaceMap, myClassMap);
            List<String> list = new ArrayList<>();
            for (String id : idSet) {
                list.add(elementMap.get(id).getName());
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
        } else if (classCount.get(className) != 1) {
            throw new ClassDuplicatedException(className);
        } else {
            List<AttributeClassInformation> list = new ArrayList<>();
            list.addAll(myClassMap.get(className).getInformationNotHidden(
                    myClassMap, classIdMap, elementMap));
            return list;
        }
    }    

}
