<!DOCTYPE html>
<html>
    <head>
        <title>Udostępnij zdjęcie</title>
        <?php include_once '../views/includes/head.php'?>
    </head>
    <body>
        <?php include_once '../views/includes/header.php'?>

        <main>

        <?php include '../views/includes/result.php'?>

        <h2>Udostępnij swoją twórczość światu</h2>
        <form method="post" enctype="multipart/form-data">
            <input type="reset" value="Resetuj"><br>

            <label for="name"> Nazwa modelu: </label>
            <input type="text" name="name" id="name" required> <br>

            <label for="author"> Autor: </label>
            <?php if (isset($_SESSION['user_id'])): ?>
                <input type="text" name="author" id="author" value="<?= $_SESSION['user_login'] ?>" readonly> <br>
            <?php endif ?>
            <?php if (!(isset($_SESSION['user_id']))): ?>
                <input type="text" name="author" id="author" > <br>
            <?php endif ?>       

            <label for="watermark"> Znak wodny: </label>
            <input type="text" name="watermark" id="watermark" required> <br>

            <label for="photo"> Zdjęcie modelu: </label>
            <input type="file" name="photo" id="photo" required> <br>

            <?php include_once '../views/includes/upload_privacy.php'?>

            <input type="submit" value="Wyślij">
        </form>

            </main>

        <?php include_once '../views/includes/footer.php'?>
    </body>
</html>