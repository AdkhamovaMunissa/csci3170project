import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class Sales {
    public static void searchByPartName(Connection con, String pNameKeyword, String priceOrder) {
        // priceOrder = "ASC" or "DESC"
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT part.pID, part.pName, manufacturer.mName, category.cName, part.pAvailableQuantity, part.pWarrantyPeriod, part.pPrice"
                + " FROM part"
                + " INNER JOIN manufacturer ON part.mID = manufacturer.mID"
                + " INNER JOIN category ON part.cID = category.cID"
                + " WHERE part.pName LIKE ? ORDER BY pPrice " + priceOrder);
            pNameKeyword = pNameKeyword.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![");
            pstmt.setString(1, "%" + pNameKeyword + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.println("| ID | Name | Manufacturer | Category | Quantity | Warranty | Price |");
            while (rs.next()) {
                int pID = rs.getInt("part.pID");
                String pName = rs.getString("part.pName");
                String mName = rs.getString("manufacturer.mName");
                String cName = rs.getString("category.cName");
                int pAvailableQuantity = rs.getInt("part.pAvailableQuantity");
                int pWarrantyPeriod = rs.getInt("part.pWarrantyPeriod");
                int pPrice = rs.getInt("part.pPrice");
                System.out.println("| " + pID + " | " + pName + " | " + mName + " | " + cName + " | " 
                    + pAvailableQuantity + " | " + pWarrantyPeriod + " | " + pPrice + " |");
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error searching by Part Name: " + e.getMessage());
        }
    }

    public static void searchByManuName(Connection con, String mNameKeyword, String priceOrder) {
        // priceOrder = "ASC" or "DESC"
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT part.pID, part.pName, manufacturer.mName, category.cName, part.pAvailableQuantity, part.pWarrantyPeriod, part.pPrice"
                + " FROM part"
                + " INNER JOIN manufacturer ON part.mID = manufacturer.mID"
                + " INNER JOIN category ON part.cID = category.cID"
                + " WHERE manufacturer.mName LIKE ? ORDER BY pPrice " + priceOrder);
            mNameKeyword = mNameKeyword.replace("!", "!!").replace("%", "!%").replace("_", "!_").replace("[", "![");
            pstmt.setString(1, "%" + mNameKeyword + "%");
            ResultSet rs = pstmt.executeQuery();

            System.out.println("| ID | Name | Manufacturer | Category | Quantity | Warranty | Price |");
            while (rs.next()) {
                int pID = rs.getInt("part.pID");
                String pName = rs.getString("part.pName");
                String mName = rs.getString("manufacturer.mName");
                String cName = rs.getString("category.cName");
                int pAvailableQuantity = rs.getInt("part.pAvailableQuantity");
                int pWarrantyPeriod = rs.getInt("part.pWarrantyPeriod");
                int pPrice = rs.getInt("part.pPrice");
                System.out.println("| " + pID + " | " + pName + " | " + mName + " | " + cName + " | " 
                    + pAvailableQuantity + " | " + pWarrantyPeriod + " | " + pPrice + " |");
            }
            
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("Error searching by Manufacturer Name: " + e.getMessage());
        }
    }

    public static boolean checkPartIdExists(Connection con, int pID) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT 1 FROM part WHERE pID = ?");
            pstmt.setInt(1, pID);
            ResultSet rs = pstmt.executeQuery();

            boolean result = rs.next();
            rs.close();
            pstmt.close();

            return result;
        } catch (SQLException e) {
            System.err.println("Error checking if a pID exists: " + e.getMessage());
        }

        return false;
    }

    public static int getPartQuantity(Connection con, int pID) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT pAvailableQuantity FROM part WHERE pID = ?");
            pstmt.setInt(1, pID);
            ResultSet rs = pstmt.executeQuery();

            int result = 0;
            while (rs.next()) {
                result += rs.getInt("pAvailableQuantity");
            }
            rs.close();
            pstmt.close();

            return result;
        } catch (SQLException e) {
            System.err.println("Error checking Part Quantity: " + e.getMessage());
        }

        return 0;
    }

    public static boolean checkSalesIdExists(Connection con, int sID) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT 1 FROM salesperson WHERE sID = ?");
            pstmt.setInt(1, sID);
            ResultSet rs = pstmt.executeQuery();

            boolean result = rs.next();
            rs.close();
            pstmt.close();

            return result;
        } catch (SQLException e) {
            System.err.println("Error checking if a sID exists: " + e.getMessage());
        }

        return false;
    }

    public static int getTransactionNextId(Connection con) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT MAX(tID) FROM transaction");
            ResultSet rs = pstmt.executeQuery();
            int result = -1;
            if (rs.next()) {
                result = rs.getInt(1) + 1;
            }
            rs.close();
            pstmt.close();

            return result;
        } catch (SQLException e) {
            System.err.println("Error getting Transaction next avilable ID: " + e.getMessage());
        }

        return -1;
    }

    public static Date getCurDateSQLServer(Connection con) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT CURDATE() FROM transaction");
            ResultSet rs = pstmt.executeQuery();
            Date result = null;
            if (rs.next()) {
                result = rs.getDate(1);
            }
            rs.close();
            pstmt.close();

            return result;
        } catch (SQLException e) {
            System.err.println("Error getting MySQL CURDATE: " + e.getMessage());
        }

        return null;
    }

    public static void sellPart(Connection con, int tID, int pID, int sID, Date sqlDate) {
        try {
            PreparedStatement pstmt = con.prepareStatement("UPDATE part SET pAvailableQuantity = pAvailableQuantity - 1 WHERE pID = ?");
            pstmt.setInt(1, pID);
            pstmt.executeUpdate();
            pstmt.close();

            try {
                pstmt = con.prepareStatement("INSERT INTO transaction (tID, pID, sID, tDate) VALUES (?, ?, ?, ?)");
                pstmt.setInt(1, tID);
                pstmt.setInt(2, pID);
                pstmt.setInt(3, sID);
                pstmt.setDate(4, sqlDate);
                pstmt.executeUpdate();
                pstmt.close();
            } catch (SQLException e) {
                System.err.println("Error creating Transaction: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.err.println("Error decrementing Part Quantity: " + e.getMessage());
        }
    }

    public static String getPartName(Connection con, int pID) {
        try {
            PreparedStatement pstmt = con.prepareStatement("SELECT pName FROM part WHERE pID = ?");
            pstmt.setInt(1, pID);
            ResultSet rs = pstmt.executeQuery();

            String result = "";
            if (rs.next()) {
                result += rs.getString("pName");
            }
            rs.close();
            pstmt.close();

            return result;
        } catch (SQLException e) {
            System.err.println("Error checking Part Name: " + e.getMessage());
        }

        return "";
    }
}


