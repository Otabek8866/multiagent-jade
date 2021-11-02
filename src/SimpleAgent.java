import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.*;
import java.net.*;

public class SimpleAgent extends Agent {

    Behaviour loop;
    InetAddress target_IP;
    int port;
    int session_time;
    String msg;
    String report_to_architect;

    @Override
    public void setup() {
        Object[] obj = getArguments();
        target_IP = (InetAddress) obj[0];
        port = (int) obj[1];
        session_time = (int) obj[2];
        msg = (String) obj[3];
        
        System.out.println("Agent Created: " + getAID().getName());
//        System.out.println("Platform: " + getAID().getAllAddresses());
//        System.out.println(getAID());
//        System.out.println(getName());
//        System.out.println(getLocalName());
//        System.out.println(getAMS());
//        System.out.println(getAID());

        loop = new TickerBehaviour(this, session_time) {
            protected void onTick() {
                try {
                    // Creating a socket for client
                    Socket client_socket = new Socket(target_IP, port);
                    SendMessage sm = new SendMessage();

                    //Setting up communication streams
                    InputStreamReader inreader = new InputStreamReader(client_socket.getInputStream());
                    BufferedReader bf = new BufferedReader(inreader);
                    PrintWriter out = new PrintWriter(client_socket.getOutputStream(), true);

                    // sending msg to server
                    out.write(msg);
                    out.flush();

                    //printing the server's response
                    String output;
                    while ((output = bf.readLine()) != null) {
                        report_to_architect = getAID().getLocalName() + "-->Sent:" + msg + " Received:" + output;
                        System.out.println(report_to_architect);
                    }
                    addBehaviour(sm);

                    client_socket.close();

                } catch (IOException ex) {
                    System.out.println("Problem : " + getAID().getLocalName() + " " + ex.toString());
                }
            }
        };

        addBehaviour(loop);
    }

    public class SendMessage extends OneShotBehaviour {

        public void action() {
            ACLMessage msg;
            String receiverName = "Architect@10.0.1.222:1099/JADE";
            String messageContent = report_to_architect;

            msg = new ACLMessage(ACLMessage.INFORM);

            AID aid = new AID(receiverName, AID.ISGUID);
            aid.addAddresses("http://10.0.1.222:1099/acc");
            msg.addReceiver(aid);
            msg.setLanguage("English");
            msg.setContent(messageContent);
            send(msg);
        }
    }
}
