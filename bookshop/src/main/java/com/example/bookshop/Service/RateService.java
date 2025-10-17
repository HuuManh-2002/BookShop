package com.example.bookshop.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookshop.Entity.Book;
import com.example.bookshop.Entity.BookOrder;
import com.example.bookshop.Entity.Rate;
import com.example.bookshop.Entity.User;
import com.example.bookshop.EntityDto.Reponse.RateReponse;
import com.example.bookshop.EntityDto.Request.RateRequest;
import com.example.bookshop.Exception.AppException;
import com.example.bookshop.Exception.ErrorCode;
import com.example.bookshop.Mapper.RateMapper;
import com.example.bookshop.Repository.BookOrderRepository;
import com.example.bookshop.Repository.BookRepository;
import com.example.bookshop.Repository.RateRepository;

@Service
public class RateService {

    @Autowired
    RateMapper rateMapper;

    @Autowired
    RateRepository rateRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookOrderRepository bookOrderRepository;

    @Autowired
    UserService userService;

    public List<RateReponse> getByBook(Long book_id) {
        Book book = bookRepository.findById(book_id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        List<Rate> rates = rateRepository.findByBook(book);
        return rateMapper.toListRateReponse(rates);
    }

    @Transactional
    public RateReponse create(RateRequest rateRequest, Long bookOrder_id) {
        BookOrder bookOrder = bookOrderRepository.findById(bookOrder_id)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_ORDER_NOT_FOUND));
        if(bookOrder.getOrder().getCurrentStatus() != 6){
            throw new AppException(ErrorCode.ORDER_NOT_COMPLETED);
        }        
        Book book = bookOrder.getBook();
        User user = userService.getUserformToKen();
        if (user.getId() != bookOrder.getOrder().getUser().getId()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        Rate rate = rateMapper.toRate(rateRequest);
        rate.setBook_order(bookOrder);
        rate.setBook(book);
        rate.setUser(user);
        rateRepository.save(rate);
        int voteNumberOld = book.getVoteNumber();
        int voteNumberNew = voteNumberOld + 1;
        float startOld = book.getStart();
        float startNew = ((voteNumberOld * startOld) + rate.getVote()) / (float) voteNumberNew;
        startNew = Math.round(startNew * 10.0f) / 10.0f;
        book.setVoteNumber(voteNumberNew);
        book.setStart(startNew);
        bookRepository.save(book);
        return rateMapper.toRateReponse(rate);
    }
}
