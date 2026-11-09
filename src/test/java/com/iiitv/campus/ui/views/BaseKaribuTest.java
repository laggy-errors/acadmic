package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.iiitv.campus.client.mock.MockApiClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class BaseKaribuTest {

    protected MockApiClient mockApiClient;
    private static final Routes routes = new Routes().autoDiscoverViews("com.iiitv.campus.ui.views");

    @BeforeEach
    public void setupVaadin() {
        MockVaadin.setup(routes);
        mockApiClient = new MockApiClient();
    }

    @AfterEach
    public void tearDownVaadin() {
        MockVaadin.tearDown();
    }
}
