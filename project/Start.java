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
                    System.out.println("\n-----Operations for salesperson menu-----");
                    System.out.println("What kinds of operation would you like to perform?");
                    System.out.println("1. Search for parts");
                    System.out.println("2. Sell a part");
                    System.out.println("3. Return to the main menu");
                    System.out.print("Enter Your Choice: ");
                    int salesOp = scanner.nextInt();

                    if (salesOp == 1) {
                        System.out.println("Choose the Search criterion:");
                        System.out.println("1. Part Name");
                        System.out.println("2. Manufacturer Name");
                        System.out.print("Choose the search criterion: ");
                        
                        scanner.nextLine();
                        int searchCritOp = scanner.nextInt();
                        if (searchCritOp == 1 || searchCritOp == 2) {
                            System.out.print("Type in the Search Keyword: ");
                            scanner.nextLine();
                            String searchKeyword = scanner.nextLine().trim();
                            System.out.println("Choose ordering:");
                            System.out.println("1. By price, ascending order");
                            System.out.println("2. By price, descending order");
                            System.out.print("Choose the search criterion: ");
                            int priceOrderOp = scanner.nextInt();
                            String priceOrder = "";
                            if (priceOrderOp == 1) {
                                priceOrder = "ASC";
                            } else if (priceOrderOp == 2) {
                                priceOrder = "DESC";
                            }
                            if (searchCritOp == 1) {
                                Sales.searchByPartName(con, searchKeyword, priceOrder);
                            } else if (searchCritOp == 2) {
                                Sales.searchByManuName(con, searchKeyword, priceOrder);
                            }
                        }
                    } else if (salesOp == 2) {
                        System.out.print("Enter The Part ID: ");
                        scanner.nextLine();
                        int partIdOp = scanner.nextInt();
                        if (Sales.checkPartIdExists(con, partIdOp)) {
                            System.out.print("Enter The Salesperson ID: ");
                            scanner.nextLine();
                            int salesIdOp = scanner.nextInt();
                            if (Sales.checkSalesIdExists(con, salesIdOp)) {
                                int tID = Sales.getTransactionNextId(con);
                                int pID = partIdOp;
                                int sID = salesIdOp;
                                Date sqlDate = Sales.getCurDateSQLServer(con);
                                Sales.sellPart(con, tID, pID, sID, sqlDate);
                                String pName = Sales.getPartName(con, pID);
                                int nowQuantity = Sales.getPartQuantity(con, pID);
                                System.out.println("Product: " + pName + "(id: " + pID + ") Remaining Quality: " + nowQuantity);
                            } else {
                                System.out.println("Salespersion ID " + salesIdOp + " not exists");
                            }
                        } else {
                            System.out.println("Part ID " + partIdOp + " not exists");
                        }
                    }
                } 
                //Manager operations
                else if (operation == 3) {
                    System.out.println("\n-----Operations for manager menu-----");
                    System.out.println("What kinds of operation would you like to perform?");
                    System.out.println("1. List all salesperson");
                    System.out.println("2. Count the number of transaction records of each salesperson within a given range on years of experience");
                    System.out.println("3. Show the total sales value of all manufacturer");
                    System.out.println("4. Show the N most popular part");
                    System.out.println("5. Return to the main menu");
                    System.out.print("Enter Your Choice: ");
                    int managerOp = scanner.nextInt();
                    
                    if(managerOp == 1) {
                        System.out.println("Choose ordering");
                        System.out.println("1. By ascending order");
                        System.out.println("2. By descending order");
                        System.out.print("Choose the list ordering: ");
                        int order = scanner.nextInt();
                        
                        if(order == 1) {
                            Statement selectStmt = con.createStatement();
                            ResultSet rs = selectStmt.executeQuery("SELECT * FROM salesperson ORDER BY sExperience ASC");
                            Manager.listSalesperson(con, selectStmt, rs);
                        } else if(order == 2) {
                            Statement selectStmt = con.createStatement();
                            ResultSet rs = selectStmt.executeQuery("SELECT * FROM salesperson ORDER BY sExperience DESC");
                            Manager.listSalesperson(con, selectStmt, rs);
                        }
                        
                        
                    } else if(managerOp == 2) {
                        System.out.print("Type in the lower bound for years of experience: ");
                        int lower = scanner.nextInt();
                        System.out.print("Type in the upper bound for years of experience: ");
                        int upper = scanner.nextInt();
                        System.out.println("Transaction Record:");
                        Manager.numTransactions(con, lower, upper);

                    } else if(managerOp == 3) {
                        Manager.manufValue(con);
                    } else if(managerOp == 4) {
                        System.out.print("Type in the number of parts: ");
                        int pop = scanner.nextInt();
                        Manager.popN(con, pop);
                    }
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
