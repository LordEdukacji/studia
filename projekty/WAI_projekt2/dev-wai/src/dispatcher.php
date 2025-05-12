<?php

function dispatch($routing, $link)
{
    $controller_name = $routing[$link];

    $model = [];
    $view_name = $controller_name($model);

    if (strpos($view_name, 'redirect:') === 0) {
        $url = substr($view_name, strlen('redirect:'));
        header("Location: " . $url);
        exit;

    } else {
        extract($model);
        include 'views/' . $view_name . '.php';
    }
}