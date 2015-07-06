/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package client;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class ClientSignupSceneController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void finalSignupButtonClicked() {
        Client.serverOutput.println("signup");
        Client.serverOutput.println(usernameField.getText());
        Client.serverOutput.println(emailField.getText());
        Client.serverOutput.println(passwordField.getText());
    }
}
