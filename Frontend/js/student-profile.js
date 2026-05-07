/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/student-profile.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Allows a Student to view and edit their own profile details.
 *          Reflects Polymorphic properties (like max course limits).
 * ─────────────────────────────────────────────────────────────
 */

'use strict';
requireLogin(); // [L1] Guard
requireRole(['STUDENT']); // [L1] Guard: Only students have this profile view
const user = getCurrentUser(); // [L1] Retrieve session user

/*
 * [L2] Loads the user's data from sessionStorage into the DOM elements.
 * Also makes an API call to get their active enrollment count.
 */
function loadProfile() {
  if (!user) return; // [L1] Abort if missing
  
  // [L1] Generate and set Avatar Initials
  const init = getInitials(user.firstName, user.lastName);
  document.getElementById('prof-avatar').textContent = init;
  
  // [L1] Populate Read-Only Header Tags
  document.getElementById('prof-name').textContent = `${user.firstName} ${user.lastName}`;
  document.getElementById('prof-email-display').textContent = user.email;
  document.getElementById('prof-dept-display').textContent = user.department || '';
  
  // [L1] Display correct Polymorphic sub-type
  document.getElementById('prof-type-badge').textContent = user.type === 'PG' ? 'Postgraduate' : 'Undergraduate';
  
  // [L1] Populate Editable Form Fields
  document.getElementById('prof-firstName').value = user.firstName || '';
  document.getElementById('prof-lastName').value = user.lastName || '';
  document.getElementById('prof-email').value = user.email || '';
  document.getElementById('prof-phone').value = user.phoneNumber || '';
  document.getElementById('prof-dob').value = user.dob || '';
  document.getElementById('prof-dept').value = user.department || '';
  
  // [L1] Populate Read-Only Stat blocks at the bottom of the profile
  document.getElementById('prof-id-display').textContent = user.studentId || '—';
  document.getElementById('prof-type-display').textContent = user.type === 'PG' ? 'Postgraduate' : 'Undergraduate';
  document.getElementById('prof-limit-display').textContent = (user.type === 'PG' ? 4 : 6) + ' courses'; // [L1] Polymorphic business rule reflection
  
  // [L1] Fetch real-time active enrollment count
  apiFetch(`/enrollments/student/${user.studentId}`).then(enrolls => {
    if (enrolls) {
      const activeE = enrolls.filter(e => e.status === 'ACTIVE'); // [L1] Filter to only active
      document.getElementById('prof-enroll-count').textContent = activeE.length; // [L1] Set text
    } else {
      document.getElementById('prof-enroll-count').textContent = '0'; // [L1] Fallback
    }
  });
}

/*
 * [L2] Save Profile changes.
 * PUTs the updated data to the API, then updates the local sessionStorage
 * to ensure the UI stays synchronized with the database.
 */
async function saveProfile() {
  const id = user.studentId; // [L1] Determine ID
  
  // [L1] Construct JSON payload from form fields
  const updated = {
    firstName: document.getElementById('prof-firstName').value,
    lastName: document.getElementById('prof-lastName').value,
    email: document.getElementById('prof-email').value,
    phoneNumber: document.getElementById('prof-phone').value,
    dob: document.getElementById('prof-dob').value,
    department: document.getElementById('prof-dept').value,
  };
  
  try {
    // [L1] Execute PUT request
    const res = await fetch(`${API}/students/${id}`, {
      method: 'PUT', 
      headers: {'Content-Type': 'application/json'}, 
      body: JSON.stringify(updated)
    });
    
    if (res.ok) { 
      const data = await res.json(); // [L1] Parse backend response
      
      Object.assign(user, data); // [L1] Merge updated data into local user object
      sessionStorage.setItem('user', JSON.stringify(user)); // [L1] Persist to sessionStorage
      
      buildSidebar(); // [L1] Re-render sidebar in case Name changed
      loadProfile();  // [L1] Re-render profile page
      showToast('Profile updated successfully!'); // [L1] Success Toast
    } else {
      const e = await res.json().catch(()=>({}));
      showToast(e.message || 'Failed to update profile.', 'error'); // [L1] Backend Error Toast
    }
  } catch (err) {
    showToast('Network error.', 'error'); // [L1] Network Error Toast
  }
}

loadProfile(); // [L1] Execute on load
