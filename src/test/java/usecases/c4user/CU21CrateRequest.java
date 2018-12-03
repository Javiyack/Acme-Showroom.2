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
public class CU21CrateRequest extends AbstractTest {

    // System under test ------------------------------------------------------
    @Autowired
    private RequestService requestService;

    /*
     * CU21. Solicitar artículo
     */
    @Test
    public void requestPositiveTest() {
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
        Assert.isTrue(request.getId() != 0);
    }

    /* Users may request items from other users.
     * CU21. Solicitar artículo
     */
    @Test(expected = IllegalArgumentException.class)
    public void requestNegativeTest1() {
        // Negativo. Un usuario no puede solicitar sus propios artículos
        int itemId;
        itemId = super.getEntityId("item5");
        super.authenticate("user1");
        Request request = requestService.create(itemId);
        request.setStatus(Request.PENDING);
        CreditCard creditCard = new CreditCard();
        creditCard.setHolderName("Holder Name");
        creditCard.setBrandName(CreditCard.MASTERCARD);
        creditCard.setCardNumber("1111-2222-3333-4444");
        creditCard.setExpirationMonth("12");
        creditCard.setExpirationYear("99");
        creditCard.setCVV("999");
        request.setCreditCard(creditCard);
        request = requestService.save(request);
        requestService.flush();
        Assert.isTrue(request.getId() != 0);
    }

    /* Users may request items from other users.
     * CU21. Solicitar artículo
     */
    @Test(expected = IllegalArgumentException.class)
    public void requestNegativeTest2() {
        // Negativo. Ha de ser de tipo USUARIO para realizar solicitudes
        int itemId;
        itemId = super.getEntityId("item5");
        super.authenticate("admin");
        Request request = requestService.create(itemId);
        request.setStatus(Request.PENDING);
        CreditCard creditCard = new CreditCard();
        creditCard.setHolderName("Holder Name");
        creditCard.setBrandName(CreditCard.MASTERCARD);
        creditCard.setCardNumber("1111-2222-3333-4444");
        creditCard.setExpirationMonth("12");
        creditCard.setExpirationYear("99");
        creditCard.setCVV("999");
        request.setCreditCard(creditCard);
        request = requestService.save(request);
        requestService.flush();
        Assert.isTrue(request.getId() != 0);
    }

    @Test
    public void requestDriver() {
        final Object testingData[][] = {{
                "item1", "user2", Request.PENDING, "HolderName", CreditCard.AMEX,
                "1111-2222-3333-4444", "20", "12", "609", null // Positive
        }, {
                "item2", "user1", Request.PENDING, "HolderName", CreditCard.DINERS,
                "1111-2222-3333-4444", "20", "12", "609", IllegalArgumentException.class // Negative. Not own item requests allowed
        }, {
                "item3", "user1", Request.PENDING, "HolderName", CreditCard.MASTERCARD,
                "1111-2222-3333-4444", "20", "12", "609", IllegalArgumentException.class // Negative. Not own item requests allowed
        }, {
                "item4", "user1", Request.PENDING, "HolderName", CreditCard.VISA,
                "1111-2222-3333-4444", "20", "12", "609", IllegalArgumentException.class  // Negative. Not own item requests allowed
        }, {
                "item5", "user3", Request.PENDING, "HolderName", CreditCard.VISA,
                "1111-2222-3333-4444", "20", "12", "609", null  // Positive
        }, {
                "item6", "admin", Request.PENDING, "HolderName", CreditCard.VISA,
                "1111-2222-3333-4444", "20", "12", "609", IllegalArgumentException.class // Negative. Non user item requests not allowed
        }, {
                "item7", "user3", Request.ACCEPTED, "HolderName", CreditCard.VISA,
                "1111-2222-3333-4444", "20", "12", "609", null  // Positive
        }};
        // comments here
        for (int i = 0; i < testingData.length; i++)
            this.requestTemplate(testingData[i]);
    }

    // Ancillary methods ------------------------------------------------------

    protected void requestTemplate(Object testingData[]) {
        Class <?> caught;
        int dbId;

        caught = null;
        try {
            int itemId;
            itemId = super.getEntityId((String) testingData[0]);
            super.authenticate((String) testingData[1]);
            Request request = requestService.create(itemId);
            request.setStatus((String) testingData[2]);
            CreditCard creditCard = new CreditCard();
            creditCard.setHolderName((String) testingData[3]);
            creditCard.setBrandName((String) testingData[4]);
            creditCard.setCardNumber((String) testingData[5]);
            creditCard.setExpirationYear((String) testingData[6]);
            creditCard.setExpirationMonth((String) testingData[7]);
            creditCard.setCVV((String) testingData[8]);
            request.setCreditCard(creditCard);
            request = requestService.save(request);
            requestService.flush();
            Assert.isTrue(request.getId() != 0);
        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        this.checkExceptions((Class <?>) testingData[9], caught);

    }

}
