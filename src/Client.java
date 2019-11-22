/*
Author: Enrique Posada Lozano
ID: A017000711
*/
import java.util.*;
import java.net.*;
import java.io.*;

public class Client {

    private static Socket socket = null;
    private static DataInputStream input = null;
    private static DataOutputStream output = null;

    private static Write_Messages write_messages;
    private static Read_Messages read_messages;
    private static Thread write_message_thread;
    private static Thread read_message_thread;

    public static boolean alive = true;
//    private static ServerMessages serverMessages;

    public static void main(String args[]) {
//        Client client = new Client("127.0.0.1", 8080);
        Scanner scanner = new Scanner(System.in);
        InetAddress ip = null;
        try {
            // change the ip address
            ip = InetAddress.getByName("localhost"); // Localhost
            System.out.println("Enter your USERNAME : ");
            String name = scanner.nextLine();
            System.out.println("Your name will be : " + name);
            socket = new Socket(ip, 8080);
//            System.out.println("Connection : " + socket.getInetAddress() + " : at port : " + socket.getPort());

            System.out.println("Great!  Connected to Server, Write your message: ");
            try {
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                output.writeUTF(name);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error sending username to server");
            }

//            String tosend = "";

            write_messages = new Write_Messages(output);
            read_messages = new Read_Messages(input);

            write_message_thread = new Thread(write_messages);
            read_message_thread = new Thread(read_messages);

            write_message_thread.start();
            read_message_thread.start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static boolean isAlive() {
        return alive;
    }

    public static void setAlive(boolean alive) {
        Client.alive = alive;
    }
}

// thread class that takes care of writing the users messages and sending them to the server
class Write_Messages implements Runnable{

    //    private static DataInputStream input = null;
    private static DataOutputStream output = null;

    public Write_Messages(DataOutputStream output) {
        this.output = output;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // read the message to deliver.
            String line_to_send = scanner.nextLine();
            if (line_to_send.equals("exit()")) {
//                Client.alive = false;
                Client.setAlive(false);
            }
            try {
                // write on the output stream
                output.writeUTF(line_to_send);
            } catch (IOException e) {
                System.out.println("An error occurred while trying to send '" + line_to_send + "' to the SERVER");
                e.printStackTrace();
            }
        }
    }
}

// thread class that takes care of reading all messages received from the SERVER
class Read_Messages implements Runnable {
    private static DataInputStream input = null;

    public Read_Messages(DataInputStream input) {
        this.input = input;
    }

    @Override
    public void run() {

        while (true) {
            if(!Client.isAlive()) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            else{
                try {
                    // read the message sent to this client
                    String line_to_read = input.readUTF();
                    System.out.println(line_to_read);
                } catch (IOException e) {
                    System.out.println("An error occurred while trying to read from the SERVER");
                    e.printStackTrace();
                }
            }
        }
    }
}
