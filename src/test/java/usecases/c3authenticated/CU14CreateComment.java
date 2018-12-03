
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
import services.ActorService;
import services.CommentService;
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
public class CU14CreateComment extends AbstractTest {

    @Autowired
    private ActorService actorService;
    @Autowired
    private CommentService commentService;

    private Map <String, Object> testingDataMap;

    /* Comments
     * CU14. Crear comentario
     */
    @Test
    public void createCommentTest() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Object userData[][] = (Object[][]) getCommentTestingData();
        for (int i = 0; i < userData.length; i++) {
            testingDataMap = new HashMap <String, Object>();
            testingDataMap.put("actor", userData[i][0]);
            testingDataMap.put("object", userData[i][1]);
            testingDataMap.put("title", userData[i][2]);
            testingDataMap.put("text", userData[i][3]);
            testingDataMap.put("rating", Integer.parseInt((String)userData[i][4]));
            Collection <String> pictures = new ArrayList <String>();
            pictures.add((String) userData[i][5]);
            pictures.add((String) userData[i][6]);
            pictures.add((String) userData[i][7]);
            testingDataMap.put("pictures", pictures);
            testingDataMap.put("moment", formatter.parse((String) userData[i][8]));
            testingDataMap.put("expected", userData[i][9]);
            this.templateCreateCommentTest();
        }
    }

    protected void templateCreateCommentTest() {
        Class <?> caught;

        caught = null;
        try {
            super.startTransaction();
            super.authenticate((String) testingDataMap.get("actor"));
            Integer objectId = (testingDataMap.get("object")!=null)?0:null;
            try{
                objectId = super.getEntityId((String) testingDataMap.get("object"));
            }catch (final Throwable oops){

            }
            Actor actor = actorService.findByPrincipal();

            Comment comment = commentService.create();
            comment.setActor(actor);
            comment.setCommentedObjectId(objectId);
            comment.setTitle((String) testingDataMap.get("title"));
            comment.setText((String) testingDataMap.get("text"));
            comment.setRating((Integer) testingDataMap.get("rating"));
            comment.setMoment((Date) testingDataMap.get("moment"));
            comment.setPictures((Collection<String>) testingDataMap.get("pictures"));
            comment = this.commentService.save(comment);
            super.unauthenticate();
            Assert.isTrue(comment.getId()!=0);

        } catch (final Throwable oops) {
            caught = oops.getClass();
        }
        super.checkExceptions((Class <?>) testingDataMap.get("expected"), caught);
        super.rollbackTransaction();
    }


    protected Object getCommentTestingData() {
        final Object testingData[][] = {
                {// Positive Showroom
                        "user1", "showroom1", "Comment name 1",
                        "Testing comment description", "3", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2018",
                        null
                },
                {// Positive
                        "user1", "item1", "Comment name 2",
                        "Testing comment description", "3", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2018",
                        null
                },
                {// Positive. Even though Wrong moment(future) is passed the system will save present moment.
                        "user1", "item1", "Comment name 3",
                        "Testing comment description", "3", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2050",
                        null
                },
                {// Negative: without title
                        "user1", "item1", "",
                        "Testing comment description", "3", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2018",
                        ConstraintViolationException.class
                },
                {// Negative: text = null
                        "user1", "showroom1", "Comment name 5",
                        null, "3", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2018",
                        ConstraintViolationException.class
                },
                {// Negative: wrong rating
                        "user1", "showroom1", "Comment name 6",
                        "Testing comment description", "4", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2016",
                        ConstraintViolationException.class
                },
                {// Negative: wrong rating
                        "user1", "item1", "Comment name 7",
                        "Testing comment description", "-1", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2014",
                        ConstraintViolationException.class
                },
                {// Negative: no commented object
                        "user1", "", "Comment name 8",
                        "Testing comment description", "3", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2016",
                        IllegalArgumentException.class
                },
                {// Negative: null commented object
                        "user1", null, "Comment name 9",
                        "Testing comment description", "3", "http://foto.jpg",
                        "http://foto.jpg", "http://foto.jpg", "15-06-2016",
                        IllegalArgumentException.class
                },
                {// Negative: wrong url
                        "user1", "item1", "Comment name 10",
                        "Testing comment description", "3", "http://foto.jpg",
                        "http://foto.jpg", "/foto.jpg", "15-06-2017",
                        ConstraintViolationException.class
                }
        };
        return testingData;
    }


}
