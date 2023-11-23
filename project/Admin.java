import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



class Admin {

    public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db9?autoReconnect=true&useSSL=false";
    public static String dbUsername = "Group9";
    public static String dbPassword = "CSCI3170";
    // public static void main (String args[]) {
    //     Connection con = connectToMySQL();
    //     deleteTables(con);
    //     createTables(con);
    //     loadCategory(con);
    //     loadManufacturer(con);
    //     loadPart(con);
    //     loadSalesperson(con);
    //     loadTransaction(con);
    //     showTableCategory(con);
    //     showTableManufacturer(con);
    //     showTablePart(con);
    //     showTableSalesperson(con);
    //     showTableTransaction(con);
    //     try {
    //         con.close();
    //     } catch (SQLException e) {
    //         System.out.println(e);
    //     }
    // }

    public static Connection connectToMySQL(){
        Connection con = null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
        } catch (ClassNotFoundException e){
            System.out.println("[Error]: Java MySQL DB Driver not found!!");
            System.exit(0);
        } catch (SQLException e){
            System.out.println(e);
        }
        return con;
    }
    
    public static void createTables(Connection con) {
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE TABLE category " + "(cID INT, " + "cName VARCHAR(50))");
            
            stmt.executeUpdate("CREATE TABLE manufacturer (mID INT, mName VARCHAR(50), mAddress VARCHAR(100), mPhoneNumber INT)");
            
            stmt.executeUpdate("CREATE TABLE part (pID INT, pName VARCHAR(50), pPrice INT, mID INT, cID INT, pWarrantyPeriod INT, pAvailableQuantity INT)");
            
            stmt.executeUpdate("CREATE TABLE salesperson (sID INT, sName VARCHAR(50), sAddress VARCHAR(100), sPhoneNumber INT, sExperience INT)"); 
            
            stmt.executeUpdate("CREATE TABLE transaction (tID INT, pID INT, sID INT, tDate DATE)");
            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    public static void deleteTables(Connection con) {
        try {
            Statement stmt = con.createStatement();

            stmt.executeUpdate("DROP TABLE IF EXISTS salesperson");
            
            stmt.executeUpdate("DROP TABLE IF EXISTS transaction");

            stmt.executeUpdate("DROP TABLE IF EXISTS part");

            stmt.executeUpdate("DROP TABLE IF EXISTS manufacturer");

            stmt.executeUpdate("DROP TABLE IF EXISTS category");

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Error deleting tables: " + e.getMessage());
        }
    }

    public static void loadCategory(Connection con, String path) {
        String thisLine;
        try {
            BufferedReader br = null;

            br = new BufferedReader(new FileReader("./" + path + "/category.txt"));
            while ((thisLine = br.readLine()) != null) {
                String[] parts = thisLine.split("\t");
                int cID = Integer.parseInt(parts[0]);

                PreparedStatement pstmt = con.prepareStatement("INSERT INTO category (cID, cName) VALUES (?, ?)");

                pstmt.setInt(1, cID);
                pstmt.setString(2, parts[1].trim());

                pstmt.executeUpdate();
            }
            
            br.close();
        }catch (IOException e){
            System.err.println("Error: " + e);
        }catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }    
    }

    public static void loadManufacturer(Connection con, String path) {
        String thisLine;
        try {
            BufferedReader br = null;
            
            br = new BufferedReader(new FileReader("./" + path + "/manufacturer.txt"));
            while ((thisLine = br.readLine()) != null) {
                String[] parts = thisLine.split("\t");
                int mID = Integer.parseInt(parts[0]);
                int mPhoneNumber = Integer.parseInt(parts[3]);

                PreparedStatement pstmt = con.prepareStatement("INSERT INTO manufacturer (mID, mName, mAddress, mPhoneNumber) VALUES (?, ?, ?, ?)");

                pstmt.setInt(1, mID);
                pstmt.setString(2, parts[1].trim());
                pstmt.setString(3, parts[2].trim());
                pstmt.setInt(4, mPhoneNumber);

                pstmt.executeUpdate();
            }
            
            br.close();
        }catch (IOException e){
            System.err.println("Error: " + e);
        }catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }    
    }

    public static void loadPart(Connection con, String path) {
        String thisLine;
        try {
            BufferedReader br = null;
            
            br = new BufferedReader(new FileReader("./" + path + "/part.txt"));
            while ((thisLine = br.readLine()) != null) {
                
                String[] parts = thisLine.split("\t");
                // System.out.println("Values array: ");
                // for (String value : parts) {
                //     System.out.println(value);
                // }
                int pID = Integer.parseInt(parts[0]);
                int pPrice = Integer.parseInt(parts[2]);
                int mID = Integer.parseInt(parts[3]);
                int cID = Integer.parseInt(parts[4]);
                int pWarrantyPeriod = Integer.parseInt(parts[5]);
                int pAvailableQuantity = Integer.parseInt(parts[6]);

                PreparedStatement pstmt = con.prepareStatement("INSERT INTO part (pID, pName, pPrice, mID, cID, pWarrantyPeriod, pAvailableQuantity) VALUES (?, ?, ?, ?, ?, ?, ?)");

                pstmt.setInt(1, pID);
                pstmt.setString(2, parts[1].trim());
                pstmt.setInt(3, pPrice);
                pstmt.setInt(4, mID);
                pstmt.setInt(5, cID);
                pstmt.setInt(6, pWarrantyPeriod);
                pstmt.setInt(7, pAvailableQuantity);

                pstmt.executeUpdate();
            }
            
            br.close();
        }catch (IOException e){
            System.err.println("Error: " + e); 
        }catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }    
    }

     public static void loadSalesperson(Connection con, String path) {
        String thisLine;
        try {
            BufferedReader br = null;

            br = new BufferedReader(new FileReader("./" + path + "/salesperson.txt"));
            while ((thisLine = br.readLine()) != null) {
                String[] parts = thisLine.split("\t");
                int sID = Integer.parseInt(parts[0]);
                int sPhoneNumber = Integer.parseInt(parts[3]);
                int sExperience = Integer.parseInt(parts[4]);

                PreparedStatement pstmt = con.prepareStatement("INSERT INTO salesperson (sID, sName, sAddress, sPhoneNumber, sExperience) VALUES (?, ?, ?, ?, ?)");

                pstmt.setInt(1, sID);
                pstmt.setString(2, parts[1].trim());
                pstmt.setString(3, parts[2].trim());
                pstmt.setInt(4, sPhoneNumber);
                pstmt.setInt(5, sExperience);

                pstmt.executeUpdate();
            }
            
            br.close();
        }catch (IOException e){
            System.err.println("Error: " + e);
        }catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }    
    }

    public static void loadTransaction(Connection con, String path) {
        String thisLine;
        try {
            BufferedReader br = null;

            br = new BufferedReader(new FileReader("./" + path + "/transaction.txt"));
            while ((thisLine = br.readLine()) != null) {
                String[] parts = thisLine.split("\t");
                int tID = Integer.parseInt(parts[0]);
                int pID = Integer.parseInt(parts[1]);
                int sID = Integer.parseInt(parts[2]);
                LocalDate date = parseDate(parts[3]);

                PreparedStatement pstmt = con.prepareStatement("INSERT INTO transaction (tID, pID, sID, tDate) VALUES (?, ?, ?, ?)");

                pstmt.setInt(1, tID);
                pstmt.setInt(2, pID);
                pstmt.setInt(3, sID);
                pstmt.setDate(4, Date.valueOf(date));

                pstmt.executeUpdate();
            }
            
            br.close();
        }catch (IOException e){
            System.err.println("Error: " + e);
        }catch (SQLException e) {
            System.err.println("Error inserting data: " + e.getMessage());
        }
        
    }

    private static LocalDate parseDate(String dateString) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dateString, format);
        } catch (Exception e) {
            System.err.println("Invalid date format: " + dateString);
            return null;
        }
    }

    public static void showTableCategory(Connection con) {
        try {
            PreparedStatement selectStmt = con.prepareStatement("SELECT * FROM category");
            ResultSet rs = selectStmt.executeQuery();

            System.out.println("| cID | cName |");
            while (rs.next()) {
                int cID = rs.getInt("cID");
                String cName = rs.getString("cName");
                System.out.println("| " + cID + " | " + cName + " | ");
            }

            rs.close();
            selectStmt.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving table content: " + e.getMessage());
        }
    }

    public static void showTableManufacturer(Connection con) {
        try {
            PreparedStatement selectStmt = con.prepareStatement("SELECT * FROM manufacturer");
            ResultSet rs = selectStmt.executeQuery();

            System.out.println("| mID | cmName | mAddress | mPhoneNumber |");
            while (rs.next()) {
                int mID = rs.getInt("mID");
                String mName = rs.getString("mName");
                String mAddress = rs.getString("mAddress");
                int mPhoneNumber = rs.getInt("mPhoneNumber");
                System.out.println("| " + mID + " | " + mName + " | " + mAddress + " | " + mPhoneNumber + " | ");
            }

            rs.close();
            selectStmt.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving table content: " + e.getMessage());
        }
    }

    public static void showTablePart(Connection con) {
        try {
            PreparedStatement selectStmt = con.prepareStatement("SELECT * FROM part");
            ResultSet rs = selectStmt.executeQuery();

            System.out.println("| pID | pName | pPrice | mID | cID | pWarrantyPeriod | pAvailableQuantity |");
            while (rs.next()) {
                int pID = rs.getInt("pID");
                String pName = rs.getString("pName");
                int pPrice = rs.getInt("pPrice");
                int mID = rs.getInt("mID");
                int cID = rs.getInt("cID");
                int pWarrantyPeriod = rs.getInt("pWarrantyPeriod");
                int pAvailableQuantity = rs.getInt("pAvailableQuantity");
                System.out.println("| " + pID + " | " + pName + " | " + pPrice + " | " + mID + " | " + cID + " | " + pWarrantyPeriod + " | " + pAvailableQuantity + " |");
            }

            rs.close();
            selectStmt.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving table content: " + e.getMessage());
        }
    }

    public static void showTableSalesperson(Connection con) {
        try {
            PreparedStatement selectStmt = con.prepareStatement("SELECT * FROM salesperson");
            ResultSet rs = selectStmt.executeQuery();

            System.out.println("| sID | sName | sAddress | sPhoneNumber | sExperience|");
            while (rs.next()) {
                int sID = rs.getInt("sID");
                String sName = rs.getString("sName");
                String sAddress = rs.getString("sAddress");
                int sPhoneNumber = rs.getInt("sPhoneNumber");
                int sExperience = rs.getInt("sExperience");
                System.out.println("| " + sID + " | " + sName + " | " + sAddress + " | " + sPhoneNumber + " | " + sExperience + " |");
            }

            rs.close();
            selectStmt.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving table content: " + e.getMessage());
        }
    }

    public static void showTableTransaction(Connection con) {
        try {
            PreparedStatement selectStmt = con.prepareStatement("SELECT * FROM transaction");
            ResultSet rs = selectStmt.executeQuery();

            System.out.println("| tID | pID | sID | tDate |");
            while (rs.next()) {
                int tID = rs.getInt("tID");
                int pID = rs.getInt("pID");
                int sID = rs.getInt("sID");
                Date tDate = rs.getDate("tDate");
                System.out.println("| " + tID + " | " + pID + " | " + sID + " | " + tDate + " |");
            }

            rs.close();
            selectStmt.close();
        } catch (SQLException e) {
            System.err.println("Error retrieving table content: " + e.getMessage());
        }
    }
}