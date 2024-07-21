import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class InventoryManager extends JFrame {
    private Inventory inventory;
    private User user;
    private JTextField idField, nameField, quantityField, priceField, thresholdField;
    private JTextArea reportArea;

    public InventoryManager() {
        inventory = new Inventory();
        user = new User("admin", "password");

        setTitle("Inventory Manager");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Authentication Panel
        JPanel authPanel = new JPanel();
        authPanel.setLayout(new GridLayout(3, 2));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        authPanel.add(new JLabel("Username:"));
        authPanel.add(userField);
        authPanel.add(new JLabel("Password:"));
        authPanel.add(passField);
        authPanel.add(new JLabel());
        authPanel.add(loginButton);

        add(authPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(6, 2));

        idField = new JTextField();
        nameField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();
        thresholdField = new JTextField();

        mainPanel.add(new JLabel("Product ID:"));
        mainPanel.add(idField);
        mainPanel.add(new JLabel("Product Name:"));
        mainPanel.add(nameField);
        mainPanel.add(new JLabel("Quantity:"));
        mainPanel.add(quantityField);
        mainPanel.add(new JLabel("Price:"));
        mainPanel.add(priceField);
        mainPanel.add(new JLabel("Low Stock Threshold:"));
        mainPanel.add(thresholdField);

        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton reportButton = new JButton("Generate Report");

        mainPanel.add(addButton);
        mainPanel.add(editButton);
        mainPanel.add(deleteButton);
        mainPanel.add(reportButton);

        add(mainPanel, BorderLayout.CENTER);

        // Report Area
        reportArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(reportArea);
        add(scrollPane, BorderLayout.SOUTH);

        // Button Actions
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());
                if (user.getUsername().equals(username) && user.authenticate(password)) {
                    authPanel.setVisible(false);
                    mainPanel.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());
                inventory.addProduct(new Product(id, name, quantity, price));
                JOptionPane.showMessageDialog(null, "Product added successfully");
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());
                inventory.editProduct(id, quantity, price);
                JOptionPane.showMessageDialog(null, "Product edited successfully");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                inventory.deleteProduct(id);
                JOptionPane.showMessageDialog(null, "Product deleted successfully");
            }
        });

        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int threshold = Integer.parseInt(thresholdField.getText());
                ArrayList<Product> lowStockProducts = inventory.getLowStockProducts(threshold);
                reportArea.setText("Low Stock Products:\n");
                for (Product product : lowStockProducts) {
                    reportArea.append(product.toString() + "\n");
                }
            }
        });

        mainPanel.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InventoryManager().setVisible(true);
            }
        });
    }
}

class Product {
    private String id;
    private String name;
    private int quantity;
    private double price;

    public Product(String id, String name, int quantity, double price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product [ID=" + id + ", Name=" + name + ", Quantity=" + quantity + ", Price=" + price + "]";
    }
}

class Inventory {
    private ArrayList<Product> products;

    public Inventory() {
        products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void editProduct(String id, int quantity, double price) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                product.setQuantity(quantity);
                product.setPrice(price);
                break;
            }
        }
    }

    public void deleteProduct(String id) {
        products.removeIf(product -> product.getId().equals(id));
    }

    public Product getProduct(String id) {
        for (Product product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<Product> getLowStockProducts(int threshold) {
        ArrayList<Product> lowStockProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getQuantity() < threshold) {
                lowStockProducts.add(product);
            }
        }
        return lowStockProducts;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }
}

class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }
}