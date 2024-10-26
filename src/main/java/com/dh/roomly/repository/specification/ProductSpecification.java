package com.dh.roomly.repository.specification;

import com.dh.roomly.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductSpecification {

    public static Specification<ProductEntity> hasName(String name) {
        return (root, query, builder) ->
                Objects.isNull(name) ? null : builder.like(builder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> hasDescription(String description) {
        return (root, query, builder) ->
                Objects.isNull(description) ? null : builder.like(builder.lower(root.get("description")),
                        "%" + description.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> hasExactAddress(String address) {
        return (root, query, builder) ->
                Objects.isNull(address) ? null : builder.like(builder.lower(root.get("exactAddress")),
                        "%" + address.toLowerCase() + "%");
    }

    public static Specification<ProductEntity> shortGreaterThanOrEqualTo(Short shortValue, String fieldName) {
        return (root, query, builder) ->
                Objects.isNull(shortValue) ? null : builder.greaterThanOrEqualTo(root.get(fieldName), shortValue);
    }

    public static Specification<ProductEntity> shortLessThanOrEqualTo(Short shortValue, String fieldName) {
        return (root, query, builder) ->
                Objects.isNull(shortValue) ? null : builder.lessThanOrEqualTo(root.get(fieldName), shortValue);
    }

    public static Specification<ProductEntity> shortEqualTo(Short shortValue, String fieldName) {
        return (root, query, builder) ->
                Objects.isNull(shortValue) ? null : builder.equal(root.get(fieldName), shortValue);
    }

    public static Specification<ProductEntity> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
        return (root, query, builder) ->
                Objects.isNull(minPrice) ? null : builder.greaterThanOrEqualTo(root.get("pricePerNight"), minPrice);
    }

    public static Specification<ProductEntity> priceLessThanOrEqualTo(BigDecimal maxPrice) {
        return (root, query, builder) ->
                Objects.isNull(maxPrice) ? null : builder.lessThanOrEqualTo(root.get("pricePerNight"), maxPrice);
    }

    public static Specification<ProductEntity> priceEqualTo(BigDecimal price) {
        return (root, query, builder) ->
                Objects.isNull(price) ? null : builder.equal(root.get("pricePerNight"), price);
    }
}
