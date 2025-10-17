package com.example.bookshop.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.CartItem;
import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.CartItemReponse;
import com.example.bookshop.EntityDto.Request.CartItemRequest;
import com.example.bookshop.EntityDto.Update.CartItemUpdate;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.CartItemMapper;
import com.example.bookshop.Repository.BookRepository;
import com.example.bookshop.Repository.CartItemRepository;

@Service
public class CartItemService {

    @Autowired
    CartItemMapper cartItemMapper;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserService userService;

    public List<CartItemReponse> getAll() {
        User user = userService.getUserformToKen();
        List<CartItemReponse> cartItemReponses = new ArrayList<>();
        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        for (CartItem cartItem : cartItems) {
            cartItemReponses.add(cartItemMapper.toCartItemReponse(cartItem));
        }
        return cartItemReponses;
    }

    public CartItemReponse create(CartItemRequest cartItemRequest) {
        User user = userService.getUserformToKen();
        Book book = bookRepository.findById(cartItemRequest.getBook_id())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        if (book.getQuantity() < cartItemRequest.getQuantity()) {
            throw new AppException(ErrorCode.BOOK_ENOUGH_QUANTITY);
        }

        Optional<CartItem> cartItemFind = cartItemRepository.findByUserAndBook(user, book);

        if (cartItemFind.isPresent()) {
            CartItem cartItem = cartItemFind.get();
            int quantity = cartItem.getQuantity() + cartItemRequest.getQuantity();

            if (book.getQuantity() < quantity) {
                throw new AppException(ErrorCode.BOOK_ENOUGH_QUANTITY);
            }

            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return cartItemMapper.toCartItemReponse(cartItem);
        }

        CartItem cartItemNew = cartItemMapper.toCartItem(cartItemRequest);
        cartItemNew.setBook(book);
        cartItemNew.setUser(user);
        cartItemRepository.save(cartItemNew);
        return cartItemMapper.toCartItemReponse(cartItemNew);
    }

    public CartItemReponse update(CartItemUpdate cartItemUpdate, Long id){
        User user = userService.getUserformToKen();
        CartItem cartItem = cartItemRepository.findById(id)
                 .orElseThrow(() -> new AppException(ErrorCode.CARTITEM_NOT_FOUND));
        if (!user.getId().equals(cartItem.getUser().getId())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
        }         
        Book book = bookRepository.findById(cartItemUpdate.getBook_id())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        if(book.getId() != cartItem.getBook().getId()){
                throw new AppException(ErrorCode.UNCATEGORIRED_EXCEPTION);
        }
        if (book.getQuantity() < cartItemUpdate.getQuantity()) {
            throw new AppException(ErrorCode.BOOK_ENOUGH_QUANTITY);
        }
        cartItem.setQuantity(cartItemUpdate.getQuantity());
        cartItemRepository.save(cartItem);
        return cartItemMapper.toCartItemReponse(cartItem);
    }

    public void delete(Long id){
        User user = userService.getUserformToKen();
        CartItem cartItem = cartItemRepository.findById(id)
                 .orElseThrow(() -> new AppException(ErrorCode.CARTITEM_NOT_FOUND));
        if (!user.getId().equals(cartItem.getUser().getId())) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        cartItemRepository.delete(cartItem);
    }
}
