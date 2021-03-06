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
import domain.Request;
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
import java.util.Iterator;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CU19DeleteShowroom extends AbstractTest {

    // System under test ------------------------------------------------------
    @Autowired
    private ShowroomService showroomService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private RequestService requestService;

    /*
     * CU19. Delete Showroom
     *
     * */
    @Test
    public void deleteShowroomTest() {
        Collection <Showroom> showrooms = showroomService.findAll();
        Showroom showroom = null;
        Showroom showroomToDelete = null;
        Iterator<Showroom> iterator = showrooms.iterator();
        if (!showrooms.isEmpty()) {
            boolean borrable = false;
            while (!borrable && iterator.hasNext()) {
                borrable = true;
                showroom = iterator.next();
                super.authenticate(showroom.getUser().getUserAccount().getUsername());
                showroomToDelete = showroomService.findOne(showroom.getId());
                Collection <Item> items = itemService.findByShowroom(showroomToDelete);
                for (Item item : items) {
                    Collection <Request> requests = requestService.findByItemId(item.getId());
                    if (!requests.isEmpty()) {
                        borrable = false;
                        break;
                    }
                }
            }
            if (borrable){
                showroomService.delete(showroomToDelete);
                showrooms = showroomService.findAll();
                Assert.isTrue(!showrooms.contains(showroom));
            }else{
                Assert.isTrue(false);
            }
            super.unauthenticate();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteShowroomNegativeTest1() {
        // Negative: Not owner, not user.
        Collection <Showroom> showrooms = showroomService.findAll();
        if (!showrooms.isEmpty()) {
            Showroom showroom = showrooms.iterator().next();
            super.authenticate("admin");
            Showroom showroomToDelete = showroomService.findOne(showroom.getId());
            showroomService.delete(showroomToDelete);
            showrooms = showroomService.findAll();
            Assert.isTrue(!showrooms.contains(showroom));
            super.unauthenticate();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteShowroomNegativeTest2() {
        // Negative: Not Logged
        Collection <Showroom> showrooms = showroomService.findAll();
        if (!showrooms.isEmpty()) {
            Showroom showroom = showrooms.iterator().next();
            Showroom showroomToDelete = showroomService.findOne(showroom.getId());
            showroomService.delete(showroomToDelete.getId());
            showrooms = showroomService.findAll();
            Assert.isTrue(!showrooms.contains(showroom));
        }
    }

}
