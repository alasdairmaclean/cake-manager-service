package com.waracle.cakemanager.db;

import com.waracle.cakemanager.model.CakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CakeRepository extends JpaRepository<CakeEntity, Integer> {
}
