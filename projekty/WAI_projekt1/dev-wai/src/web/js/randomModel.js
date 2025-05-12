const modelNumber = Math.floor(Math.random() * 4);

const section = document.getElementById("recommendations");

const br = document.createElement('br');
section.appendChild(br);

const h5 = document.createElement('h5');
const recommendText = document.createTextNode("Polecany model: ");
h5.appendChild(recommendText);
section.appendChild(h5);

const a = document.createElement('a');
let linkText;
let promoText;

switch (modelNumber) {
    case 0:
        promoText = document.createTextNode("Jedyna taka, znosząca jajka: ");
        a.setAttribute("href", "kwoka.html");
        linkText = document.createTextNode("kwoka!");
        break;
    case 1:
        promoText = document.createTextNode("Zawsze stylowe, zdobiące głowę: ");
        a.setAttribute("href", "czako.html");
        linkText = document.createTextNode("czako!");
        break;
    case 2:
        promoText = document.createTextNode("Na listy i liściki: ");
        a.setAttribute("href", "koperta.html");
        linkText = document.createTextNode("koperta!");
        break;
    case 3:
        promoText = document.createTextNode("Dla spragnionych morskich przygód: ");
        a.setAttribute("href", "lodka.html");
        linkText = document.createTextNode("łódka!");
        break;
}

a.appendChild(linkText);
section.appendChild(promoText);
section.appendChild(a);
