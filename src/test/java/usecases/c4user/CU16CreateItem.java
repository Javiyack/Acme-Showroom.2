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

import domain.Item;
import domain.Showroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import services.ItemService;
import services.ShowroomService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CU16CreateItem extends AbstractTest {

    // System under test ------------------------------------------------------
    @Autowired
    private ShowroomService showroomService;
    @Autowired
    private ItemService itemService;

    /*
     * CU16. Crear artículo
     *
     * */
    @Test
    public void createItemPositiveTest() {
        super.authenticate("user1");
        Collection <Item> result = itemService.findAll();
        Collection <Showroom> showrooms = showroomService.findByLogedActor();
        Integer count = result.size();
        if (!showrooms.isEmpty()) {
            Showroom showroom = showrooms.iterator().next();
            Item item = itemService.create(showroom);
            item.setTitle("Drone UHD");
            item.setDescription("El drone mas económico del mercado");
            item.setAvailable(true);
            item.setSKU("920615-AAAA09");
            item.setAvailable(true);
            item.setPrice(12.00);
            item = itemService.save(item);
            result = itemService.findAll();
            Assert.isTrue(result.size() == count + 1);
        }
        super.unauthenticate();
    }

    /*
     * CU16. Crear artículo. Negative, Not Logged Actor
     *
     * */
    @Test(expected = IllegalArgumentException.class)
    public void createItemNegativeTest1() {
        Collection <Showroom> showrooms = showroomService.findByLogedActor();
        if (!showrooms.isEmpty()) {
            Showroom showroom = showrooms.iterator().next();
            Item item = itemService.create(showroom);
            item.setTitle("Drone UHD");
            item.setDescription("El drone mas económico del mercado");
            item.setAvailable(true);
            item.setSKU("920615-AAAA09");
            item.setAvailable(true);
            item.setPrice(12.00);
            itemService.save(item);
        }
    }

    /*
     * CU16. Crear artículo. Negative, Bad SKU
     *
     * */
    @Test(expected = ConstraintViolationException.class)
    public void createItemNegativeTest2() {
        super.startTransaction();
        super.authenticate("user1");
        Collection <Showroom> showrooms = showroomService.findByLogedActor();
        if (!showrooms.isEmpty()) {
            Showroom showroom = showrooms.iterator().next();
            Item item = itemService.create(showroom);
            item.setTitle("Drone UHD");
            item.setDescription("El drone mas económico del mercado");
            item.setAvailable(true);
            item.setSKU("9AA615-AAAA09");
            item.setAvailable(true);
            item.setPrice(12.00);
            itemService.save(item);
        }
        super.unauthenticate();
    }
    /*
     * CU16. Crear artículo. Negative, Blank title
     *
     * */
    @Test(expected = ConstraintViolationException.class)
    public void createItemNegativeTest3() {
        super.authenticate("user1");
        Collection <Item> result = itemService.findAll();
        Collection <Showroom> showrooms = showroomService.findByLogedActor();
        Integer count = result.size();
        if (!showrooms.isEmpty()) {
            Showroom showroom = showrooms.iterator().next();
            Item item = itemService.create(showroom);
            item.setTitle("");
            item.setDescription("El drone mas económico del mercado");
            item.setAvailable(true);
            item.setSKU("920615-AAAA09");
            item.setAvailable(true);
            item.setPrice(12.00);
            itemService.save(item);
        }
    }


}


