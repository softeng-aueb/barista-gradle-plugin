package gr.aueb.android.barista.core.context;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class FuzzGPSModelTest {

    @Test
    public void getGPSCommand() {
        FuzzGPSModel model = new FuzzGPSModel(null);

        Assert.assertNotNull(model.next(1));
    }
}