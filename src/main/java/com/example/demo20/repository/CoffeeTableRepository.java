package com.example.demo20.repository;

import com.example.demo20.model.CoffeeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CoffeeTableRepository extends JpaRepository<CoffeeTable, Long> {
    Optional<CoffeeTable> findByTableNumber(int tableNumber);
    List<CoffeeTable> findByStatus(String status); // 👈 thêm dòng này để lọc bàn trống
}
