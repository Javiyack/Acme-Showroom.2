
package usecases.c3authenticated;

import domain.Actor;
import domain.Chirp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import security.UserAccountService;
import services.*;
import utilities.AbstractTest;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CU09CreateChirp extends AbstractTest {

    @Autowired
    private ActorService actorService;
    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private ChirpService chirpService;
    @Autowired
    private CommentService commentService;

    private Map <String, Object> testingDataMap;


    /* Write a chirp.
     * CU 10. Crear Chirp
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
                        "Testing chirp description", "Java Topic", "15-06-2050",
                        null
                }, {// Negative. Wrong moment. Future
                "user2", "Chirp Title",
                "Testing chirp description", "Java Topic", "15-06-2050",
                null
        }, {// Negative: without name
                "user3", "Chirp Title",
                "Testing chirp description", "Java Topic", "15-06-2050",
                null
        }, {// Negative: with no title
                "admin", "",
                "Testing chirp description", "Java Topic", "15-06-2050",
                ConstraintViolationException.class
        }
                , {// Negative: description = null
                "user1", "Chirp Title",
                null, "Java Topic", "15-06-2050",
                ConstraintViolationException.class
        }
        };
        return testingData;
    }

    protected void templateCreateChirpTest() {
        Class <?> caught;

        caught = null;
        try {
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
    }


}
