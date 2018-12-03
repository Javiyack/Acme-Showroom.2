
package usecases.c3authenticated;

import domain.Actor;
import domain.Agent;
import domain.User;
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
import services.ActorService;
import utilities.AbstractTest;
import utilities.Tools;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CU08EditActor extends AbstractTest {

    @Autowired
    private ActorService actorService;

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

        Object userData[][] = (Object[][]) getEditionTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("name", userData[i][0]);
            testingDataMap.put("surname", userData[i][1]);
            testingDataMap.put("phone", userData[i][2]);
            testingDataMap.put("email", userData[i][3]);
            testingDataMap.put("address", userData[i][4]);
            testingDataMap.put("bithdate", userData[i][5]);
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
                        Tools.getPastDate(), "MALE", "http://photo.com",
                        "Username1", "user1", "newPassword", "newPassword",
                        null
                }, {//Negative: wrong url
                "user2", "Fernandez Rodriguez",
                "652956526", "test@gmail.com", "Address Test",
                Tools.getPastDate(), "FEMALE", "htp/adsasd.asd/",
                "Username2", "user2", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {// Negative: wrong password
                "admin", "Surname Test",
                "652956526", "test@gmail.com", "Address Test",
                Tools.getPastDate(), "MALE", "http://photo.com",
                "Username3", "Password", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {//Negative: without name
                "", "Fernandez Rodriguez",
                "652956526", "test@gmail.com", "Address Test",
                Tools.getPastDate(), "FEMALE", "http://photo.com",
                "Username2", "user2", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {// Negative: wrong mail
                "admin", "Surname Test",
                "652852126", "email", "Address Test",
                Tools.getPastDate(), "MALE", "http://photo.com",
                "Username3", "admin", "newPassword", "newPassword",
                IllegalArgumentException.class
        },{// Negative: wrong phone
                "admin", "Surname Test",
                "XXXX2852126", "email@test.si", "Address Test",
                Tools.getPastDate(), "MALE", "http://photo.com",
                "Username3", "admin", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {//Negative: wrong date
                "user2", "Fernandez Rodriguez",
                "652956526", "test@gmail.com", "Address Test",
                null, "FEMALE", "http://photo.com",
                "Username2", "user2", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {// Negative: Duplicate actor user account name
                "admin", "Surname Test",
                "652956526", "test@gmail.com", "Address Test",
                Tools.getPastDate(), "MALE", "http://photo.com",
                "user1", "admin", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {// Negative: wrong phone. Blank
                "admin", "Surname Test",
                "", "test@gmail.com", "Address Test",
                Tools.getPastDate(), "MALE", "http://photo.com",
                "Username3", "admin", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {//Negative: wrong date. Future
                "user2", "Fernandez Rodriguez",
                "652956526", "test@gmail.com", "Address Test",
                Tools.getFutureDate(), "FEMALE", "http://photo.com",
                "Username2", "user2", "newPassword", "newPassword",
                IllegalArgumentException.class
        }, {// Negative: Wrong repeat password
                "admin", "Surname Test",
                "652956526", "test@gmail.com", "Address Test",
                Tools.getPastDate(), "MALE", "http://photo.com",
                "user1", "admin", "newPassword", "password",
                IllegalArgumentException.class
        }, {// Negative: Blank form
                "", "",
                "", "", "",
                "", "", "",
                "", "", "", "",
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
            super.startTransaction();
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

        } catch (final Throwable oops) {
            caught = oops.getClass();
            System.out.println("Caught: " + caught + ", " + oops.getLocalizedMessage());
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }



}
