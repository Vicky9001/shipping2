package com.example.shipping.repository;

import com.example.shipping.models.Order;
import com.example.shipping.models.User;
import com.example.shipping.models.Van;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCarriersContaining(User carrier);

    List<Order> findAll();

    List<Order> findByShipper(User shipper);
//    @Query("SELECT * FROM orders")
//    List<Order> findByCarriers
    List<Order> findByVansContaining(Van van);
//    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.carriers c WHERE (:carrierName I S NULL OR c.username LIKE %:carrierName%) AND (:shipperName IS NULL OR o.shipper.username LIKE %:shipperName%)")
//    List<Order> findByCarriersContainingAndShipperContaining(@Param("carrierName") String carrierName, @Param("shipperName") String shipperName);
}
