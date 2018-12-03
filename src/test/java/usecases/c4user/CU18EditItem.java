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

import domain.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;
import services.ItemService;
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
public class CU18EditItem extends AbstractTest {

    // System under test ------------------------------------------------------
    @Autowired
    private ItemService itemService;

    /*
     * CU18. Editar artículo
     *
     * */
    @Test
    public void editItemPositiveTest() {
        Collection <Item> items = itemService.findAll();
        if (!items.isEmpty()) {
            final Item item = items.iterator().next();
            int version = item.getVersion();
            super.authenticate(item.getShowroom().getUser().getUserAccount().getUsername());
            Item itemToEdit = itemService.findOne(item.getId());
            itemToEdit = editData(itemToEdit, "positive");
            itemService.save(itemToEdit);
            itemToEdit = itemService.findOne(item.getId());
            Assert.isTrue(version != itemToEdit.getVersion());
            super.unauthenticate();
        }
    }

    /*
     * CU18. Editar artículo
     *
     * */
    @Test()
    public void editItemPositiveTestLoop() {
        Collection <Item> items = itemService.findAll();
        for (Item item : items) {
            Item itemToEdit = itemService.findOne(item.getId());
            itemToEdit = editData(itemToEdit, "positive");
            System.out.println(" ");
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println(" ");
            System.out.println("Positive Data tested");
            templateEditItemTest(itemToEdit, null);
        }
    }
    /*
     * CU18. Editar artículo
     *
     * */
    @Test()
    public void editItemNegativeTestLoop() {
        Collection <Item> items = itemService.findAll();
        for (Item item : items) {
            Item itemToEdit = itemService.findOne(item.getId());
            itemToEdit = editData(itemToEdit, "negative");
            System.out.println(" ");
            System.out.println("-------------------------------------------------------------------------------------------");
            System.out.println(" ");
            System.out.println("Negative Data tested");
            templateEditItemTest(itemToEdit, ConstraintViolationException.class);
        }
    }


    protected void templateEditItemTest(Item item, final Class<?> expected ) {
        Class <?> caught;

        /*
         * Simulamos la creación de usuario con los datos cargados en 'testingDataMap'
         * y luego comprobamos el error esperado
         */
        caught = null;
        try {
            super.startTransaction();
            super.authenticate(item.getShowroom().getUser().getUserAccount().getUsername());
            int version = item.getVersion();
            System.out.println("Title: " + item.getTitle());
            System.out.println("Desc: " + item.getDescription());
            System.out.println("Price: " + item.getPrice());
            itemService.save(item);
            item = itemService.findOne(item.getId());
            Assert.isTrue(version != item.getVersion());
        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions(expected, caught);
        super.unauthenticate();
        super.rollbackTransaction();
    }

    private Item editData(Item itemToEdit, String testType) {
        switch (testType) {
            case "positive":
                itemToEdit.setTitle(Tools.generateBussinesName());
                itemToEdit.setDescription(Tools.generateDescription());
                itemToEdit.setSKU(itemService.generateSKU());
                break;
            case "negative":
                int n = BasicosAleatorios.getNumeroAleatorio(6);
                itemToEdit.setTitle((n < 1) ? Tools.getBlankText() : Tools.getBusinessName());
                itemToEdit.setDescription((n < 2) ? Tools.getBlankText() : Tools.generateDescription());
                itemToEdit.setPrice((n < 3) ? null : 99.90);
                itemToEdit.setTitle((n == 3) ? Tools.getBlankText() : itemToEdit.getTitle());
                itemToEdit.setDescription((n == 4) ? Tools.getBlankText() : itemToEdit.getDescription());
                itemToEdit.setPrice((n == 5) ? null : itemToEdit.getPrice());
                break;
            default:
                break;
        }
        return itemToEdit;
    }

}
