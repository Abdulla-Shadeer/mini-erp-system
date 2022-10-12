/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.erpsystem;

import static java.lang.Integer.parseInt;
import java.sql.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import dbConn;

/**
 *
 * @author shadeer
 */
public final class dashboard extends javax.swing.JFrame {

    /**
     * Creates new form dashboard
     *
     * @throws java.sql.SQLException
     */
    public dashboard() throws SQLException {
        initComponents();
        getDateTime();
        getProductData();
        loadOrderTable();
        loadOrdersCombo();
        loadSalesTable();
        setEarningsLabel();
        setOrdersLabel();
    }

    public void setOrdersLabel() throws SQLException{
    String query ="SELECT COUNT(*) FROM orders WHERE status LIKE '%processing%'";
    
    var con = dbConn.getConnection();
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    
    while(rs.next()){
    String rowCount = String.valueOf(rs.getInt("count(*)"));
    ordersNew.setText(rowCount);
    }
    
    }
    
    public void setEarningsLabel() throws SQLException{
    String query ="SELECT earning FROM sales";
    int tmp;
    int earnings =0;
    
    var con = dbConn.getConnection();
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    while(rs.next()){
    tmp = parseInt(rs.getString("earning"));
    earnings = earnings + tmp;
    }
    earningsTotal.setText(String.valueOf(earnings));
    }
    
    public void loadSalesTable() throws SQLException{
    DefaultTableModel tblmdl = (DefaultTableModel)salesTable.getModel();
    tblmdl.setRowCount(0);
        
    String query = "SELECT * FROM sales";
    
    var con = dbConn.getConnection();
    Statement stmt = con.createStatement();
    ResultSet rs = stmt.executeQuery(query);
    
    while (rs.next()){
    int saleid = rs.getInt("id");
    int oId = rs.getInt("orderId");
    String performedOn = rs.getString("datePerformed");
    String earning = rs.getString("earning");
    
    String tbldata[]={String.valueOf(saleid), String.valueOf(oId), performedOn,earning};
   
    tblmdl.addRow(tbldata);
    }
    
    }
    
    public void addSales() throws SQLException{
    String performedDate = dateTime.formattedDate;
    String earning = bomOrderTotal.getText();
    String order = String.valueOf(ordersCombo.getSelectedItem());
    String slicedOid = order.substring(0,2);
    String trimmedOid = slicedOid.trim();
    int Oid = parseInt(trimmedOid); 
    
    String query = "INSERT INTO sales (orderId, datePerformed, earning) VALUES (?,?,?)";
    
    var con = dbConn.getConnection();
    PreparedStatement stmt = con.prepareStatement(query);
    stmt.setInt(1, Oid);
    stmt.setString(2, performedDate);
    stmt.setString(3, earning);
    stmt.executeUpdate();
    }
    
    public String getProductName(int id) throws SQLException{
        var con = dbConn.getConnection();
        String pname="";
        String query = "SELECT productName FROM products WHERE id="+id;
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while(rs.next()){
            pname = rs.getString("productName");
        }
        return pname;
    }
    
    public void loadOrdersCombo() throws SQLException {
        ordersCombo.removeAllItems();
        
        String query = "SELECT o.id, o.qty, p.productName, p.id FROM orders o "+
                "JOIN products p on(o.productId = p.id) WHERE o.status LIKE '%processing%' ";
        var con = dbConn.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String oIdNameQty;

        while (rs.next()) {
            String id = String.valueOf(rs.getInt("id"));
            String product = rs.getString("productName");
            String qty = String.valueOf(rs.getInt("qty"));
            String pid = String.valueOf(rs.getInt("p.id"));
            oIdNameQty = id + " : "+pid+" "+ product+" QTY: "+qty+" ";
            ordersCombo.addItem(oIdNameQty);
        }
    }

    public void loadOrderTable() throws SQLException {

        int id;
        String product;
        String status;
        String datePlaced;
        int qty;
        String leadTime;

        String query = "SELECT o.id, o.qty, o.leadTime, o.status, o.datePlaced, p.productName"
                + " FROM orders o JOIN products p ON(o.productId = p.id)";

        var con = dbConn.getConnection();

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {

            id = rs.getInt("id");
            product = rs.getString("productName");
            datePlaced = rs.getString("datePlaced");
            status = rs.getString("status");
            qty = rs.getInt("qty");
            leadTime = rs.getString("leadTime");

            String tableData[] = {String.valueOf(id), datePlaced, product, String.valueOf(qty), leadTime, status};

            DefaultTableModel tbl = (DefaultTableModel) orderTable.getModel();
            tbl.addRow(tableData);

        }
    }

    public void getDateTime() {
        dateTime.main(null);

        String today = dateTime.formattedDate;
        String now = dateTime.formattedTime;
        date.setText(today);
        time.setText(now);
    }

    public void getProductData() throws SQLException {
        String query = "SELECT * FROM products";
        var con = dbConn.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            String id = String.valueOf(rs.getInt("id"));
            String product = rs.getString("productName");

            String tableData[] = {id, product};

            DefaultTableModel pTable = (DefaultTableModel) productTable.getModel();
            pTable.addRow(tableData);
        }

    }

    public void setProductsCombo() throws SQLException {
        String query = "SELECT * FROM products";
        var con = dbConn.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        String pIdAndName;

        //resetting product combos in all windows
        productCombo.removeAllItems();
        productComboInInventory.removeAllItems();
        productComboInOrders.removeAllItems();
        productComboInOrderModify.removeAllItems();

        while (rs.next()) {
            String id = String.valueOf(rs.getInt("id"));
            String product = rs.getString("productName");
            pIdAndName = id + " " + product;
            productCombo.addItem(pIdAndName);
            productComboInInventory.addItem(pIdAndName);
            productComboInOrders.addItem(pIdAndName);
            productComboInOrderModify.addItem(pIdAndName);
        }
    }

    public void setPartsTable() throws SQLException {

        String query = "SELECT * FROM parts";
        var con = dbConn.getConnection();
        String id;
        String part;
        int belongingProduct;
        String stock;
        String price;

        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            id = String.valueOf(rs.getInt("id"));
            part = rs.getString("partName");
            belongingProduct = rs.getInt("belongingProductId");
            stock = String.valueOf(rs.getInt("stock"));
            price = rs.getString("unitPrice");

            String tableData[] = {id, part, String.valueOf(belongingProduct), stock, price};

            DefaultTableModel tableMdl = (DefaultTableModel) partsTable.getModel();
            tableMdl.addRow(tableData);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dashboardTab = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        ordersNew = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        earningsTotal = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        date = new javax.swing.JLabel();
        time = new javax.swing.JLabel();
        usernameLable = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel12 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        productName = new javax.swing.JTextField();
        btnAddProduct = new javax.swing.JButton();
        jSeparator10 = new javax.swing.JSeparator();
        jPanel15 = new javax.swing.JPanel();
        pIdSearchInput = new javax.swing.JTextField();
        pNameSearchInput = new javax.swing.JTextField();
        pNameSearch = new javax.swing.JButton();
        pIdSearch = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        pId = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        pName = new javax.swing.JTextField();
        btnProductUpdate = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        btnProductDelete = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel16 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        partsTable = new javax.swing.JTable();
        jPanel17 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        partNameInput = new javax.swing.JTextField();
        partStockInput = new javax.swing.JTextField();
        partPriceInput = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        btnPartAdd = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        productCombo = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        partUnitsInput = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        btnPartIdSearch = new javax.swing.JButton();
        partIdSearchInput = new javax.swing.JTextField();
        btnPartNameSearch = new javax.swing.JButton();
        partNameSearchInput = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        partNameModifyInput = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        partStockModifyInput = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        partPriceModifyInput = new javax.swing.JTextField();
        btnPartUpdate = new javax.swing.JButton();
        btnPartDelete = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        productComboInInventory = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        partIdModifyInput = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        unitsPerProModify = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel19 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        jPanel20 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        productComboInOrders = new javax.swing.JComboBox<>();
        jLabel24 = new javax.swing.JLabel();
        productQtyInput = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        customerNameInput = new javax.swing.JTextField();
        customerContactInput = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        btnPlaceOrder = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        orderLeadTimeInput = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jButton16 = new javax.swing.JButton();
        orderSearchIdInput = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        productComboInOrderModify = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        orderQtyModifyInput = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        cNameInput = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        cContactInput = new javax.swing.JTextField();
        btnOrdersModify = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        ordersModifyIdInput = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        ordersLeadTimeModifyInput = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        ordersCombo = new javax.swing.JComboBox<>();
        jButton19 = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        bomTable = new javax.swing.JTable();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel41 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        orderProduct = new javax.swing.JLabel();
        orderQty = new javax.swing.JLabel();
        bomOrderTotal = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        salesTable = new javax.swing.JTable();
        jPanel22 = new javax.swing.JPanel();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel23 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        p1 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        p2 = new javax.swing.JTextField();
        p3 = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        jSeparator16 = new javax.swing.JSeparator();
        jLabel49 = new javax.swing.JLabel();
        ma = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        pr1 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        pr2 = new javax.swing.JTextField();
        pr3 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jButton27 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JSeparator();
        wma = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                formPropertyChange(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        dashboardTab.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        dashboardTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dashboardTabMouseClicked(evt);
            }
        });

        jPanel7.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jPanel7PropertyChange(evt);
            }
        });

        jPanel8.setBackground(new java.awt.Color(0, 255, 153));
        jPanel8.setPreferredSize(new java.awt.Dimension(150, 100));

        ordersNew.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        ordersNew.setForeground(new java.awt.Color(255, 255, 255));
        ordersNew.setText("00.00");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(ordersNew)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(ordersNew, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addGap(28, 28, 28))
        );

        jPanel9.setBackground(new java.awt.Color(255, 102, 102));
        jPanel9.setPreferredSize(new java.awt.Dimension(150, 100));

        earningsTotal.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        earningsTotal.setForeground(new java.awt.Color(255, 255, 255));
        earningsTotal.setText("00.00 LKR ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(earningsTotal)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(earningsTotal)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel1.setText("New Orders");

        jLabel2.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel2.setText("Total Earnings");

        jPanel10.setBackground(new java.awt.Color(102, 102, 255));

        date.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        date.setForeground(new java.awt.Color(255, 255, 255));
        date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        date.setText("Date");

        time.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        time.setForeground(new java.awt.Color(255, 255, 255));
        time.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        time.setText("Time");

        usernameLable.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        usernameLable.setForeground(new java.awt.Color(255, 255, 255));
        usernameLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        usernameLable.setText("User Name");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(time, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(usernameLable, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(usernameLable)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(date)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(time)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(239, 239, 239))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(349, Short.MAX_VALUE))
        );

        dashboardTab.addTab("Home", jPanel7);

        jTabbedPane2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Product"
            }
        ));
        jScrollPane1.setViewportView(productTable);
        if (productTable.getColumnModel().getColumnCount() > 0) {
            productTable.getColumnModel().getColumn(0).setMaxWidth(60);
        }

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("View", jPanel12);

        jLabel9.setText("Product Name :");

        btnAddProduct.setText("Add ");
        btnAddProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddProductActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel13Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(32, 32, 32)
                        .addComponent(productName, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnAddProduct))
                .addContainerGap(432, Short.MAX_VALUE))
            .addComponent(jSeparator10)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(productName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnAddProduct)
                .addContainerGap(456, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Add", jPanel13);

        pNameSearch.setText("Search By Name");
        pNameSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pNameSearchActionPerformed(evt);
            }
        });

        pIdSearch.setText("Search By ID");
        pIdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pIdSearchActionPerformed(evt);
            }
        });

        jLabel11.setText("ID");

        jLabel12.setText("Name");

        btnProductUpdate.setText("Update");
        btnProductUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductUpdateActionPerformed(evt);
            }
        });

        btnProductDelete.setText("delete");
        btnProductDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator8)
                    .addGroup(jPanel15Layout.createSequentialGroup()
                        .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(pIdSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pIdSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(71, 71, 71)
                                .addComponent(pNameSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pNameSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addComponent(btnProductUpdate)
                                .addGap(18, 18, 18)
                                .addComponent(btnProductDelete))
                            .addGroup(jPanel15Layout.createSequentialGroup()
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(61, 61, 61)
                                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pName, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(pId, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 271, Short.MAX_VALUE))
                    .addComponent(jSeparator9))
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(pIdSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pIdSearch)
                    .addComponent(pNameSearch)
                    .addComponent(pNameSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(pId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(pName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProductUpdate)
                    .addComponent(btnProductDelete))
                .addContainerGap(362, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Search/ Modify", jPanel15);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane2)
                .addContainerGap())
        );

        dashboardTab.addTab("Products", jPanel4);

        jTabbedPane3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jTabbedPane3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane3MouseClicked(evt);
            }
        });

        partsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Part ID", "Name", "Belongs to (Id, Name)", "Stock", "Unit Price (LKR)"
            }
        ));
        jScrollPane5.setViewportView(partsTable);
        if (partsTable.getColumnModel().getColumnCount() > 0) {
            partsTable.getColumnModel().getColumn(0).setMaxWidth(60);
            partsTable.getColumnModel().getColumn(1).setMinWidth(150);
            partsTable.getColumnModel().getColumn(2).setMinWidth(150);
            partsTable.getColumnModel().getColumn(4).setMinWidth(80);
        }

        javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
        jPanel16.setLayout(jPanel16Layout);
        jPanel16Layout.setHorizontalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel16Layout.setVerticalGroup(
            jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel16Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 559, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane3.addTab("View List", jPanel16);

        jLabel14.setText("Name : ");

        jLabel15.setText("Belonging Product  :");

        jLabel16.setText("Stock :");

        jLabel17.setText("Unit Price");

        partNameInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partNameInputActionPerformed(evt);
            }
        });

        jLabel18.setText("LKR");

        btnPartAdd.setText("Add to list");
        btnPartAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartAddActionPerformed(evt);
            }
        });

        productCombo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productComboMouseClicked(evt);
            }
        });
        productCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                productComboActionPerformed(evt);
            }
        });

        jLabel13.setText("Required Units Per Product :");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator4)
                            .addGroup(jPanel17Layout.createSequentialGroup()
                                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnPartAdd)
                                    .addGroup(jPanel17Layout.createSequentialGroup()
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel17))
                                        .addGap(37, 37, 37)
                                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(partPriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(partStockInput, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(partNameInput)
                                .addComponent(productCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(partUnitsInput, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(412, Short.MAX_VALUE))))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel14)
                    .addComponent(partNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(productCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(partUnitsInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel16)
                    .addComponent(partStockInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel17)
                    .addComponent(partPriceInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(btnPartAdd)
                .addContainerGap(256, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Add to list", jPanel17);

        btnPartIdSearch.setText("Search By ID");
        btnPartIdSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartIdSearchActionPerformed(evt);
            }
        });

        btnPartNameSearch.setText("Search By Name");
        btnPartNameSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartNameSearchActionPerformed(evt);
            }
        });

        partNameSearchInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partNameSearchInputActionPerformed(evt);
            }
        });

        jLabel19.setText("Name : ");

        partNameModifyInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                partNameModifyInputActionPerformed(evt);
            }
        });

        jLabel20.setText("Belonging Product :");

        jLabel21.setText("Stock :");

        jLabel22.setText("Unit Price");

        btnPartUpdate.setText("Update");
        btnPartUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartUpdateActionPerformed(evt);
            }
        });

        btnPartDelete.setText("Delete");
        btnPartDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPartDeleteActionPerformed(evt);
            }
        });

        productComboInInventory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productComboInInventoryMouseClicked(evt);
            }
        });

        jLabel3.setText("ID :");

        jLabel38.setText("Units Per Product :");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator5)
                    .addComponent(jSeparator6)
                    .addGroup(jPanel18Layout.createSequentialGroup()
                        .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(btnPartIdSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(partIdSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(71, 71, 71)
                                .addComponent(btnPartNameSearch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(partNameSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addComponent(btnPartUpdate)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPartDelete))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel22))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(partStockModifyInput)
                                    .addComponent(partPriceModifyInput, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
                            .addGroup(jPanel18Layout.createSequentialGroup()
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel20)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel38))
                                .addGap(37, 37, 37)
                                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(unitsPerProModify, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(partNameModifyInput, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                        .addComponent(productComboInInventory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(partIdModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 271, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(partIdSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPartIdSearch)
                    .addComponent(btnPartNameSearch)
                    .addComponent(partNameSearchInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(partIdModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel19)
                    .addComponent(partNameModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(productComboInInventory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(unitsPerProModify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel21)
                    .addComponent(partStockModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel22)
                    .addComponent(partPriceModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPartUpdate)
                    .addComponent(btnPartDelete))
                .addContainerGap(195, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Search/ Modify", jPanel18);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane3)
                .addContainerGap())
        );

        dashboardTab.addTab("Inventory", jPanel1);

        jTabbedPane4.setFocusable(false);
        jTabbedPane4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Date Placed", "Product", "Quantity", "Lead Time", "Order Status"
            }
        ));
        jScrollPane6.setViewportView(orderTable);
        if (orderTable.getColumnModel().getColumnCount() > 0) {
            orderTable.getColumnModel().getColumn(0).setMaxWidth(60);
            orderTable.getColumnModel().getColumn(1).setMinWidth(130);
            orderTable.getColumnModel().getColumn(1).setMaxWidth(130);
            orderTable.getColumnModel().getColumn(2).setPreferredWidth(250);
            orderTable.getColumnModel().getColumn(3).setMaxWidth(60);
        }

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(143, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("All Orders", jPanel19);

        jLabel23.setText("Select a Product");

        jLabel24.setText("Quantity");

        jLabel25.setText("Customer Name");

        jLabel27.setText("Customer Contact Number");

        btnPlaceOrder.setText("Place Order");
        btnPlaceOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlaceOrderActionPerformed(evt);
            }
        });

        jLabel4.setText("Lead Time");

        jLabel5.setText("Weeks");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3)
                    .addGroup(jPanel20Layout.createSequentialGroup()
                        .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnPlaceOrder)
                            .addGroup(jPanel20Layout.createSequentialGroup()
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel24, javax.swing.GroupLayout.Alignment.LEADING))
                                    .addComponent(jLabel27)
                                    .addComponent(jLabel4))
                                .addGap(25, 25, 25)
                                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(productQtyInput, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(productComboInOrders, 0, 200, Short.MAX_VALUE)
                                    .addComponent(customerNameInput)
                                    .addComponent(customerContactInput)
                                    .addGroup(jPanel20Layout.createSequentialGroup()
                                        .addComponent(orderLeadTimeInput, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel5)))))
                        .addGap(0, 404, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(productComboInOrders, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel24)
                    .addComponent(productQtyInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(customerNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel27)
                    .addComponent(customerContactInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(orderLeadTimeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(17, 17, 17)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnPlaceOrder)
                .addContainerGap(313, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Place Order", jPanel20);

        jButton16.setText("Search By Order ID");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jLabel26.setText("Change Product");

        productComboInOrderModify.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productComboInOrderModifyMouseClicked(evt);
            }
        });

        jLabel28.setText("Edit Qty");

        jLabel29.setText("Edit Customer Name");

        jLabel30.setText("Edit Contact Number");

        btnOrdersModify.setText("Update");
        btnOrdersModify.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOrdersModifyActionPerformed(evt);
            }
        });

        jLabel6.setText("ID");

        jLabel10.setText("Edit Lead Time");

        jButton3.setText("delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(orderSearchIdInput, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton16))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addComponent(btnOrdersModify)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3))
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel28, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(47, 47, 47)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(ordersModifyIdInput, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(orderQtyModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(productComboInOrderModify, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel21Layout.createSequentialGroup()
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel30))
                                    .addComponent(jLabel10))
                                .addGap(25, 25, 25)
                                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cNameInput)
                                    .addComponent(cContactInput, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(ordersLeadTimeModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 431, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(orderSearchIdInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(ordersModifyIdInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(productComboInOrderModify, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel28)
                    .addComponent(orderQtyModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(ordersLeadTimeModifyInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(cNameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel30)
                    .addComponent(cContactInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOrdersModify)
                    .addComponent(jButton3))
                .addContainerGap(215, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Search/ Modify Orders", jPanel21);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4)
                .addContainerGap())
        );

        dashboardTab.addTab("Orders", jPanel2);

        jLabel36.setText("Select Order :");

        jButton19.setText("Calculate BOM");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel37.setText("Product :");

        bomTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Part Name", "Units Per Product", "Unit Price ", "Total Required Units", "Total Cost"
            }
        ));
        jScrollPane8.setViewportView(bomTable);

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 102, 0));
        jLabel41.setText("Total Cost :");

        jLabel39.setText("Qty :");

        jLabel40.setText("LKR");

        jButton4.setText("Complete This Order");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        orderProduct.setText("---");

        orderQty.setText("---");

        bomOrderTotal.setText("---");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator12)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                    .addComponent(jSeparator13)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton19)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel36)
                                .addGap(18, 18, 18)
                                .addComponent(ordersCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createSequentialGroup()
                                        .addComponent(orderProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(orderQty, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel41)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bomOrderTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel40))
                            .addComponent(jButton4))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(ordersCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton19)
                .addGap(18, 18, 18)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(jLabel31)
                    .addComponent(orderProduct))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(orderQty))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(jLabel40)
                    .addComponent(bomOrderTotal))
                .addGap(18, 18, 18)
                .addComponent(jSeparator13, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addContainerGap())
        );

        dashboardTab.addTab("BOM", jPanel5);

        salesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "sales ID", "Order ID", "Date performed", "Earning"
            }
        ));
        jScrollPane2.setViewportView(salesTable);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(371, Short.MAX_VALUE))
        );

        dashboardTab.addTab("Sales", jPanel6);

        jTabbedPane5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel46.setText("Period 01 closing price");

        jLabel47.setText("Period 02 closing price");

        jLabel48.setText("Period 03 closing price");

        jButton25.setText("Calculate MA");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jLabel49.setText("Moving Average Is :");

        jLabel7.setText("LKR");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel23Layout.createSequentialGroup()
                            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel46)
                                .addComponent(jLabel48)
                                .addComponent(jLabel47))
                            .addGap(35, 35, 35)
                            .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(p2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(p3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(p1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jButton25)
                        .addComponent(jSeparator16))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel49)
                        .addGap(18, 18, 18)
                        .addComponent(ma, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(547, Short.MAX_VALUE))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(p1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(p2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48)
                    .addComponent(p3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton25)
                .addGap(18, 18, 18)
                .addComponent(jSeparator16, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(ma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addContainerGap(382, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Moving Average", jPanel23);

        jLabel51.setText("Period 01 closing price");

        jLabel52.setText("Period 02 closing price");

        jLabel53.setText("Period 03 closing price");

        jButton27.setText("Calculate WMA");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jLabel54.setText("Weighted Moving Average Is :");

        jLabel8.setText("LKR");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel24Layout.createSequentialGroup()
                            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel51)
                                .addComponent(jLabel53)
                                .addComponent(jLabel52))
                            .addGap(35, 35, 35)
                            .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(pr2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pr3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(pr1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jButton27)
                        .addComponent(jSeparator18))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel54)
                        .addGap(18, 18, 18)
                        .addComponent(wma, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(500, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51)
                    .addComponent(pr1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(pr2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(pr3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton27)
                .addGap(18, 18, 18)
                .addComponent(jSeparator18, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(wma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(390, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Weigted Moviing Average", jPanel24);

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane5)
                .addContainerGap())
        );

        dashboardTab.addTab("Sales Forcast", jPanel22);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dashboardTab, javax.swing.GroupLayout.PREFERRED_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(dashboardTab)
                .addGap(31, 31, 31))
        );

        setBounds(400, 50, 814, 763);
    }// </editor-fold>//GEN-END:initComponents

    private void partNameModifyInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partNameModifyInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partNameModifyInputActionPerformed

    private void partNameInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partNameInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partNameInputActionPerformed

    private void jPanel7PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jPanel7PropertyChange
        // TODO add your handling code here:

    }//GEN-LAST:event_jPanel7PropertyChange

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_formWindowActivated

    private void formPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_formPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_formPropertyChange

    private void btnAddProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddProductActionPerformed
        // TODO add your handling code here:

        var con = dbConn.getConnection();
        String pNameInput = this.productName.getText();
        String query = "INSERT INTO products (productName) VALUES(?)";

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, pNameInput);
            stmt.executeUpdate();
            con.close();
            this.productName.setText("");
            JOptionPane.showMessageDialog(rootPane, "Successfully added new Product ");

            DefaultTableModel pTable = (DefaultTableModel) productTable.getModel();
            pTable.setRowCount(0); //to reset previous record and prevent data redundent
            getProductData();
        } catch (SQLException ex) {
            ex.toString();
        }

    }//GEN-LAST:event_btnAddProductActionPerformed

    private void pIdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pIdSearchActionPerformed
        // TODO add your handling code here:
        pNameSearchInput.setText("");
        var con = dbConn.getConnection();
        int input = parseInt(pIdSearchInput.getText());
        String query = "SELECT * FROM products WHERE id = " + input;
        String id = "";
        String name = "";
        try {
            Statement stmt = con.createStatement();
            //stmt.setInt(1, input);
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                id = String.valueOf(rs.getInt("id"));
                name = rs.getString("productName");
            }

            pId.setText(id);
            pId.setEditable(false);
            pName.setText(name);
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pIdSearchActionPerformed

    private void pNameSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pNameSearchActionPerformed
        // TODO add your handling code here:
        pIdSearchInput.setText("");
        var con = dbConn.getConnection();
        String input = pNameSearchInput.getText();

        String query = "SELECT * FROM products WHERE productName LIKE '%" + input + "%'";
        String id = "";
        String name = "";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                id = String.valueOf(rs.getInt("id"));
                name = rs.getString("productName");
            }

            pId.setText(id);
            pId.setEditable(false);
            pName.setText(name);
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_pNameSearchActionPerformed

    private void btnProductUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductUpdateActionPerformed
        // TODO add your handling code here:
        String modifiedName = pName.getText();
        int changedProductId = parseInt(pId.getText());
        String query = "UPDATE products SET productName = ? WHERE id = ?";

        var con = dbConn.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, modifiedName);
            stmt.setInt(2, changedProductId);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(rootPane, "Successfully Updated!");
            //resetting product view table after update
            DefaultTableModel pTable = (DefaultTableModel) productTable.getModel();
            pTable.setRowCount(0);
            getProductData();
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProductUpdateActionPerformed

    private void btnProductDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductDeleteActionPerformed
        // TODO add your handling code here:
        int productIdToDelete = parseInt(pId.getText());
        String query = "DELETE FROM products WHERE id = ?";

        var con = dbConn.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, productIdToDelete);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(rootPane, "Product Deleted!");
            pId.setText("");
            pName.setText("");
            pIdSearchInput.setText("");
            pNameSearchInput.setText("");
            //resetting product view table after update
            DefaultTableModel pTable = (DefaultTableModel) productTable.getModel();
            pTable.setRowCount(0);
            getProductData();
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnProductDeleteActionPerformed

    private void productComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_productComboActionPerformed

    }//GEN-LAST:event_productComboActionPerformed

    private void productComboMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productComboMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_productComboMouseClicked

    private void jTabbedPane3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane3MouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_jTabbedPane3MouseClicked

    private void btnPartAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartAddActionPerformed
        // TODO add your handling code here:
        String partName = partNameInput.getText();
        String belongingTo = String.valueOf(productCombo.getSelectedItem());
        String sliced = belongingTo.substring(0,2);
        String trimmed = sliced.trim();
        int belongingId = parseInt(trimmed);
        int unitsPerProduct = parseInt(partUnitsInput.getText());
        int stock = parseInt(partStockInput.getText());
        String price = partPriceInput.getText();
        int partId;

        String InsertQuery = "INSERT INTO parts (partName, belongingProductId, "+
                "unitsPerProduct, stock, unitPrice) VALUES (?,?,?,?,?)";
        var con = dbConn.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(InsertQuery);
            stmt.setString(1, partName);
            stmt.setInt(2, belongingId);
            stmt.setInt(3,unitsPerProduct);
            stmt.setInt(4, stock);
            stmt.setString(5, price);
            stmt.executeUpdate();
            con.close();

            partNameInput.setText("");
            partStockInput.setText("");
            partPriceInput.setText("");
            JOptionPane.showMessageDialog(null, "Successfully inserted 01 part!");

            //resetting and updating parts table
            DefaultTableModel partTable = (DefaultTableModel) partsTable.getModel();
            partTable.setRowCount(0);
            setPartsTable();

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPartAddActionPerformed

    private void dashboardTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dashboardTabMouseClicked
        // TODO add your handling code here:
        //resetting product combo in product tab
        productCombo.removeAllItems();

        //resetting parts table in inventory window
        DefaultTableModel partTable = (DefaultTableModel) partsTable.getModel();
        partTable.setRowCount(0);

        try {
            // TODO add your handling code here:
            setPartsTable();
            setProductsCombo();

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_dashboardTabMouseClicked

    private void partNameSearchInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_partNameSearchInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_partNameSearchInputActionPerformed

    private void btnPartIdSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartIdSearchActionPerformed
        // TODO add your handling code here:
        int id = parseInt(partIdSearchInput.getText());
        String query = "SELECT p.id, p.partName, p.stock, p.unitPrice, "+
                "p.belongingProductId, p.unitsPerProduct, pd.productName FROM parts p JOIN products pd "+
                "ON(p.belongingProductId = pd.id) WHERE p.id = " + id;

        var con = dbConn.getConnection();

        try {
            Statement stmt = con.createStatement();
            //stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                partIdModifyInput.setText(String.valueOf(rs.getInt("id")));
                partIdModifyInput.setEditable(false);
                partNameModifyInput.setText(rs.getString("partName"));
                partStockModifyInput.setText(String.valueOf(rs.getInt("stock")));
                partPriceModifyInput.setText(rs.getString("unitPrice"));
                productComboInInventory.removeAllItems();
                String pid = String.valueOf(rs.getInt("belongingProductId"));
                String pname = rs.getString("pd.productName");
                String pidandname = pid + " " + pname;
                productComboInInventory.addItem(pidandname);
                unitsPerProModify.setText(String.valueOf(rs.getInt("unitsPerProduct")));
            }
            con.close();
            partNameSearchInput.setText("");

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPartIdSearchActionPerformed

    private void productComboInInventoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productComboInInventoryMouseClicked
        // TODO add your handling code here:
        productComboInInventory.removeAllItems();
        try {
            setProductsCombo();
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_productComboInInventoryMouseClicked

    private void btnPartNameSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartNameSearchActionPerformed
        // TODO add your handling code here:
        String name = partNameSearchInput.getText();
        String query = "SELECT * FROM parts WHERE partName LIKE '%" + name + "%'";

        var con = dbConn.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                partIdModifyInput.setText(String.valueOf(rs.getInt("id")));
                partNameModifyInput.setText(rs.getString("partName"));
                partStockModifyInput.setText(String.valueOf(rs.getInt("stock")));
                partPriceModifyInput.setText(rs.getString("unitPrice"));
                productComboInInventory.removeAllItems();
                productComboInInventory.addItem(rs.getString("belongingProductId"));
            }
            con.close();
            partIdSearchInput.setText("");

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPartNameSearchActionPerformed

    private void btnPartUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartUpdateActionPerformed
        // TODO add your handling code here:
        int id = parseInt(partIdModifyInput.getText());
        String name = partNameModifyInput.getText();
        String belongingProduct = String.valueOf(productComboInInventory.getSelectedItem());
        String sliced = belongingProduct.substring(0,2);
        String trimmed = sliced.trim();
        int belongingId = parseInt(trimmed);
        int stock = parseInt(partStockModifyInput.getText());
        String price = partPriceModifyInput.getText();
        int unitsperpro = parseInt(unitsPerProModify.getText());
        
        String query = "UPDATE parts SET partName = ?,belongingProductId = ?, "
                + "unitsPerProduct=?, stock = ?, unitPrice = ? WHERE id = ?";

        var con = dbConn.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setInt(2, belongingId);
            stmt.setInt(3, unitsperpro);
            stmt.setInt(4, stock);
            stmt.setString(5, price);
            stmt.setInt(6, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "successfully updated!");
            partIdModifyInput.setText("");
            unitsPerProModify.setText("");
            partNameModifyInput.setText("");
            partStockModifyInput.setText("");
            partPriceModifyInput.setText("");
            productComboInInventory.removeAllItems();

            DefaultTableModel partTable = (DefaultTableModel) partsTable.getModel();
            partTable.setRowCount(0);

            setPartsTable();

            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnPartUpdateActionPerformed

    private void btnPartDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPartDeleteActionPerformed
        // TODO add your handling code here:
        int id = parseInt(partIdModifyInput.getText());

        String query = "DELETE FROM parts WHERE id = ?";

        var con = dbConn.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "successfully deleted!");
            partIdModifyInput.setText("");
            partNameModifyInput.setText("");
            partStockModifyInput.setText("");
            partPriceModifyInput.setText("");
            productComboInInventory.removeAllItems();

            DefaultTableModel partTable = (DefaultTableModel) partsTable.getModel();
            partTable.setRowCount(0);

            setPartsTable();

            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPartDeleteActionPerformed

    private void btnPlaceOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlaceOrderActionPerformed
        // TODO add your handling code here:
        String product = String.valueOf(productComboInOrders.getSelectedItem());
        String sliced = product.substring(0, 2);
        String trimmed = sliced.trim();
        int selectedProductId = parseInt(trimmed);
        int qty = parseInt(productQtyInput.getText());
        String customer = customerNameInput.getText();
        String customerContact = customerContactInput.getText();
        String leadTime = orderLeadTimeInput.getText();
        String status = "processing";
        //getting the date from dateTime clss
        String today = dateTime.formattedDate;

        String query = "INSERT INTO orders (productId, qty, customerName, customerContact, "
                + "leadTime, datePlaced, status) VALUES(?,?,?,?,?,?,?)";

        var con = dbConn.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, selectedProductId);
            stmt.setInt(2, qty);
            stmt.setString(3, customer);
            stmt.setString(4, customerContact);
            stmt.setString(5, leadTime);
            stmt.setString(6, today);
            stmt.setString(7, status);
            stmt.executeUpdate();
            con.close();

            productQtyInput.setText("");
            customerNameInput.setText("");
            customerContactInput.setText("");
            orderLeadTimeInput.setText("");

            JOptionPane.showMessageDialog(null, "New order Placed!");
            
            setOrdersLabel();

            DefaultTableModel tbl = (DefaultTableModel) orderTable.getModel();
            tbl.setRowCount(0);
            loadOrderTable();
            ordersCombo.removeAllItems();
            loadOrdersCombo();

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnPlaceOrderActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:

        int id = parseInt(orderSearchIdInput.getText());
        String query = "SELECT o.id, o.qty, o.leadTime, o.customerName, o.customerContact, p.productName, p.id"
                + " FROM orders o JOIN products p ON(o.productId = p.id) WHERE o.id = " + id;

        var con = dbConn.getConnection();

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {

                ordersModifyIdInput.setText(String.valueOf(rs.getInt("id")));
                ordersModifyIdInput.setEditable(false);
                productComboInOrderModify.removeAllItems();
                String pid = String.valueOf(rs.getInt("id"));
                String pname = rs.getString("productName");
                String pidandname = pid+" "+pname;
                productComboInOrderModify.addItem(pidandname);
                orderQtyModifyInput.setText(String.valueOf(rs.getInt("qty")));
                ordersLeadTimeModifyInput.setText(rs.getString("leadTime"));
                cNameInput.setText(rs.getString("customerName"));
                cContactInput.setText(rs.getString("customerContact"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton16ActionPerformed

    private void productComboInOrderModifyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productComboInOrderModifyMouseClicked
        try {
            // TODO add your handling code here:
            setProductsCombo();
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_productComboInOrderModifyMouseClicked

    private void btnOrdersModifyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOrdersModifyActionPerformed
        // TODO add your handling code here:
        int id = parseInt(ordersModifyIdInput.getText());
        String product = String.valueOf(productComboInOrderModify.getSelectedItem());
        String productId = product.substring(0, 2);
        String trimmed = productId.trim();
        int pid = parseInt(trimmed);
        
        String customer = cNameInput.getText();
        String contact = cContactInput.getText();
        int qty = parseInt(orderQtyModifyInput.getText());
        String leadTime = ordersLeadTimeModifyInput.getText();

        String query = "UPDATE orders SET productId = ?, "
                + "qty = ?, customerName = ?, customerContact = ?, leadTime = ? "
                + "WHERE id = " + id;

        var con = dbConn.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, pid);
            stmt.setInt(2, qty);
            stmt.setString(3, customer);
            stmt.setString(4, contact);
            stmt.setString(5, leadTime);
            stmt.executeUpdate();
            con.close();

            JOptionPane.showMessageDialog(null, "successfully updated!");
            ordersModifyIdInput.setText("");
            cNameInput.setText("");
            cContactInput.setText("");
            orderQtyModifyInput.setText("");
            ordersLeadTimeModifyInput.setText("");
            orderSearchIdInput.setText("");

            DefaultTableModel tbl = (DefaultTableModel) orderTable.getModel();
            tbl.setRowCount(0);
            loadOrderTable();

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_btnOrdersModifyActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int id = parseInt(ordersModifyIdInput.getText());

        String query = "DELETE FROM orders WHERE id = ?";

        var con = dbConn.getConnection();

        try {
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            con.close();

            JOptionPane.showMessageDialog(null, "successfully deleted!");
            
            setOrdersLabel();
            ordersModifyIdInput.setText("");
            cNameInput.setText("");
            cContactInput.setText("");
            orderQtyModifyInput.setText("");
            ordersLeadTimeModifyInput.setText("");
            orderSearchIdInput.setText("");

            DefaultTableModel tbl = (DefaultTableModel) orderTable.getModel();
            tbl.setRowCount(0);
            loadOrderTable();

        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
        //resetting bo table
        DefaultTableModel bomtbl = (DefaultTableModel)bomTable.getModel();
        bomtbl.setRowCount(0);
        
        //getting the product ID from combo
        String product = String.valueOf(ordersCombo.getSelectedItem());
        String sliced = product.substring(4,7);
        String trimmed = sliced.trim();
        int productId = parseInt(trimmed);
        
        //getting the order ID
        String order = String.valueOf(ordersCombo.getSelectedItem());
        String slicedOid = order.substring(0,2);
        String trimmedOid = slicedOid.trim();
        int Oid = parseInt(trimmedOid);
        
        try {
            //function for getting product name
            String productname = getProductName(productId);
            orderProduct.setText(productname);
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //setting variables for storing resultsets
        String partName ;
        int unitsPerProduct;
        int unitPrice ;
        int totalRequiredUnits ;
        int qty;
        int totalCost ; 
        int orderTotal = 0;
        
        String query = "SELECT p.partName, p.unitsPerProduct, p.unitPrice, o.qty "+
                "FROM parts p JOIN orders o ON(o.productId = p.belongingProductId) WHERE o.id = "+Oid;
         
        var con = dbConn.getConnection();
        
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()){
            partName = rs.getString("partName");
            unitsPerProduct = rs.getInt("unitsPerProduct");
            unitPrice = parseInt(rs.getString("unitPrice"));
            qty = rs.getInt("qty");
            totalRequiredUnits = (qty * unitsPerProduct);
            totalCost = (qty * unitsPerProduct * unitPrice);
            
            orderQty.setText(String.valueOf(qty));
            String tbldata[] = {partName, String.valueOf(unitsPerProduct), String.valueOf(unitPrice), String.valueOf(totalRequiredUnits), String.valueOf(totalCost)};
            orderTotal = orderTotal+totalCost;
            bomtbl.addRow(tbldata);
            }
            bomOrderTotal.setText(String.valueOf(orderTotal));
            
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
        String order = String.valueOf(ordersCombo.getSelectedItem());
        String slicedOid = order.substring(0,2);
        String trimmedOid = slicedOid.trim();
        int Oid = parseInt(trimmedOid);
        
        String query = "UPDATE orders SET status = (?) WHERE id ="+Oid;
        
        var con = dbConn.getConnection();
        try {
            PreparedStatement stmt = con.prepareStatement(query);
            String stts="completed";
            stmt.setString(1, stts);
            stmt.executeUpdate();
            DefaultTableModel tbl = (DefaultTableModel) orderTable.getModel();
            tbl.setRowCount(0);
            loadOrderTable();
            addSales();
            loadSalesTable();
            setEarningsLabel();
            setOrdersLabel();
            loadOrdersCombo();
            JOptionPane.showMessageDialog(null, "Order completed! new sales record added!");
        } catch (SQLException ex) {
            Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        // TODO add your handling code here:
        float prd1= Float.parseFloat(p1.getText());
        float prd2= Float.parseFloat(p2.getText());
        float prd3= Float.parseFloat(p3.getText());
        float m_avrg = (prd1+prd2+prd3)/3;
        ma.setText(String.valueOf(m_avrg));
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        float prd1= Float.parseFloat(pr1.getText());
        float prd2= Float.parseFloat(pr2.getText());
        float prd3= Float.parseFloat(pr3.getText());
        
        float wmavg = ((prd1*3)+(prd2*2)+(prd3*1))/6;
        wma.setText(String.valueOf(wmavg));
    }//GEN-LAST:event_jButton27ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new dashboard().setVisible(true);
            } catch (SQLException ex) {
                Logger.getLogger(dashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bomOrderTotal;
    private javax.swing.JTable bomTable;
    private javax.swing.JButton btnAddProduct;
    private javax.swing.JButton btnOrdersModify;
    private javax.swing.JButton btnPartAdd;
    private javax.swing.JButton btnPartDelete;
    private javax.swing.JButton btnPartIdSearch;
    private javax.swing.JButton btnPartNameSearch;
    private javax.swing.JButton btnPartUpdate;
    private javax.swing.JButton btnPlaceOrder;
    private javax.swing.JButton btnProductDelete;
    private javax.swing.JButton btnProductUpdate;
    private javax.swing.JTextField cContactInput;
    private javax.swing.JTextField cNameInput;
    private javax.swing.JTextField customerContactInput;
    private javax.swing.JTextField customerNameInput;
    private javax.swing.JTabbedPane dashboardTab;
    public javax.swing.JLabel date;
    private javax.swing.JLabel earningsTotal;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator16;
    private javax.swing.JSeparator jSeparator18;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTextField ma;
    private javax.swing.JTextField orderLeadTimeInput;
    private javax.swing.JLabel orderProduct;
    private javax.swing.JLabel orderQty;
    private javax.swing.JTextField orderQtyModifyInput;
    private javax.swing.JTextField orderSearchIdInput;
    private javax.swing.JTable orderTable;
    private javax.swing.JComboBox<String> ordersCombo;
    private javax.swing.JTextField ordersLeadTimeModifyInput;
    private javax.swing.JTextField ordersModifyIdInput;
    private javax.swing.JLabel ordersNew;
    private javax.swing.JTextField p1;
    private javax.swing.JTextField p2;
    private javax.swing.JTextField p3;
    private javax.swing.JTextField pId;
    private javax.swing.JButton pIdSearch;
    private javax.swing.JTextField pIdSearchInput;
    private javax.swing.JTextField pName;
    private javax.swing.JButton pNameSearch;
    private javax.swing.JTextField pNameSearchInput;
    private javax.swing.JTextField partIdModifyInput;
    private javax.swing.JTextField partIdSearchInput;
    private javax.swing.JTextField partNameInput;
    private javax.swing.JTextField partNameModifyInput;
    private javax.swing.JTextField partNameSearchInput;
    private javax.swing.JTextField partPriceInput;
    private javax.swing.JTextField partPriceModifyInput;
    private javax.swing.JTextField partStockInput;
    private javax.swing.JTextField partStockModifyInput;
    private javax.swing.JTextField partUnitsInput;
    private javax.swing.JTable partsTable;
    private javax.swing.JTextField pr1;
    private javax.swing.JTextField pr2;
    private javax.swing.JTextField pr3;
    private javax.swing.JComboBox<String> productCombo;
    private javax.swing.JComboBox<String> productComboInInventory;
    private javax.swing.JComboBox<String> productComboInOrderModify;
    private javax.swing.JComboBox<String> productComboInOrders;
    private javax.swing.JTextField productName;
    private javax.swing.JTextField productQtyInput;
    private javax.swing.JTable productTable;
    private javax.swing.JTable salesTable;
    public javax.swing.JLabel time;
    private javax.swing.JTextField unitsPerProModify;
    public javax.swing.JLabel usernameLable;
    private javax.swing.JTextField wma;
    // End of variables declaration//GEN-END:variables
}
