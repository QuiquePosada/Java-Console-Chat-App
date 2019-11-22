
/*
 * Author: Enrique Posada Lozano
 * ID: A01700711
 * */

import java.util.*;
import java.net.*;
import java.io.*;

public class Server {
    //initialize socket and input stream

    //This is now passed to the main locally
    private static Socket socket = null;
    private static ServerSocket server = null;
    private static DataInputStream input =  null;
    private static DataOutputStream output = null;
    private static Thread clientThread;
    private static ClientThread client;

    public static ArrayList<ClientThread> client_list = new ArrayList<ClientThread>();

    public static int client_id = 0;

    public static void main(String args[]) throws IOException {


        System.out.println("Server has started");
        System.out.println("Waiting for Clients...");
//        ServerSocket server = null;
        try {
            server = new ServerSocket(8080);
            // Note: do some tests
            // server.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Accepting N Clients
        // unless you want to limit to N clients
        while(true){
            socket = null;
            try {

                socket = server.accept();
                System.out.println("New Client has Connected to Server");

                System.out.println("Client : " + socket);
//            System.out.println("Client NAME : " + client_id);

                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());

//            client_id = in.readUTF();

                System.out.println("Assigning new thread to incoming accepted client");
                client = new ClientThread(socket, input, output, client_id);
                clientThread = new Thread(client);
                client_list.add(client);
                clientThread.start();
                messageAll("!!Hello there, a new user has, with name -> " + client.getName());
            } catch (IOException e) {
                socket.close();
                //due to error, close connections of all connected clients
                for(int i = 0; i < client_list.size(); i++){
                    ClientThread client_to_close = client_list.get(i);
                    client_to_close.socket.close();
                    client_to_close.output.close();
                    client_to_close.input.close();
                }
                e.printStackTrace();
                System.out.println("There was an unexpected error");
            }

            client_id ++;
        }
    }
//    public static

    public static synchronized void disconnect_from_Server(int user_id){
        ClientThread client_to_close = client_list.get(user_id);
        try {
            client_to_close.output.writeUTF("Disconnected From SERVER!\n\nGoodbye : " + client_to_close.getName() + "\n\n\tPress Ctrl + C to exit program");
            client_to_close.input.close();
            client_to_close.output.close();
            client_to_close.socket.close();
            client_list.remove(user_id);
            for (int i = 0; i < client_list.size(); i++) {
                ClientThread new_id_for_client = client_list.get(i);
                System.out.println("Client THREAD IS : " + new_id_for_client);
                System.out.println("\nClient ID IS : " + new_id_for_client.getName() + "\n has ID " + i);
                new_id_for_client.setUser_id(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("An error occurred while trying to remove user : " + client_to_close.getName() + " from SERVER");
        }

    }

    public static synchronized void messageAll(String s) {
        // BROADCAST a message informing that a new user has connected to server
        System.out.println("\tHELLO in MESSAGE ALL!!!");
        for (int i = 0; i < client_list.size(); i++) {
            ClientThread client_to_recieve = client_list.get(i);
            System.out.println("Client THREAD IS : " + client_to_recieve);
            System.out.println("\nClient HERE IS : " + client_to_recieve.getName() + "\n");
            try {
                client_to_recieve.output.writeUTF(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        for (int i = 0; i < client_list.size(); i++) {
//            System.out.println("ID C : " + client_list.get(i));
//            ClientThread client_informed = client_list.get(i);
//            System.out.println("INCOMING CLIENT ID IS: -> " + client_list.get(client_list.size()-1));
//            client_informed.send_message("!USER -> " + client_list.get(client_list.size()-1) + " has entered SERVER !");
////                out.writeUTF();
//        }
    }

}

class ClientThread implements Runnable{

    public Socket socket = null;
    //    private static ServerSocket server = null;
    public  DataInputStream input =  null;
    public  DataOutputStream output = null;

    private String name;

    private int user_id;


    public ClientThread(Socket socket, DataInputStream input, DataOutputStream output, int user_id){
        this.socket = socket;
        this.input = input;
        this.output = output;
        try {
            this.name = this.input.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error fetching username");
        }
        System.out.println("New Client Thread created !  USERNAME is : " + this.name);
        this.user_id = user_id;
    }

    @Override
    public void run() {
        try
        {
            // takes input from the client socket
            String line = "";
//            System.out.println("HEY");
            output.writeUTF("HELLO "+ name +" FROM SERVER\n" +
                    "\t!!Welcome to the Chat Room!!\nType and hit ENTER in order to send all the messages that you want to the server.\nIn order to disconnect from the SERVER, you MUST type 'exit()'");


            // reads message from client until "exit()" is sent
            //Or you could have an exit server line received which closes the server
            while (true){
                try
                {
//                    System.out.println("Client : " + socket + "///(USERNAME)->" + name + " sends: ");
                    System.out.println("Client : ///(USERNAME)->" + getName() + " sends: ");
//                    out.writeUTF("Testing read message thread");
                    line = input.readUTF();
                    System.out.println("\t" + line);
                    System.out.println("Message END");
                    if(line.equals("exit()")){
                        System.out.println("Client : " + this.socket + " (USERNAME) " + getName() + " wants to EXIT");
                        System.out.println("Closing connection for " + getName());
                        Server.disconnect_from_Server(user_id);
                        break; // end the loop
                    }
                    else if(!line.equals("exit()")){ // else message everyone connected !
                        String newline = getName() + " : " + line;
                        Server.messageAll(newline);
                    }
                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println(e);
        }

//        try {
//            //            Server.client_list.remove(0)
////            out.writeUTF("Disconnected From SERVER!\n\nGoodbye : " + getName() + "\n\n\tPress Ctrl + C to exit program");
////            out.writeUTF("Good bye " + name);
////            out.close();
////            in.close();
////            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//            socket.close(); // todo same as close!!!!
    }

    public String getName() {
        return name;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

