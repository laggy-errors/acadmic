package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.views.student.EventsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class EventsViewTest extends BaseKaribuTest {

    @Test
    void testEventsLoadedState() {
        EventsView view = new EventsView(mockApiClient);
        UI.getCurrent().add(view);

        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("All Events")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Today")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("This Week")));

        // Verify RSVP button exists
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("RSVP")));
    }

    @Test
    void testEventsEmptyState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        when(emptyApi.getUpcomingEvents()).thenReturn(Collections.emptyList());

        EventsView view = new EventsView(emptyApi);
        UI.getCurrent().add(view);

        List<com.vaadin.flow.component.html.Div> emptyCards = LocatorJ._find(view, com.vaadin.flow.component.html.Div.class, spec -> spec.withClasses("empty-state-card"));
        assertFalse(emptyCards.isEmpty());
    }
}
