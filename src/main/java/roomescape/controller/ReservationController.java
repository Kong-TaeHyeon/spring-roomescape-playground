package roomescape.controller;


import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import roomescape.entity.Reservations;
import roomescape.exception.NoArgsException;
import roomescape.exception.NotFoundReservationException;
import roomescape.repository.ReservationRepo;

import java.net.URI;
import java.util.List;



@Controller
public class ReservationController {

    @Autowired
    private ReservationRepo reservationRepo;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Reservations> reservations(@RequestBody @Valid Reservations request) throws Exception {

        int id = reservationRepo.insert(request);


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
