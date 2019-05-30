import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlClass;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;

public class MyUmlClass1 extends MyUmlInterface1 {
    private UmlClass umlClass;
    private HashMap<String, UmlAttribute> attributes;//name2Attribute
    private HashMap<String, Integer> attributeCount;//name2count
    private ArrayList<UmlInterfaceRealization> interfaceRealizations;
    
    public MyUmlClass1(UmlClass e) {
        umlClass = e;
        attributes = new HashMap<>();
        attributeCount = new HashMap<>();
        interfaceRealizations = new ArrayList<>();        
    }
    
    @Override
    public void addElement(UmlElement e) {
        super.addElement(e);
        if (e instanceof UmlAttribute) {
            String name = e.getName();
            attributes.put(name, (UmlAttribute) e);
            if (attributeCount.containsKey(name)) {
                int count = attributeCount.get(name);
                attributeCount.replace(name, count, count + 1);
            } else {
                attributeCount.put(name, 1);
            }
        } else if (e instanceof UmlInterfaceRealization) {
            interfaceRealizations.add((UmlInterfaceRealization) e);
        }
    }
    
    public int getClassAttributeCount(AttributeQueryType queryType, 
            HashMap<String, MyUmlClass1> name2class,
            HashMap<String, String> id2name,
            HashMap<String, UmlElement> elementMap) {
        if (queryType == AttributeQueryType.SELF_ONLY) {
            return attributes.size();
        } else {
            if (hasSuperClass(elementMap)) {
                return attributes.size() +
                        getSuperClass(name2class, id2name, elementMap).
                        getClassAttributeCount(
                                queryType, name2class, id2name, elementMap);
            } else {
                return attributes.size();
            }            
        }        
    }
    
    public MyUmlClass1 getSuperClass(HashMap<String, MyUmlClass1> name2class, 
            HashMap<String, String> id2name,
            HashMap<String, UmlElement> elementMap) {
        if (hasSuperClass(elementMap)) {
            UmlGeneralization tmp = getGeneralizations().get(0);
            String superId = tmp.getTarget();
            return name2class.get(id2name.get(superId));
        } else {
            return this;
        }
    }
    
    public MyUmlClass1 getTopClass(HashMap<String, MyUmlClass1> name2class, 
            HashMap<String, String> id2name, 
            HashMap<String, UmlElement> elementMap) {
        if (hasSuperClass(elementMap)) {
            return getSuperClass(name2class, id2name, elementMap).
                    getTopClass(name2class, id2name, elementMap);
        } else {
            return this;
        }
    }
    
    public int getClassAssociationCount(HashMap<String, MyUmlClass1> name2class,
            HashMap<String, String> id2name,
            HashMap<String, UmlElement> elementMap,
            ArrayList<UmlAssociation> list) {
        int count;
        count = getThisAssociationCount(name2class, id2name, elementMap, list);
        if (hasSuperClass(elementMap)) {
            MyUmlClass1 sup = getSuperClass(name2class, id2name, elementMap);
            count += sup.getClassAssociationCount(
                    name2class, id2name, elementMap, list);
        }
        return count;
    }
    
    private int getThisAssociationCount(HashMap<String, MyUmlClass1> name2class,
            HashMap<String, String> id2name,
            HashMap<String, UmlElement> elementMap,
            ArrayList<UmlAssociation> list) {
        int count = 0;
        String thisId = getId();
        UmlAssociationEnd end1;
        UmlAssociationEnd end2;
        String ref1;
        String ref2;
        for (UmlAssociation as : list) {
            end1 = (UmlAssociationEnd)elementMap.get(as.getEnd1());
            end2 = (UmlAssociationEnd)elementMap.get(as.getEnd2());
            ref1 = end1.getReference();
            ref2 = end2.getReference();
            if (ref1.equals(thisId)) {
                count++;
            }
            if (ref2.equals(thisId)) {
                count++;
            }
        }
        return count;
    }
    
    public HashSet<String> getClassAssociatedClassList(
            HashMap<String, MyUmlClass1> name2class, 
            HashMap<String, String> id2name,
            HashMap<String, UmlElement> elementMap,
            ArrayList<UmlAssociation> assoList) {
        HashSet<String> idSet = new HashSet<>();
        for (UmlAssociation as : assoList) {            
            UmlAssociationEnd end1;
            UmlAssociationEnd end2;
            end1 = (UmlAssociationEnd) elementMap.get(as.getEnd1());
            end2 = (UmlAssociationEnd) elementMap.get(as.getEnd2());
            if (end1.getReference().equals(umlClass.getId())) {
                idSet.add(end2.getReference());
            } else if (end2.getReference().equals(umlClass.getId())) {
                idSet.add(end1.getReference());
            }
        }
        if (hasSuperClass(elementMap)) {
            MyUmlClass1 sup = getSuperClass(name2class, id2name, elementMap);
            HashSet<String> supSet = sup.getClassAssociatedClassList(
                    name2class, id2name, elementMap, assoList);
            idSet.addAll(supSet);
        }
        return idSet;
    }
    
    public Visibility getClassAttributeVisibility(
            String className, String attributeName,
            HashMap<String, MyUmlClass1> name2class,
            HashMap<String, String> id2name,
            HashMap<String, UmlElement> elementMap) 
            throws AttributeNotFoundException, AttributeDuplicatedException {
        if (hasSuperClass(elementMap)) {
            Visibility v;
            try {
                v = getSuperClass(name2class, id2name, elementMap).
                    getClassAttributeVisibility(className, attributeName,
                        name2class, id2name, elementMap);
            } catch (AttributeNotFoundException e) {
                if (!attributeCount.containsKey(attributeName)) {
                    throw e;
                } else if (attributeCount.get(attributeName) > 1) {
                    throw new AttributeDuplicatedException(
                            className, attributeName);
                } else {
                    return attributes.get(attributeName).getVisibility();
                }
            } catch (AttributeDuplicatedException e) {
                throw e;
            }
            if (attributeCount.containsKey(attributeName)) {
                throw new AttributeDuplicatedException(
                        className, attributeName);
            } else {
                return v;
            }
        } else {
            if (!attributeCount.containsKey(attributeName)) {
                throw new AttributeNotFoundException(className, attributeName);
            } else if (attributeCount.get(attributeName) > 1) {
                throw new AttributeDuplicatedException(
                        className, attributeName);
            } else {
                return attributes.get(attributeName).getVisibility();
            }
        }
    }
    
    @Override
    public HashSet<String> getImplementInterfaceList(
            HashMap<String, UmlElement> elementMap,
            HashMap<String, MyUmlInterface1> myInterfaceMap,
            HashMap<String, MyUmlClass1> myClassMap) {        
        HashSet<String> idSet = new HashSet<>();
        for (UmlInterfaceRealization ir : interfaceRealizations) {
            String interfaceId = ir.getTarget();
            idSet.add(interfaceId);
            String interfaceName = elementMap.get(interfaceId).getName();
            MyUmlInterface1 thisInterface = myInterfaceMap.get(interfaceName);
            idSet.addAll(thisInterface.getImplementInterfaceList(
                    elementMap, myInterfaceMap, myClassMap));
        }
        if (hasSuperClass(elementMap)) {
            UmlGeneralization tmp = getGeneralizations().get(0);
            String superId = tmp.getTarget();
            MyUmlClass1 superClass = 
                    myClassMap.get(elementMap.get(superId).getName());
            idSet.addAll(superClass.getImplementInterfaceList(
                    elementMap, myInterfaceMap, myClassMap));
        }
        return idSet;
    }

    public HashSet<AttributeClassInformation> getInformationNotHidden(
            HashMap<String, MyUmlClass1> name2class, 
            HashMap<String, String> id2name,
            HashMap<String, UmlElement> elementMap) {
        HashSet<AttributeClassInformation> set = new HashSet<>();
        for (UmlAttribute at : attributes.values()) {
            if (at.getVisibility() != Visibility.PRIVATE) {
                set.add(new AttributeClassInformation(
                        at.getName(), umlClass.getName()));
            }
        }
        if (hasSuperClass(elementMap)) {
            MyUmlClass1 superClass = getSuperClass(
                    name2class, id2name, elementMap);
            set.addAll(superClass.
                    getInformationNotHidden(name2class, id2name, elementMap));
        }
        return set;
    }
    
    public String getName() {
        return umlClass.getName();
    }
    
    public String getId() {
        return umlClass.getId();
    }
}
