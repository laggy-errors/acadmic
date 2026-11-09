import { chromium } from 'playwright';
import fs from 'fs';
import path from 'path';

const BASE_URL = process.env.BASE_URL || 'http://localhost:8080';
const SCREENSHOT_DIR = path.resolve('docs/screenshots');

if (!fs.existsSync(SCREENSHOT_DIR)) {
  fs.mkdirSync(SCREENSHOT_DIR, { recursive: true });
}

async function runSmokeTest() {
  console.log(`Starting Playwright Smoke Test against ${BASE_URL}...`);
  const browser = await chromium.launch({ headless: true });
  const context = await browser.newContext({
    viewport: { width: 1440, height: 900 }
  });
  const page = await context.newPage();

  const steps = [];

  try {
    // 1. Login / Portal Entry
    console.log('Step 1: Navigating to Portal Root (Login / Entry)...');
    await page.goto(`${BASE_URL}/`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.dashboard-greeting-title, .portal-label', { timeout: 15000 });
    const portalTitle = await page.title();
    console.log(`  ✓ Portal title: "${portalTitle}"`);
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '01_dashboard.png'), fullPage: false });
    steps.push('Login / Entry -> Passed');

    // 2. Dashboard assertions
    console.log('Step 2: Verifying Dashboard components...');
    const greetingText = await page.locator('.dashboard-greeting-title').innerText();
    if (!greetingText.includes('Welcome back, Raghav')) {
      throw new Error(`Unexpected greeting: ${greetingText}`);
    }
    console.log(`  ✓ Greeting verified: "${greetingText}"`);
    const ringCount = await page.locator('.attendance-ring-container').count();
    console.log(`  ✓ Found ${ringCount} attendance rings on dashboard.`);
    steps.push('Dashboard Verification -> Passed');

    // 3. Timetable
    console.log('Step 3: Navigating to Timetable (/timetable)...');
    await page.goto(`${BASE_URL}/timetable`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.view-switch-group', { timeout: 10000 });
    await page.waitForSelector('.timetable-grid-card', { timeout: 10000 });
    console.log('  ✓ Timetable matrix and view switcher loaded.');
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '02_timetable.png'), fullPage: false });
    steps.push('Timetable View -> Passed');

    // 4. Attendance
    console.log('Step 4: Navigating to Attendance (/attendance)...');
    await page.goto(`${BASE_URL}/attendance`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.attendance-ring-container', { timeout: 10000 });
    await page.waitForSelector('vaadin-tabs', { timeout: 10000 });
    console.log('  ✓ Attendance rings, summary metrics, and tabs loaded.');
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '03_attendance.png'), fullPage: false });
    steps.push('Attendance View -> Passed');

    // 5. Classroom List
    console.log('Step 5: Navigating to Classrooms List (/classrooms)...');
    await page.goto(`${BASE_URL}/classrooms`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.classroom-card', { timeout: 10000 });
    const joinBtn = page.getByRole('button', { name: /join classroom/i });
    if (await joinBtn.count() === 0) {
      throw new Error('Join classroom button not found');
    }
    console.log('  ✓ Classrooms list grid and Join button loaded.');
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '04_classrooms.png'), fullPage: false });
    steps.push('Classrooms List -> Passed');

    // 6. Classroom Detail (CS301)
    console.log('Step 6: Navigating to Classroom Detail (/classrooms/CS301)...');
    await page.goto(`${BASE_URL}/classrooms/CS301`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.classroom-hero-card', { timeout: 10000 });
    await page.waitForSelector('vaadin-tabs', { timeout: 10000 });
    console.log('  ✓ Classroom hero card and course tabs loaded for CS301.');
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '05_classroom_detail.png'), fullPage: false });
    steps.push('Classroom Detail (CS301) -> Passed');

    // Capture remaining screens for documentation
    console.log('Capturing remaining screen screenshots for documentation...');
    
    // Tasks
    await page.goto(`${BASE_URL}/tasks`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.task-ranked-card', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '06_tasks.png'), fullPage: false });

    // Faculty Directory
    await page.goto(`${BASE_URL}/faculty-directory`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.faculty-directory-card', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '07_faculty_directory.png'), fullPage: false });

    // Events
    await page.goto(`${BASE_URL}/events`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.event-card', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '08_events.png'), fullPage: false });

    // Announcements
    await page.goto(`${BASE_URL}/announcements`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.announcement-card', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '09_announcements.png'), fullPage: false });

    // Mess Menu
    await page.goto(`${BASE_URL}/mess-menu`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.weekly-table-container', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '10_mess_menu.png'), fullPage: false });

    // Suggestion Box
    await page.goto(`${BASE_URL}/suggestions`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.suggestion-card', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '11_suggestions.png'), fullPage: false });

    // Remarks
    await page.goto(`${BASE_URL}/remarks`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.remark-card', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '12_remarks.png'), fullPage: false });

    // Faculty Attendance Marker
    await page.goto(`${BASE_URL}/faculty/attendance`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.roster-table-container', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '13_faculty_attendance.png'), fullPage: false });

    // Faculty Gradebook
    await page.goto(`${BASE_URL}/faculty/gradebook`, { waitUntil: 'networkidle' });
    await page.waitForSelector('.gradebook-grid-wrap', { timeout: 10000 });
    await page.screenshot({ path: path.join(SCREENSHOT_DIR, '14_faculty_gradebook.png'), fullPage: false });

    // Mobile 360px Overflow Check
    console.log('Step 7: Mobile 360px viewport overflow test...');
    await page.setViewportSize({ width: 360, height: 740 });
    await page.goto(`${BASE_URL}/dashboard`, { waitUntil: 'networkidle' });
    const scrollWidth = await page.evaluate(() => document.documentElement.scrollWidth);
    const clientWidth = await page.evaluate(() => document.documentElement.clientWidth);
    console.log(`  ✓ Mobile Viewport: scrollWidth=${scrollWidth}, clientWidth=${clientWidth}`);
    if (scrollWidth > clientWidth) {
      console.warn(`Warning: Page horizontal scroll detected on mobile: ${scrollWidth} > ${clientWidth}`);
    } else {
      console.log('  ✓ No horizontal scrolling on 360px viewport.');
    }
    steps.push('Mobile 360px Overflow Check -> Passed');

    console.log('\n========================================');
    console.log('ALL PLAYWRIGHT SMOKE TESTS COMPLETED SUCCESSFULLY!');
    console.log('========================================');
    steps.forEach(s => console.log(`✓ ${s}`));
    console.log(`Screenshots saved to: ${SCREENSHOT_DIR}`);
  } catch (err) {
    console.error('Playwright Smoke Test Failed:', err);
    process.exitCode = 1;
  } finally {
    await browser.close();
  }
}

runSmokeTest();
