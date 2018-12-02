
package usecases.c3authenticated;

import domain.Actor;
import domain.Comment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
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
public class CU11UnfollowActor extends AbstractTest {
    @Autowired
    private ActorService actorService;

    /* Comments
     * CU11. Dejar de Seguir usuario
     */
    @Test
    public void createUnFollowTest() throws ParseException {
        super.authenticate("user1");
        Integer dbId = super.getEntityId("user2");
        Actor ppal = actorService.findByPrincipal();
        Actor followed = actorService.findOne(dbId);
        if (ppal.getFollows().contains(followed)) {
            actorService.follow(dbId);
            Assert.isTrue(!ppal.getFollows().contains(followed));
        }else{
            actorService.follow(dbId);
            Assert.isTrue(ppal.getFollows().contains(followed));
            actorService.follow(dbId);
            Assert.isTrue(!ppal.getFollows().contains(followed));
        }
        super.unauthenticate();

    }

    /* Comments
     * CU10. Seguir usuario. Negative, not logged
     */
    @Test(expected = IllegalArgumentException.class)
    public void createUnFollowNegativeTest1() throws ParseException {
        super.unauthenticate();
        Integer dbId = super.getEntityId("user2");
        actorService.follow(dbId);
    }

    /* Comments
     * CU10. Seguir usuario. Bad Id
     */
    @Test(expected = IllegalArgumentException.class)
    public void createUnFollowNegativeTest2() throws ParseException {
        super.authenticate("user1");
        actorService.follow(-1);
        super.unauthenticate();

    }


}
