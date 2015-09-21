<?php

class c {
	private $x;
	private $arr;
	
	public function __construct() {
		$this->arr = [123, 222, true, $this, ["abc" => -500, 777], $this];
	}
	
	public function setX($newX) {
		$this->x = $newX;
	}
	
	public function printX() {
		echo($this->x);
	}
}

class a extends c {
	private $y;
	private $arr;
	
	public function __construct() {
		$this->arr = [$this, [$this], $this];
	}
	
	public function setY($newY) {
		$this->y = $newY;
	}
	
	public function printY() {
		echo($this->y);
	}
}

$obj = new c();
$obj->setX(1234);

session_start();
$_SESSION['blankNewC'] = new c();
$_SESSION['blankNewA'] = new a();
$_SESSION['blankNewA2'] = new a();
//$_SESSION['nestedInArray'] = ["a", 555, 1.05, 5E-10, ["nested", "nelly", [1, 2 , "pizza" => ["sauce" => false], ["abc" => 123, "zyx" => 321]]]];

echo(session_encode());


?>
