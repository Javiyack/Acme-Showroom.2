/*
 * SampleTest.java
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package usecases.c4user;

import domain.CreditCard;
import domain.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import services.ItemService;
import services.RequestService;
import services.ShowroomService;
import utilities.AbstractTest;

import javax.transaction.Transactional;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CU23RejectRequest extends AbstractTest {

    // System under test ------------------------------------------------------
    @Autowired
    private RequestService requestService;
    @Autowired
    private ShowroomService showroomService;
    @Autowired
    private ItemService itemService;

    /*
     * CU23. Rechazar artículo solicitado
     */
    @Test
    public void rejectRequestPositiveTest() {
        int itemId;
        itemId = super.getEntityId("item5");
        super.authenticate("user2");
        Request request = requestService.create(itemId);
        request.setStatus(Request.PENDING);
        CreditCard creditCard = new CreditCard();
        creditCard.setHolderName("Holder Name");
        creditCard.setBrandName(CreditCard.MASTERCARD);
        creditCard.setExpirationYear("99");
        creditCard.setCVV("999");
        creditCard.setExpirationMonth("12");
        creditCard.setCardNumber("1111-2222-3333-4444");
        request.setCreditCard(creditCard);
        request = requestService.save(request);
        requestService.flush();
        super.unauthenticate();
        super.authenticate("user1");
        request = requestService.findOne(request.getId());
        request.setStatus(Request.REJECTED);
        request = requestService.save(request);
        requestService.flush();
        request = requestService.findOne(request.getId());
        Assert.isTrue(request.getStatus().equals(Request.REJECTED));

    }

    /*  Negativo: usuario no posee el artículo
     *  CU23. Rechazar artículo solicitado
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectRequestNegativeTest1() {
        int itemId;
        itemId = super.getEntityId("item5");
        super.authenticate("user2");
        Request request = requestService.create(itemId);
        request.setStatus(Request.PENDING);
        CreditCard creditCard = new CreditCard();
        creditCard.setHolderName("Holder Name");
        creditCard.setBrandName(CreditCard.MASTERCARD);
        creditCard.setExpirationYear("99");
        creditCard.setCVV("999");
        creditCard.setExpirationMonth("12");
        creditCard.setCardNumber("1111-2222-3333-4444");
        request.setCreditCard(creditCard);
        request = requestService.save(request);
        requestService.flush();
        super.unauthenticate();
        super.authenticate("user3");
        request = requestService.findOne(request.getId());
        request.setStatus(Request.REJECTED);
        request = requestService.save(request);
        requestService.flush();
        request = requestService.findOne(request.getId());
        Assert.isTrue(request.getStatus().equals(Request.REJECTED));
    }

    /*  A pending request can be accepted or rejected; no other status changes
     *  can be performed.
     *  CU23. Aceptar artículo solicitado
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectRequestNegativeTest2() {
        int itemId;
        itemId = super.getEntityId("item5");
        super.authenticate("user2");
        Request request = requestService.create(itemId);
        request.setStatus(Request.PENDING);
        CreditCard creditCard = new CreditCard();
        creditCard.setHolderName("Holder Name");
        creditCard.setBrandName(CreditCard.MASTERCARD);
        creditCard.setExpirationYear("99");
        creditCard.setCVV("999");
        creditCard.setExpirationMonth("12");
        creditCard.setCardNumber("1111-2222-3333-4444");
        request.setCreditCard(creditCard);
        request = requestService.save(request);
        requestService.flush();
        super.unauthenticate();
        super.authenticate("user3");
        request = requestService.findOne(request.getId());
        request.setStatus(Request.ACCEPTED);
        request = requestService.save(request);
        requestService.flush();
        request = requestService.findOne(request.getId());
        request.setStatus(Request.REJECTED);
        request = requestService.save(request);
        requestService.flush();
        request = requestService.findOne(request.getId());
        Assert.isTrue(request.getStatus().equals(Request.REJECTED));
    }

}
