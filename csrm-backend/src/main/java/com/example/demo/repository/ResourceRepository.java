package com.example.demo.repository;

import com.example.demo.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByType(Resource.ResourceType type);
}
