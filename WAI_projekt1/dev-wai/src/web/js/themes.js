if (!localStorage.theme) {
    localStorage.theme = "light";
}

function refreshTheme() {
    if (localStorage.theme === "contrast") {
        document.getElementsByTagName("link")[1].setAttribute("href", "css/contrastTheme.css");
        document.getElementsByTagName("link")[2].setAttribute("href", "css/contrast-jquery-ui.css");
    }
    else {
        document.getElementsByTagName("link")[1].setAttribute("href", "css/lightTheme.css");
        document.getElementsByTagName("link")[2].setAttribute("href", "css/light-jquery-ui.css");
    }
}

function setLightTheme() {
    localStorage.theme = "light";
    refreshTheme();
}

function setContrastTheme() {
    localStorage.theme = "contrast";
    refreshTheme();
}

function setupTheme() {
    document.getElementById("lightMode").style.display = "initial";
    document.getElementById("contrastMode").style.display = "initial";
    refreshTheme();
}

document.addEventListener("DOMContentLoaded", setupTheme);