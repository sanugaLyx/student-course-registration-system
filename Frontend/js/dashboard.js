/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/dashboard.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Fetches and displays overview statistics for the main dashboard.
 *          Serves both STUDENTS and LECTURERS (hiding/showing relevant sections).
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
requireLogin(); // [L1] Security Guard: Must be logged in
const user = getCurrentUser(); // [L1] Get user from session
const role = getCurrentRole(); // [L1] Get role from session

// [L2] Deans have a completely separate, more complex dashboard page
if (role === 'DEAN') { window.location.href = 'dean-dashboard.html'; } // [L1] Redirect Deans

/*
 * [L2] Main Dashboard Loader
 * Fetches necessary data from the backend to populate the dashboard cards.
 */
async function loadDashboard() {
  
  // ── Lecturer View ──────────────────────────────────────────
  if (role === 'LECTURER') {
    document.getElementById('student-dashboard').style.display = 'none'; // [L1] Hide student sections
    document.getElementById('lecturer-dashboard').style.display = ''; // [L1] Show lecturer sections
    
    const myId = user.lecturerId; // [L1] Get lecturer ID
    const courses = await apiFetch('/courses') || []; // [L1] Fetch all courses
    const myC = courses.filter(c => c.lecturerId === myId); // [L1] Filter courses taught by this lecturer
    
    // [L1] Update UI stats
    document.getElementById('lec-course-count').textContent = myC.length; 
    document.getElementById('lec-max-courses').textContent = user.type === 'FT' ? 5 : 2; // [L1] Reflect polymorphism (FT=5, PT=2)
    return; // [L1] Exit early, student code below won't run
  }

  // ── Student View ───────────────────────────────────────────
  document.getElementById('student-dashboard').style.display = ''; // [L1] Show student sections
  document.getElementById('lecturer-dashboard').style.display = 'none'; // [L1] Hide lecturer sections
  
  const myId = user.studentId; // [L1] Get student ID
  
  // [L2] Fetch enrollments and courses simultaneously for performance
  const [enrollments, courses] = await Promise.all([
    apiFetch(`/enrollments/student/${myId}`) || Promise.resolve([]), // [L1] Get personal enrollments
    apiFetch('/courses') || Promise.resolve([]) // [L1] Get full course catalog (needed to show course names)
  ]);
  
  // [L1] Filter enrollment statuses
  const activeE = enrollments.filter(e => e.status === 'ACTIVE');
  const completedE = enrollments.filter(e => e.status === 'COMPLETED');
  
  // [L1] Update UI summary cards
  document.getElementById('dash-enrollment-count').textContent = activeE.length;
  document.getElementById('dash-completed').textContent = completedE.length;
  document.getElementById('dash-limit').textContent = user.type === 'PG' ? 4 : 6; // [L1] Reflect polymorphism (PG=4, UG=6)

  // [L2] Render recent courses list
  const list = document.getElementById('dash-courses-list');
  if (activeE.length === 0) {
    // [L1] Show empty state if no active courses
    list.innerHTML = '<div class="empty-state" style="padding:24px"><span class="material-icons-round">school</span><p>No active enrollments</p></div>';
  } else {
    // [L1] Map active enrollments to HTML UI components
    list.innerHTML = activeE.map(e => {
      const c = courses.find(x => x.courseId === e.courseId) || {}; // [L1] Lookup course metadata (name, credits)
      return `<div style="display:flex;align-items:center;gap:12px;padding:12px;background:var(--col-50);border-radius:var(--radius-md);margin-bottom:8px">
        <div style="width:40px;height:40px;background:var(--col-100);border-radius:var(--radius-md);display:flex;align-items:center;justify-content:center">
          <span class="material-icons-round" style="color:var(--col-700);font-size:18px">menu_book</span>
        </div>
        <div>
          <div style="font-weight:700;font-size:.875rem;color:var(--col-900)">${c.courseName || e.courseId}</div>
          <div style="font-size:.75rem;color:var(--col-500)">${e.courseId} · ${c.credits || 3} credits</div>
        </div>
        <span class="badge badge-active" style="margin-left:auto">Active</span>
      </div>`;
    }).join(''); // [L1] Join HTML strings and inject into DOM
  }
}

loadDashboard(); // [L1] Execute on load
