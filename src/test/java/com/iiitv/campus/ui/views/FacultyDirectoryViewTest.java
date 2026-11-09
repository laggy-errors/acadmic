package com.iiitv.campus.ui.views;

import com.github.mvysny.kaributesting.v10.LocatorJ;
import com.iiitv.campus.ui.views.student.FacultyDirectoryView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FacultyDirectoryViewTest extends BaseKaribuTest {

    @Test
    void testFacultyDirectoryLoadedState() {
        FacultyDirectoryView view = new FacultyDirectoryView(mockApiClient);
        UI.getCurrent().add(view);
        view.setParameter(null, null);

        // Verify department filter buttons
        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("All Departments")));
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Computer Science")));
    }

    @Test
    void testFacultyProfileState() {
        FacultyDirectoryView view = new FacultyDirectoryView(mockApiClient);
        UI.getCurrent().add(view);
        view.setParameter(null, "FAC001"); // Dr. A. Mehta profile

        List<Button> buttons = LocatorJ._find(view, Button.class);
        assertTrue(buttons.stream().anyMatch(b -> b.getText().contains("Back to Faculty Directory")));
    }
}
