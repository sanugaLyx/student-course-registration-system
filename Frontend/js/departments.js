/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/departments.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Handles listing, filtering, adding, updating, and deleting
 *          Academic Departments. Restricted entirely to Deans.
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
requireLogin(); // [L1] Security Guard
requireRole(['DEAN']); // [L1] Only Deans manage departments

let editingDeptId = null; // [L1] State variable: if set, we are editing an existing department. If null, creating a new one.
let deptSearch = ''; // [L1] State variable: current text in search box
let allDepartments = []; // [L1] Cache of all departments loaded from API

/*
 * [L2] Renders the Departments table to the DOM.
 * Applies local search filtering before generating the HTML.
 */
function renderDepts(depts) {
  let filtered = depts;
  
  // [L1] Apply text search filter against Name and ID
  if (deptSearch) { 
    const q = deptSearch.toLowerCase(); 
    filtered = filtered.filter(d => (d.departmentName + d.departmentId).toLowerCase().includes(q)); 
  }
  
  const tbody = document.getElementById('departments-tbody');
  
  // [L1] Show empty state if no results match
  if (filtered.length === 0) { 
    tbody.innerHTML = '<tr><td colspan="4"><div class="empty-state"><span class="material-icons-round">corporate_fare</span><h3>No departments found</h3></div></td></tr>'; 
    return; 
  }
  
  // [L1] Map array to HTML table rows
  tbody.innerHTML = filtered.map(d => `<tr>
    <td class="td-id">${d.departmentId}</td><td><strong>${d.departmentName}</strong></td><td class="td-id">${d.deanId || '—'}</td>
    <td><div class="actions-cell">
      <button class="btn btn-tonal btn-sm btn-icon" onclick="editDept('${d.departmentId}')"><span class="material-icons-round">edit</span></button>
      <button class="btn btn-danger btn-sm btn-icon" onclick="confirmDelete('dept','${d.departmentId}')"><span class="material-icons-round">delete</span></button>
    </div></td>
  </tr>`).join('');
}

// [L2] Event handler for the search input box
function filterDepts() { 
  deptSearch = document.getElementById('search-depts').value; // [L1] Update state
  renderDepts(allDepartments); // [L1] Re-render table locally (no API call)
}

// [L2] Prepares the modal form for updating an existing department
function editDept(id) {
  const d = allDepartments.find(x => x.departmentId === id); // [L1] Lookup full object from cache
  if (!d) return;
  
  editingDeptId = id; // [L1] Set state to edit mode
  
  // [L1] Populate modal fields
  document.getElementById('modal-dept-title').textContent = 'Edit Department';
  document.getElementById('dept-id').value = d.departmentId; 
  document.getElementById('dept-id').disabled = true; // [L1] Cannot change ID after creation
  document.getElementById('dept-name').value = d.departmentName;
  document.getElementById('dept-dean').value = d.deanId || '';
  
  openModal('modal-dept'); // [L1] Show the modal overlay
}

/*
 * [L2] Main Save function (handles both Create and Update)
 * Makes POST or PUT API requests depending on editingDeptId state.
 */
async function saveDept() {
  hideAlert('modal-dept-alert'); // [L1] Clear old errors
  
  // [L1] Gather inputs
  const id = editingDeptId || document.getElementById('dept-id').value.trim();
  if (!id) { showAlert('modal-dept-alert', 'Department ID required.'); return; } // [L1] Client validation
  
  const body = {
    departmentId: id, 
    departmentName: document.getElementById('dept-name').value, 
    deanId: document.getElementById('dept-dean').value
  };
  
  if (!body.departmentName) { showAlert('modal-dept-alert', 'Department name required.'); return; } // [L1] Client validation
  
  try {
    const method = editingDeptId ? 'PUT' : 'POST'; // [L1] Determine HTTP verb based on mode
    const url = editingDeptId ? `${API}/departments/${id}` : `${API}/departments`; // [L1] Determine API path
    
    // [L1] Execute network request
    const res = await fetch(url, {method, headers: {'Content-Type': 'application/json'}, body: JSON.stringify(body)});
    if (!res.ok) { 
      const e = await res.json().catch(()=>({})); 
      showAlert('modal-dept-alert', e.message || 'Error saving department'); // [L1] Show backend error (e.g. constraints)
      return; 
    }
    
    // [L1] On success, cleanup and reload table
    closeModal('modal-dept'); 
    editingDeptId = null; 
    document.getElementById('dept-id').disabled = false; // [L1] Re-enable ID field for next time
    showToast('Department saved!');
    await loadDepartments(); // [L1] Fetch fresh data from backend
  } catch (err) {
    showAlert('modal-dept-alert', 'Network error.'); // [L1] Show network error
  }
}

// [L2] Callback triggered by the global executeDelete() in auth.js upon successful deletion
function afterDelete(type) { 
  if (type === 'dept') loadDepartments(); // [L1] Refresh table
}

// [L2] Initial fetch: Loads all departments from the backend and triggers render
async function loadDepartments() {
  const tbody = document.getElementById('departments-tbody');
  tbody.innerHTML = '<tr class="loading-row"><td colspan="4"><span class="spinner"></span>Loading departments...</td></tr>'; // [L1] Show loading spinner
  
  const res = await apiFetch('/departments'); // [L1] Call API
  if (res) {
    allDepartments = res; // [L1] Update cache
    renderDepts(allDepartments); // [L1] Render rows
  } else {
    tbody.innerHTML = '<tr><td colspan="4"><div class="empty-state"><span class="material-icons-round">error</span><h3>Failed to load departments</h3></div></td></tr>'; // [L1] Show failure state
  }
}

loadDepartments(); // [L1] Execute on load
