/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/enrollments.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Handles enrollment of students into courses. Views differ based
 *          on role (Students see only their own, Deans see all).
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
requireLogin(); // [L1] Guard: Must be logged in
const role = getCurrentRole(); // [L1] Retrieve role
const currentUser = getCurrentUser(); // [L1] Retrieve user to filter their specific enrollments
let enrollFilter = 'ALL', enrollSearch = ''; // [L1] UI State
let allEnrollments = []; // [L1] Cache

/*
 * [L2] Prepares the enrollment modal.
 * If the user is a STUDENT, auto-fills and locks the Student ID field.
 * If the user is a DEAN, allows them to enroll any student.
 */
function openEnrollModal() {
  if (role === 'STUDENT') {
    document.getElementById('enr-student').value = currentUser.studentId; // [L1] Auto-fill
    document.getElementById('enr-student').readOnly = true; // [L1] Lock field
  } else { 
    document.getElementById('enr-student').readOnly = false; // [L1] Unlock for Dean
  }
  openModal('modal-enroll'); // [L1] Show modal
}

/*
 * [L2] Renders the Enrollments table.
 * Applies role-based filtering first, then status and text search.
 */
function renderEnrollments(enrolls) {
  let filtered = enrolls;
  const isStudent = role === 'STUDENT';
  
  // [L1] Role-based Security View Filter: Students only see their own enrollments
  if (isStudent) filtered = filtered.filter(e => e.studentId === currentUser.studentId);
  
  // [L1] Apply Status filter (ACTIVE, COMPLETED, DROPPED)
  if (enrollFilter !== 'ALL') filtered = filtered.filter(e => e.status === enrollFilter);
  
  // [L1] Apply Text Search filter
  if (enrollSearch) { 
    const q = enrollSearch.toLowerCase(); 
    filtered = filtered.filter(e => (e.studentId + e.courseId + e.enrollmentId).toLowerCase().includes(q)); 
  }
  
  const tbody = document.getElementById('enrollments-tbody');
  if (filtered.length === 0) { 
    tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><span class="material-icons-round">how_to_reg</span><h3>No enrollments found</h3></div></td></tr>'; 
    return; 
  }
  
  // [L1] Map array to HTML rows
  tbody.innerHTML = filtered.map(e => {
    // [L1] Generate status badge dynamically
    const statusBadge = `<span class="badge badge-${e.status.toLowerCase()}"><span class="badge-dot"></span>${e.status}</span>`;
    
    // [L1] Actions differ by role. Deans can hard delete. Anyone can 'Drop' an active course.
    const actions = `<div class="actions-cell">
      ${e.status === 'ACTIVE' ? `<button class="btn btn-outline btn-sm" onclick="dropEnrollment('${e.enrollmentId}')"><span class="material-icons-round">remove_circle_outline</span>Drop</button>` : ''}
      ${role === 'DEAN' ? `<button class="btn btn-danger btn-sm btn-icon" onclick="confirmDelete('enrollment','${e.enrollmentId}')"><span class="material-icons-round">delete</span></button>` : ''}
    </div>`;
    
    return `<tr>
      <td class="td-id">${e.enrollmentId}</td>
      <td class="td-id">${e.studentId}</td>
      <td class="td-id">${e.courseId}</td>
      <td>${e.enrollmentDate}</td>
      <td>${statusBadge}</td>
      <td>${actions}</td>
    </tr>`;
  }).join('');
  
  // [L1] Hide "Add Enrollment" button from Lecturers
  const addBtn = document.getElementById('btn-enroll');
  if (addBtn) addBtn.style.display = (isStudent || role === 'DEAN') ? '' : 'none';
}

// [L2] Search text handler
function filterEnrollments() { 
  enrollSearch = document.getElementById('search-enrollments').value; 
  renderEnrollments(allEnrollments); // [L1] Re-render
}

// [L2] Status chip filter handler
function filterEnrollChip(el, f) { 
  document.querySelectorAll('#filter-chips-enrollments .chip').forEach(c => c.classList.remove('active')); // [L1] Reset visually
  el.classList.add('active'); // [L1] Highlight selected
  enrollFilter = f; // [L1] Update state
  renderEnrollments(allEnrollments); // [L1] Re-render
}

/*
 * [L2] Saves a new enrollment.
 * The backend handles all complex business logic (capacity checks, limits, prerequisites).
 * The frontend just sends the IDs and displays any returned error messages.
 */
async function saveEnrollment() {
  hideAlert('modal-enroll-alert'); // [L1] Clear old errors
  
  const studentId = document.getElementById('enr-student').value.trim(); // [L1] Get ID
  const courseId = document.getElementById('enr-course').value.trim(); // [L1] Get Course
  if (!studentId || !courseId) { showAlert('modal-enroll-alert', 'Student ID and Course ID required.'); return; }
  
  try { 
    // [L1] Send POST request
    const res = await fetch(`${API}/enrollments`, {
      method: 'POST', headers: {'Content-Type': 'application/json'}, 
      body: JSON.stringify({studentId, courseId})
    });
    
    // [L1] If backend rejects (e.g., Course Full, Max Limits reached)
    if (!res.ok) {
      const e = await res.json().catch(()=>({}));
      showAlert('modal-enroll-alert', e.message || 'Error enrolling in course. Check limits and capacity.'); // [L1] Show rejection reason
      return;
    }
    
    // [L1] Success cleanup
    closeModal('modal-enroll'); 
    showToast('Enrolled successfully!');
    await loadEnrollments(); // [L1] Refresh data
  } catch (err) {
    showAlert('modal-enroll-alert', 'Network error.'); // [L1] Network error
  }
}

/*
 * [L2] Drops an enrollment instead of deleting it.
 * Sends a PUT request to change the status to 'DROPPED'.
 */
async function dropEnrollment(id) {
  try { 
    await fetch(`${API}/enrollments/${id}`, {
      method: 'PUT', headers: {'Content-Type': 'application/json'}, 
      body: JSON.stringify({status: 'DROPPED'}) // [L1] Update status payload
    });
    showToast('Enrollment dropped.', 'error'); // [L1] Toast notification
    await loadEnrollments(); // [L1] Refresh table
  } catch (err) {
    showToast('Network error while dropping.', 'error'); // [L1] Network error
  }
}

// [L2] Global delete callback trigger
function afterDelete(type) { if (type === 'enrollment') loadEnrollments(); }

// [L2] Initial Data Fetch
async function loadEnrollments() {
  const tbody = document.getElementById('enrollments-tbody');
  tbody.innerHTML = '<tr class="loading-row"><td colspan="6"><span class="spinner"></span>Loading enrollments...</td></tr>'; // [L1] Spinner
  
  const res = await apiFetch('/enrollments'); // [L1] Call API
  if (res) {
    allEnrollments = res; // [L1] Update cache
    renderEnrollments(allEnrollments); // [L1] Render UI
  } else {
    tbody.innerHTML = '<tr><td colspan="6"><div class="empty-state"><span class="material-icons-round">error</span><h3>Failed to load enrollments</h3></div></td></tr>'; // [L1] Failure state
  }
}

loadEnrollments(); // [L1] Execute on load
