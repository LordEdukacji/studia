if (!sessionStorage.getItem(instr)) {
    sessionStorage.setItem(instr, 0);
}

function displaySlide() {
    for (i = 0; i < Number(document.getElementById("instruction").children.length); i++) {
        if (sessionStorage.getItem(instr) != i) {
            document.getElementById("instruction").children[i].style.display = "none";
        }
        else {
            document.getElementById("instruction").children[i].style.display = "block";
        }
    }
}

function changeSlide(change) {
    const numberOfSteps = document.getElementById("instruction").children.length;

    sessionStorage.setItem(instr, (Number(sessionStorage.getItem(instr)) + change + numberOfSteps) % numberOfSteps);

    displaySlide();
}

function showControls() {
    document.getElementById("prevStep").style.display = "initial";
    document.getElementById("nextStep").style.display = "initial";
}

function setupInstruction() {
    showControls();
    displaySlide();
}

document.addEventListener("DOMContentLoaded", setupInstruction);