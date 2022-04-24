package com.sellics.casestudy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sellics.casestudy.entity.ProductData;

@Repository
public interface ProductDataRepository extends JpaRepository<ProductData, Integer> {

	Optional<List<ProductData>> findByAsinAndKeyword(String asin, String keyword);

	Optional<List<ProductData>> findByKeyword(String keyword);

	Optional<List<ProductData>> findByAsin(String asin);

}
