package ilgulee.com.plantplaceslec;

import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import ilgulee.com.plantplaceslec.dto.PlantDTO;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class TestPlantDAO {
    //define a variable for the DAO we are testing.
    IPlantDAO mPlantDAO;
    @BeforeClass
    public static void setupAllTests() {
        System.out.println("BeforeClass: running init for ALL tests.");
    }
    @Before
    public void setUp(){
        mPlantDAO=new PlantDAOStub();
        System.out.println("Before: running init before EACH tests.");
    }
    @Test
    public void testPlantDAO_searchForRedbudShouldReturnAtLeastOneResult(){
        boolean redbudFound=false;
        List<PlantDTO> plants = mPlantDAO.fetchPlants("Redbud");
        for (PlantDTO plant:plants){
            if(plant.getCommon().contains("Redbud")){
                redbudFound=true;
            }
        }
        assertTrue(redbudFound);
        System.out.println("TEST: Running redbud test.");
    }
    @Test@Ignore
    public void testPlantDAO_searchForOakShouldReturnAtLeastOneWhiteOak() throws IOException, JSONException {

        // assume we do not have a match.
        boolean whiteOakFound = false;

        List<PlantDTO> plants = mPlantDAO.fetchPlants("Oak");

        for (PlantDTO plant : plants) {
            if (plant.getGenus().equalsIgnoreCase("Quercus") && plant.getSpecies().contains("alba")) {
                whiteOakFound = true;
            }
        }
        // did we find a redbud?
        assertTrue(whiteOakFound);
        System.out.println("TEST: Running white oak test.");
    }
    //
    public void testPlantDAO_searchForEShouldReturnAtLeastTwoResults() throws IOException, JSONException {
        List<PlantDTO> plants = mPlantDAO.fetchPlants("e");
        int size = plants.size();
        //boolean atLeastTwo = size > 2;
        //assertTrue(atLeastTwo);
        System.out.println("TEST: this should not run, because it is not annotated.");
         assertThat(size, is(lessThan(2)));
    }
    @After
    public void teardown() {
        System.out.println("After: Tearing down EACH test.");

    }
    @AfterClass
    public static void teardownAllTests() {
        System.out.println("AfterClass: tearing down after ALL tests.");
    }
}
