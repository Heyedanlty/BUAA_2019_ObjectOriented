import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;

public class MyUmlAssociation1 {
    private UmlAssociation association;
    private UmlAssociationEnd end1;
    private UmlAssociationEnd end2;
    
    public MyUmlAssociation1(UmlAssociation asso) {
        this(null, null);
        association = asso;
    }
    
    public MyUmlAssociation1(UmlAssociationEnd end1, UmlAssociationEnd end2) {
        this.end1 = end1;
        this.end2 = end2;
        this.association = null;
    }
    
    public void setEnd1(UmlAssociationEnd end1) {
        this.end1 = end1;
    }
    
    public void setEnd2(UmlAssociationEnd end2) {
        this.end2 = end2;
    }
    
    public UmlAssociation getAsso() {
        return association;
    }
    
    public UmlAssociationEnd getEnd1() {
        return end1;
    }
    
    public UmlAssociationEnd getEnd2() {
        return end2;
    }
}
