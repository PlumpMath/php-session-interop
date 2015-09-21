<?php
session_start();
$_SESSION['test_bool'] = true;
$_SESSION['tEsTsTrInG'] = 'sica';
$_SESSION['TestNum'] = 34;
$_SESSION['testArray'] = ["a", 555, 1.05, 5E-10, ["nested", "nelly", [1, 2 , "pizza" => ["sauce" => false], ["abc" => 123, "zyx" => 321]]]];

echo(session_encode());
?>
