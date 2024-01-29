package roomescape.controller.dto;

import roomescape.domain.Reservation;

import java.util.Collections;
import java.util.List;

public record ReservationDto(long id, String name, String date, String time) {

    public static List<ReservationDto> from(List<Reservation> reservations) {
        if (reservations == null || reservations.isEmpty()) {
            return Collections.emptyList();
        }

        return reservations.stream()
                .map(res -> new ReservationDto(
                        res.getId(),
                        res.getName(),
                        res.getDate().toString(),
                        res.getTime().toString())
                )
                .toList();
    }

    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate().toString(),
                reservation.getTime().toString());
    }
}