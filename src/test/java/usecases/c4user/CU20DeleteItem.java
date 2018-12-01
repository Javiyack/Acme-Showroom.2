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
import domain.Item;
import domain.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import services.ItemService;
import services.RequestService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CU20DeleteItem extends AbstractTest {

    // System under test ------------------------------------------------------
    @Autowired
    private ItemService itemService;
    @Autowired
    private RequestService requestService;


    /*
     * CU20. Delete item
     *
     * */
    @Test
    public void deleteItemTest() {

        Collection <Item> items = itemService.findAll();
        if (!items.isEmpty()) {
            Item item = items.iterator().next();
            super.authenticate(item.getShowroom().getUser().getUserAccount().getUsername());
            Item itemToDelete = itemService.findOne(item.getId());
            itemService.delete(itemToDelete);
            items = itemService.findAll();
            Assert.isTrue(!items.contains(item.getId()));
            super.unauthenticate();
        }
    }  /*
     * CU20. Delete Item
     *
     * */

    @Test
    public void deleteItemTest1() {

        Collection <Item> items = itemService.findAll();
        if (!items.isEmpty()) {
            Item item = items.iterator().next();
            super.authenticate(item.getShowroom().getUser().getUserAccount().getUsername());
            Item itemToDelete = itemService.findOne(item.getId());
            itemService.delete(itemToDelete);
            items = itemService.findAll();
            Assert.isTrue(!items.contains(item));
            super.unauthenticate();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteItemNegativeTest1() {
        // Negative: Not owner, not user.
        Collection <Item> items = itemService.findAll();
        if (!items.isEmpty()) {
            Item item = items.iterator().next();
            super.authenticate("admin");
            Item itemToDelete = itemService.findOne(item.getId());
            itemService.delete(itemToDelete);
            items = itemService.findAll();
            Assert.isTrue(!items.contains(item));
            super.unauthenticate();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteItemNegativeTest2() {
        // Negative: Not Logged
        Collection <Item> items = itemService.findAll();
        if (!items.isEmpty()) {
            Item item = items.iterator().next();
            Item itemToDelete = itemService.findOne(item.getId());
            itemService.delete(itemToDelete.getId());
            items = itemService.findAll();
            Assert.isTrue(!items.contains(item));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteItemNegativeTest3() {
        // Negative: Item Has requests
        super.authenticate("user3");
        Collection <Item> items = itemService.findAll();
        if (!items.isEmpty()) {
            Item item = items.iterator().next();
            Request request = requestService.create(item.getId());
            CreditCard creditCard = new CreditCard();
            creditCard.setHolderName("Holder Name");
            creditCard.setBrandName(CreditCard.MASTERCARD);
            creditCard.setExpirationYear("99");
            creditCard.setCVV("999");
            creditCard.setExpirationMonth("12");
            creditCard.setCardNumber("1111-2222-3333-4444");
            request.setCreditCard(creditCard);
            requestService.save(request);
            itemService.delete(item.getId());
            items = itemService.findAll();
            Assert.isTrue(!items.contains(item));
        }
    }


}
