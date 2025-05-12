<!DOCTYPE html>
<html>
    <head>
        <title>Rejestracja</title>
        <?php include_once '../views/includes/head.php'?>
    </head>
    <body>
        <?php include_once '../views/includes/header.php'?>

        <main>
        <?php include '../views/includes/result.php'?>

        <h2>Zarejestruj się</h2>
        <form method="post">
            <input type="reset" value="Resetuj"><br>

            <label for="mail"> Adres e-mail: </label>
            <input type="email" name="mail" id="mail" required> <br>

            <label for="login"> Login: </label>
            <input type="text" name="login" id="login" required> <br>

            <label for="password"> Hasło: </label>
            <input type="password" name="password" id="password" required> <br>

            <label for="repeat_password"> Powtórz hasło: </label>
            <input type="password" name="repeat_password" id="repeat_password" required> <br>

            <input type="submit" value="Zarejestruj się">
        </form>
        </main>

        <?php include_once '../views/includes/footer.php'?>
    </body>
</html>