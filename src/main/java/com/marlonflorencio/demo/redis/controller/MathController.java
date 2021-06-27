package com.marlonflorencio.demo.redis.controller;

import com.marlonflorencio.demo.redis.service.SquareService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("square")
@AllArgsConstructor
public class MathController {

    private final SquareService squareService;

    @PostMapping("{value}")
    public ResponseEntity<Double> create(@PathVariable("value") Double value)  {
        Double result = squareService.create(value);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("{value}")
    public ResponseEntity<Double> put(@PathVariable("value") Double value)  {
        Double result = squareService.update(value);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("{value}")
    public ResponseEntity<?> delete(@PathVariable("value") Double value)  {
        squareService.delete(value);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

