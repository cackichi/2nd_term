package com.example.hotelswebapp.runners;

import com.example.hotelswebapp.services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ReservationCleanUpRunner implements CommandLineRunner {
    private final ReservationService reservationService;

    @Override
    public void run(String... args) throws Exception {
        reservationService.cleanUpReservations();
    }
}
