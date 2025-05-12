<?php if (isset($_SESSION['user_id'])): ?>
    <label for="privacy"> Prywatność </label>
    <input type="radio" name="privacy" id="public" value="public" checked> <label for="public"> Publiczne </label>
    <input type="radio" name="privacy" id="private" value="private"> <label for="private"> Prywatne </label>
<?php endif ?>