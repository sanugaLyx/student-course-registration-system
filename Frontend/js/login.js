/*
 * ─────────────────────────────────────────────────────────────
 * 📁 FILE: Frontend/js/login.js
 * 🏗  LAYER: Frontend — Page Logic
 * 📋 ROLE: Handles the login screen interactions. Captures email/password,
 *          sends them to the API, and stores the resulting User object
 *          in the browser's sessionStorage.
 * ─────────────────────────────────────────────────────────────
 */

'use strict';

let selectedRole = 'STUDENT'; // [L1] Default role selection (mostly aesthetic now, backend checks all roles)

// [L2] Handles UI changes when clicking the Student/Lecturer/Dean tabs on the login form
function switchRole(role, btn) {
  selectedRole = role; // [L1] Update state
  document.querySelectorAll('.login-tab').forEach(b => b.classList.remove('active')); // [L1] Remove active styling from all tabs
  btn.classList.add('active'); // [L1] Add active styling to clicked tab
}

/*
 * [L2] Primary login function triggered by the "Sign In" button or Enter key.
 * Sends credentials to the backend /auth/login endpoint.
 */
async function doLogin() {
  const email = document.getElementById('login-email').value.trim(); // [L1] Get and trim email
  const pass = document.getElementById('login-password').value; // [L1] Get password
  hideAlert('login-alert'); // [L1] Clear previous errors
  
  // [L1] Basic client-side validation
  if (!email || !pass) { showAlert('login-alert', 'Please enter your email and password.'); return; }

  try {
    // [L1] Send POST request to backend AuthController
    const res = await fetch(`${API}/auth/login`, {
      method: 'POST', headers: {'Content-Type':'application/json'},
      body: JSON.stringify({email, password: pass}) // [L1] Payload matches Map<String, String> expected by Spring
    });
    
    if (res.ok) { // [L1] HTTP 200 OK
      const data = await res.json(); // [L1] Parse backend response containing user, role, redirect URL
      
      // [L1] Extract the specific ID depending on the user's role
      const id = data.user.studentId || data.user.lecturerId || data.user.deanId;
      
      // [L1] Save authentication state to sessionStorage
      sessionStorage.setItem('user', JSON.stringify(data.user));
      sessionStorage.setItem('role', data.role);
      sessionStorage.setItem('userId', id);
      
      // [L1] Redirect user to their appropriate dashboard
      window.location.href = data.redirect;
    } else {
      // [L1] Authentication failed (e.g., 401 Unauthorized)
      const errorData = await res.json().catch(()=>({}));
      showAlert('login-alert', errorData.message || 'Invalid email or password.'); // [L1] Display error to user
    }
  } catch(e) {
    // [L1] Backend is down or network disconnected
    showAlert('login-alert', 'Unable to connect to the server. Please try again later.');
  }
}

/*
 * [L2] Auto-redirect IIFE (Immediately Invoked Function Expression)
 * Runs immediately when login.js loads.
 * If the user is already logged in (session exists), send them straight to the dashboard
 * instead of forcing them to log in again.
 */
(function() {
  const u = sessionStorage.getItem('user'); // [L1] Check for session
  const r = sessionStorage.getItem('role'); // [L1] Check role
  if (u && r) window.location.href = r === 'DEAN' ? 'dean-dashboard.html' : 'dashboard.html'; // [L1] Redirect if session active
})();
