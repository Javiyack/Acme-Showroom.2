
package usecases.c1all;

import domain.Showroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
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
public class CU01SearchAndListShowroom extends AbstractTest {

    @Autowired
    private ShowroomService showroomService;
    private Map <String, Object> testingDataMap;


    /* Casos de uso 1, 3, 4 y 8
     * Search for items using a single keyword that must appear in its SKU, title, or description.
     * Given an item, he or she must be able to navigate to the corresponding showroom,
     * to the corresponding user, and so on.
     */

    /*
     * CU1. Buscar y listar escaparates
     * Este test realiza busqueda de distintos terminos y comprueba los errores esperados.
     * Carga los datos en 'testingDataMap' y luego llama a la plantilla 'templateSearchItemTest'
     */

    @Test
    public void searShowroomTest() throws ParseException {

        Object userData[][] = (Object[][]) getTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("word", userData[i][0]);
            testingDataMap.put("expected", userData[i][1]);
            this.templateSearchShowroomTest();
        }
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
    /*
     * Este test pretende comprobar que el sistema lista todos los escaparates correctamente
     */
    @Test
    public void searchAllShowrooms() throws ParseException {
        Collection <Showroom> result = showroomService.findAll();
        Integer count = result.size();
        super.authenticate("user1");
        Showroom showroom = showroomService.create();
        showroom.setName("Lalalá");
        showroom.setDescription("Todo sobre eurovisión");
        showroom.setLogo("http://www.qwerty.jpg");
        showroomService.save(showroom);
        result = showroomService.findAll();
        Assert.isTrue(result.size() == count + 1);
        count = result.size();
        showroomService.delete(result.iterator().next());
        result = showroomService.findAll();
        Assert.isTrue(result.size() == count - 1);
    }


    protected void templateSearchShowroomTest() {
        Class <?> caught;

        /*
         * Rescatamos todos los escaparates (findAll()).
         * Filtramos en java el termino de busqueda.
         * Buscamos en el repositorio el termino buscado
         * y comprobamos que ambos resultados sean iguales
         */
        caught = null;
        try {
            String word = (String) testingDataMap.get("word");
            Collection <Showroom> all = showroomService.findAll();
            Collection <Showroom> matches = showroomService.findAll();
            for (Showroom showroom : all) {
                if(!showroom.getName().toLowerCase().contains(word.toLowerCase())
                        && !showroom.getDescription().toLowerCase().contains(word.toLowerCase())){
                    matches.remove(showroom);
                }
            }
            Collection <Showroom> result = showroomService.findByKeyWord(word);
            for (Showroom showroom : result) {
                Assert.isTrue(showroom.getName().toLowerCase().contains(word.toLowerCase()) || showroom.getDescription().toLowerCase().contains(word.toLowerCase()));
            }
            Assert.isTrue(matches.size()==result.size());
            for (Showroom showroom : result) {
                Assert.isTrue(matches.contains(showroom));
            }

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
    }


}
