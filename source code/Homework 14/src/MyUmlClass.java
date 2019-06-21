import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

public class MyUmlClass {
    private UmlClass umlClass;
    
    private HashMap<String, UmlAttribute> attMap;//name2Attribute
    private ArrayList<UmlAttribute> attList;//attributeList
    private HashMap<String, Integer> attCount;//name2count
    
    private HashMap<String, MyUmlOperation> opeMap;//id2opes
    
    private UmlGeneralization generalization;//gene
    
    private ArrayList<UmlInterfaceRealization> interRealList;//interRealList
    
    private boolean opNotCount = true;
    private int returnOp;
    private int nonReturnOp;
    private int paramOp;
    private int nonParamOp;    
    private boolean assoNotCount = true;
    private int assoCount;    
    private boolean assoListNot = true;
    private HashSet<String> idSet;    
    private boolean allAtNotCount = true;
    private int allAtCount;
    private boolean interNotList = true;
    private HashSet<String> interSet;
    private boolean inforNotFind = true;
    private List<AttributeClassInformation> infoList;
    
    private boolean interReady;
    private HashSet<String> inters;
    private boolean uml009;
    
    public MyUmlClass(UmlClass umlClass) {
        this.umlClass = umlClass;
        attMap = new HashMap<>();
        attList = new ArrayList<>();
        attCount = new HashMap<>();
        opeMap = new HashMap<>();
        generalization = null;
        interRealList = new ArrayList<>();
        
        interReady = false;
        inters = new HashSet<>();
        uml009 = false;
    }
    
    public void addElement(UmlElement e) {
        if (e instanceof UmlAttribute) {
            String name = e.getName();
            attMap.put(name, (UmlAttribute) e);
            attList.add((UmlAttribute) e);
            if (attCount.containsKey(name)) {
                int count = attCount.get(name);
                attCount.replace(name, count, count + 1);
            } else {
                attCount.put(name, 1);
            }
        } else if (e instanceof UmlOperation) {
            MyUmlOperation tmpOp = new MyUmlOperation((UmlOperation)e);
            opeMap.put(e.getId(), tmpOp);
        } else if (e instanceof UmlParameter) {
            opeMap.get(e.getParentId()).addPara((UmlParameter) e);            
        } else if (e instanceof UmlGeneralization) {
            generalization = (UmlGeneralization) e;
        } else if (e instanceof UmlInterfaceRealization) {
            interRealList.add((UmlInterfaceRealization) e);
        }
    }
    
    public int getClassOperationCount(OperationQueryType type) {
        if (type == OperationQueryType.RETURN) {
            if (opNotCount) {
                countOperations();
            }
            return returnOp;
        } else if (type == OperationQueryType.NON_RETURN) {
            if (opNotCount) {
                countOperations();
            }
            return nonReturnOp;
        } else if (type == OperationQueryType.PARAM) {
            if (opNotCount) {
                countOperations();
            }
            return paramOp;
        } else if (type == OperationQueryType.NON_PARAM) {
            if (opNotCount) {
                countOperations();
            }
            return nonParamOp;
        } else {
            return opeMap.size();
        }
    }
    
    private void countOperations() {
        int countReturn = 0;
        int countParam = 0;
        for (MyUmlOperation op : opeMap.values()) {
            if (op.hasReturn()) {
                countReturn++;
            }
            if (op.hasParameter()) {
                countParam++;
            }
        }
        returnOp = countReturn;
        nonReturnOp = opeMap.size() - countReturn;
        paramOp = countParam;
        nonParamOp = opeMap.size() - countParam;
        opNotCount = false;
    }
    
    public int getClassAttributeCount(AttributeQueryType type,
            HashMap<String, MyUmlClass> classMap) {
        if (type == AttributeQueryType.SELF_ONLY) {
            return attList.size();
        } else {
            if (allAtNotCount) {
                if (hasSuperClass()) {
                    allAtCount = attList.size() + getSuperClass(classMap).
                            getClassAttributeCount(type, classMap);
                } else {
                    allAtCount = attList.size();
                }
                allAtNotCount = false;
            }
            return allAtCount;
        }        
    }
    
    public boolean hasSuperClass() {
        return generalization != null;
    }
    
    public MyUmlClass getSuperClass(HashMap<String, MyUmlClass> classMap) {
        String superId = generalization.getTarget();
        return classMap.get(superId);
    }
    
    public MyUmlClass getTopClass(HashMap<String, MyUmlClass> classMap) {
        if (hasSuperClass()) {
            return getSuperClass(classMap).getTopClass(classMap);
        } else {
            return this;
        }
    }
    
    public int getClassAssociationCount(HashMap<String, MyUmlClass> classMap,
            HashMap<String, UmlAssociationEnd> endMap) {
        if (assoNotCount) {
            countAssoCount(classMap, endMap);
        }        
        return assoCount;
    }
    
    private void countAssoCount(HashMap<String, MyUmlClass> classMap,
            HashMap<String, UmlAssociationEnd> endMap) {
        int count = 0;
        String thisId = getId();
        for (UmlAssociationEnd ed : endMap.values()) {
            if (ed.getReference().equals(thisId)) {
                count++;
            }
        }
        if (hasSuperClass()) {
            count += getSuperClass(classMap).
                    getClassAssociationCount(classMap, endMap);
        }
        assoCount = count;
        assoNotCount = false;
    }
    
    public HashSet<String> getClassAssociatedClassList(
            HashMap<String, MyUmlClass> classMap,
            HashMap<String, UmlAssociation> assoMap,
            HashMap<String, UmlAssociationEnd> endMap) {
        if (assoListNot) {
            idSet = new HashSet<>();
            String thisId = getId();
            for (UmlAssociation as : assoMap.values()) {
                UmlAssociationEnd end1 = endMap.get(as.getEnd1());
                UmlAssociationEnd end2 = endMap.get(as.getEnd2());
                if (end1.getReference().equals(thisId)) {
                    idSet.add(end2.getReference());
                } else if (end2.getReference().equals(thisId)) {
                    idSet.add(end1.getReference());
                }
            }
            if (hasSuperClass()) {
                HashSet<String> superSet = getSuperClass(classMap).
                        getClassAssociatedClassList(classMap, assoMap, endMap);
                idSet.addAll(superSet);
            }
            assoListNot = false;
        }
        return idSet;
    }
    
    public Map<Visibility, Integer> getClassOperationVisibility(String opName) {
        HashMap<Visibility, Integer> vsMap = new HashMap<>();
        int publicCount = 0;
        int packageCount = 0;
        int protectedCount = 0;
        int privateCount = 0;
        for (MyUmlOperation op : opeMap.values()) {
            if (op.getOperation().getName().equals(opName)) {
                switch (op.getOperation().getVisibility()) {
                    case PUBLIC:
                        publicCount++;
                        break;
                    case PACKAGE:
                        packageCount++;
                        break;
                    case PROTECTED:
                        protectedCount++;
                        break;
                    case PRIVATE:
                        privateCount++;
                        break;
                    default:
                        break;
                }
            }
        }
        vsMap.put(Visibility.PUBLIC, publicCount);
        vsMap.put(Visibility.PACKAGE, packageCount);
        vsMap.put(Visibility.PROTECTED, protectedCount);
        vsMap.put(Visibility.PRIVATE, privateCount);
        return vsMap;
    }

    public Visibility getClassAttributeVisibility(String className, 
            String atName, HashMap<String, MyUmlClass> classMap) 
            throws AttributeNotFoundException, AttributeDuplicatedException {
        if (hasSuperClass()) {
            Visibility v;
            try {
                v = getSuperClass(classMap).
                    getClassAttributeVisibility(className, atName, classMap);
            } catch (AttributeNotFoundException e) {
                if (!attCount.containsKey(atName)) {
                    throw e;
                } else if (attCount.get(atName) > 1) {
                    throw new AttributeDuplicatedException(className, atName);
                } else {
                    return attMap.get(atName).getVisibility();
                }
            } catch (AttributeDuplicatedException e) {
                throw e;
            }
            if (attCount.containsKey(atName)) {
                throw new AttributeDuplicatedException(className, atName);
            } else {
                return v;
            }
        } else {
            if (!attCount.containsKey(atName)) {
                throw new AttributeNotFoundException(className, atName);
            } else if (attCount.get(atName) > 1) {
                throw new AttributeDuplicatedException(
                        className, atName);
            } else {
                return attMap.get(atName).getVisibility();
            }
        }
    }
    
    public HashSet<String> getImplementInterfaceList(
            HashMap<String, MyUmlClass> classMap,
            HashMap<String, MyUmlInter> interMap) {
        if (interNotList) {
            interSet = new HashSet<>();
            for (UmlInterfaceRealization ir : interRealList) {
                String interId = ir.getTarget();
                interSet.add(interId);
                MyUmlInter interNow = interMap.get(interId);
                interSet.addAll(interNow.getImplementInterfaceList(interMap));
            }
            if (hasSuperClass()) {
                MyUmlClass superClass = getSuperClass(classMap);
                interSet.addAll(
                    superClass.getImplementInterfaceList(classMap, interMap));
            }
            interNotList = false;
        }        
        return interSet;
    }
    
    public List<AttributeClassInformation> getInformationNotHidden(
            HashMap<String, MyUmlClass> classMap) {
        if (inforNotFind) {
            infoList = new ArrayList<>();
            for (UmlAttribute at : attList) {
                if (at.getVisibility() != Visibility.PRIVATE) {
                    infoList.add(
                        new AttributeClassInformation(at.getName(), getName()));
                }
            }
            if (hasSuperClass()) {
                infoList.addAll(
                    getSuperClass(classMap).getInformationNotHidden(classMap));
            }
            inforNotFind = false;
        }
        return infoList;
    }
    
    public String getId() {
        return umlClass.getId();
    }
    
    public String getName() {
        return umlClass.getName();
    }
    
    public Set<AttributeClassInformation> checkUml002(
            HashMap<String, UmlAssociation> assoMap,
            HashMap<String, UmlAssociationEnd> endMap) {
        Set<AttributeClassInformation> set = new HashSet<>();
        for (String name : attCount.keySet()) {
            if (attCount.get(name) != 1) {
                set.add(new AttributeClassInformation(name, getName()));
            }
        }
        for (UmlAssociation asso : assoMap.values()) {
            UmlAssociationEnd end1 = endMap.get(asso.getEnd1());
            UmlAssociationEnd end2 = endMap.get(asso.getEnd2());
            if (end1.getReference().equals(getId())) {
                String name = end2.getName();
                if (name != null && attMap.containsKey(name)) {
                    set.add(new AttributeClassInformation(name, getName()));
                }
            }
            if (end2.getReference().equals(getId())) {
                String name = end1.getName();
                if (name != null && attMap.containsKey(name)) {
                    set.add(new AttributeClassInformation(name, getName()));
                }
            }
        }
        return set;
    }
    
    public UmlClass getUmlClass() {
        return umlClass;
    }
    
    public void updateInters(HashMap<String, MyUmlClass> classMap,
            HashMap<String, MyUmlInter> interMap) {
        if (interReady) {
            return;
        }
        if (hasSuperClass()) {
            MyUmlClass superClass = getSuperClass(classMap);
            superClass.updateInters(classMap, interMap);
            if (superClass.getUml009()) {
                interReady = true;
                uml009 = true;
                return;
            } else {
                HashSet<String> superSet = superClass.getInters();
                for (String key : superSet) {
                    inters.add(key);
                }
            }
        }        
        for (UmlInterfaceRealization ir : interRealList) {
            String interId = ir.getTarget();
            MyUmlInter it = interMap.get(interId);
            if (it.getUml009()) {
                interReady = true;
                uml009 = true;
                return;
            }
            if (inters.contains(interId)) {
                interReady = true;
                uml009 = true;
                return;
            } else {
                inters.add(interId);
            }
            HashSet<String> interSet = interMap.get(interId).getInters();
            for (String key : interSet) {
                if (inters.contains(key)) {
                    interReady = true;
                    uml009 = true;
                    return;
                } else {
                    inters.add(key);
                }
            }
        }
        interReady = true;
        return;
    }
    
    public HashSet<String> getInters() {
        return inters;
    }
    
    public boolean getUml009() {
        return uml009;
    }
}
