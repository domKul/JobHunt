package com.jobhunt.domain.offer;

import lombok.Builder;

@Builder
record Offer(
        String companyName,
        String salary,
        String position,
        String offerUrl
) {

}
