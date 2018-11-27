
package usecases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import domain.Agent;
import forms.ActorForm;
import forms.AdminForm;
import forms.AgentForm;
import forms.UserForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import domain.Actor;
import domain.User;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import security.Authority;
import security.UserAccount;
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
public class UseCaseAnonymous extends AbstractTest {

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
     * Caso de uso 01: Register to the system as a user.
     */
    @Test
    public void createUserTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateUserTest'
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
            testingDataMap.put("bithdate", formatter.parse((String) userData[i][5]));
            testingDataMap.put("genere", userData[i][6]);
            testingDataMap.put("photo", userData[i][7]);
            testingDataMap.put("username", userData[i][8]);
            testingDataMap.put("password", userData[i][9]);
            testingDataMap.put("expected", userData[i][10]);
            this.templateCreateUserTest();
        }
    }
    /*
     * Caso de uso 01: Register to the system as a user.
     */
    @Test
    public void editUserTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateUserTest'
         */

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Object userData[][]=(Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap<String, Object>();
            testingDataMap.put("name", userData[i][0]);
            testingDataMap.put("surname", userData[i][1]);
            testingDataMap.put("phone", userData[i][2]);
            testingDataMap.put("email", userData[i][3]);
            testingDataMap.put("address", userData[i][4]);
            testingDataMap.put("bithdate", formatter.parse((String) userData[i][5]));
            testingDataMap.put("genere", userData[i][6]);
            testingDataMap.put("photo", userData[i][7]);
            testingDataMap.put("username", userData[i][8]);
            testingDataMap.put("password", userData[i][9]);
            testingDataMap.put("newPassword", userData[i][10]);
            testingDataMap.put("confirmPassword", userData[i][11]);
            testingDataMap.put("expected", userData[i][12]);
            this.templateEditUserTest();
        }
    }
    protected Object getEditionTestingData() {
        final Object testingData[][] = {
                {// Positive
                        "user1", "Dominguez Lopez",
                        "652956526", "test@gmail.com", "Address Test",
                        "15-06-1972", "MALE", "http://photo.com",
                        "Username1", "user1", "newPassword", "newPassword",
                        null
                }, {//Negative: without url
                "user2", "Fernandez Rodriguez",
                "652956526", "test@gmail.com", "Address Test",
                "25-08-1987", "FEMALE", "",
                "Username2", "user2", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {// Negative: wrong pasword
                "admin", "Surname Test",
                "652956526", "email", "Address Test",
                "24-03-2001", "MALE", "http://photo.com",
                "Username3", "Password", "newPassword", "newPassword",
                IllegalArgumentException.class
        }
        };
        return testingData;
    }
    protected Object getCreationTestingData() {
        final Object testingData[][] = {
                {// Positive
                        "Username1", "Dominguez Lopez",
                        "652956526", "test@gmail.com", "Address Test",
                        "15-06-1972", "MALE", "http://photo.com",
                        "Username1", "Password1",
                        null
                }, {//Positive
                "Username2", "Fernandez Rodriguez",
                "652956526", "test@gmail.com", "Address Test",
                "25-08-1987", "FEMALE", "http://photo.com",
                "Username2", "Password2",
                null
        }, {// Negative: without name
                "", "Surname Test",
                "652956526", "email", "Address Test",
                "24-03-2001", "MALE", "http://photo.com",
                "Username3", "Password3",
                ConstraintViolationException.class
        }, {// Negative: with name null
                null, "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "30-12-1968", "MALE", "http://photo.com",
                "Username4", "Password4",
                DataIntegrityViolationException.class
        }
                , {// Negative: with short username
                "Username5", "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "13-12-1985", "MALE", "http://photo.com",
                "User", "Password4",
                DataIntegrityViolationException.class
        }
                , {// Negative: with birth date not past
                "Username6", "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "15-06-2099", "FEMALE", "http://photo.com",
                "Username6", "Password4",
                DataIntegrityViolationException.class
        }
                , {// Negative: with wrong url
                "Username7", "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "21-04-1992", "UNDEFINED", "http:/photo.com",
                "Username7", "Password4",
                DataIntegrityViolationException.class
        }
                , {// Negative: with duplicate username
                "Username7", "Surname Test",
                "652956526", "emailTest@email.com", "Address Test",
                "21-04-1992", "UNDEFINED", "http://photo.com",
                "user1", "Password4",
                DataIntegrityViolationException.class
        }
        };
        return testingData;
    }
    protected void templateCreateUserTest() {
        Class<?> caught;
        /*
            Simulamos la creación de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            final User user = new User();
            final UserAccount userAccount = this.userAccountService.create(Authority.USER);
            user.setName((String) testingDataMap.get("name"));
            user.setSurname((String) testingDataMap.get("surname") );
            user.setPhone((String) testingDataMap.get("phone") );
            user.setEmail((String) testingDataMap.get("email") );
            user.setBirthdate((java.util.Date)  testingDataMap.get("bithdate") );;
            user.setGenere((String)  testingDataMap.get("genere"));
            user.setAddress((String)  testingDataMap.get("address"));
            user.setPhoto(((String)  testingDataMap.get("photo")));
            userAccount.setPassword((String)  testingDataMap.get("password"));
            userAccount.setUsername((String)  testingDataMap.get("username"));
            user.setUserAccount(userAccount);
            user.setFollows(new HashSet<Actor>());
            user.setTopics(new HashSet<String>());
            this.actorService.save(user);
            this.actorService.flush();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class<?>) testingDataMap.get("expected"), caught);
    }

    protected void templateEditUserTest() {
        Class<?> caught;
        /*
            Simulamos la edición  de usuario con los datos cargados en 'testingDataMap'
            y luego comprobamos el error esperado
        */
        caught = null;
        try {
            super.authenticate((String) testingDataMap.get("name"));
            Integer userId = super.getEntityId((String) testingDataMap.get("name"));
            Actor actor = actorService.findOne(userId);
            ActorForm actorForm = null;
            switch (actor.getUserAccount().getAuthorities().iterator().next().getAuthority()) {
                case Authority.ADMINISTRATOR:
                    actorForm = new ActorForm(actor);
                    break;
                case Authority.USER:
                    actorForm = new UserForm((User)actor);
                    ((UserForm) actorForm).setBirthdate((java.util.Date)  testingDataMap.get("bithdate") );
                    ((UserForm) actorForm).setGenere((String)  testingDataMap.get("genere"));
                    ((UserForm) actorForm).setPhoto(((String)  testingDataMap.get("photo")));
                    break;
                case Authority.AGENT:
                    actorForm = new AgentForm((Agent) actor);
                    ((AgentForm) actorForm).setCompany(((String)  testingDataMap.get("compay")));
                    break;
                default:
                    break;
            }
            actorForm.setName((String) testingDataMap.get("name"));
            actorForm.setSurname((String) testingDataMap.get("surname") );
            actorForm.setPhone((String) testingDataMap.get("phone") );
            actorForm.setEmail((String) testingDataMap.get("email") );
            actorForm.setAddress((String)  testingDataMap.get("address"));
            actorForm.setPassword((String)  testingDataMap.get("password"));
            actorForm.setNewPassword((String)  testingDataMap.get("newPassword"));
            actorForm.setConfirmPassword((String)  testingDataMap.get("confirmPassword"));
            actorForm.setUsername((String)  testingDataMap.get("username"));
            DataBinder dataBinder = new DataBinder(actorForm);
            BindingResult binding = dataBinder.getBindingResult();
            actor = actorService.reconstructActor(actorForm, binding);
            actor = this.actorService.save(actor);
            this.actorService.flush();
            super.unauthenticate();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class<?>) testingDataMap.get("expected"), caught);
    }

}
