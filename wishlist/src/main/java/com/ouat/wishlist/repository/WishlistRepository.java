package com.ouat.wishlist.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ouat.wishlist.entity.Wishlist;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{

	@Transactional
	@Modifying
    @Query("delete from Wishlist u where u.productId = ?1 and u.customerId = ?2")
	int deleteByproductId(@Param("productId") Long productId, @Param("customerId") Long customerId);
	
	@Query("select count(1) from Wishlist u where u.customerId = :customerId")
	Long countWislistAddedItem(@Param("customerId") Long customerId);
	
	@Query(value = "select u.productId from  Wishlist u where u.customerId=:customerId order by u.id desc")
	public List<Long> findProductIdByCustomer(Long customerId);
	
}
