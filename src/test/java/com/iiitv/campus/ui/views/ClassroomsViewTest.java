package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.views.student.ClassroomsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ClassroomsViewTest extends BaseKaribuTest {

    @Test
    void testClassroomListLoadedState() {
        ClassroomsView view = new ClassroomsView(mockApiClient);
        UI.getCurrent().add(view);
        view.setParameter(null, null); // List mode

        // Join Classroom button
        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().toLowerCase().contains("join classroom")));
    }

    @Test
    void testClassroomDetailViewCS301State() {
        ClassroomsView view = new ClassroomsView(mockApiClient);
        UI.getCurrent().add(view);
        view.setParameter(null, "CS301"); // Detail mode for CS301

        // Verify tabs: Stream, Classwork, People
        Tabs tabs = LocatorJ._get(view, Tabs.class);
        assertNotNull(tabs);
        List<Tab> tabList = LocatorJ._find(tabs, Tab.class);
        assertEquals(3, tabList.size());

        // Switch to Classwork tab
        tabs.setSelectedTab(tabList.get(1));
        assertNotNull(view);

        // Switch to People tab
        tabs.setSelectedTab(tabList.get(2));
        assertNotNull(view);
    }

    @Test
    void testClassroomEmptyState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        when(emptyApi.getClassrooms()).thenReturn(Collections.emptyList());

        ClassroomsView view = new ClassroomsView(emptyApi);
        UI.getCurrent().add(view);
        view.setParameter(null, null);

        List<EmptyState> emptyStates = LocatorJ._find(view, EmptyState.class);
        assertFalse(emptyStates.isEmpty());
        assertTrue(emptyStates.getFirst().getHeading().contains("No Enrolled Classrooms"));
    }
}
