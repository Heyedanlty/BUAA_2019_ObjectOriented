import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlClassModelInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlClassOrInterface;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

public class MyUmlClassModelInteraction implements UmlClassModelInteraction {

    private HashMap<String, UmlElement> elementMap;//id2ele
    private HashMap<String, MyUmlClass> classMap;//id2class
    private HashMap<String, MyUmlInter> interMap;//id2interface    

    private HashMap<String, Integer> classCount;//name2classCount
    private HashMap<String, Integer> interCount;//name2interCount
    
    private HashMap<String, MyUmlClass> classNameMap;//name2class
    private HashMap<String, MyUmlInter> interNameMap;//name2inter
    
    private HashMap<String, UmlAssociation> assoMap;//id2asso
    private HashMap<String, UmlAssociationEnd> endMap;//id2end
    
    public MyUmlClassModelInteraction(ArrayList<UmlElement> elements) {
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
        for (UmlElement e : elements) {
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
        MyUmlClass tmp = new MyUmlClass(e);
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
        MyUmlInter tmp = new MyUmlInter(e);
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
        if (classMap.containsKey(classId)) {
            classMap.get(classId).addElement(e);
        }        
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

    public void checkUml002() throws UmlRule002Exception {
        Set<AttributeClassInformation> exceptionSet = new HashSet<>();
        for (MyUmlClass cl : classMap.values()) {
            exceptionSet.addAll(cl.checkUml002(assoMap, endMap));
        }
        if (exceptionSet.size() != 0) {
            throw new UmlRule002Exception(exceptionSet);
        }
    }
    
    public void checkUml008() throws UmlRule008Exception {
        Set<UmlClassOrInterface> set = new HashSet<>();
        set.addAll(checkClass008());        
        set.addAll(checkInter008());
        if (set.size() != 0) {
            throw new UmlRule008Exception(set);
        }
    }
    
    private Set<UmlClassOrInterface> checkClass008() {
        HashSet<UmlClassOrInterface> set = new HashSet<>();
        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Boolean> marked = new HashMap<>();
        for (String id : classMap.keySet()) {
            marked.put(id, false);            
        }
        for (String id : classMap.keySet()) {
            if (marked.get(id)) {
                set.add(classMap.get(id).getUmlClass());
                continue;
            }
            for (String reset : classMap.keySet()) {
                visited.put(reset, false);
            }
            visited.put(id, true);
            if (!classMap.get(id).hasSuperClass()) {
                continue;
            }
            MyUmlClass superClass = classMap.get(id).getSuperClass(classMap);
            if (checkClassInRing(superClass.getId(), id, visited, marked)) {
                set.add(classMap.get(id).getUmlClass());
            }
        }
        return set;
    }
    
    private boolean checkClassInRing(String currentId, String findingId,
            HashMap<String, Boolean> visited, HashMap<String, Boolean> marked) {
        if (currentId.equals(findingId)) {
            marked.put(currentId, true);
            return true;
        }
        if (visited.get(currentId)) {
            return false;
        }
        visited.put(currentId, true);
        if (!classMap.get(currentId).hasSuperClass()) {
            return false;
        }
        MyUmlClass superClass = classMap.get(currentId).getSuperClass(classMap);
        if (checkClassInRing(superClass.getId(), findingId, visited, marked)) {
            marked.put(currentId, true);
            return true;
        }
        return false;
    }
    
    private Set<UmlClassOrInterface> checkInter008() {
        HashSet<UmlClassOrInterface> set = new HashSet<>();
        HashMap<String, Boolean> visited = new HashMap<>();
        HashMap<String, Boolean> marked = new HashMap<>();
        for (String id : interMap.keySet()) {
            marked.put(id, false);
        }
        for (String id : interMap.keySet()) {
            if (marked.get(id)) {
                set.add(interMap.get(id).getUmlInter());
                continue;
            }
            for (String reset : interMap.keySet()) {
                visited.put(reset, false);
            }
            visited.put(id, true);
            if (!interMap.get(id).hasSuperInter()) {
                continue;
            }
            Iterator<MyUmlInter> it = interMap.get(id).getSuperInters(interMap);
            while (it.hasNext()) {
                MyUmlInter superInter = it.next();
                if (checkInterInRing(superInter.getId(), id, visited, marked)) {
                    set.add(interMap.get(id).getUmlInter());
                } else {
                    continue;
                }
            }
        }
        return set;
    }
    
    private boolean checkInterInRing(String currentId, String findingId,
            HashMap<String, Boolean> visited, HashMap<String, Boolean> marked) {
        if (currentId.equals(findingId)) {
            marked.put(currentId, true);
            return true;
        }
        if (visited.get(currentId)) {
            return false;
        }
        visited.put(currentId, true);
        if (!interMap.get(currentId).hasSuperInter()) {
            return false;
        }
        Iterator<MyUmlInter> it = 
                interMap.get(currentId).getSuperInters(interMap);
        while (it.hasNext()) {
            MyUmlInter superInter = it.next();
            if (checkInterInRing(
                    superInter.getId(), findingId, visited, marked)) {
                marked.put(currentId, true);
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
    
    public void checkUml009() throws UmlRule009Exception {
        for (MyUmlInter it : interMap.values()) {
            it.updateInters(interMap);
        }
        for (MyUmlClass cl : classMap.values()) {
            cl.updateInters(classMap, interMap);
        }
        Set<UmlClassOrInterface> set = new HashSet<>();
        for (MyUmlInter it : interMap.values()) {
            if (it.getUml009()) {
                set.add(it.getUmlInter());
            }
        }
        for (MyUmlClass cl : classMap.values()) {
            if (cl.getUml009()) {
                set.add(cl.getUmlClass());
            }
        }
        if (set.size() != 0) {
            throw new UmlRule009Exception(set);
        }
    }
}
