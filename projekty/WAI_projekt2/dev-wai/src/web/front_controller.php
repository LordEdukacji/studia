<?php
require '../../vendor/autoload.php';

require_once '../dispatcher.php';
require_once '../routing.php';
require_once '../controllers.php';

session_start();

$link = $_GET['link'];
dispatch($routing, $link);