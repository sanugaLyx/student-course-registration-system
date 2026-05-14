/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/students.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Handles listing, creating, and modifying Students.
 *          View restricted to DEANS. 
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
requireLogin(); // [L1] Guard
requireRole(['DEAN']); // [L1] Guard: Deans only
const role = getCurrentRole(); // [L1] Extract role
const currentUser = getCurrentUser(); // [L1] Extract user
let editingStudentId = null; // [L1] State var for Update vs Create mode
let studentFilter = 'ALL'; // [L1] Type Filter state
let studentSearch = ''; // [L1] Text Filter state
let allStudents = []; // [L1] Cached API data

/*
 * [L2] Renders the Students table.
 * Applies Type (UG/PG) and text-based filters.
 */
function renderStudents(students) {
  const isDean = role === 'DEAN';
  const isStudent = role === 'STUDENT'; // [L1] Unused strictly here due to requireRole(['DEAN']), but safe for future modularity
  let filtered = students;
  
  // [L1] Filter by Polymorphic Type (Undergrad / Postgrad)
  if (studentFilter !== 'ALL') filtered = filtered.filter(s => s.type === studentFilter);
  
  // [L1] Text Search Filter
  if (studentSearch) filtered = filtered.filter(s =>
    `${s.firstName} ${s.lastName} ${s.email} ${s.studentId}`.toLowerCase().includes(studentSearch.toLowerCase())
  );
  
  const tbody = document.getElementById('students-tbody');
  if (filtered.length === 0) {
    tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><span class="material-icons-round">group_off</span><h3>No students found</h3><p>Try adjusting your search</p></div></td></tr>'; // [L1] Empty state UI
    return;
  }
  
  // [L1] Map into HTML Table Rows
  tbody.innerHTML = filtered.map(s => {
    // [L1] Role-based action access
    const canEdit = isDean || (isStudent && s.studentId === currentUser.studentId);
    const actions = canEdit ? `<div class="actions-cell">
      <button class="btn btn-tonal btn-sm btn-icon" onclick="editStudent('${s.studentId}')" title="Edit"><span class="material-icons-round">edit</span></button>
      ${isDean ? `<button class="btn btn-danger btn-sm btn-icon" onclick="confirmDelete('student','${s.studentId}')" title="Delete"><span class="material-icons-round">delete</span></button>` : ''}
    </div>` : '—';
    
    return `<tr>
      <td class="td-id">${s.studentId}</td>
      <td><strong>${s.firstName} ${s.lastName}</strong></td>
      <td>${s.email}</td>
      <td>${s.department || '—'}</td>
      <td><span class="badge badge-${(s.type || '').toLowerCase()}">${s.type === 'PG' ? 'Postgraduate' : 'Undergraduate'}</span></td>
      <td>${s.phoneNumber || '—'}</td>
      <td>${actions}</td>
    </tr>`;
  }).join('');
}

// [L2] Text Search Input handler
function filterStudents() { 
  studentSearch = document.getElementById('search-students').value; 
  renderStudents(allStudents); // [L1] Local re-render
}

// [L2] Chip Filter Input handler
function filterStudentChip(el, f) {
  document.querySelectorAll('#filter-chips-students .chip').forEach(c => c.classList.remove('active')); // [L1] Visually un-toggle all
  el.classList.add('active'); // [L1] Visually toggle selected
  studentFilter = f; // [L1] Update state
  renderStudents(allStudents); // [L1] Local re-render
}

// [L2] Prepares Modal for Edit Mode
function editStudent(id) {
  const s = allStudents.find(x => x.studentId === id); if (!s) return; // [L1] Lookup Student object
  
  editingStudentId = id; // [L1] Enter Edit Mode
  document.getElementById('modal-student-title').textContent = 'Edit Student';
  
  // [L1] Populate form with current values
  document.getElementById('st-id').value = s.studentId; 
  document.getElementById('st-id').disabled = true; // [L1] Disallow changing primary key
  document.getElementById('st-type').value = s.type;
  document.getElementById('st-first').value = s.firstName;
  document.getElementById('st-last').value = s.lastName;
  document.getElementById('st-email').value = s.email;
  document.getElementById('st-pass').value = ''; // [L1] Don't reveal password
  document.getElementById('st-phone').value = s.phoneNumber || '';
  document.getElementById('st-dob').value = s.dob || '';
  document.getElementById('st-dept').value = s.department || '';
  
  openModal('modal-student'); // [L1] Pop modal
}

/*
 * [L2] Save Logic (Create & Update)
 * The "type" parameter triggers Jackson's Polymorphic SubType deserialization
 * on the Spring Backend to create either UndergraduateStudent or PostgraduateStudent.
 */
async function saveStudent() {
  hideAlert('modal-student-alert'); // [L1] Clear old UI alerts
  
  const id = editingStudentId || document.getElementById('st-id').value.trim(); // [L1] Fetch ID
  if (!id) { showAlert('modal-student-alert', 'Student ID is required.'); return; }
  
  // [L1] Build JSON request payload
  const body = {
    studentId: id, 
    type: document.getElementById('st-type').value, // [L1] "UG" or "PG"
    firstName: document.getElementById('st-first').value,
    lastName: document.getElementById('st-last').value,
    email: document.getElementById('st-email').value,
    password: document.getElementById('st-pass').value || 'pass000', // [L1] Fallback password
    phoneNumber: document.getElementById('st-phone').value,
    dob: document.getElementById('st-dob').value,
    department: document.getElementById('st-dept').value
  };
  
  if (!body.firstName || !body.lastName || !body.email) { showAlert('modal-student-alert', 'Name and email are required.'); return; } // [L1] Simple Validation
  
  try {
    const method = editingStudentId ? 'PUT' : 'POST'; // [L1] Update vs Create
    const url = editingStudentId ? `${API}/students/${id}` : `${API}/students`; // [L1] URL routing
    
    // [L1] Execute Call
    const res = await fetch(url, {method, headers: {'Content-Type': 'application/json'}, body: JSON.stringify(body)});
    if (!res.ok) { 
      const e = await res.json().catch(()=>({})); 
      showAlert('modal-student-alert', e.message || 'Error saving student'); // [L1] Backend error
      return; 
    }
    
    // [L1] Cleanup & Refresh
    closeModal('modal-student');
    document.getElementById('st-id').disabled = false;
    showToast(editingStudentId ? 'Student updated!' : 'Student added successfully!');
    editingStudentId = null;
    resetStudentForm();
    await loadStudents(); // [L1] Reload from DB
  } catch (err) {
    showAlert('modal-student-alert', 'Network error. Please try again.'); // [L1] Network issue
  }
}

// [L2] Hard resets the form inputs
function resetStudentForm() {
  ['st-id','st-first','st-last','st-email','st-pass','st-phone','st-dob','st-dept'].forEach(id => {
    const el = document.getElementById(id); if (el) { el.value = ''; el.disabled = false; } // [L1] Loop and clear
  });
  editingStudentId = null; // [L1] Leave edit mode
  document.getElementById('modal-student-title').textContent = 'Add New Student';
}

// [L2] Global delete callback trigger
function afterDelete(type) { if (type === 'student') loadStudents(); }

// [L2] Initial Data Load
async function loadStudents() {
  const tbody = document.getElementById('students-tbody');
  tbody.innerHTML = '<tr class="loading-row"><td colspan="7"><span class="spinner"></span>Loading students...</td></tr>'; // [L1] Spinner
  
  const res = await apiFetch('/students'); // [L1] Invoke Fetch
  if (res) {
    allStudents = res; // [L1] Cache Array
    renderStudents(allStudents); // [L1] Draw Table
  } else {
    tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><span class="material-icons-round">error</span><h3>Failed to load students</h3></div></td></tr>'; // [L1] Error
  }
}

loadStudents(); // [L1] Auto-execute
