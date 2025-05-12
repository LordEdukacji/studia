<!DOCTYPE html>
<html>
    <head>
        <title>Podgląd zdjęcia</title>
        <?php include_once '../views/includes/head.php'?>
    </head>
    <body>
        <?php include_once '../views/includes/header.php'?>

        <main>
        <h2><em><?= $photo['name'] ?></em></h1>
        <h3>autorstwa <em><?= $photo['author'] ?></em></h3>

        <figure><img src="/images/watermark/<?= $photo['_id'] ?><?= $photo['extension'] ?>"></figure>

        <?php if ($photo['privacy'] == 'private'): ?>
            <br> Zdjęcie prywatne
        <?php endif ?>
        </main>

        <?php include_once '../views/includes/footer.php'?>
    </body>
</html>