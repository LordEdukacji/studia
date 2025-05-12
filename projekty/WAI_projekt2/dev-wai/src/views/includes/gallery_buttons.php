<section>

Strona <?= $page?> z <?= $num_of_pages?> <br>

<?php if ($page > 1): ?>
    <a href="gallery?page=<?= $page - 1 ?>"><button type="button"> Poprzednia </button></a>
<?php endif ?>

<?php if ($page == 1): ?>
    <button type="button" disabled> Poprzednia </button>
<?php endif ?>

<?php if ($page < $num_of_pages): ?>
    <a href="gallery?page=<?= $page + 1 ?>"><button type="button"> Następna </button></a>
<?php endif ?>

<?php if ($page == $num_of_pages): ?>
    <button type="button" disabled> Następna </button>
<?php endif ?>

</section>