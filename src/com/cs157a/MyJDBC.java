package com.cs157a;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class MyJDBC {

    public static void main(String[] args)
    {

        /*
        test any of the functions you write here.
        important note: our mysql databases will have different usernames and passwords,
        so change the code as necessary to make the connections work.
         */

        calculateTotal();

        Scanner input = new Scanner(System.in);
        System.out.println("Are you a returning user? Press 1 for yes and 2 for no.");
        int numb = input.nextInt();
        input.nextLine();
        boolean done = false;
        String name;
        String userID;
        int age;

        while(!done)
        {
            switch(numb){

                case 1:
                {
                    System.out.println("Please enter your user ID:");
                    userID = input.nextLine();
                    int uID  = Integer.parseInt(userID);

                    if (uID > 999 && uID < 8999)
                    {
                        System.out.println("Welcome back! Here are your current reservations: ");
                        login(userID);

                        input.nextLine();

                        int userFunction;

                        userFunction = input.nextInt();

                        System.out.println("What would you like to do? \n"
                        + "Press 1 to make a reservation. \n"
                        + "Press 2 to edit a current reservation. \n"
                        + "Press 3 to cancel a reservation. \n"
                        + "Press 4 to order room service. \n"
                        + "Press 5 to pay to use the gym. \n"
                        + "Press 6 to pay to use the pool. \n"
                        + "Press 7 to check into your room.\n"
                        + "Press 8 to check out of your room.\n"

                        + "Press any other key to exit.");

                        boolean done2 = false;

                        while(!done2) {

                            switch (userFunction) {
                                case 1: {
                                    makeReservation();
                                    break;
                                }

                                case 2: {
                                    editReservation();
                                    break;
                                }

                                case 3: {
                                    cancelReservation();
                                    break;
                                }

                                case 4: {
                                    orderRoomService();
                                    break;

                                }

                                case 5: {
                                    useGym();
                                    break;

                                }

                                case 6: {
                                    usePool();
                                    break;

                                }

                                case 7: {
                                    checkIn();
                                    break;

                                }

                                case 9: {
                                    getReservationsAfter();
                                    break;
                                }
                                case 10: {
                                    calculateReservationPaymentTotal();
                                    break;
                                }

                                default: {
                                    System.out.println("See you later!");
                                    done2 = true;
                                    break;

                                }


                            }
                        }

                        done = true;
                        break;
                    }

                    else if (uID < 999)
                    {
                        System.out.println("That is not a valid userID. Exiting....");
                        done = true;
                        break;
                    }

                    else
                    {
                        System.out.println("Admin user identified. What would you like to do?");
                        int adminFunction;

                    }

                }

                case 2:
                {
                    System.out.println("Please enter your name.");
                    name = input.nextLine();
                    System.out.println("Please enter your age.");
                    age = input.nextInt();
                    System.out.println("Account successfully created. Please login again.");

                    done = true;
                    break;


                }
        }
        }
    }

    public static void login(String userID){
        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root", "Pob9483wtf213!");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from reservation where uID = " + userID);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public static void makeReservation()
    {
        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root",
                    "jedors123");

            System.out.print("Your user Id: ");
            int userId = scan.nextInt();

            System.out.print("Room id: ");
            int roomID = scan.nextInt();

            System.out.println("Start date(format: yyyy-mm-dd): ");
            String startDate = scan.next();

            System.out.println("End date(format: yyyy-mm-dd): ");
            String endDate = scan.next();

            scan.close();

            PreparedStatement stmt = connection.prepareStatement(
                    "insert into `reservation` (uID, roomID, startDate, endDate, dateReserved, updatedAt)  values(?, ?, ? , ?,current_date, current_date )");

            stmt.setInt(1, userId);
            stmt.setInt(2, roomID);
            stmt.setDate(3, Date.valueOf(startDate));
            stmt.setDate(4, Date.valueOf(endDate));
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkIn()
    {

        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_reservation", "root", "Trey1998$$$");


            System.out.print("Enter room id: ");
            int roomID = scan.nextInt();

            scan.nextLine();

            System.out.println("Enter reservation date(format: yyyy-mm-dd): ");
            String resDate = scan.next();

            PreparedStatement stmt = connection.prepareStatement("update room\n" +
                    "set isOccupied = 1\n" +
                    "where roomID in (select reservation.roomID from reservation, user where user.uID = reservation.uID)\n");


            stmt.setInt(2, roomID);
            stmt.executeUpdate();

            if (resDate.equals(LocalDate.now().toString())){
                System.out.println("Thank you for checking in!");
            }
            else{
                System.out.println("Your reservation is not for today!");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void editReservation()
    {
        //jonathan
        // insert code here. use any parameters as needed.
        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root",
                    "jedors123");

            System.out.print("Enter reservation Id: ");
            int reservationID = scan.nextInt();

            System.out.print("Room id: ");
            int roomID = scan.nextInt();

            System.out.println("Start date(format: yyyy-mm-dd): ");
            String startDate = scan.next();

            System.out.println("End date(format: yyyy-mm-dd): ");
            String endDate = scan.next();

            scan.close();

            PreparedStatement stmt = connection.prepareStatement(
                    "update reservation set roomId = ?, startDate = ?, endDate = ?, " +
                            "updatedAt = current_date where rID = ? ");

            stmt.setInt(1, roomID);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));
            stmt.setInt(4, reservationID);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cancelReservation()
    {
        //jonathan
        //insert code here. use any parameters as needed
        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root",
                    "jedors123");

            System.out.print("Enter reservation Id: ");
            int reservationID = scan.nextInt();

            System.out.print("Enter user id: ");
            int userID = scan.nextInt();

            scan.close();

            PreparedStatement stmt = connection.prepareStatement(
                    "delete from reservation where rID = ? and uID = ? ");

            stmt.setInt(1, reservationID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void orderRoomService()
    {
        //jonathan
        //insert code here. use any parameters as needed
        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root",
                    "jedors123");

            System.out.print("Enter payment Id: ");
            int pID = scan.nextInt();

            System.out.print("Reservation id: ");
            int reservationID = scan.nextInt();


            System.out.print("Amount: ");
            int amount = scan.nextInt();

            scan.close();

            PreparedStatement stmt = connection.prepareStatement(
                    "insert into `payment` (pId, rId, amount, type)  values(?, ?, ? , ?)");

            stmt.setInt(1, pID);
            stmt.setInt(2, reservationID);
            stmt.setInt(3, amount);
            stmt.setString(4, "room service");
            stmt.executeUpdate();
            System.out.println("Your room service has been ordered");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void useGym()
    {
        //jonathan
        //insert code here.
        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root",
                    "jedors123");

            System.out.print("Enter payment Id: ");
            int pID = scan.nextInt();

            System.out.print("Reservation id: ");
            int reservationID = scan.nextInt();


            System.out.print("Amount: ");
            int amount = scan.nextInt();

            scan.close();

            PreparedStatement stmt = connection.prepareStatement(
                    "insert into `payment` (pId, rId, amount, type)  values(?, ?, ? , ? )");

            stmt.setInt(1, pID);
            stmt.setInt(2, reservationID);
            stmt.setInt(3, amount);
            stmt.setString(4, "gym");
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static void adminReservations(int adminID)
    {
        if (adminID > 8999)
        {
            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root", "Pob9483wtf213!");

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery("select * from reservation");
                ResultSetMetaData rsmd = resultSet.getMetaData();
                int columnsNumber = rsmd.getColumnCount();

                while (resultSet.next()) {
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = resultSet.getString(i);
                        System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    }
                    System.out.println("");
                }

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("That is not a Admin ID. Exiting...");
            return;
        }

    }

    public static void adminCheckRoom()
    {
        //paul
        //insert code here
        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root", "Pob9483wtf213!");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select roomID from reservations, room where isOccupied = false and reservations.roomID = room.roomID");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void cleanRoom()
    {
        //paul
        //insert code here
        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root", "Pob9483wtf213!");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select roomID from room where cleaned = false");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void adminCheckIn()
    {
        //paul
        //insert code here
        try {

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root", "Pob9483wtf213!");

            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("select name, reservation.uID, room.roomID, isOccupied\n" +
                    "from user natural join reservation natural join room\n" +
                    "where current_date() >= startDate or current_date() <= endDate\n");
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public static void getReservationsAfter()
    {
        //trey
        //insert code here
        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root",
                    "jedors123");

            System.out.println("Enter Start date(format: yyyy-mm-dd): ");
            String reservationDate = scan.next();
            scan.close();
            PreparedStatement stmt = connection.prepareStatement("select * from reservation where startDate > ?");

            stmt.setDate(1,  Date.valueOf(reservationDate));
            ResultSet resultSet = stmt.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            System.out.println("Here are all the reservation that start after " + reservationDate);
            while (resultSet.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void calculateReservationPaymentTotal()
    {

        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root",
                    "jedors123");

            System.out.println("Enter the reservation ID: ");
            int reservationID = scan.nextInt();
            scan.close();

            PreparedStatement stmt = connection.prepareStatement("select sum(amount) from payment " +
                    "where rID = ?");
            stmt.setInt(1,  reservationID);

            ResultSet resultSet = stmt.executeQuery();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (resultSet.next()) {
                    String columnValue = resultSet.getString(1);
                    if(columnValue == null)
                        System.out.println("No payment made by this reservation");
                    else
                        System.out.println("Total payment sum by this reservation was: $" + columnValue);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void usePool()
    {
        //trey
        //insert code here
        //jonathan
        //insert code here.
        try {

            Scanner scan = new Scanner(System.in);
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hotel_system", "root",
                    "jedors123");

            System.out.print("Enter payment Id: ");
            int pID = scan.nextInt();

            System.out.print("Reservation id: ");
            int reservationID = scan.nextInt();


            System.out.print("Amount: ");
            int amount = scan.nextInt();

            scan.close();

            PreparedStatement stmt = connection.prepareStatement(
                    "insert into `payment` (pId, rId, amount, type)  values(?, ?, ? , ? )");

            stmt.setInt(1, pID);
            stmt.setInt(2, reservationID);
            stmt.setInt(3, amount);
            stmt.setString(4, "pool");
            stmt.executeUpdate();
            System.out.println("Payment for pool have been made!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    }


