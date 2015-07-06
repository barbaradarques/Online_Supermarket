/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package server;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;

public class Server extends Application {
    public static Session emailSession;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static ServerSocket serverSocket;
    public static HashMap<Socket, String> loggedUsers; // < conex�o, nome do usu�rio conectado >
    Supermarket supermarket;
    public static LocalDate today;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainWindow) throws Exception {
        loggedUsers = new HashMap<>();
        supermarket = Supermarket.getInstance();
        today = LocalDate.now();

        startServer();
        startEmailSession();

        Parent loginScene = FXMLLoader.load(getClass().getResource("serverMainScene.fxml"));
        mainWindow.setTitle("Supermercado Online - Servidor");
        mainWindow.setScene(new Scene(loginScene, 800, 535));
        mainWindow.setResizable(false);
        mainWindow.setOnCloseRequest(e -> {
            // salvar as modifica��es da sess�o que n�o foram salvas ainda
            supermarket.saveProducts();
            try {
                supermarket.saveNotificationList();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        mainWindow.show();


    }

    public static void startEmailSession() {
        Properties props = new Properties();
        // configura a conex�o com o servidor SMTP do gmail (s� envia e-mail de endere�os gmail, mas qualquer um pode ser recipiente)
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.mime.charset", "ISO-8859-1");

        // inicia a conex�o com o servidor de email
        emailSession = Session.getDefaultInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("supermercadopooicmc@gmail.com", "trabalhodepoo"); // dados da conta que envia o email
                    }
                }
        );
    }

    public static void sendEmail(String emailAddress) {
        Message msg = new MimeMessage(emailSession);
        emailSession.setDebug(true); // pra mostrar o log do envio

        try {
            msg.setFrom(new InternetAddress("supermercadopoo@gmail.com")); //destinat�rio
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailAddress));
            msg.setText("O produto que voce queria ja chegou!");  // texto do email
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void startServer() {
        try {
            serverSocket = new ServerSocket(12345); // porta = 12345
        } catch (IOException e) {
            e.printStackTrace();
        }

        Task connectionManager = new Task() {
            @Override
            protected Object call() throws Exception {
                Socket client;
                SocketListener messenger;
                while (true) {
                    client = serverSocket.accept();
                    messenger = new SocketListener(client);
                    Thread clientThread = new Thread(messenger);
                    clientThread.setDaemon(true);
                    clientThread.start();
                }
            }
        };

        Thread serverThread = new Thread(connectionManager);
        serverThread.setDaemon(true);
        serverThread.start();
    }
}
