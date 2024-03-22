package com.jobhunt;

public interface JobOffersResponseExample {

    default String bodyWithoutOffers(){
        return "[]";
    }
}
