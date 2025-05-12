<!DOCTYPE html>
<html>
    <head>
        <title>Logowanie</title>
        <?php include_once '../views/includes/head.php'?>
    </head>
    <body>
        <?php include_once '../views/includes/header.php'?>

        <main>
        
        <?php include '../views/includes/result.php'?>

        <h2>Logowanie</h2>

        <form method="post">
            <label for="login"> Login: </label>
            <input type="text" name="login" id="login" required>

            <label for="password"> Hasło: </label>
            <input type="password" name="password" id="password" required>

            <input type="submit" value="Zaloguj się">
        </form>
        </main>

        <?php include_once '../views/includes/footer.php'?>
    </body>
</html>