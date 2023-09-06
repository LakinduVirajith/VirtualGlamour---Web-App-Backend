package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.CommonFunctions;
import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ProductDTO;
import com.web.app.virtual.glamour.dto.ProductStockDTO;
import com.web.app.virtual.glamour.entity.Product;
import com.web.app.virtual.glamour.entity.ProductStock;
import com.web.app.virtual.glamour.entity.User;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.repository.ProductRepository;
import com.web.app.virtual.glamour.repository.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ProductStockRepository stockRepository;
    private final CommonFunctions commonFunctions;
    private final ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) throws NotFoundException {
        User user = commonFunctions.getUser();

        // PRODUCT STORING
        Product product = new Product();
        modelMapper.map(productDTO, product);
        product.setUser(user);

        product = productRepository.save(product);
        modelMapper.map(product, productDTO);

        // PRODUCT STOCK STORING
        List<ProductStockDTO> productStockDTOS = productDTO.getProductStockDTOS();

        for (ProductStockDTO productStockDTO: productStockDTOS) {
            ProductStock productStock = new ProductStock();
            modelMapper.map(productStockDTO, productStock);

            productStock.setProduct(product);
            stockRepository.save(productStock);
        }

        return productDTO;
    }

    @Override
    public List<ProductDTO> getAllProducts() throws NotFoundException {
        User user = commonFunctions.getUser();

        List<Product> products = productRepository.findAllByUserUserId(user.getUserId());
        if (products.isEmpty()) {
            throw new NotFoundException("No any products found for the vendor");
        }

        // SET PRODUCT DETAILS
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

            // SET PRODUCT STOCK DETAILS
            List<ProductStockDTO> productStockDTOS = product.getProductStocks()
                    .stream()
                    .map(data -> modelMapper.map(data, ProductStockDTO.class))
                    .collect(Collectors.toList());

            productDTO.setProductStockDTOS(productStockDTOS);
            productDTOs.add(productDTO);
        }

        return productDTOs;
    }

    @Override
    public Page<ProductDTO> getAllProductsPageable(Pageable pageable) throws NotFoundException {
        User user = commonFunctions.getUser();

        Page<Product> productsPage = productRepository.findAllByUserUserId(user.getUserId(), pageable);
        if (productsPage.isEmpty()) {
            throw new NotFoundException("No any products found for the vendor");
        }

        // SET PRODUCT DETAILS
        List<ProductDTO> productDTOs = productsPage.getContent()
                .stream()
                .map(product -> {
                    ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

                    // SET PRODUCT STOCK DETAILS
                    List<ProductStockDTO> productStockDTOS = product.getProductStocks()
                            .stream()
                            .map(data -> modelMapper.map(data, ProductStockDTO.class))
                            .collect(Collectors.toList());

                    productDTO.setProductStockDTOS(productStockDTOS);
                    return productDTO;
                })
                .collect(Collectors.toList());

        return new PageImpl<>(productDTOs, pageable, productsPage.getTotalElements());
    }

    @Override
    public ProductDTO getProductById(Long productId) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        // PRODUCT NOT FOUND EXCEPTION
        if(optionalProduct.isEmpty()){
            throw new NotFoundException("No any Product to found given Id");
        }
        Product product = optionalProduct.get();
        ProductDTO productDTO = new ProductDTO();
        modelMapper.map(product, productDTO);

        // SET PRODUCT DETAILS
        List<ProductStockDTO> productStockDTOS =product.getProductStocks()
                .stream()
                .map(data -> modelMapper.map(data, ProductStockDTO.class))
                .collect(Collectors.toList());

        productDTO.setProductStockDTOS(productStockDTOS);
        return productDTO;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        // PRODUCT NOT FOUND EXCEPTION
        if(optionalProduct.isEmpty()){
            throw new NotFoundException("No any Product to found given Id");
        }
        Product product = optionalProduct.get();

        // PRODUCT STORING
        modelMapper.map(productDTO, product);
        productDTO.setProductStockDTOS(null);
        product = productRepository.save(product);
        modelMapper.map(product, productDTO);

        return productDTO;
    }

    @Override
    public ResponseEntity<ResponseMessage> listedOption(Long productId) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        // PRODUCT NOT FOUND EXCEPTION
        if(optionalProduct.isEmpty()){
            throw new NotFoundException("No any Product to found given Id");
        }
        Product product = optionalProduct.get();

        // HANDLE LISTED OPTIONS
        ResponseMessage successResponse = new ResponseMessage();
        if(product.getIsListed()){
            product.setIsListed(false);
            successResponse.setMessage("Product unlisted successfully");
        }else{
            product.setIsListed(true);
            successResponse.setMessage("Product listed successfully");
        }

        productRepository.save(product);

        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteProduct(Long productId) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        // PRODUCT NOT FOUND EXCEPTION
        if(optionalProduct.isEmpty()){
            throw new NotFoundException("No any Product to found given Id");
        }

        productRepository.deleteById(productId);

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("Product deleted successfully");
        return ResponseEntity.ok().body(successResponse);
    }
}
