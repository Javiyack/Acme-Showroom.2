/*
 * SampleTest.java
 *
 * Copyright (C) 2017 Universidad de Sevilla
 *
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package usecases.c4user;

import domain.Showroom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import services.ItemService;
import services.RequestService;
import services.ShowroomService;
import utilities.AbstractTest;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CU15CreateShowrrom extends AbstractTest {

    /*
     * Manage an arbitrary number of showrooms, which includes
     * listing, showing, creating, updating, and deleting them.
     *
     *  */

    // System under test ------------------------------------------------------
    @Autowired
    private RequestService requestService;
    @Autowired
    private ShowroomService showroomService;
    @Autowired
    private ItemService itemService;

    /*
     * CU15. Crear escaparate
     *
     * */
    @Test
    public void createShowroomPositiveTest() {
        super.authenticate("user3");
        Showroom showroom = showroomService.create();
        showroom.setName("Showroom Name");
        showroom.setDescription("Ten siempre presente este ecaparate");
        showroom.setLogo("http://www.qwerty.jpg");
        showroom = showroomService.save(showroom);
        showroomService.flush();
        Collection<Showroom> result = showroomService.findAll();
        Assert.isTrue(result.size() >0);
        Assert.isTrue(result.contains(showroom));
    }


    /*
     * CU15. Crear escaparate. egative, Bad logo url
     *
     * */
    @Test(expected = ConstraintViolationException.class)
    public void createShowroomNegativeTest1() {
        super.authenticate("user3");
        Showroom showroom = showroomService.create();
        showroom.setName("Lalalá");
        showroom.setDescription("Todo sobre eurovisión");
        showroom.setLogo("htt:/ww.qwerty.jpg");
        showroom = showroomService.save(showroom);
        showroomService.flush();
        Collection<Showroom> result = showroomService.findAll();
        Assert.isTrue(result.size() >0);
        Assert.isTrue(result.contains(showroom));
    }

    /*
     * CU15. Crear escaparate. egative, Blank name
     *
     * */
    @Test(expected = ConstraintViolationException.class)
    public void createShowroomNegativeTest2() {
        super.authenticate("user3");
        Showroom showroom = showroomService.create();
        showroom.setName("");
        showroom.setDescription("Todo sobre eurovisión");
        showroom.setLogo("http://www.qwerty.jpg");
        showroom = showroomService.save(showroom);
        showroomService.flush();
        Collection<Showroom> result = showroomService.findAll();
        Assert.isTrue(result.size() >0);
        Assert.isTrue(result.contains(showroom));
    }



}
