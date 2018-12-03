
package usecases.c3authenticated;

import domain.Actor;
import domain.Chirp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import services.ActorService;
import services.ChirpService;
import utilities.AbstractTest;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CU09CreateChirp extends AbstractTest {

    @Autowired
    private ActorService actorService;
    @Autowired
    private ChirpService chirpService;

    private Map <String, Object> testingDataMap;


    /* Write a chirp.
     * CU09. Crear Chirp
     */
    @Test
    public void createChirpTest() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Object userData[][] = (Object[][]) getChirpTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("actor", userData[i][0]);
            testingDataMap.put("title", userData[i][1]);
            testingDataMap.put("description", userData[i][2]);
            testingDataMap.put("topic", userData[i][3]);
            testingDataMap.put("moment", formatter.parse((String) userData[i][4]));
            testingDataMap.put("expected", userData[i][5]);
            this.templateCreateChirpTest();
        }
    }

    protected Object getChirpTestingData() {
        final Object testingData[][] = {
                {// Positive
                        "user1", "Chirp Title",
                        "Testing chirp description", "Java Topic", "15-06-2018",
                        null
                }, {// Negative. Wrong moment. Future
                "user2", "Chirp Title",
                "Testing chirp description", "Java Topic", "15-06-2050",
                null
        }, {// Negative: without description
                "user3", "Chirp Title",
                "", "Java Topic", "15-06-2018",
                ConstraintViolationException.class
        }, {// Negative: with no title
                "admin", "",
                "Testing chirp description", "Java Topic", "15-06-2018",
                ConstraintViolationException.class
        }
                , {// Negative: description = null
                "user1", "Chirp Title",
                null, "Java Topic", "15-06-2018",
                ConstraintViolationException.class
        }
        };
        return testingData;
    }

    protected void templateCreateChirpTest() {
        Class <?> caught;

        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("actor"));
            Integer userId = super.getEntityId((String) testingDataMap.get("actor"));
            Actor actor = actorService.findOne(userId);

            Chirp chirp = chirpService.create();
            chirp.setTitle((String) testingDataMap.get("title"));
            chirp.setDescription((String) testingDataMap.get("description"));
            chirp.setTopic((String) testingDataMap.get("topic"));
            chirp.setMoment((Date) testingDataMap.get("moment"));
            chirp.setActor(actor);
            this.chirpService.save(chirp);
            this.chirpService.flush();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }


}
