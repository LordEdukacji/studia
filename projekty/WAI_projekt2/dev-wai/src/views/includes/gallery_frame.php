<h4><?= $photo['name'] ?></h4>
autorstwa <em><?= $photo['author'] ?></em>
<?php if ($photo['privacy'] == 'private'): ?>
    (prywatne)
<?php endif ?><br>
<a href="photo?id=<?= $photo['_id'] ?>">
    <img src="/images/thumbnail/<?= $photo['_id'] ?><?= $photo['extension'] ?>">
</a><br>