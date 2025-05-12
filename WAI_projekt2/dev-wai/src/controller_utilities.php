<?php

function create_thumbnail($mime_type, $target, $file_name)
{
    $thumbnail_width = 200;
    $thumbnail_height = 125;
    $thumbnail_ratio = $thumbnail_width / $thumbnail_height;
    
    if ($mime_type === 'image/jpeg') {
        $img = imagecreatefromjpeg($target);
    }
    if ($mime_type === 'image/png') {
        $img = imagecreatefrompng($target);
    }

    $orig_width = imagesx($img);
    $orig_height = imagesy($img);
    $orig_ratio= $orig_width / $orig_height;

    if ($thumbnail_ratio > $orig_ratio) {
        $cropped_width = $thumbnail_width;
        $cropped_height = $cropped_width / $orig_ratio;
    }
    else {
        $cropped_height = $thumbnail_height;
        $cropped_width = $cropped_height * $orig_ratio;
    }

    $thumbnail = imagecreatetruecolor($thumbnail_width, $thumbnail_height);
    imagecopyresampled(
        $thumbnail,
        $img,
        0 - ($cropped_width - $thumbnail_width) / 2,
        0 - ($cropped_height - $thumbnail_height) / 2,
        0,
        0,
        $cropped_width,
        $cropped_height,
        $orig_width,
        $orig_height
    );

    if ($mime_type === 'image/jpeg') {
        imagejpeg($thumbnail, '/var/www/prod/src/web/images/thumbnail/' . $file_name);
    }
    if ($mime_type === 'image/png') {
        imagepng($thumbnail, '/var/www/prod/src/web/images/thumbnail/' . $file_name);
    }

    return;
}

function add_watermark($mime_type, $target, $file_name, $watermark_text)
{
    if ($mime_type === 'image/jpeg') {
        $img = imagecreatefromjpeg($target);
    }
    if ($mime_type === 'image/png') {
        $img = imagecreatefrompng($target);
    }

    $watermark_width = strlen($watermark_text)*10+205;
    $watermark_height = 250;

    $watermark = imagecreatetruecolor($watermark_width, $watermark_height);

    imagefilledrectangle($watermark, 0, 0, $watermark_width-1, $watermark_height-1, 0x000000);
    imagefilledrectangle($watermark, 2, 2, $watermark_width-3, $watermark_height-3, 0xFFFFFF);

    imagestring($watermark, 5, 105, 120, $watermark_text, 0x000000);

    imagecopymerge($img, $watermark, (imagesx($img)-$watermark_width)/2, (imagesy($img)-$watermark_height)/2, 0, 0, imagesx($watermark), imagesy($watermark), 25);

    if ($mime_type === 'image/jpeg') {
        imagejpeg($img, '/var/www/prod/src/web/images/watermark/' . $file_name);
    }
    if ($mime_type === 'image/png') {
        imagepng($img, '/var/www/prod/src/web/images/watermark/' . $file_name);
    }

    return;
}

function &get_saved()
{
    if (!isset($_SESSION['saved'])) {
        $_SESSION['saved'] = [];
    }

    return $_SESSION['saved'];
}