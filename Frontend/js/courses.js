/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/courses.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Handles listing, filtering, creating, updating, and deleting Courses.
 *          Viewable by everyone, but only DEANs can create/edit/delete.
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
requireLogin(); // [L1] Security Guard
const role = getCurrentRole(); // [L1] Retrieve role context
let editingCourseId = null, courseFilter = 'ALL', courseSearch = ''; // [L1] UI State variables
let allCourses = []; // [L1] Data cache

/*
 * [L2] Render function for Courses table
 * Applies both chip-filtering (CORE/ELEC) and text-search filtering.
 */
function renderCourses(courses) {
  let filtered = courses;
  
  // [L1] Filter by type (Polymorphism representation: CoreCourse vs ElectiveCourse)
  if (courseFilter !== 'ALL') filtered = filtered.filter(c => c.type === courseFilter);
  
  // [L1] Filter by text (Name, ID, Dept)
  if (courseSearch) { 
    const q = courseSearch.toLowerCase(); 
    filtered = filtered.filter(c => (c.courseName||'').toLowerCase().includes(q) || (c.courseId||'').toLowerCase().includes(q) || (c.departmentId||'').toLowerCase().includes(q)); 
  }
  
  const tbody = document.getElementById('courses-tbody');
  const isDean = role === 'DEAN'; // [L1] Check privileges
  
  if (filtered.length === 0) { 
    tbody.innerHTML = '<tr><td colspan="8"><div class="empty-state"><span class="material-icons-round">library_books</span><h3>No courses found</h3></div></td></tr>'; 
    return; 
  }
  
  // [L1] Map array to HTML rows
  tbody.innerHTML = filtered.map(c => {
    // [L1] Only Deans get Edit/Delete buttons. Students/Lecturers get "View only" text.
    const actions = isDean 
      ? `<div class="actions-cell">
           <button class="btn btn-tonal btn-sm btn-icon" onclick="editCourse('${c.courseId}')"><span class="material-icons-round">edit</span></button>
           <button class="btn btn-danger btn-sm btn-icon" onclick="confirmDelete('course','${c.courseId}')"><span class="material-icons-round">delete</span></button>
         </div>` 
      : '<span style="color:var(--col-300);font-size:.8rem">View only</span>';
      
    return `<tr>
      <td class="td-id">${c.courseId}</td>
      <td><strong>${c.courseName}</strong></td>
      <td class="td-id">${c.lecturerId||'—'}</td>
      <td>${c.departmentId||'—'}</td>
      <td>${c.credits}</td>
      <td>${c.maxCapacity}</td>
      <td><span class="badge badge-${(c.type||'').toLowerCase()}">${c.type}</span></td>
      <td>${actions}</td>
    </tr>`;
  }).join('');
  
  // [L1] Hide the "Add Course" button at the top of the page if not a Dean
  const addBtn = document.getElementById('btn-add-course');
  if (addBtn) addBtn.style.display = isDean ? '' : 'none';
}

// [L2] Search Input handler
function filterCourses() { 
  courseSearch = document.getElementById('search-courses').value; // [L1] Update state
  renderCourses(allCourses); // [L1] Render locally
}

// [L2] Chip Filter handler (All, Core, Elective)
function filterCourseChip(el, f) { 
  document.querySelectorAll('#filter-chips-courses .chip').forEach(c => c.classList.remove('active')); // [L1] Reset all chips visually
  el.classList.add('active'); // [L1] Highlight selected chip
  courseFilter = f; // [L1] Update filter state
  renderCourses(allCourses); // [L1] Render locally
}

// [L2] Prepares the modal for editing an existing course
function editCourse(id) {
  const c = allCourses.find(x => x.courseId === id); // [L1] Lookup object
  if (!c) return;
  
  editingCourseId = id; // [L1] Set state to edit mode
  
  // [L1] Populate modal fields
  document.getElementById('modal-course-title').textContent = 'Edit Course';
  document.getElementById('crs-id').value = c.courseId; 
  document.getElementById('crs-id').disabled = true; // [L1] Cannot alter primary key
  document.getElementById('crs-type').value = c.type; 
  document.getElementById('crs-name').value = c.courseName;
  document.getElementById('crs-lecturer').value = c.lecturerId || ''; 
  document.getElementById('crs-dept').value = c.departmentId || '';
  document.getElementById('crs-credits').value = c.credits; 
  document.getElementById('crs-cap').value = c.maxCapacity;
  
  openModal('modal-course'); // [L1] Display modal
}

/*
 * [L2] Save logic for Courses (Create and Update)
 * Ensures required fields are provided before calling the API.
 */
async function saveCourse() {
  hideAlert('modal-course-alert'); // [L1] Clear old errors
  
  // [L1] Build JSON payload
  const id = editingCourseId || document.getElementById('crs-id').value.trim();
  if (!id) { showAlert('modal-course-alert', 'Course ID required.'); return; }
  
  const body = { 
    courseId: id, 
    type: document.getElementById('crs-type').value, // [L1] Critical for Backend polymorphic deserialization (CORE vs ELEC)
    courseName: document.getElementById('crs-name').value,
    lecturerId: document.getElementById('crs-lecturer').value, 
    departmentId: document.getElementById('crs-dept').value,
    credits: parseInt(document.getElementById('crs-credits').value) || 3, // [L1] Convert to int, default 3
    maxCapacity: parseInt(document.getElementById('crs-cap').value) || 30 // [L1] Convert to int, default 30
  };
  
  if (!body.courseName) { showAlert('modal-course-alert', 'Course name required.'); return; }
  
  try {
    const method = editingCourseId ? 'PUT' : 'POST'; // [L1] Determine REST verb
    const url = editingCourseId ? `${API}/courses/${id}` : `${API}/courses`; // [L1] Determine REST endpoint
    
    // [L1] Execute request
    const res = await fetch(url, {method, headers: {'Content-Type': 'application/json'}, body: JSON.stringify(body)});
    if (!res.ok) { 
      const e = await res.json().catch(()=>({})); 
      showAlert('modal-course-alert', e.message || 'Error saving course'); // [L1] Display backend error
      return; 
    }

    // [L1] Cleanup UI on success
    closeModal('modal-course'); 
    editingCourseId = null; 
    document.getElementById('crs-id').disabled = false;
    showToast('Course saved!');
    await loadCourses(); // [L1] Reload fresh data
  } catch (err) {
    showAlert('modal-course-alert', 'Network error.'); // [L1] Handle disconnects
  }
}

// [L2] Global delete callback trigger
function afterDelete(type) { if (type === 'course') loadCourses(); }

// [L2] Initial Data Fetch
async function loadCourses() {
  const tbody = document.getElementById('courses-tbody');
  tbody.innerHTML = '<tr class="loading-row"><td colspan="8"><span class="spinner"></span>Loading courses...</td></tr>'; // [L1] Spinner
  
  const res = await apiFetch('/courses'); // [L1] Fetch API
  if (res) {
    allCourses = res; // [L1] Update local cache
    renderCourses(allCourses); // [L1] Render UI
  } else {
    tbody.innerHTML = '<tr><td colspan="8"><div class="empty-state"><span class="material-icons-round">error</span><h3>Failed to load courses</h3></div></td></tr>'; // [L1] Failure state
  }
}

loadCourses(); // [L1] Execute on load
