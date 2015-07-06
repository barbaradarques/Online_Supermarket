/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package server;

import com.itextpdf.text.DocumentException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ServerMainSceneController {
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button saveTodayButton;
    @FXML
    private TextField quantityField;
    @FXML
    private Label todayField;
    @FXML
    private TableView<Product> productsTable;
    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, String> supplierColumn;
    @FXML
    private TableColumn<Product, LocalDate> expirationColumn;
    @FXML
    private TableColumn<Product, Integer> stockColumn;
    @FXML
    private RadioButton available;
    @FXML
    private RadioButton unavailable;
    @FXML
    private RadioButton all;

    public void generateMonthPDF() {
        try {
            Supermarket.getInstance().createSalesPDF("Mes");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void generateDayPDF() {
        try {
            Supermarket.getInstance().createSalesPDF("Dia");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void updateStock() {
        Product product = productsTable.getSelectionModel().getSelectedItem(); // pega o produto selecionado na tabela
        String quantity = quantityField.getText();
        if (quantity.matches("^\\d+$") && product != null) { //se o valor digitado � um inteiro mesmo e se tem algo selecionado
            product.setStock(Integer.parseInt(quantityField.getText())); // atualiza o estoque na ObservableList
            // checa se algu�m tinha interesse nesse produto
            Supermarket.notificationList.stream()
                    .filter(n -> (n.productName.equals(product.getName()) && Integer.parseInt(quantity) >= n.quantity))
                    .forEach(n -> {
                        Server.sendEmail(Supermarket.getInstance().getUsers().get(n.username).getEmail());
                    });
        }
        quantityField.clear();
    }

    @FXML
    private void initialize() {
        todayField.setText(Server.today.format(Server.FORMATTER));

        all.setOnAction(e -> {
            productsTable.setItems(Supermarket.productsList);
        });
        available.setOnAction(e -> {
            ObservableList<Product> availableProducts = FXCollections.observableArrayList();
            Supermarket.productsList.stream().filter(p -> p.getStock() != 0).forEach(p -> availableProducts.add(p));
            productsTable.setItems(availableProducts);
        });
        unavailable.setOnAction(e -> {
            ObservableList<Product> unavailableProducts = FXCollections.observableArrayList();
            Supermarket.productsList.stream().filter(p -> p.getStock() == 0).forEach(p -> unavailableProducts.add(p));
            productsTable.setItems(unavailableProducts);
        });


        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());
        supplierColumn.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        expirationColumn.setCellValueFactory(cellData -> cellData.getValue().expirationProperty());
        expirationColumn.setCellFactory(column -> {
            return new TableCell<Product, LocalDate>() {
                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(Server.FORMATTER.format(item));
                    }
                }
            };
        });

        productsTable.setItems(Supermarket.productsList);

    }

    public void changeToday() {
        Server.today = datePicker.getValue();
        todayField.setText(Server.today.format(Server.FORMATTER));
    }

    public void openNewProductWindow() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Novo produto");
        window.setResizable(false);
        Parent outOfStockWindow = null;
        try {
            outOfStockWindow = FXMLLoader.load(getClass().getResource("serverNewProductScene.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        window.setScene(new Scene(outOfStockWindow));
        window.showAndWait();
    }
}
