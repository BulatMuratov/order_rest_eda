package com.bulka.infrastructure.repository;

import com.bulka.domain.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Modifying
    @Query("""
        UPDATE Product p
        SET p.stock = p.stock - :quantity
        WHERE p.id = :productId
        AND p.stock >= :quantity
        """)
    int reserve(@Param("productId") Long productId,
                @Param("quantity") Integer quantity);

    @Modifying
    @Query("""
        UPDATE Product p
        SET p.stock = p.stock + :quantity
        WHERE p.id = :productId
        """)
    int increaseStock(@Param("productId") Long productId,
                      @Param("quantity") Integer quantity);
//    @Modifying
//    @Query("""
//    UPDATE Product p
//    SET p.stock = p.stock - :quantity
//    WHERE p.id = :productId AND p.stock >= :quantity
//""")
//    int deductStock(Long productId, Integer quantity);
}
