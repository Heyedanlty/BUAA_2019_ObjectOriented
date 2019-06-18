import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlAssociation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;
import com.oocourse.uml2.models.elements.UmlClass;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private MyUmlClassModelInteraction classModel;
    private MyUmlStateChartInteraction stateChart;
    private MyUmlCollaborationInteraction collaboration;
    
    public MyUmlGeneralInteraction(UmlElement... elements) { 
        ArrayList<UmlElement> classModelList = new ArrayList<>();
        ArrayList<UmlElement> stateChartList = new ArrayList<>();
        ArrayList<UmlElement> collaborationList = new ArrayList<>();
        for (UmlElement e : elements) {
            if (e instanceof UmlAssociation || e instanceof UmlAssociationEnd ||
                e instanceof UmlGeneralization || e instanceof UmlClass) {
                classModelList.add(e);
            } else if (e instanceof UmlAttribute || e instanceof UmlOperation ||
                    e instanceof UmlInterface || e instanceof UmlParameter ||
                    e instanceof UmlInterfaceRealization) {
                classModelList.add(e);
            } else if (e instanceof UmlLifeline || e instanceof UmlMessage ||
                    e instanceof UmlInteraction) {
                collaborationList.add(e);
            } else if (e instanceof UmlStateMachine || e instanceof UmlRegion ||
                    e instanceof UmlPseudostate || e instanceof UmlFinalState ||
                    e instanceof UmlState || e instanceof UmlTransition) {
                stateChartList.add(e);
            }
        }
        classModel = new MyUmlClassModelInteraction(classModelList);
        stateChart = new MyUmlStateChartInteraction(stateChartList);
        collaboration = new MyUmlCollaborationInteraction(collaborationList);
    }

    @Override
    public List<String> getClassAssociatedClassList(String arg0)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classModel.getClassAssociatedClassList(arg0);
    }

    @Override
    public int getClassAssociationCount(String arg0) 
            throws ClassNotFoundException, ClassDuplicatedException {
        return classModel.getClassAssociationCount(arg0);
    }

    @Override
    public int getClassAttributeCount(String arg0, AttributeQueryType arg1)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classModel.getClassAttributeCount(arg0, arg1);
    }

    @Override
    public Visibility getClassAttributeVisibility(String arg0, String arg1) 
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        return classModel.getClassAttributeVisibility(arg0, arg1);
    }

    @Override
    public int getClassCount() {
        return classModel.getClassCount();
    }

    @Override
    public int getClassOperationCount(String arg0, OperationQueryType arg1)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classModel.getClassOperationCount(arg0, arg1);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String arg0, String arg1)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classModel.getClassOperationVisibility(arg0, arg1);
    }

    @Override
    public List<String> getImplementInterfaceList(String arg0)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classModel.getImplementInterfaceList(arg0);
    }

    @Override
    public List<AttributeClassInformation> getInformationNotHidden(String arg0)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classModel.getInformationNotHidden(arg0);
    }

    @Override
    public String getTopParentClass(String arg0)
            throws ClassNotFoundException, ClassDuplicatedException {
        return classModel.getTopParentClass(arg0);
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        classModel.checkUml002();
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        classModel.checkUml008();
    }

    @Override
    public void checkForUml009() throws UmlRule009Exception {
        classModel.checkUml009();        
    }

    @Override
    public int getIncomingMessageCount(String arg0, String arg1)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return collaboration.getIncomingMessageCount(arg0, arg1);
    }

    @Override
    public int getMessageCount(String arg0)
            throws InteractionNotFoundException,InteractionDuplicatedException {
        return collaboration.getMessageCount(arg0);
    }

    @Override
    public int getParticipantCount(String arg0)
            throws InteractionNotFoundException,InteractionDuplicatedException {
        return collaboration.getParticipantCount(arg0);
    }

    @Override
    public int getStateCount(String arg0) 
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateChart.getStateCount(arg0);
    }

    @Override
    public int getSubsequentStateCount(String arg0, String arg1)
            throws StateMachineNotFoundException, StateNotFoundException,
            StateMachineDuplicatedException, StateDuplicatedException {
        return stateChart.getSubsequentStateCount(arg0, arg1);
    }

    @Override
    public int getTransitionCount(String arg0) 
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        return stateChart.getTransitionCount(arg0);
    }

}
