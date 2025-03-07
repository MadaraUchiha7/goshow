package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GoShowApp {
    public static void main(String[] args) throws IOException {
        GoShowSys sys = new GoShowSys();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter city: ");
        String city = br.readLine();
        sys.displayTheaters(city);
        System.out.println("Enter Theater Id and Movie Id: ");
        int theaterId = Integer.parseInt(br.readLine());
        int movieId = Integer.parseInt(br.readLine());
        sys.displayShows(movieId, theaterId);
        System.out.println("Enter Show Id and Number of seats: ");
        int showId = Integer.parseInt(br.readLine());
        int numberofSeats = Integer.parseInt(br.readLine());
        List<String>  seats = new ArrayList<>();
        for(int i = 1; i <= numberofSeats; i++ ){
            seats.add(sc.nextLine());
        }
        sys.bookTickets(1, showId, seats);
    }
}
