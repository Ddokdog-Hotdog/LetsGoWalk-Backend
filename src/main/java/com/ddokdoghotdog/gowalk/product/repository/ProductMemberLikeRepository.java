package com.ddokdoghotdog.gowalk.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ddokdoghotdog.gowalk.entity.ProductMemberLike;
import com.ddokdoghotdog.gowalk.entity.ProductMemberLike.ProductMemberLikeId;

@Repository
public interface ProductMemberLikeRepository extends JpaRepository<ProductMemberLike, ProductMemberLikeId>{

	@Modifying
	@Query(value = "INSERT INTO product_member_likes (productid, memberid)\r\n"
			+ "VALUES (:productId, :memberId)", nativeQuery = true)
	void insertLike(@Param("productId") Long productId, @Param("memberId") Long memberId);
	
	@Modifying
	@Query(value = "DELETE FROM product_member_likes\r\n"
			+ "WHERE productid = :productId\r\n"
			+ "  AND memberid = :memberId" , nativeQuery = true)
	void deleteLike(@Param("productId") Long productId, @Param("memberId") Long memberId);
	
}
