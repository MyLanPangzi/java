package com.hiscat.springrest.repository;

import com.hiscat.springrest.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Administrator
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
