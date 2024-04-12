package roomescape.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import roomescape.exception.NoArgsException;
import roomescape.exception.NotFoundReservationException;
import roomescape.repository.ReservationRepo;

import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


// 현재는 데이터베이스가 없으니, 임의의 DTO 를 컨트롤러에 생성!

@Controller
public class ReservationController {

    @Autowired
    private ReservationRepo reservationRepo;





    public static class Reservations {

        public int id;

        @NotEmpty
        public String name;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @NotNull
        public LocalDate date;
        @DateTimeFormat(pattern = "HH:mm")
        @NotNull
        public LocalTime time;

        public Reservations(int id, String name,  LocalDate date,  LocalTime time)  {
            this.id = id;
            this.name = name;
            this.date = date;
            this.time = time;
        }
    }



    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";
    }

    @GetMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservations>> reservations() {
        List<Reservations> reservations = reservationRepo.findAll();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }



    @PostMapping("/reservations")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Reservations> reservations(@RequestBody @Valid Reservations request) throws Exception {

        int id = reservationRepo.insert(request);

        System.out.println("id - - -- - - " + id);


        return ResponseEntity.created(URI.create("/reservations/" + id)).body(request);
    }
    @GetMapping("/reservations/{id}")
    public ResponseEntity<Reservations> reservations(@PathVariable int id) throws Exception {
        Reservations response;
        try {
            response = reservationRepo.findById(id);

        } catch (Exception err) {
          throw new NotFoundReservationException();

        }
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Object> reservationDelete(@PathVariable int id) throws Exception {
        reservationRepo.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }


    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<Object> handleException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoArgsException.class)
    public ResponseEntity<Object> handleException(NoArgsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleException(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body("값이 올바르지 않습니다.");
    }
}
