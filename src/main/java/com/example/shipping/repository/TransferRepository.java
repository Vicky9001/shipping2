package com.example.shipping.repository;

import com.example.shipping.models.Order;
import com.example.shipping.models.Transfer;
import com.example.shipping.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findByOrder(Order order);
}
