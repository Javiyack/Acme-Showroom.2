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
import services.RequestService;
import services.ShowroomService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CU16CreateItem extends AbstractTest {

    // System under test ------------------------------------------------------
    @Autowired
    private RequestService requestService;
    @Autowired
    private ShowroomService showroomService;
    @Autowired
    private ItemService itemService;

    /*
     * CU7. Crear artículo
     *
     * */
    @Test
    public void createItemPositiveTest() {
        super.authenticate("user1");
        Collection <Item> result = itemService.findAll();
        Collection <Showroom> showrooms = showroomService.findByLogedActor();
        Integer count = result.size();
        if(!showrooms.isEmpty()){
            Showroom showroom = showrooms.iterator().next();
            Item item = itemService.create(showroom);
            item.setTitle("Drone UHD");
            item.setDescription("El drone mas economico del mercado");
            item.setAvailable(true);
            item.setSKU("920615-AAAA09");
            item.setAvailable(true);
            item.setPrice(12.00);
            itemService.save(item);
            result = itemService.findAll();
            Assert.isTrue(result.size() == count + 1);
            count = result.size();
            itemService.delete(result.iterator().next());
            result = itemService.findAll();
            Assert.isTrue(result.size() == count - 1);
        }
    }

    /*
     * CU7. Editar artículo
     *
     * */
    @Test
    public void editItemPositiveTest() {
        /*
         * a) you must select a use case that involves a listing and an edition requirement; for that use case, you
         * must implement at least 10 test cases that provide a good enough coverage of the
         * statements and the parameter boundaries in your code.
         *
         * */



    }


}
