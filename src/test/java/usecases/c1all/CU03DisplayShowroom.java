
package usecases.c1all;

import domain.Showroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import services.ActorService;
import services.ItemService;
import services.ShowroomService;
import utilities.AbstractTest;

import java.text.ParseException;
import java.util.Collection;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CU03DisplayShowroom extends AbstractTest {

    @Autowired
    private ActorService actorService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShowroomService showroomService;
    private Map <String, Object> testingDataMap;
    /* Casos de uso 1, 3, 4 y 8
     * Search for items using a single keyword that must appear in its SKU, title, or description.
     * Given an item, he or she must be able to navigate to the corresponding showroom,
     * to the corresponding user, and so on.
     */


    /*
     * CU3. Mostrar Showroom
     */
    @Test
    public void displayShowroom() throws ParseException {
        Collection <Showroom> showrooms = showroomService.findAll();
        if(!showrooms.isEmpty()){
            Showroom showroom = showrooms.iterator().next();
            Showroom showroomToDisplay = showroomService.findOne(showroom.getId());

            Assert.isTrue(showroomToDisplay.equals(showroom));
        }
    }


}
