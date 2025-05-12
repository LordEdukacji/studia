<!DOCTYPE html>
<html>
    <head>
        <title>Zapamiętane zdjęcia</title>
        <?php include_once '../views/includes/head.php'?>
    </head>
    <body>
        <?php include_once '../views/includes/header.php'?>

        <main>
        <h2>Zapisane zdjęcia</h2>

        <?php if (count($saved)): ?>
            <form method="post" action="saved_modify">
                    <?php foreach ($saved as $photo): ?>
                        <div class="gallery_frame">
                            <?php include '../views/includes/gallery_frame.php' ?>
                            <input type="checkbox" name="saved_photos[<?= $photo['_id'] ?>]" value="false"><label for="saved_photos[<?= $photo['_id'] ?>]">Usuń</label>
                        </div>
                    <?php endforeach ?>
                <input type="submit" value="Usuń zaznaczone z zapamiętanych">
            </form>
        <?php else: ?>
            <section>Brak zdjęć do wyświetlenia</section>
        <?php endif ?>
        </main>

        <?php include_once '../views/includes/footer.php'?>
    </body>
</html>