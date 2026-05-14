/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/lecturers.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Handles listing, creating, and modifying Lecturers.
 *          View restricted to DEANS. Lecturers themselves can only
 *          edit their own profile (if that feature is exposed).
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
requireLogin(); // [L1] Guard
requireRole(['DEAN']); // [L1] Guard: Only Deans can view the global lecturers list
const role = getCurrentRole(); // [L1] Retrieve role
const currentUser = getCurrentUser(); // [L1] Retrieve user ID
let editingLecturerId = null, lecturerFilter = 'ALL', lecturerSearch = ''; // [L1] State variables
let allLecturers = []; // [L1] Cache

/*
 * [L2] Render the Lecturers table
 * Applies type filters (FT/PT) and search text.
 */
function renderLecturers(lecs) {
  let filtered = lecs;
  
  // [L1] Filter by Polymorphic Type (Full-Time vs Part-Time)
  if (lecturerFilter !== 'ALL') filtered = filtered.filter(l => l.type === lecturerFilter);
  
  // [L1] Filter by Search String (Name, Email, ID)
  if (lecturerSearch) filtered = filtered.filter(l =>
    `${l.firstName} ${l.lastName} ${l.email} ${l.lecturerId}`.toLowerCase().includes(lecturerSearch.toLowerCase())
  );
  
  const tbody = document.getElementById('lecturers-tbody');
  const isDean = role === 'DEAN';
  const isLec = role === 'LECTURER'; // [L1] Unused strictly here due to requireRole(['DEAN']) guard above, but good for future proofing
  
  if (filtered.length === 0) { 
    tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><span class="material-icons-round">badge</span><h3>No lecturers found</h3></div></td></tr>'; 
    return; 
  }
  
  // [L1] Build HTML Rows
  tbody.innerHTML = filtered.map(l => {
    // [L1] A Dean can edit anyone. A Lecturer can edit themselves.
    const canEdit = isDean || (isLec && l.lecturerId === currentUser.lecturerId);
    
    const actions = canEdit ? `<div class="actions-cell">
      <button class="btn btn-tonal btn-sm btn-icon" onclick="editLecturer('${l.lecturerId}')"><span class="material-icons-round">edit</span></button>
      ${isDean ? `<button class="btn btn-danger btn-sm btn-icon" onclick="confirmDelete('lecturer','${l.lecturerId}')"><span class="material-icons-round">delete</span></button>` : ''}
    </div>` : '—';
    
    return `<tr>
      <td class="td-id">${l.lecturerId}</td>
      <td><strong>${l.firstName} ${l.lastName}</strong></td>
      <td>${l.email}</td>
      <td>${l.department || '—'}</td>
      <td><span class="badge badge-${(l.type||'').toLowerCase()}">${l.type === 'FT' ? 'Full-Time' : 'Part-Time'}</span></td>
      <td class="td-id">${l.courseId || '—'}</td>
      <td>${actions}</td>
    </tr>`;
  }).join('');
  
  // [L1] Add button is Dean-only
  const addBtn = document.getElementById('btn-add-lecturer');
  if (addBtn) addBtn.style.display = isDean ? '' : 'none';
}

// [L2] Search Input handler
function filterLecturers() { 
  lecturerSearch = document.getElementById('search-lecturers').value; 
  renderLecturers(allLecturers); 
}

// [L2] Chip Filter handler
function filterLecturerChip(el, f) {
  document.querySelectorAll('#filter-chips-lecturers .chip').forEach(c => c.classList.remove('active')); // [L1] Visually reset
  el.classList.add('active'); // [L1] Highlight new selection
  lecturerFilter = f; // [L1] Update State
  renderLecturers(allLecturers); // [L1] Re-render
}

// [L2] Prepares Modal for Editing
function editLecturer(id) {
  const l = allLecturers.find(x => x.lecturerId === id); if (!l) return; // [L1] Lookup object
  
  editingLecturerId = id; // [L1] Switch to Edit Mode
  document.getElementById('modal-lecturer-title').textContent = 'Edit Lecturer';
  
  // [L1] Populate modal inputs
  document.getElementById('lec-id').value = l.lecturerId; 
  document.getElementById('lec-id').disabled = true; // [L1] Lock primary key
  document.getElementById('lec-type').value = l.type;
  document.getElementById('lec-first').value = l.firstName;
  document.getElementById('lec-last').value = l.lastName;
  document.getElementById('lec-email').value = l.email;
  document.getElementById('lec-pass').value = ''; // [L1] Don't show existing password
  document.getElementById('lec-phone').value = l.phoneNumber || '';
  document.getElementById('lec-dept').value = l.department || '';
  document.getElementById('lec-course').value = l.courseId || '';
  
  openModal('modal-lecturer'); // [L1] Show
}

/*
 * [L2] Save Logic (Create and Update)
 * The JSON "type" property sent here drives Jackson's Polymorphic
 * Deserialization on the Spring Boot backend.
 */
async function saveLecturer() {
  hideAlert('modal-lecturer-alert'); // [L1] Clear old errors
  
  const id = editingLecturerId || document.getElementById('lec-id').value.trim(); // [L1] Grab ID
  if (!id) { showAlert('modal-lecturer-alert', 'Lecturer ID is required.'); return; }
  
  // [L1] Construct JSON Payload. Note the 'type' field ("FT" or "PT")
  const body = {
    lecturerId: id, 
    type: document.getElementById('lec-type').value, // [L1] Polymorphic discriminator
    firstName: document.getElementById('lec-first').value, 
    lastName: document.getElementById('lec-last').value,
    email: document.getElementById('lec-email').value, 
    password: document.getElementById('lec-pass').value || 'lec000', // [L1] Default fallback password
    phoneNumber: document.getElementById('lec-phone').value, 
    department: document.getElementById('lec-dept').value,
    courseId: document.getElementById('lec-course').value
  };
  
  if (!body.firstName || !body.email) { showAlert('modal-lecturer-alert', 'Name and email required.'); return; } // [L1] Validation
  
  try {
    const method = editingLecturerId ? 'PUT' : 'POST'; // [L1] Verb
    const url = editingLecturerId ? `${API}/lecturers/${id}` : `${API}/lecturers`; // [L1] Path
    
    // [L1] Execute network call
    const res = await fetch(url, {method, headers: {'Content-Type': 'application/json'}, body: JSON.stringify(body)});
    
    if (!res.ok) { 
      const e = await res.json().catch(()=>({})); 
      showAlert('modal-lecturer-alert', e.message || 'Error saving lecturer'); // [L1] Backend error (e.g., email already exists)
      return; 
    }
    
    // [L1] Cleanup and refresh
    closeModal('modal-lecturer'); 
    editingLecturerId = null; 
    document.getElementById('lec-id').disabled = false;
    showToast('Lecturer saved!');
    await loadLecturers();
  } catch (err) {
    showAlert('modal-lecturer-alert', 'Network error.'); // [L1] Network failure
  }
}

// [L2] Global delete callback trigger
function afterDelete(type) { if (type === 'lecturer') loadLecturers(); }

// [L2] Initial Data Fetch
async function loadLecturers() {
  const tbody = document.getElementById('lecturers-tbody');
  tbody.innerHTML = '<tr class="loading-row"><td colspan="7"><span class="spinner"></span>Loading lecturers...</td></tr>'; // [L1] Spinner
  
  const res = await apiFetch('/lecturers'); // [L1] Fetch API
  if (res) {
    allLecturers = res; // [L1] Cache
    renderLecturers(allLecturers); // [L1] Render
  } else {
    tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><span class="material-icons-round">error</span><h3>Failed to load lecturers</h3></div></td></tr>'; // [L1] Error state
  }
}

loadLecturers(); // [L1] Execute on load
