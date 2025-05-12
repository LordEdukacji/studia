<!DOCTYPE html>
<html>
    <head>
        <title>Galeria modeli origami</title>
        <?php include_once '../views/includes/head.php'?>
    </head>
    <body>
        <?php include_once '../views/includes/header.php'?>

        <main>
            <?php include '../views/includes/result.php'?>

            <h2>Galeria modeli origami</h2>
    
            <?php if (count($gallery)): ?>
                <?php include '../views/includes/gallery_buttons.php' ?>
            
                <form method="post" action="saved_modify">
                        <?php foreach ($gallery as $photo): ?>
                            <div class="gallery_frame">
                                <?php include '../views/includes/gallery_frame.php' ?>
                                <?php if (isset($saved["'".$photo['_id']."'"])): ?>
                                    <input type="checkbox" checked disabled><label>Zapisane</label>
                                <?php endif ?>
                                <?php if (!(isset($saved["'".$photo['_id']."'"]))): ?>
                                    <input type="checkbox" name="saved_photos[<?= $photo['_id'] ?>]" value="true"><label for="saved_photos[<?= $photo['_id'] ?>]">Zapisz</label>
                                <?php endif ?>
                            </div>
                        <?php endforeach ?>
                    <input type="submit" value="Zapisz wybrane">
                </form>
            <?php else: ?>
                <section>Brak zdjęć do wyświetlenia</section>
            <?php endif ?>
        </main>

        <?php include_once '../views/includes/footer.php'?>
    </body>
</html>