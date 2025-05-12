<?php
require_once 'business.php';
require_once 'controller_utilities.php';

function upload(&$model)
{
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['name']) && isset($_POST['author']) && isset($_POST['watermark'])) {
        $result = null;

        $size_error = false;
        $extension_error = false;

        if ($_FILES['photo']['size'] > 1000000) {
            $size_error = true;
        }
        
        $finfo = finfo_open(FILEINFO_MIME_TYPE);
        $file_name = $_FILES['photo']['tmp_name'];
        if (empty($file_name)) {
            $size_error = true;
            $extension_error = true;
        } else {
            $mime_type = finfo_file($finfo, $file_name);
            if (!($mime_type === 'image/jpeg') && !($mime_type === 'image/png') && !(pathinfo($_FILES['photo']['name'], PATHINFO_EXTENSION) === 'jpg') && !(pathinfo($_FILES['photo']['name'], PATHINFO_EXTENSION) === 'jpg' )) {
                $extension_error = true;
            }
        }

        if($size_error && $extension_error) {
            $result = "Zbyt duży rozmiar pliku (max 1MB) i zły format pliku (dopuszczalne PNG i JPG)";
            header('Location: upload?result='.$result);
            exit;
        }
        if($size_error && !($extension_error)) {
            $result = "Zbyt duży rozmiar pliku (max 1MB)";
            header('Location: upload?result='.$result);
            exit;
        }
        if(!($size_error) && $extension_error) {
            $result = "Zły format pliku (dopuszczalne PNG i JPG)";
            header('Location: upload?result='.$result);
            exit;
        }

        if ($mime_type === 'image/jpeg') {
            $extension = '.jpg';
        }
        if ($mime_type === 'image/png') {
            $extension = '.png';
        }

        $privacy = 'public';
        $real_author = null;
        if (isset($_POST['privacy']) && $_POST['privacy'] == 'private') {
            $privacy = 'private';
            $real_author = $_SESSION['user_id'];
        }

        $new_id = generate_id();

        $photo = [
            '_id' => $new_id,
            'name' => $_POST['name'],
            'author' => $_POST['author'],
            'watermark' => $_POST['watermark'],
            'extension' => $extension,
            'privacy' => $privacy,
            'real_author' => $real_author
        ];
        
        try {
            save_photo_info($photo);
            $result = "Zdjęcie zostało przesłane pomyślnie";

            $upload_dir = '/var/www/prod/src/web/images/';
            $file = $_FILES['photo'];
            $file_name = $new_id.$extension;
            $target = $upload_dir . $file_name;
            $tmp_path = $file['tmp_name'];
        
            move_uploaded_file($tmp_path, $target);

            add_watermark($mime_type, $target, $file_name, $photo['watermark']);
            create_thumbnail($mime_type, $target, $file_name);
        } catch (Exception $e) {
            $result = "Nie udało się przesłać zdjęcia";
            header('Location: upload?result='.$result);
            exit;
        }

        header('Location: upload?result='.$result);
        exit;
    }

    return 'upload_view';
}

function gallery(&$model)
{
    if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['page'])) {
        $page = $_GET['page'];
    }

    if (empty($_GET['page'])) {
        $page = 1;
    }

    $gallery = get_gallery();
    $photos_count = count($gallery);
    
    $page_size = 12;
    $num_of_pages = $photos_count % $page_size ? intdiv($photos_count, $page_size)+1 : $photos_count / $page_size;

    $gallery = get_page($page, $page_size);

    $model['gallery'] = $gallery;
    $model['page'] = $page;
    $model['num_of_pages'] = $num_of_pages;

    $model['saved'] = get_saved();

    return 'gallery_view';
}

function photo(&$model)
{
    if (!empty($_GET['id'])) {
        if ($photo = get_photo($_GET['id'])) {
            $model['photo'] = $photo;
            return 'photo_view';
        }
    }

    http_response_code(404);
    exit;
}

function signup(&$model)
{
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['mail']) && isset($_POST['login']) && isset($_POST['password']) && isset($_POST['repeat_password'])) {
        $result = null;

        $mail = $_POST['mail'];
        $login = $_POST['login'];
        $password = $_POST['password'];
        $repeat_password = $_POST['repeat_password'];

        if (!($password === $repeat_password)) {
            $result = "Hasło i powtórzone hasło muszą być identyczne!";
            header('Location: signup?result='.$result);
            exit;
        }

        if ((login_taken($login))) {
            $result = "Login jest już zajęty!";
            header('Location: signup?result='.$result);
            exit;
        }

        $user = [
            'mail' => $mail,
            'login' => $login,
            'password' => password_hash($password, PASSWORD_DEFAULT)
        ];

        try {
            register_new_user($user);
            $result = "Rejestracja zakończona powodzeniem";
            header('Location: login?result='.$result);
            exit;
        } catch (Exception $e) {
            $result = "Rejestracja nieudana";
            header('Location: login?result='.$result);
            exit;
        } 
    }

    return 'signup_view';
}

function login(&$model)
{
    if (isset($_SESSION['user_id'])) {
        header('Location: gallery');
        exit;
    }

    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['login']) && isset($_POST['password'])) {
        $result = null;

        $login = $_POST['login'];
        $password = $_POST['password'];

        $user = find_user_by_login($login);

        if($user == null || !(password_verify($password, $user['password']))){
            $result = "Błędny login lub hasło!";
            header('Location: login?result='.$result);
            exit;
        }

        session_regenerate_id();
        $_SESSION['user_id'] = $user['_id'];
        $_SESSION['user_login'] = $user['login'];
        
        $result = "Zalogowano";
        header('Location: gallery?result='.$result);
        exit;

    }

    return 'login_view';
}

function logout(&$model)
{
    session_destroy();

    $result = "Wylogowano";
    header('Location: gallery?result='.$result);
    exit;
}

function saved(&$model)
{
    $model['saved'] = get_saved();

    return 'saved_view';
}

function saved_modify(&$model)
{
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['saved_photos'])) {
        $saved = &get_saved();
        $saved_photos = $_POST['saved_photos'];

        foreach ($saved_photos as $id => $added) {
            if ($added == 'true') {
                $saved["'".$id."'"] = get_photo($id);
            }
            else {
                unset($saved["'".$id."'"]);
            }
        }

        return 'redirect:' . $_SERVER['HTTP_REFERER'];
    } 

    return 'redirect:' . $_SERVER['HTTP_REFERER'];
}

function index(&$model)
{
    return 'index_view';
}

function basics(&$model)
{
    return 'basics_view';
}

function instructions(&$model)
{
    return 'instructions_view';
}

function czako(&$model)
{
    return 'czako_view';
}

function koperta(&$model)
{
    return 'koperta_view';
}

function kwoka(&$model)
{
    return 'kwoka_view';
}

function lodka(&$model)
{
    return 'lodka_view';
}