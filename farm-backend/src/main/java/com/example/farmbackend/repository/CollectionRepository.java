package com.example.farmbackend.repository;

import com.example.farmbackend.models.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection, Long> {

    List<Collection> findAllByEmployeeIdAndProductId(Long employeeId, Long productId);
    List<Collection> findAllByProductId( Long productId);
    List<Collection> findAllByEmployeeId(Long employeeId);
}
