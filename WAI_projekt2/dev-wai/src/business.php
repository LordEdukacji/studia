<?php

use MongoDB\BSON\ObjectID;

function get_db()
{
    $mongo = new MongoDB\Client(
        "mongodb://localhost:27017/wai",
        [
            'username' => 'wai_web',
            'password' => 'w@i_w3b',
        ]);

    $db = $mongo->wai;

    return $db;
}

function get_gallery()
{
    $db = get_db();

    if (isset($_SESSION['user_id'])) {
        $query = [
            '$or' => [
                ['privacy' => 'public'],
                ['real_author' => $_SESSION['user_id']]
            ]
        ];
    } else {
        $query = ['privacy' => 'public'];
    }
    
    return $db->gallery->find($query)->toArray();
}

function get_page($page, $page_size)
{
    $db = get_db();
    
    if (isset($_SESSION['user_id'])) {
        $query = [
            '$or' => [
                ['privacy' => 'public'],
                ['real_author' => $_SESSION['user_login']]
            ]
        ];
    } else {
        $query = ['privacy' => 'public'];
    }

    $options = [
        'skip' => ($page - 1) * $page_size,
        'limit' => $page_size
        ];
    return $db->gallery->find($query, $options)->toArray();
}

function get_photo($id)
{
    $db = get_db();
    return $db->gallery->findOne(['_id' => new ObjectID($id)]);
}

function save_photo_info($photo)
{
    $db = get_db();
    return $db->gallery->insertOne($photo);
}

function generate_id()
{
    return new ObjectID();
}

function login_taken($login)
{
    $db = get_db();
    return count($db->users->findOne(['login' => $login]));
}

function register_new_user($user)
{
    $db = get_db();
    return $db->users->insertOne($user);
}

function find_user_by_login($login)
{
    $db = get_db();
    return $db->users->findOne(['login' => $login]);
}