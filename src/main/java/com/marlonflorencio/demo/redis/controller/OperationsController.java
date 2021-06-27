package com.marlonflorencio.demo.redis.controller;

import com.marlonflorencio.demo.redis.service.BitmapService;
import com.marlonflorencio.demo.redis.service.GeoHashService;
import com.marlonflorencio.demo.redis.service.HashService;
import com.marlonflorencio.demo.redis.service.HyperLogLogService;
import com.marlonflorencio.demo.redis.service.ListService;
import com.marlonflorencio.demo.redis.service.PipelineService;
import com.marlonflorencio.demo.redis.service.SetService;
import com.marlonflorencio.demo.redis.service.SortedSetService;
import com.marlonflorencio.demo.redis.service.StringService;
import com.marlonflorencio.demo.redis.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@AllArgsConstructor
public class OperationsController {

    private final StringService stringService;
    private final HashService hashService;
    private final ListService listService;
    private final SetService setService;
    private final SortedSetService sortedSetService;
    private final HyperLogLogService hyperLogLogService;
    private final BitmapService bitmapService;
    private final GeoHashService geoHashService;
    private final PipelineService pipelineService;
    private final TransactionService transactionService;

    @GetMapping("string")
    public ResponseEntity<?> string() {
        stringService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("hash")
    public ResponseEntity<?> hash() {
        hashService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("list")
    public ResponseEntity<?> list() {
        listService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("set")
    public ResponseEntity<?> set() {
        setService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("sortedSet")
    public ResponseEntity<?> sortedSet() {
        sortedSetService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("hyperLogLog")
    public ResponseEntity<?> hyperLogLog() {
        hyperLogLogService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("bitmap")
    public ResponseEntity<?> bitmap() {
        bitmapService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("geoHash")
    public ResponseEntity<?> geoHash() {
        geoHashService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("pipeline")
    public ResponseEntity<?> pipeline() {
        pipelineService.execute();
        return ResponseEntity.ok().build();
    }

    @GetMapping("transaction")
    public ResponseEntity<?> transaction() {
        transactionService.execute();
        return ResponseEntity.ok().build();
    }

}

