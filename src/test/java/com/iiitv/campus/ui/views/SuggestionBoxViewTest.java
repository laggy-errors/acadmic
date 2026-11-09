package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.views.student.SuggestionBoxView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SuggestionBoxViewTest extends BaseKaribuTest {

    @Test
    void testSuggestionBoxLoadedState() {
        SuggestionBoxView view = new SuggestionBoxView(mockApiClient);
        UI.getCurrent().add(view);

        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("New Suggestion")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("All Proposals")));
    }

    @Test
    void testSuggestionBoxEmptyState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        when(emptyApi.getSuggestions()).thenReturn(Collections.emptyList());

        SuggestionBoxView view = new SuggestionBoxView(emptyApi);
        UI.getCurrent().add(view);

        List<com.vaadin.flow.component.html.Div> emptyCards = LocatorJ._find(view, com.vaadin.flow.component.html.Div.class, spec -> spec.withClasses("empty-state-card"));
        assertFalse(emptyCards.isEmpty());
    }
}
