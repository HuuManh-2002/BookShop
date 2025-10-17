package com.example.bookshop.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.Rate;
import com.example.bookshop.EntityDto.Reponse.RateReponse;
import com.example.bookshop.EntityDto.Request.RateRequest;

@Service
public class RateMapper {

    public Rate toRate(RateRequest rateRequest){
        return Rate.builder()
            .feeback(rateRequest.getFeeback())
            .vote(rateRequest.getVote())
            .createdTime(LocalDateTime.now())
            .build();
    }

    public RateReponse toRateReponse(Rate rate){
        return RateReponse.builder()
            .id(rate.getId())
            .feeback(rate.getFeeback())
            .vote(rate.getVote())
            .createdTime(rate.getCreatedTime())
            .build();
    }
    public List<RateReponse> toListRateReponse(List<Rate> rates){
        List<RateReponse> rateReponses = new ArrayList<>();
        for (Rate rate : rates) {
            rateReponses.add(toRateReponse(rate));
        }
        return rateReponses;
    }
}
