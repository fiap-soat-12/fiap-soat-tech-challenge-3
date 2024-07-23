package br.com.fiap.tech_challenge.adapters.driven.infra.repository;

import br.com.fiap.tech_challenge.adapters.driven.infra.entities.ProductEntity;
import br.com.fiap.tech_challenge.core.domain.models.Product;
import br.com.fiap.tech_challenge.core.domain.ports.ProductPersistence;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class ProductPersistenceImpl implements ProductPersistence {

    public static final String INACTIVE = "INACTIVE";
    private final ProductRepository repository;

    public ProductPersistenceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<Product> findByCategory(String category, Pageable pageable) {
        return repository.findByCategoryAndStatusNot(category, INACTIVE, pageable).map(ProductEntity::toProduct);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findByIdAndStatusNot(id, INACTIVE).map(ProductEntity::toProduct);
    }

    @Override
    public void delete(UUID id) {
        repository.findById(id).ifPresent(product -> {
            product.setStatus(INACTIVE);
            repository.save(product);
        });
    }
}
