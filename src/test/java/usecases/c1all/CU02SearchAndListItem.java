
package usecases.c1all;

import domain.Item;
import domain.Showroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
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
public class CU02SearchAndListItem extends AbstractTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private ShowroomService showroomService;
    private Map <String, Object> testingDataMap;

    /* Casos de uso 2, 3, 4 y 8
     * Search for showrooms using a single keyword that must appear in its title or its de
     * scription. Given a showroom, he or she must be able to navigate to the corresponding items,
     * to the profile of the corresponding user, and so on.
     *
     * CU2. Buscar y listar art�culos
     * Este test realiza busqueda de distintos terminos y comprueba los errores esperados.
     * Carga los datos en 'testingDataMap' y luego llama a la plantilla 'templateSearchShowroomTest'
     */
    @Test
    public void searItemTest() throws ParseException {

        Object userData[][] = (Object[][]) getTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("word", userData[i][0]);
            testingDataMap.put("expected", userData[i][1]);
            this.templateSearchItemTest();
        }
    }

    /*
     * Este test pretende comprobar que el sistema lista todos los art�culos correctamente
     */
    @Test
    public void searchAllItems() throws ParseException {
        Collection <Item> result = itemService.findAll();
        Integer count = result.size();
        super.authenticate("user1");
        Collection <Showroom> showrooms = showroomService.findByLogedActor();
        if (!showrooms.isEmpty()) {
            Showroom showroom = showrooms.iterator().next();
            Item item = itemService.create(showroom);
            item.setTitle("Lalal�");
            item.setDescription("Todo sobre eurovisi�n");
            item.setAvailable(true);
            item.setSKU("920615-AAAA09");
            item.setAvailable(true);
            item.setPrice(10.00);
            item = itemService.save(item);
            result = itemService.findAll();
            Assert.isTrue(result.size() == count + 1);
            count = result.size();
            itemService.delete(item);
            result = itemService.findAll();
            Assert.isTrue(result.size() == count - 1);
        }
    }

    protected void templateSearchItemTest() {
        Class <?> caught;
        /*
         * Rescatamos todos los item (findAll()).
         * Filtramos en java el termino de busqueda.
         * Buscamos en el repositorio el termino buscado
         * y comparamos ambos resultados. Deben ser iguales.
         */
        caught = null;
        try {
            String word = (String) testingDataMap.get("word");
            Collection <Item> all = itemService.findAll();
            Collection <Item> matches = itemService.findAll();
            for (Item item : all) {
                if (!item.getTitle().toLowerCase().contains(word.toLowerCase())
                        && !item.getDescription().toLowerCase().contains(word.toLowerCase())
                        && !item.getSKU().toLowerCase().contains(word.toLowerCase())) {
                    matches.remove(item);
                }
            }
            Collection <Item> result = itemService.findByKeyWord(word);
            for (Item item : result) {
                Assert.isTrue(item.getTitle().toLowerCase().contains(word.toLowerCase())
                        || item.getDescription().toLowerCase().contains(word.toLowerCase())
                        || item.getSKU().toLowerCase().contains(word.toLowerCase()));
            }
            Assert.isTrue(matches.size() == result.size());
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
                {"�?..%&@...#�!(..)", null},
                {null, NullPointerException.class}
        };
        return testingData;
    }


}
