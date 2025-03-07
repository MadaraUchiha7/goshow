package service;

import config.DatabaseConfig;

import java.sql.*;
import java.util.List;
import java.util.Scanner;

public class GoShowSys {

    Scanner sc  = new Scanner(System.in);


    public void displayMovies(){
        try{
            Connection connect = DatabaseConfig.getConnection();
            Statement statement = connect.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from movies");
            System.out.println("------------------------Available Movies-------------------------");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("movie_id") + ". " + resultSet.getString("title") + " (" + resultSet.getString("genre") + ")" + ". " + resultSet.getString("lang"));

            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void  displayTheaters(String city){
        try{
            Connection connect = DatabaseConfig.getConnection();
            PreparedStatement  preparedStatement = connect.prepareStatement("select * from theaters where city = ?");
            preparedStatement.setString(1, city);
            ResultSet resultSet =  preparedStatement.executeQuery();
            System.out.println("Theaters in " + city + ":");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("theater_id") + ". " + resultSet.getString("name"));

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void displayShows(int movieId, int theaterId){

        try {
            Connection connect  = DatabaseConfig.getConnection();
            PreparedStatement preparedStatement = connect.prepareStatement("select * from shows where movie_id = ? and theater_id = ?");
            preparedStatement.setInt(1, movieId);
            preparedStatement.setInt(2, theaterId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("----------------------------Available Shows------------------------------");
            while (resultSet.next()){
                System.out.println(resultSet.getInt("show_id") + ". " + resultSet.getString("timing") + " - Seats Available:" + resultSet.getString("available_seats"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    public void bookTickets(int userId, int showId, List<String> selectedSeats){
        try{
            Connection connect = DatabaseConfig.getConnection();
            connect.setAutoCommit(false);
            //check if seats are available
            boolean alreadyBookedSeats = false;
            for(String seat: selectedSeats){
                PreparedStatement preparedStatement = connect.prepareStatement("select * from seats where seat_number = ? and show_id = ?");
                preparedStatement.setString(1, seat);
                preparedStatement.setInt(2, showId);
                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next() && resultSet.getBoolean("is_booked")){
                    alreadyBookedSeats = true;
                    System.out.println("seat" + seat + "is already booked! Choose another seat.");

                }
            }
            if (alreadyBookedSeats){
                System.out.println("Booking failed! Some seats are already booked");
                connect.rollback();
                return;
            }
            for (String seat: selectedSeats){
                PreparedStatement preparedStatement = connect.prepareStatement("update seats set is_booked = true where seat_number = ? and show_id = ?");
                preparedStatement.setString(1, seat);
                preparedStatement.setInt(2, showId);
                preparedStatement.executeUpdate();

            }
            double seatPrice = 400.0;
            double totalPrice = selectedSeats.size() * seatPrice;
            PreparedStatement preparedStatement = connect.prepareStatement("insert into bookings(user_id, show_id, seat_booked, total_price) values (?, ?, ?, ?)");
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, showId);
            preparedStatement.setString(3, String.join(",", selectedSeats));
            preparedStatement.setDouble(4, totalPrice);
            PreparedStatement preparedStatement1 = connect.prepareStatement("update shows set available_seats = available_seats - ? where show_id = ?");
            preparedStatement1.setInt(1, selectedSeats.size());
            preparedStatement1.setInt(2, showId);
            preparedStatement.executeUpdate();
            preparedStatement1.executeUpdate();
            connect.commit();
            System.out.println("Booking Successful! Seats: " + selectedSeats + " | Total Price "  + totalPrice);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}