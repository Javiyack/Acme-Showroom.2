
package usecases.c1all;

import domain.Item;
import domain.Showroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
public class CU04DisplayItem extends AbstractTest {
    /* Casos de uso 1, 3, 4 y 8
     * Search for items using a single keyword that must appear in its SKU, title, or description.
     * Given an item, he or she must be able to navigate to the corresponding showroom,
     * to the corresponding user, and so on.
     */
    @Autowired
    private ActorService actorService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShowroomService showroomService;
    private Map <String, Object> testingDataMap;

    /*
     * CU4. Mostrar artículo
     */
    @Test
    public void displayItem() throws ParseException {
        Collection <Item> items = itemService.findAll();
        if (!items.isEmpty()) {
            Item item = items.iterator().next();
            Item itemToDisplay = itemService.findOne(item.getId());
            Assert.isTrue(itemToDisplay.equals(item));
        } else {
            Collection <Showroom> showrooms = showroomService.findAll();
            if (!showrooms.isEmpty()) {
                Showroom showroom = showrooms.iterator().next();
                Item item = itemService.create(showroom);
                item.setTitle("Lalalá");
                item.setDescription("Todo sobre eurovisión");
                item.setAvailable(true);
                item.setSKU("920615-AAAA09");
                item.setAvailable(true);
                item.setPrice(10.00);
                item = itemService.save(item);
                Item itemToDisplay = itemService.findOne(item.getId());

                Assert.isTrue(itemToDisplay.equals(item));
            }
        }
    }


    /*
     * CU4. Mostrar Item Negative 1
     */
    @Test(expected = IllegalArgumentException.class)
    public void displayItemNegativeTest1() throws ParseException {
        Collection <Item> Items = itemService.findAll();
        if (!Items.isEmpty()) {
            Item Item = Items.iterator().next();
            Item ItemToDisplay = itemService.findOne(Item.getId() + 1);

            Assert.isTrue(ItemToDisplay.equals(Item));
        }
    }


    /*
     * CU4. Mostrar Item Negative 2
     */
    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void displayItemNegativeTest2() throws ParseException {
        Collection <Item> Items = itemService.findAll();
        if (!Items.isEmpty()) {
            Item Item = Items.iterator().next();
            Item ItemToDisplay = itemService.findOne(null);

            Assert.isTrue(ItemToDisplay.equals(Item));
        }
    }


}
