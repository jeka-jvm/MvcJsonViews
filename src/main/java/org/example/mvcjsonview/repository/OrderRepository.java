package org.example.mvcjsonview.repository;

import org.example.mvcjsonview.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {
}
