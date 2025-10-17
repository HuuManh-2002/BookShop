package com.example.bookshop.Mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.bookshop.Entity.StatusOrder;
import com.example.bookshop.EntityDto.Reponse.StatusOrderReponse;

@Service
public class StatusOrderMapper {

    public StatusOrderReponse toStatusOrderReponse(StatusOrder statusOrder){
        return StatusOrderReponse.builder()
                    .id(statusOrder.getId())
                    .updateTime(statusOrder.getUpdateTime())
                    .description(statusOrder.getDescription())
                    .build();
    }
    
    public List<StatusOrderReponse> toListStatusOrderReponse(List<StatusOrder> statusOrders){
        List<StatusOrderReponse> statusOrderReponses = new ArrayList<>();
        for (StatusOrder statusOrder : statusOrders) {
            statusOrderReponses.add(toStatusOrderReponse(statusOrder));
        }
        return statusOrderReponses;
    }

}
