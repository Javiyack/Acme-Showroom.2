
package usecases.c3authenticated;

import domain.Actor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import services.ActorService;
import utilities.AbstractTest;

import java.text.ParseException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CU12FollowTopic extends AbstractTest {

    @Autowired
    private ActorService actorService;

    /* Comments
     * CU12. Follow Topic
     */
    @Test
    public void createFollowTopicTest() throws ParseException {
        super.authenticate("user1");
        String topic = "Tema";
        Actor ppal = actorService.findByPrincipal();
        if (!ppal.getTopics().contains(topic)) {
            actorService.subscribe(topic);
            actorService.flush();
            Assert.isTrue(ppal.getTopics().contains(topic));
        }else{
            actorService.unSubscribe(topic);
            actorService.flush();
            Assert.isTrue(!ppal.getTopics().contains(topic));
            actorService.subscribe(topic);
            actorService.flush();
            Assert.isTrue(ppal.getTopics().contains(topic));
        }
        super.unauthenticate();

    }

    /* Comments
     * CU12. Follow Topic. Negative, not logged
     */
    @Test(expected = IllegalArgumentException.class)
    public void createFollowTopicNegativeTest1() throws ParseException {
        String topic = "Tema";
        actorService.subscribe(topic);
    }

    /* Comments
     * CU12. Follow Topic. Negative, Bad string
     */
    @Test(expected = IllegalArgumentException.class)
    public void createFollowTopicNegativeTest2() throws ParseException {
        super.authenticate("user1");
        actorService.subscribe(null);
        super.unauthenticate();

    }



}
