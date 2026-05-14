/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/auth.js
 * 🏗  LAYER: Frontend — Global Script
 * 📋 ROLE: Global authentication logic and shared API utilities.
 *          Handles session management (login/logout/roles), dynamic
 *          sidebar rendering, and API fetch wrappers.
 *          Included on almost every HTML page.
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
const API = 'http://localhost:8080'; // [L1] Base URL for the Spring Boot backend API

/* ── Session Helpers ──────────────────────────────────────── */
// [L2] Helper to retrieve the parsed user object from sessionStorage
function getCurrentUser() {
  const raw = sessionStorage.getItem('user'); // [L1] Get raw JSON string from session
  return raw ? JSON.parse(raw) : null; // [L1] Parse to object if it exists
}

// [L2] Helper to retrieve the current user's role (STUDENT, LECTURER, DEAN)
function getCurrentRole() { return sessionStorage.getItem('role'); }

// [L2] Helper to retrieve the current user's unique ID
function getCurrentUserId() { return sessionStorage.getItem('userId'); }

/*
 * [L2] requireLogin()
 * Security/UX Guard: If the user is not logged in, immediately redirect
 * them to the login page. Called at the very top of protected JS files.
 */
function requireLogin() {
  if (!getCurrentUser()) window.location.href = 'login.html'; // [L1] Redirect if null
}

/*
 * [L2] requireRole(allowedRolesArray)
 * Security/UX Guard: If the user's role is not in the allowed list,
 * redirect them back to the dashboard to prevent unauthorized access.
 */
function requireRole(allowed) {
  const role = getCurrentRole();
  if (!allowed.includes(role)) window.location.href = 'dashboard.html'; // [L1] E.g., block Student from accessing lecturers.html
}

// [L2] Clears session completely and returns to login screen
function logout() {
  sessionStorage.clear(); // [L1] Wipe all session data
  window.location.href = 'login.html'; // [L1] Redirect
}


/* ── Utility Functions ────────────────────────────────────── */
// [L2] Generates initials (e.g., "John Doe" -> "JD") for the sidebar avatar
function getInitials(first, last) {
  return (((first||'')[0] || '') + ((last||'')[0] || '')).toUpperCase() || '?';
}

// [L2] Displays a non-blocking toast notification (success or error)
function showToast(msg, type='success') {
  let c = document.getElementById('toast-container'); // [L1] Check if container exists
  if (!c) { c = document.createElement('div'); c.id='toast-container'; document.body.appendChild(c); } // [L1] Create if not
  const t = document.createElement('div');
  t.className = `toast toast-${type}`; // [L1] Apply CSS class based on type
  const icon = type==='success'?'check_circle':'error'; // [L1] Material icon choice
  t.innerHTML = `<span class="material-icons-round">${icon}</span>${msg}`; // [L1] Set HTML
  c.appendChild(t); // [L1] Attach to DOM
  setTimeout(()=>{t.classList.add('out');setTimeout(()=>t.remove(),300)},3000); // [L1] Auto-remove after 3 seconds with fade out
}

// [L2] Shows an inline alert box (used inside modals)
function showAlert(id, msg) {
  const el = document.getElementById(id);
  const txt = document.getElementById(id+'-text');
  if (el && txt) { txt.textContent = msg; el.classList.remove('hidden'); } // [L1] Remove hidden class to display
}

// [L2] Hides an inline alert box
function hideAlert(id) {
  const el = document.getElementById(id);
  if (el) el.classList.add('hidden'); // [L1] Add hidden class to conceal
}

// [L2] Simple modal toggles using CSS 'hidden' class
function openModal(id) { document.getElementById(id).classList.remove('hidden'); }
function closeModal(id) { document.getElementById(id).classList.add('hidden'); }

/* ── API Fetch with timeout ───────────────────────────────── */
/*
 * [L2] Wrapper around the native fetch API.
 * Adds a 3-second timeout so the UI doesn't hang indefinitely if the backend is down.
 * Automatically parses JSON on success.
 */
async function apiFetch(endpoint) {
  try {
    const res = await fetch(`${API}${endpoint}`, {signal: AbortSignal.timeout(3000)}); // [L1] Fetch with 3s timeout
    if (res.ok) return await res.json(); // [L1] Parse JSON if HTTP 200 OK
    return null; // [L1] Return null on error status
  } catch { return null; } // [L1] Catch network failures/timeouts and return null
}

/* ── Sidebar Builder ──────────────────────────────────────── */
/*
 * [L2] Dynamic Sidebar Rendering
 * Builds the navigation menu on the left side of the screen.
 * Filters which links are shown based on the user's role (STUDENT, LECTURER, DEAN).
 */
function buildSidebar() {
  const user = getCurrentUser();
  const role = getCurrentRole();
  if (!user || !role) return; // [L1] Abort if not logged in

  // Update user info section at the top of the sidebar
  const sbName = document.getElementById('sb-name');
  const sbRole = document.getElementById('sb-role');
  const sbAvatar = document.getElementById('sb-avatar');
  if (sbName) sbName.textContent = `${user.firstName||''} ${user.lastName||''}`; // [L1] Set name
  if (sbRole) sbRole.textContent = role; // [L1] Set role text
  if (sbAvatar) sbAvatar.textContent = getInitials(user.firstName, user.lastName); // [L1] Set avatar initials

  // Build nav links dynamically
  const nav = document.getElementById('sidebar-nav-main');
  if (!nav) return;
  nav.innerHTML = ''; // [L1] Clear existing links

  // [L1] Array of all possible pages and the roles allowed to see them
  const items = [
    {label:'Dashboard',icon:'dashboard',href:role==='DEAN'?'dean-dashboard.html':'dashboard.html',roles:['STUDENT','LECTURER','DEAN']},
    {label:'Students',icon:'groups',href:'students.html',roles:['DEAN']},
    {label:'My Profile',icon:'account_circle',href:'student-profile.html',roles:['STUDENT']},
    {label:'Lecturers',icon:'badge',href:'lecturers.html',roles:['DEAN']},
    {label:'Courses',icon:'library_books',href:'courses.html',roles:['STUDENT','LECTURER','DEAN']},
    {label:'Enrollments',icon:'how_to_reg',href:'enrollments.html',roles:['STUDENT','DEAN']},
    {label:'Departments',icon:'corporate_fare',href:'departments.html',roles:['DEAN']},
  ];

  const currentPage = window.location.pathname.split('/').pop(); // [L1] E.g., 'courses.html'
  
  // [L1] Filter items based on role, then create HTML elements
  items.filter(i => i.roles.includes(role)).forEach(item => {
    const a = document.createElement('a');
    a.className = 'nav-item' + (item.href === currentPage ? ' active' : ''); // [L1] Highlight active page
    a.href = item.href;
    a.innerHTML = `<span class="material-icons-round">${item.icon}</span>${item.label}`;
    nav.appendChild(a); // [L1] Append to sidebar
  });
}

/* ── Confirm Delete Modal helper ──────────────────────────── */
// [L2] Global state to remember what the user is trying to delete
let pendingDelete = {type:null, id:null};

/*
 * [L2] Opens the generic confirmation modal.
 * Prevents accidental deletions by requiring a second click.
 */
function confirmDelete(type, id) {
  pendingDelete = {type, id}; // [L1] Store target in global state
  document.getElementById('confirm-text').textContent = `Delete ${type} "${id}"? This cannot be undone.`; // [L1] Set warning text
  document.getElementById('confirm-delete-btn').onclick = executeDelete; // [L1] Bind execution function
  openModal('modal-confirm'); // [L1] Show modal
}

/*
 * [L2] Executes the actual DELETE API call based on the stored pendingDelete state.
 */
async function executeDelete() {
  const {type, id} = pendingDelete;
  // [L1] Map entity types to their API endpoint paths
  const epMap = {student:'students',lecturer:'lecturers',course:'courses',enrollment:'enrollments',dept:'departments'};
  try {
    const res = await fetch(`${API}/${epMap[type]}/${id}`, {method: 'DELETE'}); // [L1] Execute HTTP DELETE
    if (!res.ok) {
      const data = await res.json().catch(()=>({})); // [L1] Try to parse error message
      showToast(data.message || `Failed to delete ${type}.`, 'error'); // [L1] Show failure
      closeModal('modal-confirm');
      return;
    }
  } catch(e) {
    showToast(`Network error while deleting ${type}.`, 'error'); // [L1] Show network failure
    closeModal('modal-confirm');
    return;
  }

  closeModal('modal-confirm'); // [L1] Hide modal
  showToast(`${type.charAt(0).toUpperCase()+type.slice(1)} deleted.`, 'success'); // [L1] Show success
  
  // [L1] If the specific page defines an afterDelete callback, run it to refresh the table
  if (typeof afterDelete === 'function') afterDelete(type);
}

/* ── Modal click-outside close ────────────────────────────── */
// [L2] Runs once the HTML document has fully loaded
document.addEventListener('DOMContentLoaded', () => {
  // [L1] Attach listener to all modals so clicking the dark overlay closes them
  document.querySelectorAll('.modal-overlay').forEach(o => {
    o.addEventListener('click', e => { if(e.target===o) o.classList.add('hidden'); });
  });
  buildSidebar(); // [L1] Build sidebar on every page load
});
