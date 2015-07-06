/*------------------------------------------------------------
 *                      === Supermercado ===
 *
 *
 *  @author  Barbara Darques (ICMC-USP)
 *
 *-----------------------------------------------------------*/


package client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ClientMainSceneController {

    @FXML
    private TextField quantityField; // recupera o campo com id "quantityField" do arquivo FXML
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
    private RadioButton available;
    @FXML
    private RadioButton unavailable;
    @FXML
    private RadioButton all;
    // vari�veis para que outras classes tenham acesso aos campos daqui
    public static String chosenQuantity; // quantidade desejada
    public static String selectedProductName; // nome do produto selecionado

    public static ObservableList<Product> products = FXCollections.observableArrayList();


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

    public void buyProductButtonClicked() throws IOException {
        Product selectedProduct = productsTable.getSelectionModel().getSelectedItem();
        String quantity = quantityField.getText();
        if (quantity.matches("^\\d+$") && selectedProduct != null) { // checa se a quantidade � um inteiro e se tem algo selecionado
            if (selectedProduct.getStock() < Integer.parseInt(quantity)) { //se o estoque for menor que a quantidade desejada
                chosenQuantity = quantity;
                selectedProductName = selectedProduct.getName();
                openOutOfStockWindow(); // abre janela que pergunta se quer ser notificado
            } else {
                Client.serverOutput.println("buy");
                Client.serverOutput.println(selectedProduct.getName()); // envia o nome do produto
                Client.serverOutput.println(quantityField.getText()); //envia a quantidade que vai ser comprada
            }
        }
        quantityField.clear();
    }

    private void openOutOfStockWindow() throws IOException { //cria uma janela perguntando se o cliente quer ser notiicado ou n�o
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Produto em falta!");
        window.setResizable(false);
        Parent outOfStockWindow = FXMLLoader.load(getClass().getResource("outOfStockScene.fxml"));
        window.setScene(new Scene(outOfStockWindow));
        window.showAndWait();
    }


    @FXML
    private void initialize() {
        all.setOnAction(e -> {
            productsTable.setItems(products);
        });
        available.setOnAction(e -> {
            ObservableList<Product> availableProducts = FXCollections.observableArrayList();
            products.stream().filter(p -> p.getStock() != 0).forEach(p -> availableProducts.add(p));
            productsTable.setItems(availableProducts);
        });
        unavailable.setOnAction(e -> {
            ObservableList<Product> availableProducts = FXCollections.observableArrayList();
            products.stream().filter(p -> p.getStock() == 0).forEach(p -> availableProducts.add(p));
            productsTable.setItems(availableProducts);
        });


        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
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
                        setText(Client.FORMATTER.format(item));
                    }
                }
            };
        });

        productsTable.setItems(products);

    }
}
