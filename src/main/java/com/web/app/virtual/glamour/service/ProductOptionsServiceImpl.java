package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ProductStockDTO;
import com.web.app.virtual.glamour.entity.Product;
import com.web.app.virtual.glamour.entity.ProductStock;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.repository.ProductOptionsRepository;
import com.web.app.virtual.glamour.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductOptionsServiceImpl implements ProductOptionsService{

    private final ProductOptionsRepository optionsRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProductStockDTO addProductOption(Long productId, ProductStockDTO productStockDTO) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(optionalProduct.isEmpty()){
            throw new NotFoundException("Sorry, no product was found with the provided ID");
        }
        Product product = optionalProduct.get();

        //SET PRODUCT STOCK DATA
        ProductStock productStock = new ProductStock();
        modelMapper.map(productStockDTO, productStock);
        productStock.setProduct(product);

        productStock = optionsRepository.save(productStock);
        modelMapper.map(productStock, productStockDTO);
        return productStockDTO;
    }

    @Override
    public ProductStockDTO updateProductOption(Long optionId, ProductStockDTO productStockDTO) throws NotFoundException {
        Optional<ProductStock> stockOptional = optionsRepository.findById(optionId);

        if(stockOptional.isEmpty()){
            throw new NotFoundException("Sorry, the product option you are looking for could not be found");
        }
        ProductStock productStock = stockOptional.get();
        productStock.setSizeName(productStockDTO.getSizeName());
        productStock.setColor(productStockDTO.getColor());
        productStock.setStockQuantity(productStockDTO.getStockQuantity());

        productStock = optionsRepository.save(productStock);
        modelMapper.map(productStock, productStockDTO);
        return productStockDTO;
    }

    @Override
    public List<ProductStockDTO> getAllProductOption(Long productId) throws NotFoundException {
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if(optionalProduct.isEmpty()){
            throw new NotFoundException("Sorry, no product was found with the provided ID");
        }
        Product product = optionalProduct.get();

        //CONVERT AND RETURN DTO LIST
        return product.getProductStocks()
                .stream()
                .map(productStock -> {
                    ProductStockDTO stockDTO = new ProductStockDTO();
                    modelMapper.map(productStock, stockDTO);
                    return stockDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ProductStockDTO getProductOption(Long optionId) throws NotFoundException {
        Optional<ProductStock> stockOptional = optionsRepository.findById(optionId);

        if(stockOptional.isEmpty()){
            throw new NotFoundException("Sorry, the product option you are looking for could not be found");
        }

        ProductStockDTO stockDTO = new ProductStockDTO();
        modelMapper.map(stockOptional.get(), stockDTO);
        return stockDTO;
    }

    @Override
    public ResponseEntity<ResponseMessage> removeProductOption(Long optionId) throws NotFoundException, BadRequestException {
        Optional<ProductStock> stockOptional = optionsRepository.findById(optionId);

        if(stockOptional.isEmpty()){
            throw new NotFoundException("Sorry, the product option you are looking for could not be found");
        }

        Optional<Product> optionalProduct = productRepository.findById(stockOptional.get().getProduct().getProductId());
        if(optionalProduct.isPresent()){
            if(optionalProduct.get().getProductStocks().size() == 1){
                throw new BadRequestException("Product needs at least one product option.");
            }
        }

        ProductStock productStock = stockOptional.get();
        productStock.setProduct(null);
        optionsRepository.save(productStock);
        optionsRepository.deleteById(optionId);

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("Product option deleted successfully");
        return ResponseEntity.ok().body(successResponse);
    }
}
