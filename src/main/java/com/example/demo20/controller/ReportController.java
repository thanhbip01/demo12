package com.example.demo20.controller;

import com.example.demo20.dto.ReportResponse;
import com.example.demo20.model.Order;
import com.example.demo20.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/report")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ReportController {
    private final OrderService orderService;

    public ReportController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/daily")
    public ResponseEntity<ReportResponse> dailyReport() {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        var orders = orderService.getOrdersBetween(start, end);
        long customers = orders.size();
        double revenue = orders.stream().mapToDouble(Order::getTotalPrice).sum();

        return ResponseEntity.ok(new ReportResponse(customers, revenue));
    }
}
