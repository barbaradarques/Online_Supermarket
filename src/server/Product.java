/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package server;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.HashMap;

public class Product {

    private StringProperty name;
    private DoubleProperty price;
    private IntegerProperty stock;
    private ObjectProperty<LocalDate> expirationDate;
    private StringProperty supplier;
    private HashMap<String, Integer> notificationList; // <NomeDoCliente, quantidadeDesejada>

    public Product(String name, double price, int stock, LocalDate expirationDate, String supplier) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.stock = new SimpleIntegerProperty(stock);
        this.expirationDate = new SimpleObjectProperty<>(expirationDate);
        this.supplier = new SimpleStringProperty(supplier);
        notificationList = new HashMap<>();
    }

    public final String getName() {
        return name.get();
    }

    public final double getPrice() {
        return price.get();
    }

    public final LocalDate getExpirationDate() {
        return expirationDate.get();
    }

    public final String getSupplier() {
        return supplier.get();
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public final int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.setValue(stock);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObjectProperty<LocalDate> expirationProperty() {
        return expirationDate;
    }

    public HashMap<String, Integer> getNotificationList() {
        return notificationList;
    }

    public StringProperty supplierProperty() {
        return supplier;
    }
}
