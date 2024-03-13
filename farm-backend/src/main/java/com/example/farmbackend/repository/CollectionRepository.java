package com.example.farmbackend.repository;


import com.example.farmbackend.models.Collection;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    Optional<Collection> findAllByEmployeeIdAndProductId(Long employeeId, Long productId);
}
