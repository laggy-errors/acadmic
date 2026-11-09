package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.MealMenu;
import com.iiitv.campus.ui.views.student.MessMenuView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class MessMenuViewTest extends BaseKaribuTest {

    @Test
    void testMessMenuLoadedState() {
        MessMenuView view = new MessMenuView(mockApiClient);
        UI.getCurrent().add(view);

        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Rate Today's Meal")));
    }

    @Test
    void testMessMenuEmptyState() {
        ApiClient emptyApi = Mockito.mock(ApiClient.class);
        when(emptyApi.getTodayMealMenu()).thenReturn(new MealMenu("Sunday", "Special", true, "Special Dal, Paneer Butter Masala, Roti, Rice", "Dinner at 20:00"));
        when(emptyApi.getWeeklyMealMenu()).thenReturn(Collections.emptyList());

        MessMenuView view = new MessMenuView(emptyApi);
        UI.getCurrent().add(view);
        assertNotNull(view);
    }

    @Test
    void testMessMenuViewModesAndDayPicker() {
        MessMenuView view = new MessMenuView(mockApiClient);
        UI.getCurrent().add(view);

        List<Button> buttons = LocatorJ._find(view, Button.class);

        // Assert View Mode buttons exist
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Combined")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Today's Detailed Meals")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("One-Week Full Schedule")));

        // Click Today's Detailed Meals
        Button todayOnlyBtn = buttons.stream().filter(b -> b.getText().contains("Today's Detailed Meals")).findFirst().orElseThrow();
        LocatorJ._click(todayOnlyBtn);
        assertNotNull(view);

        // Click One-Week Full Schedule
        Button weekOnlyBtn = buttons.stream().filter(b -> b.getText().contains("One-Week Full Schedule")).findFirst().orElseThrow();
        LocatorJ._click(weekOnlyBtn);
        assertNotNull(view);

        // Click Combined
        Button combinedBtn = buttons.stream().filter(b -> b.getText().contains("Combined")).findFirst().orElseThrow();
        LocatorJ._click(combinedBtn);
        assertNotNull(view);

        // Test day picker: Click Mon
        Button monBtn = buttons.stream().filter(b -> b.getText().startsWith("Mon")).findFirst().orElseThrow();
        LocatorJ._click(monBtn);
        assertNotNull(view);
    }
}
