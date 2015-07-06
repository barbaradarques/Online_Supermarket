/*------------------------------------------------------------
 *                      === Supermercado ===
 *
 *
 *  @author  Barbara Darques (ICMC-USP)
 *
 *-----------------------------------------------------------*/


package client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Client extends Application {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static Socket serverSocket;
    public static BufferedReader serverInput;
    public static PrintWriter serverOutput;
    public static String loggedUsername;

    @Override
    public void start(Stage mainWindow) throws Exception {
        loggedUsername = "barbara";
        connectToServer();
        Parent loginScene = FXMLLoader.load(getClass().getResource("clientLoginScene.fxml"));
        mainWindow.setTitle("Supermercado Online");
        mainWindow.setScene(new Scene(loginScene, 800, 500));
        mainWindow.setResizable(false);
        mainWindow.show();
    }


    public static void main(String[] args) {
        launch(args);

    }

    public static boolean checkLoginData(String username, String password) {
        // TODO
        return true;
    }

    private static void connectToServer() throws IOException {
        serverSocket = new Socket("localhost", 12345);    //inicia uma conex�o com o servidor
        serverInput = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        serverOutput = new PrintWriter(serverSocket.getOutputStream(), true);
        serverOutput.println(loggedUsername);

        Task connectionManager = new Task() {
            @Override
            protected Object call() throws Exception {

                loadProducts();
                return null;
            }
        };
        Thread clientThread = new Thread(connectionManager);
        clientThread.setDaemon(true);
        clientThread.start();
    }

    public static void loadProducts() throws IOException { // recebe os produtos do servidor
        String nextLine;
        serverOutput.println("loadProducts");
        while ((nextLine = serverInput.readLine()) != null) {
            if (!nextLine.equals("fim")) {
                System.out.println(nextLine);
                //Product(String name, double price, int stock, LocalDate expirationDate, String supplier)
                Double price = Double.parseDouble(serverInput.readLine());
                int stock = Integer.parseInt(serverInput.readLine());
                LocalDate expirationDate = LocalDate.parse(serverInput.readLine(), FORMATTER);
                String supplier = serverInput.readLine();
                Product product = new Product(nextLine, price, stock, expirationDate, supplier);
                ClientMainSceneController.products.add(product);
            } else {
                break;
            }
        }
    }

    public static void readInput() {

        Platform.runLater(() -> {
            String str = null;
            try {
                str = new BufferedReader(new InputStreamReader(serverSocket.getInputStream())).readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("-=:" + str);
        });
//        Task task = new Task() {
//            @Override
//            protected String call() throws Exception {
//                //System.out.println("===>"+str);
//                    String str;
//
////                    while((str = serverInput.readLine())!=null){
////                        System.out.println("===>"+str);
////                    }
//
//                  //str = serverInput.readLine();
//                str= new BufferedReader(new InputStreamReader(serverSocket.getInputStream())).readLine();
//                   System.out.println(str);
//
//                return str;
//            }
//        };
//        Thread t = new Thread(task);
//        t.setDaemon(true);
//        t.start();

    }
}
