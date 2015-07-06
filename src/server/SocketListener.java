/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package server;

import javafx.concurrent.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketListener extends Task<String> { // thread que faz a conex�o com cada cliente
    final Socket client;
    PrintWriter clientOutput; // o que o cliente vai ver
    BufferedReader clientInput; // o que o cliente manda
    String username;
    final Supermarket supermarket;

    public SocketListener(Socket client) {
        supermarket = Supermarket.getInstance();
        this.client = client;
        try {
            clientOutput = new PrintWriter(client.getOutputStream(), true);
            clientInput = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected synchronized String call() throws Exception {
        String nextLine;
        username = clientInput.readLine();
        System.out.println(username + " conectou-se");
        Server.loggedUsers.put(client, username); // adiciona aos clientes atualmente logados ao sistema

        while ((nextLine = clientInput.readLine()) != null) {
            System.out.println(nextLine);
            switch (nextLine) {
                case "loadProducts":
                    sendProducts();
                    break;
                case "buy":
                    sell();
                    break;
                case "login":
                    if (checkLogin()) {
                        clientOutput.println("sim");
                    } else {
                        clientOutput.println("nao");
                    }
                    break;
                case "notifyMe":
                    addToNotificationList();
                    break;
                case "signup":
                    addNewUser();
                    break;
            }
        }
        return username + " desconectou-se";
    }

    private synchronized void addNewUser() {
        try {
            String name = clientInput.readLine();
            String email = clientInput.readLine();
            String password = clientInput.readLine();
            supermarket.registerNewUser(name, email, password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addToNotificationList() {
        try {

            String product = clientInput.readLine();
            String quantity = clientInput.readLine();
            Supermarket.notificationList.add(new Notifiable(product, username, Integer.parseInt(quantity)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendProducts() {
        Supermarket.productsList.stream().forEach(product -> {
            //public Product(String name, double price, int stock, LocalDate expirationDate, String supplier)
            clientOutput.println(product.getName());
            clientOutput.println(product.getPrice());
            clientOutput.println(product.getStock());
            clientOutput.println(product.getExpirationDate().format(Supermarket.FORMATTER));
            clientOutput.println(product.getSupplier());
        });
        clientOutput.println("fim");
    }

    public synchronized boolean checkLogin() {
        try {
            String username = clientInput.readLine();
            System.out.println(username);
            String password = clientInput.readLine();
            System.out.println(password);
            if (supermarket.getUsers().get(username).getPassword().equals(password)) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized void sell() {
        try {
            String product = clientInput.readLine();
            String quantity = clientInput.readLine();
            supermarket.registerNewSale(product, username, Server.today.format(Supermarket.FORMATTER), quantity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Venda realizada!");
    }
}
