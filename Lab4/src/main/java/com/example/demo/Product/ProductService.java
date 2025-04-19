package com.example.demo.Product;

import com.example.demo.Annotations.DistributedLock;
import com.example.demo.Product.dto.CreateProductDTO;
import com.example.demo.Product.dto.UpdateProductDTO;
import com.example.demo.Redis.RedisClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RedisClient redisClient;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String ALL_PRODUCTS_CACHE_KEY = "products:all";
    private static final Duration CACHE_TTL = Duration.ofMinutes(10);
    private static final String PRODUCT_LOCK_PREFIX = "product";

    // Get all products with caching
    public List<Product> getAllProducts() {
        try {
            String cachedProductsJson = redisClient.get(ALL_PRODUCTS_CACHE_KEY);
            if (cachedProductsJson != null) {
                log.info("Cache hit for key: {}", ALL_PRODUCTS_CACHE_KEY);
                return objectMapper.readValue(cachedProductsJson, new TypeReference<List<Product>>() {});
            }
        } catch (JsonProcessingException e) {
            log.error("Error deserializing cached products from Redis for key {}: {}", ALL_PRODUCTS_CACHE_KEY, e.getMessage());
        } catch (Exception e) {
            log.error("Error accessing Redis for key {}: {}", ALL_PRODUCTS_CACHE_KEY, e.getMessage());
        }

        log.info("Cache miss for key: {}. Fetching from database.", ALL_PRODUCTS_CACHE_KEY);
        List<Product> products = productRepository.findAll();
        if (products != null && !products.isEmpty()) {
            try {
                String productsJson = objectMapper.writeValueAsString(products);
                redisClient.set(ALL_PRODUCTS_CACHE_KEY, productsJson, CACHE_TTL);
                log.info("Stored {} products in cache for key: {}", products.size(), ALL_PRODUCTS_CACHE_KEY);
            } catch (JsonProcessingException e) {
                log.error("Error serializing products for caching for key {}: {}", ALL_PRODUCTS_CACHE_KEY, e.getMessage());
            } catch (Exception e) {
                log.error("Error storing data in Redis for key {}: {}", ALL_PRODUCTS_CACHE_KEY, e.getMessage());
            }
        }
        return products;
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getBySku(String sku) {
        return productRepository.findBySku(sku);
    }

    @Transactional
    public Product createProduct(CreateProductDTO productDTO) {
        if (productRepository.findBySku(productDTO.getSku()).isPresent()) {
            throw new IllegalArgumentException("SKU " + productDTO.getSku() + " already exists.");
        }
        Product product = new Product(
                productDTO.getSku(),
                productDTO.getName(),
                productDTO.getPrice(),
                productDTO.getStockQuantity()
        );
        return productRepository.save(product);
    }

    @Transactional
    @DistributedLock(keyPrefix = PRODUCT_LOCK_PREFIX, keyIdentifierExpression = "#id", leaseTime = 120, timeUnit = TimeUnit.SECONDS)
    public Product updateProduct(Long id, UpdateProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (productDTO.getSku() != null && !productDTO.getSku().equals(existingProduct.getSku())) {
            if (productRepository.findBySku(productDTO.getSku()).isPresent()) {
                throw new IllegalArgumentException("SKU " + productDTO.getSku() + " already exists.");
            }
        }

        BeanUtils.copyProperties(productDTO, existingProduct, getNullPropertyNames(productDTO));

        log.info("Product update for id {}: Starting 10-second delay...", id);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            log.warn("Product update for id {}: Sleep interrupted!", id);
            Thread.currentThread().interrupt();
        }
        log.info("Product update for id {}: Finished delay. Saving...", id);

        return productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
