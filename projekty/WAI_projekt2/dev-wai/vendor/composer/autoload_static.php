<?php

// autoload_static.php @generated by Composer

namespace Composer\Autoload;

class ComposerStaticInit61308b4cb5c041fcfb27dde91fb40baf
{
    public static $files = array (
        '3a37ebac017bc098e9a86b35401e7a68' => __DIR__ . '/..' . '/mongodb/mongodb/src/functions.php',
    );

    public static $prefixLengthsPsr4 = array (
        'M' => 
        array (
            'MongoDB\\' => 8,
        ),
    );

    public static $prefixDirsPsr4 = array (
        'MongoDB\\' => 
        array (
            0 => __DIR__ . '/..' . '/mongodb/mongodb/src',
        ),
    );

    public static function getInitializer(ClassLoader $loader)
    {
        return \Closure::bind(function () use ($loader) {
            $loader->prefixLengthsPsr4 = ComposerStaticInit61308b4cb5c041fcfb27dde91fb40baf::$prefixLengthsPsr4;
            $loader->prefixDirsPsr4 = ComposerStaticInit61308b4cb5c041fcfb27dde91fb40baf::$prefixDirsPsr4;

        }, null, ClassLoader::class);
    }
}
