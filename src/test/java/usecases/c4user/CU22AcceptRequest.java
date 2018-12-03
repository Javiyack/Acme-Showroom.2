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
import services.RequestService;
import utilities.AbstractTest;

import javax.transaction.Transactional;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CU22AcceptRequest extends AbstractTest {

    // System under test ------------------------------------------------------
    @Autowired
    private RequestService requestService;

    /*
     * CU22. Aceptar artículo solicitado
     */
    @Test
    public void acceptRequestPositiveTest() {
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
        request.setStatus(Request.ACCEPTED);
        request = requestService.save(request);
        requestService.flush();
        request = requestService.findOne(request.getId());
        Assert.isTrue(request.getStatus().equals(Request.ACCEPTED));

    }

    /*  Negativo: usuario no posee el artículo
     *  CU22. Aceptar artículo solicitado
     */
    @Test(expected = IllegalArgumentException.class)
    public void acceptRequestNegativeTest1() {
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
        requestService.save(request);
        requestService.flush();
        super.unauthenticate();
    }

    /*  A pending request can be accepted or rejected; no other status changes
     *  can be performed.
     *  CU22. Aceptar artículo solicitado
     */
    @Test(expected = IllegalArgumentException.class)
    public void acceptRequestNegativeTest2() {
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
        request.setStatus(Request.ACCEPTED);
        request = requestService.save(request);
        requestService.flush();
        request = requestService.findOne(request.getId());
        Assert.isTrue(request.getStatus().equals(Request.ACCEPTED));
    }

}
