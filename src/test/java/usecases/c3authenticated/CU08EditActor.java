
package usecases.c3authenticated;

import domain.*;
import forms.ActorForm;
import forms.AdminForm;
import forms.AgentForm;
import forms.UserForm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import security.Authority;
import security.UserAccountService;
import services.*;
import utilities.AbstractTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CU08EditActor extends AbstractTest {

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

    /* Edit his or her user account data.
     * CU 22. Editar usuario
     */
    @Test
    public void editUserTest() throws ParseException {
        /*
         * Aquí cargamos  los datos a probar y el error esperado en 'testingDataMap'
         * y llamamos a la plantilla 'templateCreateUserTest'
         */

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
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

    protected void templateEditUserTest() {
        Class <?> caught;
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
                    actorForm = new AdminForm(actor);
                    break;
                case Authority.USER:
                    actorForm = new UserForm((User) actor);
                    ((UserForm) actorForm).setBirthdate((Date) testingDataMap.get("bithdate"));
                    ((UserForm) actorForm).setGenere((String) testingDataMap.get("genere"));
                    ((UserForm) actorForm).setPhoto(((String) testingDataMap.get("photo")));
                    break;
                case Authority.AGENT:
                    actorForm = new AgentForm((Agent) actor);
                    ((AgentForm) actorForm).setCompany(((String) testingDataMap.get("compay")));
                    break;
                default:
                    break;
            }
            actorForm.setName((String) testingDataMap.get("name"));
            actorForm.setSurname((String) testingDataMap.get("surname"));
            actorForm.setPhone((String) testingDataMap.get("phone"));
            actorForm.setEmail((String) testingDataMap.get("email"));
            actorForm.setAddress((String) testingDataMap.get("address"));
            actorForm.setPassword((String) testingDataMap.get("password"));
            actorForm.setNewPassword((String) testingDataMap.get("newPassword"));
            actorForm.setConfirmPassword((String) testingDataMap.get("confirmPassword"));
            actorForm.setUsername((String) testingDataMap.get("username"));
            DataBinder dataBinder = new DataBinder(actorForm);
            BindingResult binding = dataBinder.getBindingResult();
            actor = actorService.reconstructActor(actorForm, binding);
            Assert.isTrue(!binding.hasErrors());
            actor = this.actorService.save(actor);
            this.actorService.flush();
            super.unauthenticate();

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
    }



}
