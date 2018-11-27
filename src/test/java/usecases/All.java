
package usecases;

import domain.Item;
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
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class All extends AbstractTest {

    @Autowired
    private ActorService actorService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private ShowroomService showroomService;
    private Map <String, Object> testingDataMap;

    /*
     * CU 01:Listar Escaparates (CU01)
     */
    @Test
    public void searchAll() throws ParseException {
        Collection <Item> result = itemService.findAll();
        Integer count = result.size();
        super.authenticate("user1");
        Collection <Showroom> showrooms = showroomService.findByLogedActor();
        if(!showrooms.isEmpty()){
            Showroom showroom = showrooms.iterator().next();
            Item item = itemService.create(showroom);
            item.setTitle("Lalalá");
            item.setDescription("Todo sobre eurovisión");
            item.setAvailable(true);
            item.setSKU("920615-AAAA09");
            item.setAvailable(true);
            item.setPrice(10.00);
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
     * CU 02:Buscar Item (CU02)
     */
    @Test
    public void displayItem() throws ParseException {
        super.authenticate("user1");
        Collection <Showroom> showrooms = showroomService.findByLogedActor();
        if(!showrooms.isEmpty()){
            Showroom showroom = showrooms.iterator().next();
            Item item = itemService.create(showroom);
            item.setTitle("Lalalá");
            item.setDescription("Todo sobre eurovisión");
            item.setAvailable(true);
            item.setSKU("920615-AAAA09");
            item.setAvailable(true);
            item.setPrice(10.00);
            item = itemService.save(item);
            Item iteToDisplay = itemService.findOne(item.getId());

            Assert.isTrue(iteToDisplay.equals(item));
        }
    }


    /*
     * CU 02:Buscar Item (CU02)
     */
    @Test
    public void searchItemTest() throws ParseException {

        Object userData[][] = (Object[][]) getTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("word", userData[i][0]);
            testingDataMap.put("expected", userData[i][1]);
            this.templateSearchItemTest();
        }
    }

    protected void templateSearchItemTest() {
        Class <?> caught;

        caught = null;
        try {
            String word = (String) testingDataMap.get("word");
            Collection <Item> all = itemService.findAll();
            Collection <Item> matches = itemService.findAll();
            for (Item item : all) {
                if(!item.getTitle().toLowerCase().contains(word.toLowerCase())
                        && !item.getDescription().toLowerCase().contains(word.toLowerCase())){
                    matches.remove(item);
                }
            }
            Collection <Item> result = itemService.findByKeyWord(word);
            for (Item item : result) {
                Assert.isTrue(item.getTitle().toLowerCase().contains(word.toLowerCase())
                        || item.getDescription().toLowerCase().contains(word.toLowerCase()));
            }
            Assert.isTrue(matches.size()==result.size());
            for (Item item : result) {
                Assert.isTrue(matches.contains(item));
            }

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
    }


    protected Object getTestingData() {
        final Object testingData[][] = {
                {"", null},
                {"todo", null},
                {"escap", null},
                {"¿?..%&@...#¡!(..)", null},
                {null, NullPointerException.class}
        };
        return testingData;
    }

}
