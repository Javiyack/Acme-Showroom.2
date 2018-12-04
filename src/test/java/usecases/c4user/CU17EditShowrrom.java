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
import services.ShowroomService;
import utilities.AbstractTest;
import utilities.BasicosAleatorios;
import utilities.Tools;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.Collection;

@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CU17EditShowrrom extends AbstractTest {

    /* 10.1
     * Manage an arbitrary number of showrooms, which includes
     * listing, showing, creating, updating, and deleting them.
     *
     *  */

    // System under test ------------------------------------------------------
    @Autowired
    private ShowroomService showroomService;

    /*
     * CU17. Editar escaparate
     *
     * */
    @Test
    public void editShowroomPositiveTest() {
        Collection <Showroom> showrooms = showroomService.findAll();
        if (!showrooms.isEmpty()) {
            final Showroom showroom = showrooms.iterator().next();
            int version = showroom.getVersion();
            super.authenticate(showroom.getUser().getUserAccount().getUsername());
            Showroom showroomToEdit = showroomService.findOne(showroom.getId());
            showroomToEdit = editData(showroomToEdit, "positive");
            showroomService.save(showroomToEdit);
            showroomToEdit = showroomService.findOne(showroom.getId());
            Assert.isTrue(version != showroomToEdit.getVersion());
            super.unauthenticate();
        }
    }

    /*
     * CU17. Editar escaparate
     *
     * */
    @Test()
    public void editShowroomPositiveTestLoop() {
        Collection <Showroom> showrooms = showroomService.findAll();
        for (Showroom showroom : showrooms) {
            Showroom showroomToEdit = showroomService.findOne(showroom.getId());
            showroomToEdit = editData(showroomToEdit, "positive");
            templateEditShowroomTest(showroomToEdit, null);
        }
    }
    /*
     * CU17. Editar escaparate
     *
     * */
    @Test()
    public void editShowroomNegativeTestLoop() {
        Collection <Showroom> showrooms = showroomService.findAll();
        for (Showroom showroom : showrooms) {
            Showroom showroomToEdit = showroomService.findOne(showroom.getId());
            showroomToEdit = editData(showroomToEdit, "negative");
            templateEditShowroomTest(showroomToEdit, ConstraintViolationException.class);
        }
    }


    protected void templateEditShowroomTest(Showroom showroom, final Class<?> expected ) {
        Class <?> caught;

        /*
         * Simulamos la creación de usuario con los datos cargados en 'testingDataMap'
         * y luego comprobamos el error esperado
         */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate(showroom.getUser().getUserAccount().getUsername());
            int version = showroom.getVersion();
            showroomService.save(showroom);
            showroom = showroomService.findOne(showroom.getId());
            Assert.isTrue(version != showroom.getVersion());
        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions(expected, caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

    private Showroom editData(Showroom showroomToEdit, String testType) {
        switch (testType) {
            case "positive":
                showroomToEdit.setName(Tools.generateBussinesName());
                showroomToEdit.setDescription(Tools.getBusinessName());
                showroomToEdit.setLogo(Tools.getUrlAleatoria());
                break;
            case "negative":
                int n = BasicosAleatorios.getNumeroAleatorio(6);
                showroomToEdit.setName((n < 1) ? Tools.getBlankText() : Tools.getBusinessName());
                showroomToEdit.setDescription((n < 2) ? Tools.getBlankText() : Tools.generateDescription());
                showroomToEdit.setLogo((n < 3) ? Tools.getBlankText() : Tools.getUrlAleatoria());
                showroomToEdit.setName((n == 3) ? Tools.getBlankText() : showroomToEdit.getName());
                showroomToEdit.setDescription((n == 4) ? Tools.getBlankText() : showroomToEdit.getDescription());
                showroomToEdit.setLogo((n == 5) ? Tools.getBlankText() : showroomToEdit.getLogo());
                break;
            default:
                break;
        }
        return showroomToEdit;
    }

}
