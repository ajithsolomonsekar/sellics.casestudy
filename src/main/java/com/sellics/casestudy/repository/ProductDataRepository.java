package com.sellics.casestudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sellics.casestudy.entity.ProductData;

@Repository
public interface ProductDataRepository extends JpaRepository<ProductData, String> {

}
