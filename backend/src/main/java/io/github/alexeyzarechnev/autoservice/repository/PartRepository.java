package io.github.alexeyzarechnev.autoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.alexeyzarechnev.autoservice.model.Part;

@Repository
public interface PartRepository extends JpaRepository<Part, Long> {
    boolean existsByName(String name);
}
