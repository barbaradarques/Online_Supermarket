/*------------------------------------------------------------
 *                      === Supermercado ===
 *
 *
 *  @author  Barbara Darques (ICMC-USP)
 *
 *-----------------------------------------------------------*/


package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGeneralController {

    @FXML
    private TextField usernameField; // recupera o campo com id "usernameField" do arquivo FXML
    @FXML
    private PasswordField passwordField; // recupera o campo com id "passwordField" do arquivo FXML


    public void signupButtonClicked() throws IOException {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); // para que n�o seja poss�vel mexer na outra janela enquanto essa estiver aberta
        Parent signupWindow = FXMLLoader.load(getClass().getResource("clientSignupScene.fxml")); // carrega o arquivo FXML que descreve a cena
        Scene scene = new Scene(signupWindow);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }

    public void loginButtonClicked(ActionEvent event) throws IOException {
//
//            Client.serverOutput.println("login");
//            Client.serverOutput.println(usernameField.getText());
//            Client.serverOutput.println(passwordField.getText());
        // checa se os dados de login existem no servidor
//            try {
//                System.out.println(Client.serverInput.readLine());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            Client.readInput();

        //System.out.println("input = ");

//        while ((nextLine = Client.serverInput.readLine()) != null){
//            System.out.println(nextLine);
//            if(nextLine=="sim"){
        Stage mainWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent mainScreen = FXMLLoader.load(getClass().getResource("clientMainScene.fxml"));
        mainWindow.setScene(new Scene(mainScreen));
//            } else {
//                displayErrorWindow("Erro!", "Usuario e/ou senha incorretos.");
//            }
//            break;
//        }

    }


    public static void displayErrorWindow(String title, String message) { // mostra uma mensagem de erro customiz�vel
        Stage window = new Stage(); //cria uma nova janela

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        window.setMinHeight(100);

        Label label = new Label();
        label.setText(message);
        Button closeButton = new Button("Fechar");
        closeButton.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.setResizable(false);
        window.showAndWait();
    }


    public void yesButtonClicked(ActionEvent event) {
        Client.serverOutput.println("notifyMe");
        Client.serverOutput.println(ClientMainSceneController.selectedProductName);
        Client.serverOutput.println(ClientMainSceneController.chosenQuantity);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close(); // fecha a janela de di�logo, voltando � janela principal
    }

    public void noButtonClicked(ActionEvent event) { //cliente n�o quer ser notificado
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close(); // fecha a janela de di�logo, voltando � janela principal
    }


}
