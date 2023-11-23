import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Start {

    public static void main (String args[]) {
        boolean programRunning = true;
        Scanner scanner = new Scanner(System.in);

        try {
            Connection con = Admin.connectToMySQL();
            while(programRunning){
                System.out.println("Welcome to sales system!");
                System.out.println("-----Main menu-----");
                System.out.println("What kinds of operation would you like to perform?");
                System.out.println("1. Operations for administrator");
                System.out.println("2. Operations for salesperson");
                System.out.println("3. Operations for manager");
                System.out.println("4. Exit this program");
                System.out.print("Enter Your Choice: ");
                int operation = scanner.nextInt();

                //Administrator operations
                if(operation == 1) {
                    System.out.println("\n-----Operations for administrator menu-----");
                    System.out.println("What kinds of operation would you like to perform?");
                    System.out.println("1. Create all tables");
                    System.out.println("2. Delete all tables");
                    System.out.println("3. Load from datafile");
                    System.out.println("4. Show content of a table");
                    System.out.println("5. Return to the main menu");
                    System.out.print("Enter Your Choice: ");
                    int adminOp = scanner.nextInt();
                    
                    if(adminOp == 1) {
                        Admin.deleteTables(con);
                        Admin.createTables(con);
                        System.out.print("Processing...");
                        System.out.println("Done! Database is initialized!\n");
                    } else if(adminOp == 2) {
                        Admin.deleteTables(con);
                        System.out.print("Processing...");
                        System.out.println("Done! Database is removed!\n");
                    } else if(adminOp == 3) {
                        System.out.print("Type in the Source Data Folder Path: ");
                        scanner.nextLine();
                        String path = scanner.nextLine().trim();
                        Admin.loadCategory(con, path);
                        Admin.loadManufacturer(con, path);
                        Admin.loadPart(con, path);
                        Admin.loadSalesperson(con, path);
                        Admin.loadTransaction(con, path);
                        System.out.print("Processing...Data is inputted to the database!");
                    } else if(adminOp == 4) {
                        System.out.print("Which file would you like to show: ");
                        scanner.nextLine();
                        String file = scanner.nextLine().trim();
                        if (file.equals("category")){
                            Admin.showTableCategory(con);
                        } else if(file.equals("manufacturer")) {
                            Admin.showTableManufacturer(con);
                        } else if(file.equals("part")) {
                            Admin.showTablePart(con);
                        } else if(file.equals("salesperson")) {
                            Admin.showTableSalesperson(con);
                        } else if(file.equals("transaction")) {
                            Admin.showTableTransaction(con);
                        } else {
                            System.out.println("Wrong file");
                        }
                    }

                } 
                //Salesperson operations
                else if (operation == 2) {

                } 
                //Manager operations
                else if (operation == 3) {
                    
                } 
                //Exit the program
                else if (operation == 4) {
                    programRunning = false;
                    break;
                } else {
                    System.out.println("Wrong operation input");
                }
                System.out.println("\n");
            }
            
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        
    }
    
}
