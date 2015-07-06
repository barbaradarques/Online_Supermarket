/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package server;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ServerNewProductSceneController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField supplierField;
    @FXML
    private TextField quantityField;
    @FXML
    private DatePicker datePicker;

    public void saveNewProduct(ActionEvent event) {

        if (quantityField.getText().matches("^\\d+$") && priceField.getText().matches("[0-9]{1,13}(\\.[0-9]*)?")) { // checa se s�o n�meros v�lidos
            Supermarket.productsList.add(
                    new Product(nameField.getText(), Double.parseDouble(priceField.getText()),
                            Integer.parseInt(quantityField.getText()), datePicker.getValue(), supplierField.getText()));
        }

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close(); // fecha a janela de di�logo, voltando � janela principal
    }

}
