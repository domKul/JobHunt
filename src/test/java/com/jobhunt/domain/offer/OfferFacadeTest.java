package com.jobhunt.domain.offer;

import com.jobhunt.domain.offer.dto.JobOfferResponse;
import com.jobhunt.domain.offer.dto.OfferRequestDto;
import com.jobhunt.domain.offer.dto.OfferResponseDto;
import com.jobhunt.domain.offer.exception.OfferNotFoundException;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;


class OfferFacadeTest {

    @Test
    public void shouldFetchFromRemoteAndSaveAllOffers() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration().offerFacadeTests();
        assertThat(offerFacade.findAllOffersFromDb()).isEmpty();

        // when
        List<OfferResponseDto> result = offerFacade.fetchOffersAndSaveAllIfNotExists();

        // then
        assertThat(result).hasSize(5);
    }

//    @Test
//    public void shouldSavOnly2OffersWhenRepositoryHad_4_addedWithOfferUrls() {
//        // given
//        OfferFacade offerFacade = new OfferFacadeTestConfiguration(
//                List.of(
//                        new JobOfferResponse("title", "company", "salary", "1"),
//                        new JobOfferResponse("title", "company", "salary", "2"),
//                        new JobOfferResponse("title", "company", "salary", "3"),
//                        new JobOfferResponse("title", "company", "salary", "4"),
//                        new JobOfferResponse("Junior", "Comarch", "1000", "someurl.pl/5"),
//                        new JobOfferResponse("Mid", "Finanteq", "2000", "someother.pl/6")
//                )
//        ).offerFacadeTests();
//        offerFacade.saveOffer(new OfferRequestDto("name", "pos", "sal", "1"));
//        offerFacade.saveOffer(new OfferRequestDto("name", "pos", "sal", "2"));
//        offerFacade.saveOffer(new OfferRequestDto("name", "pos", "sal", "3"));
//        offerFacade.saveOffer(new OfferRequestDto("name", "pos", "sal", "4"));
//        assertThat(offerFacade.findAllOffersFromDb()).hasSize(4);
//
//        // when
//        List<OfferResponseDto> response = offerFacade.fetchOffersAndSaveAllIfNotExists();
//
//        // then
//        assertThat(List.of(
//                        response.get(0).offerUrl(),
//                        response.get(1).offerUrl()
//                )
//        ).containsExactlyInAnyOrder("someurl.pl/5", "someother.pl/6");
//    }

    @Test
    public void shouldSave_4_OffersWhenThereAreNoOffersInDatabase() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeTests();

        // when
        offerFacade.saveOffer(new OfferRequestDto("asd", "asa", "dasa", "1"));
        offerFacade.saveOffer(new OfferRequestDto("dsd", "asds", "asda", "2"));
        offerFacade.saveOffer(new OfferRequestDto("fdf", "asdasdadaq", "asda", "3"));
        offerFacade.saveOffer(new OfferRequestDto("asdq", "asds", "aaa", "4"));

        // then
        assertThat(offerFacade.findAllOffersFromDb()).hasSize(4);
    }

    @Test
    public void shouldFindOfferById() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeTests();
        OfferResponseDto offerResponseDto = offerFacade.saveOffer(new OfferRequestDto("company", "position", "salary", "url.example.com"));
        // when
        OfferResponseDto offerById = offerFacade.findOfferByGivenId(offerResponseDto.id());

        // then
        assertThat(offerById).isEqualTo(OfferResponseDto.builder()
                .id(offerResponseDto.id())
                .company("company")
                .position("position")
                .salary("salary")
                .offerUrl("url.example.com")
                .build()
        );
    }

    @Test
    public void shouldThrowNotFoundExceptionWhenOfferNotFoundWithGivenId() {
        // given
        OfferFacade offerFacade = new OfferFacadeTestConfiguration(List.of()).offerFacadeTests();
        assertThat(offerFacade.findAllOffersFromDb()).isEmpty();

        // when
        Throwable thrown = catchThrowable(() -> offerFacade.findOfferByGivenId("33"));

        // then
        AssertionsForClassTypes.assertThat(thrown)
                .isInstanceOf(OfferNotFoundException.class)
                .hasMessage("Offer not found");
    }
}