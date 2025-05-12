<header>
    <div id="mainTitle">ORIGAMI</div>
    <nav>
      <ul>
        <li><a href="home"> Strona główna </a></li>
        <li><a>Nauka</a>
          <ul>
            <li><a href="basics"> Podstawy </a></li>
            <li><a href="instructions"> Instrukcje </a></li>
          </ul>
        </li>
        <li><a href="gallery"> Galeria </a>
          <ul>
            <li><a href="gallery"> Zdjęcia </a></li>
            <li><a href="upload"> Udostępnianie </a></li>
            <li><a href="saved"> Zapisane </a></li>
          </ul>
        </li>
      </ul>
    </nav>

    <div id="logging">
    <?php if (empty($_SESSION['user_id'])): ?>
        <a href="login"><button type="button" class="lightMode">Zaloguj się</button></a>
        <a href="signup"><button type="button" class="lightMode">Zarejestruj się</button></a>
    <?php endif ?>
    
    <?php if (isset($_SESSION['user_id'])): ?>
        <a href="logout"><button type="button" class="lightMode">Wyloguj się</button></a>
    <?php endif ?>
    </div>
</header>