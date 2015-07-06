/*------------------------------------------------------------
 *                      === Supermercado ===
 *  
 *
 *  @author  Barbara Darques (ICMC-USP)
 *             
 *-----------------------------------------------------------*/

package server;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Supermarket {
    public static final String USERS_CSV = "src/users.csv";
    public static final String PRODUCTS_CSV = "src/products.csv";
    public static final String NOTIFICATIONLIST_CSV = "src/notificationList.csv";
    public static final String SALES_CSV = "src/sales.csv";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private HashMap<String, User> users; // lista de usu�rios do sistema
    private HashMap<String, Product> products; //lista de produtos do sistema
    private ArrayList<Sale> sales;
    public static ArrayList<Notifiable> notificationList;
    private static Supermarket supermarket;
    public static ObservableList<Product> productsList = FXCollections.observableArrayList();

    private Supermarket() { // CLASSE SINGLETON
        users = new HashMap<>();
        products = new HashMap<>();
        sales = new ArrayList<>();
        notificationList = new ArrayList<>();
        loadUsers();
        loadProducts();
        loadNotificationList();
        loadSales();
    }


    public static Supermarket getInstance() { // método que faz com que sempre haja somente uma instâncida de Supermarket
        if (supermarket == null) {
            return new Supermarket();
        } else {
            return supermarket;
        }
    }

    public HashMap<String, User> getUsers() {
        return users;
    }

    public HashMap<String, Product> getProducts() {
        return products;
    }

    public ArrayList<Sale> getSales() {
        return sales;
    }

    //=============================================================================================

    public void loadUsers() { // carrega os usu�rios j� registrados no sistema
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(USERS_CSV));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // "<username>, <email>, <password>"
                users.put(values[0], new User(values[0], values[1], values[2]));
            }
        } catch (Exception e) {
            System.err.println("\nErro ao ler o arquivo CSV.\n");
            e.printStackTrace();
        }
    }

    public void registerNewUser(String name, String email, String password) {
        if (users.containsKey(name)) {
            System.err.println("\nEsse usu�rio j� existe no sistema.\n");
        } else {
            users.put(name, new User(name, email, password));
            FileWriter writer;
            try {
                writer = new FileWriter(USERS_CSV, true);
                writer.append(name + "," + email + "," + password + "\n");
                writer.close();
            } catch (IOException ex) {
                System.err.println("\nErro ao escrever no arquivo CSV.\n");
            }
            System.out.println("\nOpera��o realizada com sucesso.\n");
        }
    }


    //=============================================================================================

    public void loadProducts() { // carrega os produtos j� registrados no sistema
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(PRODUCTS_CSV));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // "<nome>, <pre�o>, <estoque>, <validade>, <fornecedor>"
                Product product = new Product(
                        values[0],
                        Double.parseDouble(values[1]),
                        Integer.parseInt(values[2]),
                        LocalDate.parse(values[3], FORMATTER),
                        values[4]);
                products.put(values[0], product);
                productsList.add(product);
            }
        } catch (Exception e) {
            System.err.println("\nErro ao ler o arquivo CSV.\n");
            e.printStackTrace();
        }
    }

    public void registerNewProduct(String name, String price, String stock, String expirationDate, String supplier) {
        if (products.containsKey(name)) {
            System.err.println("\nEsse produto j� existe no sistema.\n");
        } else {
            Product product = new Product(
                    name,
                    Double.parseDouble(price),
                    Integer.parseInt(stock),
                    LocalDate.parse(expirationDate, FORMATTER),
                    supplier);
            products.put(name, product);
            productsList.add(product);
            FileWriter writer;
            try {
                writer = new FileWriter(PRODUCTS_CSV, true);
                writer.append(name + "," + price + "," + stock + "," + expirationDate + "," + supplier + "\n");
                writer.close();
            } catch (IOException ex) {
                System.err.println("\nErro ao escrever no arquivo CSV.\n");
            }
            System.out.println("\nOpera��o realizada com sucesso.\n");
        }
    }

    //=============================================================================================

    public void loadNotificationList() { // carrega os usu�rios j� registrados na lista de notifica��o
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(NOTIFICATIONLIST_CSV));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // "<nome do produto>, <nome do usu�rio>, <quantidade desejada>"
                notificationList.add(new Notifiable(values[0], values[1], Integer.parseInt(values[2])));
            }
        } catch (Exception e) {
            System.err.println("\nErro ao ler o arquivo CSV.\n");
            e.printStackTrace();
        }
    }

    //=============================================================================================

    public void loadSales() { // carrega as vendas j� realizadas
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(SALES_CSV));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // "<nome do produto>, <nome do usu�rio>, <data>, <quantidade >"
                sales.add(new Sale(values[0], values[1], LocalDate.parse(values[2], FORMATTER), Integer.parseInt(values[3])));
            }
        } catch (Exception e) {
            System.err.println("\nErro ao ler o arquivo CSV.\n");
            e.printStackTrace();
        }
    }

    public synchronized void registerNewSale(String productName, String username, String date, String quantity) { // registra uma nova venda
        Product product = products.get(productName);
        product.setStock(product.getStock() - Integer.parseInt(quantity)); // retira do estoque a quantidade comprada
        sales.add(new Sale(productName, username, LocalDate.parse(date, FORMATTER), Integer.parseInt(quantity))); // adiciona � lista de vendas realizadas
        FileWriter writer;
        try {// atualiza arquivo csv
            writer = new FileWriter(SALES_CSV, true);
            writer.append(productName + "," + username + "," + date + "," + quantity + "\n");
            writer.close();
        } catch (IOException ex) {
            System.err.println("\nErro ao escrever no arquivo CSV.\n");
        }
        System.out.println("\nOpera��o realizada com sucesso.\n");
    }

    public void createSalesPDF(String period) //"Dia" ou "Mes"
            throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("Relatorio de Vendas do " + period + ".pdf"));
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Font.NORMAL, BaseColor.BLACK);
        Paragraph title = new Paragraph("Relatorio de Vendas do " + period, font);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);
        document.add(salesTable(period));
        document.close();
        writer.close();

    }

    public PdfPTable salesTable(String period) { // "Dia" ou "Mes"
        PdfPTable table = new PdfPTable(4);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("DATA"));
        cell.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("PRODUTO"));
        cell.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("QUANTIDADE"));
        cell.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("COMPRADOR"));
        cell.setBackgroundColor(BaseColor.ORANGE);
        table.addCell(cell);

        if (period.equals("Mes")) {
            sales.stream()
                    .filter(s -> (s.getDate().getMonthValue() == Server.today.getMonthValue() && s.getDate().getYear() == Server.today.getYear()))
                    .sorted(Comparator.comparing(server.Sale::getDate))
                    .forEach(s -> {
                                table.addCell(s.getDate().format(FORMATTER));
                                table.addCell(s.getProductName());
                                table.addCell(String.valueOf(s.getQuantity()));
                                table.addCell(s.getUsername());
                            }
                    );
        } else if (period.equals("Dia")) {
            System.out.println("Hoje � " + Server.today);
            sales.stream()
                    .filter(s -> s.getDate().isEqual(Server.today))
                    .sorted(Comparator.comparing(Sale::getDate))
                    .forEach(s -> {
                                table.addCell(s.getDate().format(FORMATTER));
                                table.addCell(s.getProductName());
                                table.addCell(String.valueOf(s.getQuantity()));
                                table.addCell(s.getUsername());
                            }
                    );
        }

        return table;
    }

    public void saveProducts() {
        try {
            FileWriter eraser = new FileWriter(PRODUCTS_CSV);
            eraser.append(""); //para apagar o conte�do anterior
            eraser.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        productsList.forEach(p -> {
            FileWriter writer;
            try {// atualiza arquivo csv
                writer = new FileWriter(PRODUCTS_CSV, true);
                writer.append(p.getName() + "," + p.getPrice() + "," + p.getStock() + ","
                        + p.getExpirationDate().format(FORMATTER) + "," + p.getSupplier() + "\n");
                writer.close();
            } catch (IOException ex) {
                System.err.println("\nErro ao escrever no arquivo CSV.\n");
            }
        });
    }

    public void saveNotificationList() throws IOException {
        FileWriter eraser = new FileWriter(NOTIFICATIONLIST_CSV);
        eraser.append(""); //para apagar o conte�do anterior
        eraser.close();

        FileWriter writer = new FileWriter(NOTIFICATIONLIST_CSV, true);
        notificationList.stream().forEach(n -> {
            try {
                writer.append(n.productName + "," + n.username + "," + n.quantity + "\n");
            } catch (IOException e) {
                System.err.println("\nErro ao escrever no arquivo CSV.\n");
            }
        });
        writer.close();

    }
}
