
package usecases.c5administrator;

import domain.Actor;
import domain.Administrator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import security.Authority;
import security.UserAccount;
import security.UserAccountService;
import services.ActorService;
import services.AgentService;
import services.UserService;
import utilities.AbstractTest;

import javax.validation.ConstraintViolationException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CU24CrearAdministrador extends AbstractTest {

	@Autowired
    private ActorService		actorService;
    @Autowired
    private UserService userService;
    @Autowired
    private AgentService agentService;
    @Autowired
	private UserAccountService	userAccountService;
	private Map<String, Object> testingDataMap;

    /*
    /* An actor who is authenticated as an administrator must be able to:
     * Create user accounts for new administrators.
     */

    /*
     * CU24. Un administrador puede crear cuentas de administrador
     */
    @Test
    public void createAdministratorTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateUserTest'. Se prueban casos positivos y negativos
         */

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Object userData[][]=(Object[][]) getCreationTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap<String, Object>();
            testingDataMap.put("name", userData[i][0]);
            testingDataMap.put("surname", userData[i][1]);
            testingDataMap.put("phone", userData[i][2]);
            testingDataMap.put("email", userData[i][3]);
            testingDataMap.put("address", userData[i][4]);
            testingDataMap.put("username", userData[i][5]);
            testingDataMap.put("password", userData[i][6]);
            testingDataMap.put("expected", userData[i][7]);
            testingDataMap.put("administrator", userData[i][8]);
            this.templateCreateUserTest();
        }
    }
    protected void templateCreateUserTest() {
        Class<?> caught;
        /*
         * Simulamos la creación de usuario con los datos cargados en 'testingDataMap'
         * y luego comprobamos el error esperado
         */
        caught = null;
        try {

            super.authenticate((String) testingDataMap.get("administrator"));
            final Administrator administrator = new Administrator();
            final UserAccount userAccount = this.userAccountService.create(Authority.ADMINISTRATOR);
            administrator.setName((String) testingDataMap.get("name"));
            administrator.setSurname((String) testingDataMap.get("surname") );
            administrator.setPhone((String) testingDataMap.get("phone") );
            administrator.setEmail((String) testingDataMap.get("email") );
            administrator.setAddress((String)  testingDataMap.get("address"));
            userAccount.setPassword((String)  testingDataMap.get("password"));
            userAccount.setUsername((String)  testingDataMap.get("username"));
            administrator.setUserAccount(userAccount);
            administrator.setFollows(new HashSet<Actor>());
            administrator.setTopics(new HashSet<String>());
            this.actorService.save(administrator);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class<?>) testingDataMap.get("expected"), caught);
    }
    protected Object getCreationTestingData() {
        final Object testingData[][] = {
                {// Positive
                        "AdminName1", "Dominguez Lopez",
                        "652956526", "test@gmail.com", "Address Test",
                        "Username1", "Password1",
                        null, "admin"
                }, {//Positive
                "Username2", "Fernandez Rodriguez",
                "652956526", "test@gmail.com", "Address Test",
                "Username2", "Password2",
                null, "admin"
        }, {// Negative: without name
                "", "Surname Test",
                "652956526", "email", "Address Test",
                "Username3", "Password3",
                ConstraintViolationException.class, "admin"
        }, {// Negative: with name null
                null, "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "Username4", "Password4",
                DataIntegrityViolationException.class, "admin"
        }
                , {// Negative: with short username
                "Username5", "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "User", "Password4",
                DataIntegrityViolationException.class, "admin"
        }
                , {// Negative: with birth date not past
                "Username6", "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "Username6", "Password4",
                DataIntegrityViolationException.class, "admin"
        }
                , {// Negative: with wrong url
                "Username7", "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "Name7", "Password4",
                DataIntegrityViolationException.class, "admin"
        }
                , {// Negative: with duplicate username
                "Name7", "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "user1", "Password4",
                DataIntegrityViolationException.class, "admin"
        }
                , {// Negative: Logged actor is not aan administrator
                "AdminName12", "Dominguez Lopez",
                "652956526", "test@gmail.com", "Address Test",
                "Username1", "Password1",
                DataIntegrityViolationException.class, "user1"
        }
        };
        return testingData;
    }


}
