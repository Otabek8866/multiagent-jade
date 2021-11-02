import java.net.*; // need this for InetAddress, Socket, ServerSocket
import java.io.*; // need this for I/O stuff

public class ServerTCP extends Thread {

    static final int BUFSIZE = 1024;
    private Socket s;

    public ServerTCP(Socket s) {
        this.s = s;
    }

    public static void main(String[] args) {
        try {
            // initialize the server
            ServerSocket sever = new ServerSocket(8888);
            System.out.println("Server is listening on port " + "8888" + "\n");

            for (;;) {
                Socket client = sever.accept();
                Thread server = new ServerTCP(client);
                server.start();
            }
        } catch (IOException e) {
            System.out.println("Problem Server: " + e.toString());
        }
    }

    public void run() {
        try {
            handleClient(s);
        } catch (IOException e) {
            System.out.println("Problem Server: " + e.toString());
        }
    }

    static void handleClient(Socket s) throws IOException {

        byte[] buff = new byte[BUFSIZE];

        // Set up streams
        InputStream in = s.getInputStream();
        // writer to client
        PrintWriter writer = new PrintWriter(s.getOutputStream(), true);

        // read/write loop
        while (in.read(buff) != -1) {
            String request = new String(buff).trim();
            String result;

            // calculations
            result = fibonacci(Integer.parseInt(request));

            // responding to client
            writer.println(result);
            writer.flush();

            System.out.println("Port:" + s.getPort() + "-->Request:" + request + " Reply:" + result);
            s.close();
            buff = new byte[BUFSIZE];

            Thread t1 = currentThread();
            try {
                t1.join();
            } catch (InterruptedException ex) {
                System.out.println("Problem Server: " + ex.toString());
            }
        }
    }

    public static String fibonacci(Long req) {
        Long a = new Long(1);
        Long b = new Long(0);
        Long temp;

        while (req >= 0) {
            temp = a;
            a = a + b;
            b = temp;
            req--;
        }
        return Long.toString(b);
    }
}
