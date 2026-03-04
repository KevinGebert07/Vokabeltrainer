// --------------------- Animation für die Seitenleiste ---------------------

const sidebar = document.querySelector(".sidebar");
const sidebarToggler = document.querySelector(".sidebar-toggler");
const menuToggler = document.querySelector(".menu-toggler");

// Ensure these heights match the CSS sidebar height values
let collapsedSidebarHeight = "56px"; // Height in mobile view (collapsed)
let fullSidebarHeight = "calc(100vh - 32px)"; // Height in larger screen

// Toggle sidebar's collapsed state
sidebarToggler.addEventListener("click", () => {
    sidebar.classList.toggle("collapsed");
});

// Update sidebar height and menu toggle text
const toggleMenu = (isMenuActive) => {
    sidebar.style.height = isMenuActive ? `${sidebar.scrollHeight}px` : collapsedSidebarHeight;
    menuToggler.querySelector("span").innerText = isMenuActive ? "close" : "menu";
}

// Toggle menu-active class and adjust height
menuToggler.addEventListener("click", () => {
    toggleMenu(sidebar.classList.toggle("menu-active"));
});

// (Optional code): Adjust sidebar height on window resize
window.addEventListener("resize", () => {
    if (window.innerWidth >= 1024) {
        sidebar.style.height = fullSidebarHeight;
    } else {
        sidebar.classList.remove("collapsed");
        sidebar.style.height = "auto";
        toggleMenu(sidebar.classList.contains("menu-active"));
    }
});



// --------------------- Animation für die "active" Logos der Seitenleiste ---------------------

const container = document.getElementById('container');
const registerBtn = document.getElementById('register');
const loginBtn = document.getElementById('login');

registerBtn.addEventListener('click', () => {
    container.classList.add("active");
});

loginBtn.addEventListener('click', () => {
    container.classList.remove("active");
});



// --------------------- Laden der nächsten Vokabel nach 1 Sekunde auf der Lernseite ---------------------

document.addEventListener("DOMContentLoaded", () => {
    const resultElement = document.querySelector(".learn-result");
    const isCorrect = resultElement?.querySelector(".result-correct");

    if (resultElement) {
        // nach 1 Sekunde neue Vokabel laden
        setTimeout(() => {
            window.location.reload();
        }, 1000);
    }

    // Optional: Fokus automatisch auf Eingabefeld setzen
    const deutschInput = document.getElementById("deutschInput");
    if (deutschInput) {
        deutschInput.focus();
    }
});
