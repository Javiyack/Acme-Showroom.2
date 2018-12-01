
package usecases.c5administrator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import services.AdministratorService;
import utilities.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:spring/junit.xml"
})
@Transactional
public class CU25DisplayDashBoard extends AbstractTest {

    @Autowired
    private AdministratorService administratorService;
    
    /* An actor who is authenticated as an administrator must be able to:
     * Display a dashboard with the following information...
     */

    /*
     * CU25. Mostrar Dashboard
     */
    @Test
    public void displayDashboard() throws ParseException {
        
        administratorService.findAverageShowroomsPerUser();
        administratorService.findAverageItemsPerUser();
        administratorService.findAverageChirpsPerUser();
        administratorService.findAverageFollowedsPerUser();
        administratorService.findAverageFollowersPerUser();
        administratorService.findAverageRequestsPerUser();
        administratorService.findAverageRejectedRequestsPerUser();
        administratorService.findAverageCommentsPerUser();
        administratorService.findAverageCommentsPerShowroom();
        administratorService.findAverageCommentsPerItem();

        administratorService.findMaximunShowroomsPerUser();
        administratorService.findMaximunItemsPerUser();
        administratorService.findMaximunChirpsPerUser();
        administratorService.findMaximunFollowedsPerUser();
        administratorService.findMaximunFollowersPerUser();
        administratorService.findMaximunRequestsPerUser();
        administratorService.findMaximunRejectedRequestsPerUser();
        administratorService.findMaximunCommentsPerUser();
        administratorService.findMaximunCommentsPerShowroom();
        administratorService.findMaximunCommentsPerItem();

        administratorService.findMinimunShowroomsPerUser();
        administratorService.findMinimunItemsPerUser();
        administratorService.findMinimunChirpsPerUser();
        administratorService.findMinimunFollowedsPerUser();
        administratorService.findMinimunFollowersPerUser();
        administratorService.findMinimunRequestsPerUser();
        administratorService.findMinimunRejectedRequestsPerUser();
        administratorService.findMinimunCommentsPerUser();
        administratorService.findMinimunCommentsPerShowroom();
        administratorService.findMinimunCommentsPerItem();
        
        administratorService.findStdevShowroomsPerUser();
        administratorService.findStdevItemsPerUser();
        administratorService.findStdevChirpsPerUser();
        administratorService.findStdevFollowedsPerUser();
        administratorService.findStdevFollowersPerUser();
        administratorService.findStdevRequestsPerUser();
        administratorService.findStdevRejectedRequestsPerUser();
        administratorService.findStdevCommentsPerUser();
        administratorService.findStdevCommentsPerShowroom();
        administratorService.findStdevCommentsPerItem();
        
    }


}
