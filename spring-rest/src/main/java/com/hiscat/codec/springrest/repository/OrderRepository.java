package com.hiscat.codec.springrest.repository;

import com.hiscat.codec.springrest.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Administrator
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}
