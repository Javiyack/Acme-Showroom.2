
package usecases.c2anonymous;

import java.text.ParseException;
import java.util.*;

import domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.util.Assert;
import security.UserAccountService;
import services.ActorService;
import services.AgentService;
import services.UserService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CU07AccesoUsuario extends AbstractTest {

	@Autowired
    private ActorService		actorService;
    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
	private UserAccountService	userAccountService;
	private Map<String, Object> testingDataMap;

    /* Edit his or her user account data.
     * CU21. Acceso usuario
     */
    @Test
    public void positiveLoginTest()throws ParseException {
        super.authenticate("user1");
        Actor actor = actorService.findByPrincipal();
        Assert.isTrue(actor.getUserAccount().getUsername().equals("user1"));
        super.unauthenticate();
    }
    @Test(expected = IllegalArgumentException.class)
    public void negativeLoginTest1()throws ParseException {
        super.authenticate("asd");
        Actor actor = actorService.findByPrincipal();
        Assert.isNull(actor);
        super.unauthenticate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeLoginTest2()throws ParseException {
        super.authenticate("");
        Actor actor = actorService.findByPrincipal();
        Assert.isNull(actor);
        super.unauthenticate();
    }
    @Test(expected = NullPointerException.class)
    public void negativeLoginTest3()throws ParseException {
        super.authenticate(null);
        Actor actor = actorService.findByPrincipal();
        Assert.isTrue(actor.getUserAccount().getUsername().equals("user1"));
        super.unauthenticate();
    }


}
