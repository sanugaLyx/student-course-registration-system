/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/dean-dashboard.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Handles the Admin (Dean) specific dashboard. Fetches massive
 *          amounts of data across the system to provide a high-level
 *          overview of system health and metrics.
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
requireLogin(); // [L1] Guard: Must be logged in
requireRole(['DEAN']); // [L1] Guard: Must explicitly be a DEAN

/*
 * [L2] Main Loader for Dean Dashboard
 * Fetches almost every major entity in the system to calculate totals.
 * This is an expensive operation but acceptable for an admin overview.
 */
async function loadDeanDashboard() {
  // [L1] Fetch data arrays sequentially (could be parallelized with Promise.all for speed)
  const students = (await apiFetch('/students')) || [];
  const lecturers = (await apiFetch('/lecturers')) || [];
  const courses = (await apiFetch('/courses')) || [];
  const enrollments = (await apiFetch('/enrollments')) || [];
  const departments = (await apiFetch('/departments')) || [];

  // [L1] Update numerical counter cards at the top of the dashboard
  document.getElementById('dean-students-count').textContent = students.length;
  document.getElementById('dean-lecturers-count').textContent = lecturers.length;
  document.getElementById('dean-courses-count').textContent = courses.length;
  document.getElementById('dean-enrollments-count').textContent = enrollments.filter(e => e.status === 'ACTIVE').length; // [L1] Only count ACTIVE enrollments

  // [L2] Render Department Tags Summary
  // Iterates over departments and creates small visual pill/chip elements.
  document.getElementById('dean-dept-summary').innerHTML = departments.map(d =>
    `<span style="display:inline-block;margin-right:8px;margin-bottom:4px;padding:4px 10px;background:var(--col-100);border-radius:var(--radius-full);font-weight:600;font-size:.75rem;color:var(--col-700)">${d.departmentName}</span>`
  ).join('') || '<span style="color:var(--col-300)">No departments found</span>'; // [L1] Fallback if empty

  // [L2] Render Recent Enrollments List
  // Takes the last 3 enrollments (slice(-3)) and displays them in a mini-list.
  document.getElementById('dean-enroll-summary').innerHTML = enrollments.slice(-3).map(e =>
    `<div style="font-size:.8125rem;padding:4px 0;border-bottom:1px solid var(--col-outline)">${e.studentId} → ${e.courseId} <span class="badge badge-${e.status.toLowerCase()}" style="margin-left:8px">${e.status}</span></div>`
  ).join('') || '<span style="color:var(--col-300)">No enrollments yet</span>'; // [L1] Fallback if empty
}

loadDeanDashboard(); // [L1] Execute on load
