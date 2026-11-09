package com.iiitv.campus.ui.views.student;

import com.iiitv.campus.client.ApiClient;
import com.iiitv.campus.dto.MealMenu;
import com.iiitv.campus.dto.MealRating;
import com.iiitv.campus.dto.WeeklyMealDay;
import com.iiitv.campus.security.RequiresRole;
import com.iiitv.campus.security.Role;
import com.iiitv.campus.ui.components.EmptyState;
import com.iiitv.campus.ui.components.PageHeader;
import com.iiitv.campus.ui.components.StatusChip;
import com.iiitv.campus.ui.layout.MainLayout;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Route(value = "mess-menu", layout = MainLayout.class)
@PageTitle("Mess Menu - IIIT Vadodara Campus Portal")
@RequiresRole({Role.STUDENT, Role.FACULTY, Role.ADMIN})
public class MessMenuView extends VerticalLayout {

    private final ApiClient apiClient;

    @Autowired
    public MessMenuView(ApiClient apiClient) {
        this.apiClient = apiClient;

        addClassNames("view-container", "neo-mess-view");
        setSizeFull();
        setSpacing(true);
        setPadding(false);

        MealMenu todayMenu = apiClient.getTodayMealMenu();

        // Top Action: Rate Today's Meal
        Button rateMealHeaderBtn = new Button("Rate Today's Meal");
        rateMealHeaderBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");
        rateMealHeaderBtn.addClickListener(e -> openMealRatingDialog("Lunch"));

        PageHeader header = new PageHeader(
                "Mess Menu",
                "Daily dining schedule, weekly rotation, and student meal feedback",
                rateMealHeaderBtn
        );
        header.addClassName("neo-mess-header");

        // Metrics Strip - Glass panel with 4 stat blocks
        Div metricsStrip = new Div();
        metricsStrip.addClassNames("mess-metrics-strip", "neo-mess-metrics-strip", "neo-glass-panel");

        metricsStrip.add(
                createMetric("Current Meal", "Lunch (12:15 – 14:00)", "stat-info"),
                createMetric("Next Service", todayMenu.nextMealTime(), "stat-info"),
                createMetric("Student Rating", "4.2 / 5.0 (148 reviews)", "stat-gold"),
                createMetric("Hygiene Audit", "Grade A (SAFE)", "stat-safe")
        );

        // Section: View Mode Switcher (Combined / Today Only / Week Matrix)
        HorizontalLayout viewModeBar = new HorizontalLayout();
        viewModeBar.addClassNames("tab-group", "mess-view-mode-bar", "neo-dept-filter-bar", "neo-mess-view-mode-bar");
        viewModeBar.setSpacing(true);

        Button btnCombined = new Button("Combined (Today & One-Week)");
        btnCombined.addClassNames("btn-tab", "neo-dept-pill", "tab-active", "neo-dept-pill-active", "font-ui");

        Button btnTodayOnly = new Button("Today's Detailed Meals");
        btnTodayOnly.addClassNames("btn-tab", "neo-dept-pill", "font-ui");

        Button btnWeekOnly = new Button("One-Week Full Schedule");
        btnWeekOnly.addClassNames("btn-tab", "neo-dept-pill", "font-ui");

        viewModeBar.add(btnCombined, btnTodayOnly, btnWeekOnly);

        // Section 1: Today's / Inspected Day's Strip
        Div todaySectionWrapper = new Div();
        todaySectionWrapper.setWidthFull();
        todaySectionWrapper.addClassName("neo-today-section-wrapper");

        H3 todayTitle = new H3("Today's Dining Schedule (" + todayMenu.date() + ")");
        todayTitle.addClassNames("font-ui", "text-navy", "margin-none", "neo-section-title");

        // Day Quick-Picker Bar for One-Week Inspection
        HorizontalLayout dayPickerBar = new HorizontalLayout();
        dayPickerBar.addClassNames("mess-day-picker-bar", "neo-dept-filter-bar", "neo-mess-day-bar");
        dayPickerBar.setSpacing(true);

        Span inspectLabel = new Span("Inspect Day:");
        inspectLabel.addClassNames("font-ui", "text-muted", "neo-inspect-label");
        dayPickerBar.add(inspectLabel);

        Div todayStrip = new Div();
        todayStrip.addClassNames("today-meal-strip", "neo-today-meal-strip");

        List<WeeklyMealDay> week2Days = apiClient.getWeeklyMealMenu("week 2");
        if (week2Days == null) {
            week2Days = Collections.emptyList();
        }
        WeeklyMealDay activeTuesday = week2Days.stream()
                .filter(WeeklyMealDay::isToday)
                .findFirst()
                .orElse(week2Days.isEmpty() ? null : week2Days.getFirst());

        if (activeTuesday != null) {
            populateMealStrip(todayStrip, activeTuesday, true);
        } else {
            todayStrip.add(new EmptyState("Menu Not Published", "No meal schedule currently available for this day."));
        }

        List<Button> dayButtons = new ArrayList<>();
        for (WeeklyMealDay d : week2Days) {
            String label = d.isToday() ? d.day().substring(0, 3) + " (Today)" : d.day().substring(0, 3);
            Button dayBtn = new Button(label);
            dayBtn.addClassNames("btn-tab", "mess-day-picker-btn", "neo-dept-pill", "font-ui");
            if (d.isToday()) {
                dayBtn.addClassNames("tab-active", "neo-dept-pill-active");
            }
            dayBtn.addClickListener(e -> {
                dayButtons.forEach(b -> {
                    b.removeClassName("tab-active");
                    b.removeClassName("neo-dept-pill-active");
                });
                dayBtn.addClassNames("tab-active", "neo-dept-pill-active");
                if (d.isToday()) {
                    todayTitle.setText("Today's Dining Schedule (" + d.day() + ", " + d.date() + ")");
                } else {
                    todayTitle.setText("Dining Schedule · " + d.day() + " (" + d.date() + ")");
                }
                populateMealStrip(todayStrip, d, d.isToday());
            });
            dayButtons.add(dayBtn);
            dayPickerBar.add(dayBtn);
        }

        todaySectionWrapper.add(todayTitle, dayPickerBar, todayStrip);

        // Section 2: Weekly Schedule Table Header + Week Switcher
        Div weekSectionWrapper = new Div();
        weekSectionWrapper.setWidthFull();
        weekSectionWrapper.addClassName("neo-week-section-wrapper");

        HorizontalLayout weekHeaderRow = new HorizontalLayout();
        weekHeaderRow.setWidthFull();
        weekHeaderRow.setAlignItems(FlexComponent.Alignment.CENTER);
        weekHeaderRow.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        H3 weeklyTitle = new H3("Weekly Dining Schedule · Week 2 (09–15 Nov 2026)");
        weeklyTitle.addClassNames("font-ui", "text-navy", "margin-none", "neo-section-title");

        HorizontalLayout weekFilterBar = new HorizontalLayout();
        weekFilterBar.setSpacing(true);
        weekFilterBar.addClassNames("tab-group", "neo-dept-filter-bar", "neo-mess-week-bar");

        Button btnWeek2 = new Button("Week 2 (09–15 Nov 2026)");
        btnWeek2.addClassNames("btn-tab", "neo-dept-pill", "tab-active", "neo-dept-pill-active", "font-ui");

        Button btnWeek1 = new Button("Week 1 (02–08 Nov 2026)");
        btnWeek1.addClassNames("btn-tab", "neo-dept-pill", "font-ui");

        Div tableContainer = new Div();
        tableContainer.addClassNames("weekly-table-container", "neo-glass-card", "neo-weekly-table-card");
        renderWeeklyTableContent(tableContainer, apiClient.getWeeklyMealMenu("week 2"));

        btnWeek2.addClickListener(e -> {
            btnWeek2.addClassNames("tab-active", "neo-dept-pill-active");
            btnWeek1.removeClassName("tab-active");
            btnWeek1.removeClassName("neo-dept-pill-active");
            weeklyTitle.setText("Weekly Dining Schedule · Week 2 (09–15 Nov 2026)");
            renderWeeklyTableContent(tableContainer, apiClient.getWeeklyMealMenu("week 2"));
        });

        btnWeek1.addClickListener(e -> {
            btnWeek1.addClassNames("tab-active", "neo-dept-pill-active");
            btnWeek2.removeClassName("tab-active");
            btnWeek2.removeClassName("neo-dept-pill-active");
            weeklyTitle.setText("Weekly Dining Schedule · Week 1 (02–08 Nov 2026)");
            renderWeeklyTableContent(tableContainer, apiClient.getWeeklyMealMenu("week 1"));
        });

        weekFilterBar.add(btnWeek2, btnWeek1);
        weekHeaderRow.add(weeklyTitle, weekFilterBar);
        weekSectionWrapper.add(weekHeaderRow, tableContainer);

        // View Mode Listeners
        btnCombined.addClickListener(e -> {
            btnCombined.addClassNames("tab-active", "neo-dept-pill-active");
            btnTodayOnly.removeClassName("tab-active");
            btnTodayOnly.removeClassName("neo-dept-pill-active");
            btnWeekOnly.removeClassName("tab-active");
            btnWeekOnly.removeClassName("neo-dept-pill-active");
            todaySectionWrapper.setVisible(true);
            weekSectionWrapper.setVisible(true);
        });

        btnTodayOnly.addClickListener(e -> {
            btnTodayOnly.addClassNames("tab-active", "neo-dept-pill-active");
            btnCombined.removeClassName("tab-active");
            btnCombined.removeClassName("neo-dept-pill-active");
            btnWeekOnly.removeClassName("tab-active");
            btnWeekOnly.removeClassName("neo-dept-pill-active");
            todaySectionWrapper.setVisible(true);
            weekSectionWrapper.setVisible(false);
        });

        btnWeekOnly.addClickListener(e -> {
            btnWeekOnly.addClassNames("tab-active", "neo-dept-pill-active");
            btnCombined.removeClassName("tab-active");
            btnCombined.removeClassName("neo-dept-pill-active");
            btnTodayOnly.removeClassName("tab-active");
            btnTodayOnly.removeClassName("neo-dept-pill-active");
            todaySectionWrapper.setVisible(false);
            weekSectionWrapper.setVisible(true);
        });

        // Section 3: Dining Hall Policies
        Div guidelinesCard = new Div();
        guidelinesCard.addClassNames("campus-card", "stream-card", "neo-glass-panel", "neo-mess-guidelines");

        H3 guideTitle = new H3("Dining Hall Guidelines & Committee Operations");
        guideTitle.addClassNames("font-ui", "text-navy", "margin-none", "neo-card-title");

        Paragraph rule1 = new Paragraph("• Access to the Central Dining Hall is authenticated via student RFID smart cards at entry turnstiles.");
        rule1.addClassNames("font-ui", "text-main", "neo-guidelines-text");

        Paragraph rule2 = new Paragraph("• Packed meals for sports teams and students representing the institute in official competitions must be requested 24 hours in advance via the Hall Warden.");
        rule2.addClassNames("font-ui", "text-main", "neo-guidelines-text");

        Paragraph rule3 = new Paragraph("• Routine food quality inspections and hygiene reviews are carried out bi-weekly by the student Mess Committee.");
        rule3.addClassNames("font-ui", "text-muted", "neo-guidelines-text");

        guidelinesCard.add(guideTitle, rule1, rule2, rule3);

        add(header, metricsStrip, viewModeBar, todaySectionWrapper, weekSectionWrapper, guidelinesCard);
    }

    private void populateMealStrip(Div todayStrip, WeeklyMealDay day, boolean isToday) {
        todayStrip.removeAll();
        todayStrip.add(
                buildTodayMealCard("Breakfast", "07:30 – 09:30", day.breakfast(), false),
                buildTodayMealCard("Lunch", "12:15 – 14:00", day.lunch(), isToday),
                buildTodayMealCard("Evening Snacks", "16:30 – 17:30", day.snacks(), false),
                buildTodayMealCard("Dinner", "19:30 – 21:30", day.dinner(), false)
        );
    }

    private Div createMetric(String label, String value, String colorClass) {
        Div cell = new Div();
        cell.addClassNames("mess-metric-item", "neo-remarks-stat-block", "neo-mess-stat-block");

        Span l = new Span(label);
        l.addClassNames("mess-metric-label", "neo-remarks-stat-label");

        Span v = new Span(value);
        v.addClassNames("mess-metric-val", "neo-remarks-stat-val", "font-mono", colorClass);

        cell.add(l, v);
        return cell;
    }

    private Div buildTodayMealCard(String mealName, String timing, String items, boolean isNow) {
        Div card = new Div();
        card.addClassNames("today-meal-card", "neo-glass-card", "neo-meal-card");
        if (isNow) {
            card.addClassNames("is-now", "neo-meal-now");
        }

        Div top = new Div();
        top.addClassNames("today-meal-top", "neo-meal-card-top");

        H4 title = new H4(mealName);
        title.addClassNames("today-meal-title", "neo-meal-title", "font-ui");

        top.add(title);

        if (isNow) {
            Span nowChip = new Span("NOW");
            nowChip.addClassNames("chip", "neo-chip-gold", "font-mono");
            top.add(nowChip);
        }

        Span timeSpan = new Span(timing);
        timeSpan.addClassNames("today-meal-time", "neo-meal-time", "font-mono");

        Paragraph itemsPara = new Paragraph(items);
        itemsPara.addClassNames("today-meal-items", "neo-meal-items", "font-ui");

        Button rateBtn = new Button("Rate " + mealName);
        rateBtn.addClassNames("btn-secondary", "neo-btn-outline", "font-ui", "neo-meal-rate-btn");
        rateBtn.addClickListener(e -> openMealRatingDialog(mealName));

        card.add(top, timeSpan, itemsPara, rateBtn);
        return card;
    }

    private void renderWeeklyTableContent(Div container, List<WeeklyMealDay> weekDays) {
        container.removeAll();
        Html table = new Html("""
            <div class="neo-table-scroll-wrap">
            <table class="weekly-menu-table neo-weekly-menu-table">
                <thead>
                    <tr>
                        <th>Day & Date</th>
                        <th>Breakfast (07:30–09:30)</th>
                        <th>Lunch (12:15–14:00)</th>
                        <th>Snacks (16:30–17:30)</th>
                        <th>Dinner (19:30–21:30)</th>
                        <th>Special Item</th>
                    </tr>
                </thead>
                <tbody>
                """ + buildTableRowsHtml(weekDays) + """
                </tbody>
            </table>
            </div>
            """);

        container.add(table);
    }

    private String buildTableRowsHtml(List<WeeklyMealDay> weekDays) {
        StringBuilder sb = new StringBuilder();
        for (WeeklyMealDay d : weekDays) {
            String rowClass = d.isToday() ? " class=\"is-today-row neo-today-row\"" : "";
            String todayTag = d.isToday() ? " <span class=\"chip font-mono neo-chip-today\">TODAY</span>" : "";

            sb.append("<tr").append(rowClass).append(">");
            sb.append("<td><strong>").append(d.day()).append("</strong><br><span class=\"font-mono text-muted neo-table-date\">").append(d.date()).append("</span>").append(todayTag).append("</td>");
            sb.append("<td>").append(d.breakfast()).append("</td>");
            sb.append("<td>").append(d.lunch()).append("</td>");
            sb.append("<td>").append(d.snacks()).append("</td>");
            sb.append("<td>").append(d.dinner()).append("</td>");
            sb.append("<td><span class=\"special-item-chip neo-chip-special font-mono\">").append(d.specialItem()).append("</span></td>");
            sb.append("</tr>");
        }
        return sb.toString();
    }

    /* =========================================================================
       RATE A MEAL DIALOG
       ========================================================================= */

    private void openMealRatingDialog(String defaultMeal) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Rate a Meal");
        dialog.addClassNames("neo-glass-dialog", "neo-mess-rating-modal");

        VerticalLayout form = new VerticalLayout();
        form.setPadding(false);
        form.setSpacing(true);
        form.setWidth("420px");

        Paragraph desc = new Paragraph("Your anonymous ratings are reviewed directly by the Student Mess Committee and the catering manager.");
        desc.addClassNames("font-ui", "text-muted");

        ComboBox<String> mealSelect = new ComboBox<>("Select Meal");
        mealSelect.setItems("Breakfast", "Lunch", "Evening Snacks", "Dinner");
        mealSelect.setValue(defaultMeal != null ? defaultMeal : "Lunch");
        mealSelect.setWidthFull();

        ComboBox<String> ratingSelect = new ComboBox<>("Overall Rating");
        ratingSelect.setItems(
                "5 - Excellent",
                "4 - Very Good",
                "3 - Good",
                "2 - Fair",
                "1 - Needs Improvement"
        );
        ratingSelect.setValue("4 - Very Good");
        ratingSelect.setWidthFull();

        ComboBox<String> aspectSelect = new ComboBox<>("Primary Aspect");
        aspectSelect.setItems("Taste & Seasoning", "Hygiene & Cleanliness", "Serving Temperature", "Quantity & Refills", "Prompt Service");
        aspectSelect.setValue("Taste & Seasoning");
        aspectSelect.setWidthFull();

        TextArea commentsArea = new TextArea("Feedback & Suggestions");
        commentsArea.setPlaceholder("Share specific remarks on salt, spiciness, freshness, or items you liked...");
        commentsArea.setWidthFull();
        commentsArea.setMinHeight("90px");

        form.add(desc, mealSelect, ratingSelect, aspectSelect, commentsArea);
        dialog.add(form);

        Button submitBtn = new Button("Submit Review", e -> {
            int score = 4;
            String sel = ratingSelect.getValue();
            if (sel != null && !sel.isEmpty()) {
                score = Character.getNumericValue(sel.charAt(0));
            }

            MealRating rating = new MealRating(
                    mealSelect.getValue(),
                    "10 Nov 2026",
                    score,
                    aspectSelect.getValue(),
                    commentsArea.getValue()
            );

            apiClient.submitMealRating(rating);
            Notification.show("Review submitted for " + mealSelect.getValue() + ". Thank you!", 3000, Notification.Position.BOTTOM_END);
            dialog.close();
        });
        submitBtn.addClassNames("btn-primary", "neo-btn-maroon", "font-ui");

        Button cancelBtn = new Button("Cancel", e -> dialog.close());
        cancelBtn.addClassNames("btn-secondary", "neo-btn-outline", "font-ui");

        HorizontalLayout footer = new HorizontalLayout(cancelBtn, submitBtn);
        footer.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        footer.setWidthFull();

        dialog.getFooter().add(footer);
        dialog.open();
    }
}
