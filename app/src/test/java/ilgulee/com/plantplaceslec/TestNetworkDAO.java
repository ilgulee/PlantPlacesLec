package ilgulee.com.plantplaceslec;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class TestNetworkDAO {
    private NetworkDAO mNetworkDAO;

    @Before
    public void setUp(){
        mNetworkDAO = new NetworkDAO();
    }

    @Test
    public void fetchShouldSuceedWhenGivenValidURI() throws IOException {
        String result = mNetworkDAO.fetch("http://plantplaces.com/perl/mobile/viewplantsjson.pl?Combined_Name=akjdf;lajksdf");
        System.out.println(result);
        assertEquals("{\"plants\":[]}-1", result);
    }
}
