import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Manager {
    // public static String dbAddress = "jdbc:mysql://projgw.cse.cuhk.edu.hk:2633/db9?autoReconnect=true&useSSL=false";
    // public static String dbUsername = "Group9";
    // public static String dbPassword = "CSCI3170";

    // public static Connection connectToMySQL(){
    //     Connection con = null;
    //     try{
    //         Class.forName("com.mysql.cj.jdbc.Driver");
    //         con = DriverManager.getConnection(dbAddress, dbUsername, dbPassword);
    //     } catch (ClassNotFoundException e){
    //         System.out.println("[Error]: Java MySQL DB Driver not found!!");
    //         System.exit(0);
    //     } catch (SQLException e){
    //         System.out.println(e);
    //     }
    //     return con;
    // }
    
    public static void listSalesperson(Connection con, Statement selectStmt, ResultSet rs) {
        try {
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

    public static void numTransactions(Connection con, int lower, int upper) {
        try {
            String l=String.valueOf(lower);
            String u=String.valueOf(upper);
            Statement selectStmt = con.createStatement();
            ResultSet rs = selectStmt.executeQuery("SELECT s.sID, s.sName, s.sExperience, COUNT(t.tID) as numT FROM salesperson s JOIN transaction t ON s.sID = t.sID WHERE sExperience >= " + l + " AND sExperience <= " + u + " GROUP BY s.sID");
            System.out.println("| ID | sName | Years of Experience | Number of Transactions |");
            while (rs.next()) {
                int sID = rs.getInt("sID");
                String sName = rs.getString("sName");
                int sExperience = rs.getInt("sExperience");
                int tID = rs.getInt("numT");
                System.out.println("| " + sID + " | " + sName + " | " + sExperience + " | " + tID + " |");
            }
            rs.close();
            selectStmt.close();
        } catch (SQLException e) {
            System.err.println("Error deleting tables: " + e.getMessage());
        }
    }

    public static void manufValue(Connection con) {
        try {
            Statement selectStmt = con.createStatement();
            ResultSet rs = selectStmt.executeQuery("SELECT m.mID, m.mName, SUM(p.pPrice) as totalSales " +
                                                   "FROM manufacturer m "+
                                                   " JOIN part p ON m.mID = p.mID "+
                                                   "JOIN transaction t ON t.pID = p.pID " +
                                                   "GROUP BY m.mID" +
                                                   " ORDER BY totalSales DESC");
            System.out.println("| Manufacturer ID | Manufacturer Name | Total Sales Value |");
            while (rs.next()) {
                int mID = rs.getInt("mID");
                String mName = rs.getString("mName");
                int totalSales = rs.getInt("totalSales");
                System.out.println("| " + mID + " | " + mName + " | " + totalSales + " |");
            }
            System.out.println("End of Query");
            rs.close();
            selectStmt.close();
        } catch (SQLException e) {
            System.err.println("Error deleting tables: " + e.getMessage());
        }
    }
    public static void popN(Connection con, int N) {
        try {
            Statement selectStmt = con.createStatement();
            ResultSet rs = selectStmt.executeQuery("SELECT p.pID, p.pName, COUNT(t.tID) as numT " +
                                                   "FROM part p "+
                                                   " JOIN transaction t ON p.pID = t.pID "+
                                                   "GROUP BY p.pID" +
                                                   " ORDER BY numT DESC");
            System.out.println("| Part ID | Part Name | No. of Transaction |");
            for (int i=0; i<N; i++) {
                rs.next();
                int pID = rs.getInt("pID");
                String pName = rs.getString("pName");
                int numT = rs.getInt("numT");
                System.out.println("| " + pID + " | " + pName + " | " + numT + " |");
            }
            System.out.println("End of Query");
            rs.close();
            selectStmt.close();
        } catch (SQLException e) {
            System.err.println("Error deleting tables: " + e.getMessage());
        }
    }

    
}
