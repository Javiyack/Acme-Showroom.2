
package usecases.c1all;

import domain.Actor;
import domain.Showroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import services.ActorService;
import services.ShowroomService;
import utilities.AbstractTest;

import java.text.ParseException;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CU05DisplayUser extends AbstractTest {

    /* Casos de uso 1, 3, 4 y 8
     * Search for items using a single keyword that must appear in its SKU, title, or description.
     * Given an item, he or she must be able to navigate to the corresponding showroom,
     * to the corresponding user, and so on.
     */

    @Autowired
    private ActorService actorService;
    @Autowired
    private ShowroomService showroomService;


    /*
     * CU08. Mostrar Usuario
     */
    @Test
    public void displayUser() throws ParseException {
        Collection <Showroom> showrooms = showroomService.findAll();
        if(!showrooms.isEmpty()){
            Showroom showroom = showrooms.iterator().next();
            Integer actorId = showroom.getUser().getId();
            Actor actorToDisplay = actorService.findOne(actorId);
            Assert.isTrue(showroom.getUser().equals(actorToDisplay));
        }else{
            Collection <Actor> actors = actorService.findAll();
            if(!actors.isEmpty()){
                Actor actor = actors.iterator().next();
                Actor actorToDisplay = actorService.findOne(actor.getId());
                Assert.isTrue(actor.equals(actorToDisplay));
            }

        }
    }

    /*
     * CU08. Mostrar Usuario Negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void negativeDisplayUser1() throws ParseException {
        Collection <Showroom> showrooms = showroomService.findAll();
        if(!showrooms.isEmpty()){
            Showroom showroom = showrooms.iterator().next();
            Integer actorId = showroom.getUser().getId();
            Actor actorToDisplay = actorService.findOne(-1);
            Assert.isTrue(showroom.getUser().equals(actorToDisplay));
        }else{
            Collection <Actor> actors = actorService.findAll();
            if(!actors.isEmpty()){
                Actor actor = actors.iterator().next();
                Actor actorToDisplay = actorService.findOne(-1);
                Assert.isTrue(actor.equals(actorToDisplay));
            }

        }
    }

    /*
     * CU08. Mostrar Usuario Negative
     */
    @Test(expected = IllegalArgumentException.class)
    public void negativeDisplayUser2() throws ParseException {
        Collection <Showroom> showrooms = showroomService.findAll();
        if(!showrooms.isEmpty()){
            Showroom showroom = showrooms.iterator().next();
            Integer actorId = showroom.getUser().getId();
            Actor actorToDisplay = actorService.findOne(actorId+1);
            Assert.isTrue(showroom.getUser().equals(actorToDisplay));
        }else{
            Collection <Actor> actors = actorService.findAll();
            if(!actors.isEmpty()){
                Actor actor = actors.iterator().next();
                Actor actorToDisplay = actorService.findOne(actor.getId()+1);
                Assert.isTrue(actor.equals(actorToDisplay));
            }

        }
    }

}
