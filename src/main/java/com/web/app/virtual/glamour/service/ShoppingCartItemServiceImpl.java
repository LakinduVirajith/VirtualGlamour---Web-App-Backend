package com.web.app.virtual.glamour.service;

import com.web.app.virtual.glamour.common.CommonFunctions;
import com.web.app.virtual.glamour.common.ResponseMessage;
import com.web.app.virtual.glamour.dto.ShoppingCartItemDTO;
import com.web.app.virtual.glamour.entity.ProductStock;
import com.web.app.virtual.glamour.entity.ShoppingCartItem;
import com.web.app.virtual.glamour.entity.User;
import com.web.app.virtual.glamour.exception.BadRequestException;
import com.web.app.virtual.glamour.exception.NotFoundException;
import com.web.app.virtual.glamour.repository.ProductStockRepository;
import com.web.app.virtual.glamour.repository.ShoppingCartItemRepository;
import com.web.app.virtual.glamour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartItemServiceImpl implements ShoppingCartItemService {

    private final ShoppingCartItemRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductStockRepository stockRepository;
    private final CommonFunctions commonFunctions;

    @Override
    public ResponseEntity<ResponseMessage> addToCart(Long optionalId, Integer quantity) throws NotFoundException, BadRequestException {
        ProductStock productStock = stockRepository.findById(optionalId)
                .orElseThrow(() -> new NotFoundException("Optional product not found with the provided ID"));

        // EXCEPTION HANDLING
        if (!productStock.getProduct().getIsListed()) {
            throw new BadRequestException("This product is not available for purchase. Please choose a different product.");
        }
        if (productStock.getStockQuantity() < quantity) {
            throw new BadRequestException("Insufficient stock. Please adjust quantity or choose another product.");
        }

        Optional<ShoppingCartItem> cartItem = cartRepository.findByProductStockProductStockId(productStock.getProductStockId());
        User user = commonFunctions.getUser();
        ResponseMessage successResponse = new ResponseMessage();

        // STORE OR UPDATE CART DATA
        if(cartItem.isEmpty()){
            ShoppingCartItem shoppingCart = ShoppingCartItem.builder()
                    .quantity(quantity)
                    .productStock(productStock)
                    .user(user).build();
            cartRepository.save(shoppingCart);

            successResponse.setMessage("Product added to the shopping cart successfully");
        }else{
            ShoppingCartItem shoppingCartItem = cartItem.get();
            shoppingCartItem.setQuantity(quantity);
            cartRepository.save(shoppingCartItem);

            successResponse.setMessage("Product item updated in the cart successfully");
        }

        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public ResponseEntity<ResponseMessage> updateCart(Long optionalId, Integer quantity) throws NotFoundException, BadRequestException {
        User user = commonFunctions.getUser();
        List<ShoppingCartItem> shoppingCarts = user.getShoppingCart();

        boolean found = false;
        // UPDATE PRODUCT OPTION
        for (ShoppingCartItem item : shoppingCarts) {
            if (Objects.equals(item.getProductStock().getProductStockId(), optionalId)) {

                if (item.getProductStock().getStockQuantity() < quantity) {
                    throw new BadRequestException("Insufficient stock. Please adjust quantity or choose another product.");
                }

                item.setQuantity(quantity);
                cartRepository.save(item);
                found = true;
                break;
            }
        }

        // NO CART OPTIONAL PRODUCT EXIST EXCEPTION
        if(!found){
            throw new NotFoundException("No cart item found with the provided ID");
        }

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("Product item updated in the cart successfully");
        return ResponseEntity.ok().body(successResponse);
    }

    @Override
    public List<ShoppingCartItemDTO> getAllCartData() throws NotFoundException {
        User user = commonFunctions.getUser();
        List<ShoppingCartItem> shoppingCarts = user.getShoppingCart();

        // CART EMPTY EXCEPTION
        if(shoppingCarts.isEmpty()){
            throw new NotFoundException("Your shopping carts is Empty.");
        }

        List<ShoppingCartItemDTO> shoppingCartItemDTOS = new ArrayList<>();
        for (ShoppingCartItem item : shoppingCarts) {
            ProductStock productStock = item.getProductStock();

            ShoppingCartItemDTO itemDTO = ShoppingCartItemDTO.builder()
                    .cartId(item.getCartId())
                    .productName(productStock.getProduct().getProductName())
                    .sizeName(productStock.getSizeName())
                    .color(productStock.getColor())
                    .price(productStock.getPrice())
                    .quantity(item.getQuantity())
                    .stockQuantity(productStock.getStockQuantity())
                    .build();

            if(productStock.getSizeImage() != null){
                itemDTO.setSizeImage(productStock.getSizeImage());
            }
            if(productStock.getDiscount() != null){
                itemDTO.setDiscount(productStock.getDiscount());
            }
            if(productStock.getDiscountStartDate() != null){
                itemDTO.setDiscountStartDate(productStock.getDiscountStartDate());
            }
            if(productStock.getDiscountEndDate() != null){
                itemDTO.setDiscountEndDate(productStock.getDiscountEndDate());
            }

            shoppingCartItemDTOS.add(itemDTO);
        }

        return shoppingCartItemDTOS;
    }

    @Override
    public ResponseEntity<ResponseMessage> deleteData(Long cartItemId) throws NotFoundException {
        ShoppingCartItem cartItem = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new NotFoundException("No cart item found with the provided ID"));

        cartItem.setUser(null);
        cartItem.setProductStock(null);
        cartRepository.save(cartItem);
        cartRepository.deleteById(cartItemId);

        ResponseMessage successResponse = new ResponseMessage();
        successResponse.setStatusCode(200);
        successResponse.setStatus(HttpStatus.OK);
        successResponse.setMessage("Product removed from the shopping cart successfully");
        return ResponseEntity.ok().body(successResponse);
    }
}
