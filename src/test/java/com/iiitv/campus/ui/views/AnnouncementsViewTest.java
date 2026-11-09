package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.views.student.AnnouncementsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AnnouncementsViewTest extends BaseKaribuTest {

    @Test
    void testAnnouncementsLoadedState() {
        AnnouncementsView view = new AnnouncementsView(mockApiClient);
        UI.getCurrent().add(view);

        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Mark All Read")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("All")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Academic")));
    }

    @Test
    void testAnnouncementsEmptyState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        when(emptyApi.getAnnouncements()).thenReturn(Collections.emptyList());

        AnnouncementsView view = new AnnouncementsView(emptyApi);
        UI.getCurrent().add(view);

        List<com.vaadin.flow.component.html.Div> emptyCards = LocatorJ._find(view, com.vaadin.flow.component.html.Div.class, spec -> spec.withClasses("empty-state-card"));
        assertFalse(emptyCards.isEmpty());
    }
}
