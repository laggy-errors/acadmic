package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.views.student.TasksView;
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

class TasksViewTest extends BaseKaribuTest {

    @Test
    void testTasksLoadedState() {
        TasksView view = new TasksView(mockApiClient);
        UI.getCurrent().add(view);

        // Verify tabs: All, Pending, Completed
        Tabs tabs = LocatorJ._get(view, Tabs.class);
        assertNotNull(tabs);
        List<Tab> tabList = LocatorJ._find(tabs, Tab.class);
        assertEquals(3, tabList.size());

        // Verify Quick-add button
        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Add Task")));
    }

    @Test
    void testTasksEmptyState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        when(emptyApi.getPendingTasks()).thenReturn(Collections.emptyList());

        TasksView view = new TasksView(emptyApi);
        UI.getCurrent().add(view);

        List<com.vaadin.flow.component.html.Div> emptyBoxes = LocatorJ._find(view, com.vaadin.flow.component.html.Div.class, spec -> spec.withClasses("empty-state-box"));
        assertFalse(emptyBoxes.isEmpty());
    }
}
